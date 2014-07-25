package com.mofiler.service;
/*
 * @(#)LocationService	1.0 05-10-31
 *
	Mofiler
	
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/


//#ifdef UseJSR179
import java.util.*;
import javax.microedition.lcdui.*;
import javax.microedition.location.*;
import javax.microedition.location.Criteria;

import org.json.me.JSONObject;
import org.json.me.JSONException;

import com.mofiler.api.ApiListener;

public class LocationService
{
    private Coordinates lastKnownLocation;
    public String   strLastExceptions;
    private boolean bFirstTime = true;
    private int     iLBSTimeout = 60;
    private int     iLBSTimeoutAsked = 60;
    private Thread iupingGPSThread = null;
    private static final Object iuping_GPS_THREAD_LOCK = new Object();
    private Vector vectDataObtained = null;
    private boolean bNewDataAwaiting = false;

    public static final int K_GPSHANDLER_MODE_CELLSITE = 0;
    public static final int K_GPSHANDLER_MODE_ASSIST   = 1;
    public static final int K_GPSHANDLER_MODE_AUTONOMOUS = 2;

    private int     iMode = K_GPSHANDLER_MODE_AUTONOMOUS;
    static private Hashtable msgListeners = null;

    private int iIntentCount = 0;
    private Timer timerAux;
    private TTask taskAux;
    private boolean bUseGPSON = true;

    public LocationService(int a_iTimeout, int a_imode)
    {
        iLBSTimeout = a_iTimeout;
        iLBSTimeoutAsked = a_iTimeout;
        iMode = a_imode;

        timerAux = null;
        taskAux = null;
    }

    public void setGPS(boolean a_bValToSet)
    {
//#ifdef useBlackberry
        bUseGPSON = a_bValToSet;
        if (bUseGPSON == false)
        {
          iMode = K_GPSHANDLER_MODE_CELLSITE;
        } /* end if */
        else
        {
          iMode = K_GPSHANDLER_MODE_ASSIST;
        } /* end if */
//#else
        bUseGPSON = true;
