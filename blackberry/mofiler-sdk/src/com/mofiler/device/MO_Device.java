package com.mofiler.device;

import org.json.me.JSONArray;
import org.json.me.JSONException;
import org.json.me.JSONObject;

import com.mofiler.util.Utils;

import net.rim.device.api.i18n.Locale;
import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.WLANInfo;
import net.rim.blackberry.api.phone.Phone;
import net.rim.device.api.system.RadioInfo;
import net.rim.device.api.system.GPRSInfo;
import net.rim.device.api.system.CDMAInfo;
import net.rim.device.api.system.GlobalEventListener;
import net.rim.device.api.system.ApplicationManager;
import net.rim.device.api.system.ApplicationDescriptor;

public class MO_Device {

	
	public MO_Device()
	{
		//generic constructor
		/*
		 * This class should represent everything that can be informed by a device, such as:
		 * -network interface being used
		 * -display dimensions
		 * -device name
		 * -device manufacturer
		 * -colors
		 * -audio capabilities
		 * -touchscreen / keyboard capabilities
		 * -orientation, accelerometer, etc.
		 */
	}
	

	public static String getDisplaySize()
	{
		return Display.getWidth() + "x" + Display.getHeight();
	}
	
	
	public static String getDeviceManufacturer()
	{
		return "Blackberry";
	}
	
	public static String getDeviceModelName()
	{
        String strDeviceName = DeviceInfo.getDeviceName();
        return strDeviceName;
	}
	
	public static String getLocale()
	{
        Locale objlocale = Locale.getDefaultForSystem();
        String strCurrlocale = null;
        if (objlocale != null)
        {
            strCurrlocale = objlocale.getLanguage();
        } /* end if */

        return strCurrlocale;
	}
	
