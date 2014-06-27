/*
 * @(#)HelloProsciutto		v1.0 11-16-2008
 *

    Copyright (C) 2006-2008  Mario Zorz email me at marionetazorz at yahoo dot com
    The Prosciutto Project website: http://www.prosciuttoproject.org
    The Prosciutto Project blog:   http://prosciutto.boutiquestartups.com
    ZA System Development website: http://www.zasysdev.com.ar

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


//import java.awt.Font;
import java.util.*;

import javax.microedition.lcdui.*;

import com.zasysdev.zamae.*;

import com.mofiler.Mofiler;
import com.mofiler.api.ApiListener;
import com.mofiler.api.RESTApi;
import com.mofiler.exception.AppKeyNotSetException;
import com.mofiler.exception.IdentityNotSetException;

import org.json.me.JSONException;
import org.json.me.JSONObject;

public class Navigation extends za_UI_api implements za_UI_api_callbacks, com.mofiler.api.ApiListener
{

    private Mofiler mof;
    private int iTestCounter;

    public Navigation(Mofiler a_mof)
    {
        mof = a_mof;

    }

    public void setNavigationDefinition(boolean bDBFlagFound, boolean bPostInit)
    {

        //create these messages only before XML loading, so XML loading will show the defined messages
        if (bPostInit == false)
        {
            proshUserMsg.startUIUserMessageDefinition();
            proshUserMsg.createUserMessageDefinition(
                                                    "1000", // uid
                                                    ProsciuttoForm.K_ZA_UI_FORM,
                                                    "30000",
                                                    "Remote form"
                                                    );
            proshUserMsg.addElementDefinition();
            proshUserMsg.endUIUserMessageDefinition();


            proshUserMsg.startUIUserMessageDefinition();
            proshUserMsg.createUserMessageDefinition(
                                                    "1000", // uid
                                                    ProsciuttoForm.K_ZA_UI_FORM,
                                                    "13000",
                                                    "Main form"
                                                    );
            proshUserMsg.addElementDefinition();
            proshUserMsg.endUIUserMessageDefinition();
        } /* end if */

    }

    public boolean runActionExecute(int ifocusedElementID, int keyPressed, int[] ivectFormElementsDefinition, int iElementIndex, boolean bIsTimer)
    {
        boolean bHandled = false;

        /* here override any behavior needed from actions on forms or elements of a form. */
        /* Remember to return TRUE if the thing was handled, or false if you want the default
        behavior to take place. */

        if (keyPressed == FIRE)
        {
            int iFormID = (ifocusedElementID / 1000);
            iFormID = iFormID * 1000;

            if (iFormID == 13000) //main menu
            {
                if (ifocusedElementID == 13012) //"init mofiler" clicked
                {
                    try
                    {
                        mof.setAppKey("MY-APPKEY-HERE");
                        mof.setAppName("MyTestJ2MEApplication");
                        //mof.setURL("mofiler.com:8081");
                        //mof.setURL("localhost:3000");
                        mof.setURL("localhost:8081");
                        mof.addIdentity("username", "johndoe");
                        mof.setUseLocation(true); //defaults to true
                        mof.setListener(this);

                    } catch (Exception ex)
                    {
                        System.err.println(ex.getMessage());
                        ex.printStackTrace();
                    }

                } /* end if */
                else
                    if (ifocusedElementID == 13013) //send data to mofiler
                {
                    try
                    {
                        mof.injectValue("mykey" + iTestCounter, "myvalue");
                        //mof.injectValue("mykey2", "myvalue2", System.currentTimeMillis() + (1000*60*60*24));
                        iTestCounter++;
                        ZA_UI_Api_GenericAction_ChangeButtonTextAndStateAndShowItsForm(13013, "Send Data to Mofiler - "+ iTestCounter, null);
                        //workaround for a bug in Prosciutto that would not refresh the focused element... weneed to refresh it in order to show the counter change.
                        ZA_UI_Api_Action_SetFocusOnThisElementID(13052);
                        ZA_UI_Api_Action_SetFocusOnThisElementID(13013);


                    } catch (Exception ex)
                    {
                        System.err.println(ex.getMessage());
                        ex.printStackTrace();
                    }

                } /* end if */
                else
                    if (ifocusedElementID == 13014) // receive data
                {
                    try
                    {
                        mof.getValue("mykey0", "username", "johndoe");
                    } catch (Exception ex)
                    {
                        System.err.println(ex.getMessage());
                        ex.printStackTrace();
                    }

                } else
                    if (ifocusedElementID == 13015)
                {
                    try
                    {
                        mof.flushDataToMofiler();
                    } catch (Exception ex)
                    {
                        System.err.println(ex.getMessage());
                        ex.printStackTrace();
                    }
                }

            } /* end if */
        } /* end if */


        return bHandled;

    }



    /**
     * This method is called each time the midlet is notificated of
     * a repaint event, and prior to Prosciutto painting anything on
     * the screen
     * 
     * @param a_proshGraphics
     *                   the Graphics object onto which the
     *                   developer can draw
     * 
     * @return true if event was handled
     *         false if event was not handled
     */
    public void prePaint(Graphics a_proshGraphics, int a_ifocusedElementID, int a_iCurrentFormID)
    {

    }

    /**
     * This method is called each time the midlet is notificated of
     * a repaint event, and after Prosciutto has ended painting
     * anything on the screen
     * 
     * @param a_proshGraphics
     *                   the Graphics object onto which the
     *                   developer can draw
     * 
     * @return true if event was handled
     *         false if event was not handled
     */
    public void postPaint(Graphics a_proshGraphics, int a_ifocusedElementID, int a_iCurrentFormID)
    {

    }



    public void methodResponded(String a_methodCalled, Vector a_vectBusinessObject)
    {
        System.err.println("HelloMofilerAAAA: This is the handler: " + a_methodCalled);

        if (a_methodCalled.startsWith(RESTApi.K_MOFILER_API_METHOD_NAME_get))
        {
            System.err.println("HelloMofiler: This is the handler for the 'get' result");

            if (a_vectBusinessObject != null && a_vectBusinessObject.size() > 1)
            {
                JSONObject jsonResult = (JSONObject) a_vectBusinessObject.elementAt(1);

                try
                {

                    System.out.println("HELLOMOFILER: resulting JSON object is: " + jsonResult.toString());

                    if (jsonResult.has("result"))
                    {
                        String strResult = (String) jsonResult.getString("result");
                        if (strResult.equals("ok"))
                        {

                            //TODO HANDLE YOUR DATA HERE
                            //TODO HANDLE YOUR DATA HERE
                            //TODO HANDLE YOUR DATA HERE
                            //TODO HANDLE YOUR DATA HERE

                            System.err.println("HelloMofiler: This is the result of the get");
                            //JSONObject jsonValue = jsonResult.getJSONObject("value");
                            String jsonValue = jsonResult.getString("value");
                            System.out.println(jsonValue);
                            System.out.println("HELLOMOFILER READY");
                            //TODO: do your work here, as in process the result, etc.
                            //TODO: do your work here, as in process the result, etc.
                            //TODO: do your work here, as in process the result, etc.
                        }
                    } else if (jsonResult.has("error"))
                    {

                        //TODO: HANDLE YOUR ERROR HERE
                        //TODO: HANDLE YOUR ERROR HERE
                        //TODO: HANDLE YOUR ERROR HERE
                        //TODO: HANDLE YOUR ERROR HERE

                        String strError = (String) jsonResult.getString("error");
                        System.err.println(strError);
                        System.err.println("HELLOMOFILER ERROR HANDLER");
                    }

                } catch (JSONException ex)
                {
                    ex.printStackTrace();
                }

            }
        }

    }

}