//#endif
    }

    public int getCurrentMode()
    {
        return iMode;
    }

    public int getCurrentIntentCount()
    {
        return iIntentCount;
    }

    public String getCurrentModeString()
    { 
        String strToReturn = null;
        switch (iMode)
        {
            case K_GPSHANDLER_MODE_CELLSITE:
            {
                strToReturn = K_GPSHANDLER_MODE_CELLSITE + "CELLSITE";
                break;
            } /* end case */
        
            case K_GPSHANDLER_MODE_ASSIST:
            {
                strToReturn = K_GPSHANDLER_MODE_ASSIST + "ASSIST";
                break;
            } /* end case */
        
            default:
            { // assume K_GPSHANDLER_MODE_AUTONOMOUS

                strToReturn = K_GPSHANDLER_MODE_AUTONOMOUS + "AUTONOMOUS";
                break;
            } /* end case */
        } /* end switch */
        
        return strToReturn;
    }

    static public void addMethodListener(String a_strMethodToListenTo, ApiListener listener) {
        if (msgListeners == null) {
            msgListeners = new Hashtable();
        }
        addMethodListener(a_strMethodToListenTo, listener, msgListeners);
        //setMethodListener(a_strMethodToListenTo, listener, msgListeners);
    }

    static public void unregisterMethodListener(String a_strMethodToListenTo, ApiListener listener) {
        if (msgListeners != null)
        {
            Vector vec = (Vector) msgListeners.get(a_strMethodToListenTo);
            if (vec == null) {
                return;
            }
            if (vec.contains(listener)) {
                vec.removeElement(listener);
            }
        } /* end if */
    }

    static public void unregisterAllMethodsListener(ApiListener listener) {

        unregisterMethodListener("gps", listener);
    }

    static private void addMethodListener(String a_code, ApiListener listener, Hashtable msgListeners)
    {
        if (msgListeners == null) {
            msgListeners = new Hashtable();
        }
        Vector vec = (Vector) msgListeners.get(a_code);
        if (vec == null) {
            vec = new Vector();
            vec.addElement(listener);
            msgListeners.put(a_code, vec);
            return;
        }
        if (!vec.contains(listener)) {
            vec.addElement(listener);
        }
    }

    public boolean startProvider()
    {
      boolean bSuppported = isJSR179Supported();

      /* get location data */
      tryAgain(false);

      return bSuppported;
    }

    public void stopProvider()
    {
        if (timerAux != null)
        {
            timerAux.cancel();
        } /* end if */
    }


    /* iterates through modes and tries again */
    public void tryAgain(boolean a_bTryBetterMode)
    {
        if (iupingGPSThread != null)
        {
            if (!(iupingGPSThread.isAlive()))
            {
                iIntentCount++;

                if (
                    (a_bTryBetterMode)
                    ||
                    (iIntentCount > 3)
                   )
                {
                    if (bUseGPSON)
                    {
                        iIntentCount = 0;
                        switch (iMode)
                        {
                            case K_GPSHANDLER_MODE_CELLSITE:
                            { 
                                iLBSTimeout = iLBSTimeoutAsked;
                                switchMode(K_GPSHANDLER_MODE_ASSIST);
                                break;
                            } /* end case */
                        
                            case K_GPSHANDLER_MODE_ASSIST:
                            {
                                //override timeout for AUTONOMOUS mode
                                iLBSTimeout = 300;
                                switchMode(K_GPSHANDLER_MODE_AUTONOMOUS);
                                break;
                            } /* end case */
                
                            case K_GPSHANDLER_MODE_AUTONOMOUS:
                            {
                                iLBSTimeout = iLBSTimeoutAsked;
                                switchMode(K_GPSHANDLER_MODE_CELLSITE);
                                break;
                            } /* end case */
                        
                            default:
                            {
                                iLBSTimeout = iLBSTimeoutAsked;
                                switchMode(K_GPSHANDLER_MODE_CELLSITE);
                                break;
                            } /* end case */
                        } /* end switch */
                    } /* end if */
                    else
                    {
                        //force CELLSITE, don't use GPS!!
                        iLBSTimeout = iLBSTimeoutAsked;
                        switchMode(K_GPSHANDLER_MODE_CELLSITE);
                    }
                } /* end if */
    
                GPSThreadLaunch();
            } /* end if */
        } /* end if */
        else
        {
            GPSThreadLaunch();
        }
            
    }

    public void switchMode(int a_imode)
    {
        iMode = a_imode;
    }

    public Vector GPS_GetData()
    {
        Vector vectResult = null;
        String strLatitude = "";
        String strLongitude = "";
        String strExceptions = new String();
        int err = 0;

        LocationProvider lp=null;
        javax.microedition.location.Location location=null;
        //javax.microedition.location.Criteria myCriteria;
        Criteria myCriteria = new Criteria();

        switch ( iMode )
        {
            case K_GPSHANDLER_MODE_CELLSITE: // CELLSITE
                myCriteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_LOW);
                myCriteria.setHorizontalAccuracy(Criteria.NO_REQUIREMENT);
                myCriteria.setVerticalAccuracy(Criteria.NO_REQUIREMENT);
                myCriteria.setCostAllowed(true);
                break;
            case K_GPSHANDLER_MODE_ASSIST: // ASSIST
                myCriteria.setPreferredPowerConsumption(Criteria.POWER_USAGE_MEDIUM);
                myCriteria.setHorizontalAccuracy(5000); //100
                myCriteria.setVerticalAccuracy(Criteria.NO_REQUIREMENT);
                myCriteria.setCostAllowed(true);
                break;
            case K_GPSHANDLER_MODE_AUTONOMOUS: // AUTONOMOUS
                iLBSTimeout = 300;
                myCriteria.setCostAllowed(false);
                break;
        }


        try
        {
          //lp= LocationProvider.getInstance(null);
          lp= LocationProvider.getInstance(myCriteria);
        }
        catch(LocationException e)
        {
          ////System.err.println("Exception: " + e);
          strExceptions += "EX1: " + e.getMessage(); // + "\n";
          err = 1;
        }
        catch(Exception e)
        {
          ////System.err.println("Exception: " + e);
          strExceptions += "EX1.1: " + e.getMessage(); // + "\n";
          err = 11;
        }

        if (lp != null)
        {
            try
            {
                if (bFirstTime)
                {
                    location = lp.getLastKnownLocation();
                    bFirstTime = false;

                    if (location == null)
                        location = lp.getLocation(iLBSTimeout); //set to 60 seconds!!
                }
                else
                {    
                   //timeout
                    location = lp.getLocation(iLBSTimeout); //set to 60 seconds!!
                } /* end if */
            }
            catch(LocationException e)
            {
                  ////System.err.println("Exception: " + e);
                  strExceptions += "EX2: " + e.getMessage(); // + "\n";
                  err = 2;
    
            }
            catch(InterruptedException e)
            {
                  ////System.err.println("Exception: " + e);
                  strExceptions += "EX3: " + e.getMessage(); // + "\n";
                  err = 3;
    
            }
            catch (SecurityException e)
            {
                strExceptions += e.toString(); // + "\n";;
                err = 5;
            }
            catch(Exception e)
            {
              ////System.err.println("Exception: " + e);
              strExceptions += "EX1.2: " + e.getMessage(); // + "\n";
              err = 12;
            }
        } /* end if */
            
        if (err == 0)
        {
            try{
    
                Coordinates coordinates = location.getQualifiedCoordinates();
                lastKnownLocation = coordinates;
                strLatitude += coordinates.getLatitude();
                strLongitude += coordinates.getLongitude();
                //resultado = res;
            }catch(Exception e){
                ////System.err.println("Exception: " + e);
                strExceptions += "EX4: " + e.getMessage(); // + "\n";
                err = 4;
            }
        } /* end if */

        strLastExceptions = strExceptions;
        if (err == 0)
        {
            vectResult = new Vector();
            vectResult.addElement(strLatitude);
            vectResult.addElement(strLongitude);
        } /* end if */
        
        return vectResult; //resultado; // += strExceptions;
    }

    public boolean isJSR179Supported()
    {
        String version = System.getProperty("microedition.location.version");
        return (version != null && !version.equals("")) ? true : false;
    }


    public void GPSThreadLaunch()
    {
        /* we need this in order to avoid 2 or more UI threads calling the fetcher at the same time */
        synchronized (iuping_GPS_THREAD_LOCK)
        {
            if (timerAux != null)
            {
                timerAux.cancel();
            } /* end if */
            
            timerAux = new Timer();
            taskAux = new TTask();

            iupingGPSThread = new Thread(new Runnable() {
                public void run()
                {
                    Thread mythread = Thread.currentThread();
                    // If cancelled, discard images and return immediately
                    if (iupingGPSThread != mythread) {
                        return;
                    }

                    bNewDataAwaiting = false;
                    vectDataObtained = GPS_GetData();
                    if (vectDataObtained != null)
                    {
                        bNewDataAwaiting = true;

                        /* if we get new data, then keep trying on this every 2 minutes */
                        timerAux.schedule(taskAux, 60000);

                        if (msgListeners != null)
                        {
                            Vector listeners = (Vector) msgListeners.get("gps");
                            if (listeners != null)
                            {
                                Vector vectToReturn = new Vector();
                                vectToReturn.addElement("gps");
                                vectToReturn.addElement(vectDataObtained.elementAt(0));
                                vectToReturn.addElement(vectDataObtained.elementAt(1));
                                for (int iter = 0; iter < listeners.size(); iter++)
                                {
                                    ((ApiListener) listeners.elementAt(iter)).methodResponded("gps", vectToReturn);//a_strPayload);
                                } /* end for */
                            } /* end if */
                        } /* end if */
                    } /* end if */
                    else
                    {
                        //ERROR BROADCASTING
                        if (msgListeners != null)
                        {
                            Vector listeners = (Vector) msgListeners.get("gps");
                            if (listeners != null)
                            {
                                Vector vectToReturn = new Vector();
                                vectToReturn.addElement("error_gps");
                                //strLastExceptions
                                vectToReturn.addElement(strLastExceptions);
                                for (int iter = 0; iter < listeners.size(); iter++)
                                {
                                    ((ApiListener) listeners.elementAt(iter)).methodResponded("error_gps", vectToReturn);//a_strPayload);
                                } /* end for */
                            } /* end if */
                        } /* end if */

                        /* if we DID NOT get new data, then keep trying on this every 2 minutes */
                        timerAux.schedule(taskAux, 5000);

                    }
                }
              });

            iupingGPSThread.start();

        } /* end block */

    }

    public boolean gpsQueryDataAvailable()
    {
        return bNewDataAwaiting;
    }

    public Vector gpsGetLatestData()
    {
        if (
            (vectDataObtained != null)
            &&
            (bNewDataAwaiting)
           )
        {
            bNewDataAwaiting = false;
            return vectDataObtained;
        } /* end if */
        return null;
    }

    public Coordinates getLastKnownLocation() {
            return lastKnownLocation;
    }

    public JSONObject getLastKnownLocationJSON() {
            //build JSON object from latest location
            String strLat, strLon;
            if (lastKnownLocation != null){
                strLat = lastKnownLocation.getLatitude() + "";
                strLon = lastKnownLocation.getLongitude() + "";
            } else {
                strLat = "0.0"; // default to 0.0 if we still haven't got location from device HW
                strLon = "0.0";
            }
            JSONObject json = new JSONObject();
            try {
                    json.put("latitude", strLat);
                    json.put("longitude", strLon);
            } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
            }
            return json;
    }


    private class TTask extends TimerTask
    {
        public void run()
        {
            /* here check the Timer array. Evrey active timer should run an iteration
            here */
            try
            {
                tryAgain(false);
                //bDoingTimerAction = false;
            } catch ( Exception ex )
            {
                System.err.println("Exception on timer run: " + ex);
            }
        }
    }


}
//#endif