    /**
     * Determines what connection type to use and returns the necessary string to use it.
     * @return A string with the connection info
     */
    public static String getConnectionString()
    {
        // This code is based on the connection code developed by Mike Nelson of AccelGolf.
        // http://blog.accelgolf.com/2009/05/22/blackberry-cross-carrier-and-cross-network-http-connection
        //String connectionString = null;
        String connectionString = "";

        // Simulator behavior is controlled by the USE_MDS_IN_SIMULATOR variable.
        if (DeviceInfo.isSimulator())
        {
//              if(UploaderThread.USE_MDS_IN_SIMULATOR)
//              {
//                      System.err.println("Device is a simulator and USE_MDS_IN_SIMULATOR is true");
//                      connectionString = ";deviceside=false";
//              }
//              else
            {
                //System.err.println("Device is a simulator and USE_MDS_IN_SIMULATOR is false");
                connectionString = ";deviceside=true";
            }
        }

        // Wifi is the preferred transmission method
        else if (WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED)
        {
            //System.err.println("Device is connected via Wifi.");
            connectionString = ";interface=wifi";
        }

        // Is the carrier network the only way to connect?
        else if ((CoverageInfo.getCoverageStatus() & CoverageInfo.COVERAGE_DIRECT) == CoverageInfo.COVERAGE_DIRECT)
        {
            //System.err.println("Carrier coverage.");

            String carrierUid = getCarrierBIBSUid();
            if (carrierUid == null)
            {
                // Has carrier coverage, but not BIBS.  So use the carrier's TCP network
                //System.err.println("No Uid");
                connectionString = ";deviceside=true";
            } else
            {
                // otherwise, use the Uid to construct a valid carrier BIBS request
                //System.err.println("uid is: " + carrierUid);
                connectionString = ";deviceside=false;connectionUID="+carrierUid + ";ConnectionType=mds-public";
            }
        }

        // Check for an MDS connection instead (BlackBerry Enterprise Server)
        else if ((CoverageInfo.getCoverageStatus() & CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS)
        {
            //System.err.println("MDS coverage found");
            connectionString = ";deviceside=false";
        }

        // If there is no connection available abort to avoid bugging the user unnecssarily.
        else if (CoverageInfo.getCoverageStatus() == CoverageInfo.COVERAGE_NONE)
        {
            //System.err.println("There is no available connection.");
        }

        // In theory, all bases are covered so this shouldn't be reachable.
        else
        {
            //System.err.println("no other options found, assuming device.");
            connectionString = ";deviceside=true";
        }

        return connectionString;
    }

    /**
     * Looks through the phone's service book for a carrier provided BIBS network
     * @return The uid used to connect to that network.
     */
    private static String getCarrierBIBSUid()
    {
        ServiceRecord[] records = ServiceBook.getSB().getRecords();
        int currentRecord;

        for (currentRecord = 0; currentRecord < records.length; currentRecord++)
        {
            if (records[currentRecord].getCid().toLowerCase().equals("ippp"))
            {
                if (records[currentRecord].getName().toLowerCase().indexOf("bibs") >= 0)
                {
                    return records[currentRecord].getUid();
                }
            }
        }

        return null;
    }
	

//  public static String getCarrierName(TelephonyManager telephonyManager)
//  {
//      String operatorName = telephonyManager.getNetworkOperatorName();
//
//      return operatorName;
//  }
//
//  public static String getSIMCarrierName(TelephonyManager telephonyManager)
//  {
//      String operatorName = telephonyManager.getSimOperatorName();
//      return operatorName;
//  }



    /**
     * Whatever other information is available to describe the device context, will be packaged into an "extras" object.
     * @return A string with the connection info
     */
    public static JSONObject getExtras(boolean bUseVerboseExtras)
    {
        JSONObject extras = new JSONObject();

        try {
            switch (RadioInfo.getNetworkType()) {
            case RadioInfo.NETWORK_CDMA:
                extras.put("phonetype", "CDMA");
                break;
            case RadioInfo.NETWORK_GPRS:
                extras.put("phonetype", "GSM");
                break;
            case RadioInfo.NETWORK_IDEN:
                extras.put("phonetype", "IDEN");
                break;
            case RadioInfo.NETWORK_UMTS:
                extras.put("phonetype", "UMTS");
                break;
            case RadioInfo.NETWORK_802_11:
                extras.put("phonetype", "802_11");
                break;
            default:
                extras.put("phonetype", "NONE");
                break;
            }

            if (!Utils.isNullOrWhitespace(Phone.getDevicePhoneNumber(true) ))
                extras.put("linenumber", Phone.getDevicePhoneNumber(true) );

            if (!Utils.isNullOrWhitespace(RadioInfo.getNetworkCountryCode(RadioInfo.getCurrentNetworkIndex())))
                extras.put("operator_mcc", RadioInfo.getNetworkCountryCode(RadioInfo.getCurrentNetworkIndex()));

            extras.put("operator_mccmnc", RadioInfo.getNetworkId(RadioInfo.getCurrentNetworkIndex())+ "");

            if (!Utils.isNullOrWhitespace(RadioInfo.getCurrentNetworkName()))
                extras.put("operator_name", RadioInfo.getCurrentNetworkName());

            /*if (!Utils.isNullOrWhitespace(mgr.getSimOperatorName()))
                extras.put("sim_operator_name", mgr.getSimOperatorName());

            if (!Utils.isNullOrWhitespace(mgr.getSimOperator()))
                extras.put("sim_operator_mccmnc", mgr.getSimOperator());

            if (!Utils.isNullOrWhitespace(mgr.getSubscriberId()))
                extras.put("subscriber_id", mgr.getSubscriberId());*/

            if (RadioInfo.getNetworkType() == RadioInfo.NETWORK_CDMA)
            {
                String esn = CDMAInfo.getESN() + "";
                if (!Utils.isNullOrWhitespace(esn))
                    extras.put("device_id", esn);
            } 
            else 
            if (RadioInfo.getNetworkType() == RadioInfo.NETWORK_GPRS)
            {
               String imei = GPRSInfo.imeiToString(GPRSInfo.getIMEI());
               if (!Utils.isNullOrWhitespace(imei))
                   extras.put("device_id", imei);
            }

            //VERBOSE EXTRAS GO HERE!!
            if (bUseVerboseExtras)
                extras.put("running_apps", getRunningApps());


        } catch (Exception ex){
            ex.printStackTrace();
        }

        return extras;
    }


    public static JSONArray getRunningApps(){
        JSONArray apps = new JSONArray();

        ApplicationManager appMan = ApplicationManager.getApplicationManager();
        ApplicationDescriptor[] mAppDes = appMan.getVisibleApplications();
        for (int i = 0; i < mAppDes.length; i++) {
               boolean isFG = appMan.getProcessId(mAppDes[i]) == appMan
                               .getForegroundProcessId();
               if (isFG)
               {
                   JSONObject jsonForegroundApp = new JSONObject();
                   try{
                       jsonForegroundApp.put("foregroundapp", mAppDes[i].getName());
                       apps.put(jsonForegroundApp);
                   } catch (JSONException ex)
                   {
                       apps.put(mAppDes[i].getName());
                   }
               }
               else 
               {
                   apps.put(mAppDes[i].getName());
               }
        }


        return apps;
    }

}
