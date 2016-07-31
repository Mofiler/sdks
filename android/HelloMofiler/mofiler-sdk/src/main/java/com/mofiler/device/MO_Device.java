package com.mofiler.device;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

import com.mofiler.util.Utils;

public class MO_Device {


    public MO_Device() {
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


    public static String getDisplaySize(final Context context) {
        String strDimensions = "";
        final Point point = new Point();

        WindowManager window = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = window.getDefaultDisplay();

        try {
            display.getSize(point);
        } catch (java.lang.NoSuchMethodError ignore) { // Older device
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        int width = point.x;
        int height = point.y;
        strDimensions = width + "x" + height;
        return strDimensions;

    }

    public static String getDeviceManufacturer() {
        return Build.MANUFACTURER;
    }

    public static String getDeviceModelName() {
        return Build.MODEL;
    }

    public static String getLocale() {
        return Locale.getDefault().getLanguage();
    }

    /**
     * Determines what connection type to use and returns the necessary string to use it.
     *
     * @return A string with the connection info
     */
    public static String getConnectionString(Context context) {
        String connectionString = "";

        TelephonyManager telephonyManager = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));

        //CARRIER NAME
        connectionString = connectionString + "Carrier name: " + getCarrierName(telephonyManager);
        connectionString = connectionString + "; SIM Carrier name: " + getSIMCarrierName(telephonyManager);

        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo currentNetwork = cm.getActiveNetworkInfo();

        connectionString = connectionString + "; NetworkType: " + currentNetwork.getTypeName();
        connectionString = connectionString + "; NetworkSubType: " + currentNetwork.getSubtypeName();
        connectionString = connectionString + "; isRoaming: " + currentNetwork.isRoaming();
        connectionString = connectionString + "; isFailover: " + currentNetwork.isFailover();


        return connectionString;
    }

    public static String getCarrierName(TelephonyManager telephonyManager) {
        String operatorName = telephonyManager.getNetworkOperatorName();

        return operatorName;
    }

    public static String getSIMCarrierName(TelephonyManager telephonyManager) {
        String operatorName = telephonyManager.getSimOperatorName();
        return operatorName;
    }


    /**
     * Whatever other information is available to describe the device context, will be packaged into an "extras" object.
     *
     * @return A string with the connection info
     */
    public static JSONObject getExtras(Context context, boolean bUseVerboseExtras) {
        JSONObject extras = new JSONObject();

        try {
            TelephonyManager mgr = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
            switch (mgr.getPhoneType()) {
                case TelephonyManager.PHONE_TYPE_CDMA:
                    extras.put("phonetype", "CDMA");
                    break;
                case TelephonyManager.PHONE_TYPE_SIP:
                    extras.put("phonetype", "SIP");
                    break;
                case TelephonyManager.PHONE_TYPE_GSM:
                    extras.put("phonetype", "GSM");
                    break;
                case TelephonyManager.PHONE_TYPE_NONE:
                default:
                    extras.put("phonetype", "NONE");
                    break;
            }

            if (!Utils.isNullOrWhitespace(mgr.getLine1Number()))
                extras.put("linenumber", mgr.getLine1Number());

            if (!Utils.isNullOrWhitespace(mgr.getNetworkCountryIso()))
                extras.put("operator_mcc", mgr.getNetworkCountryIso());

            if (!Utils.isNullOrWhitespace(mgr.getNetworkOperator()))
                extras.put("operator_mccmnc", mgr.getNetworkOperator());

            if (!Utils.isNullOrWhitespace(mgr.getNetworkOperatorName()))
                extras.put("operator_name", mgr.getNetworkOperatorName());

            if (!Utils.isNullOrWhitespace(mgr.getSimOperatorName()))
                extras.put("sim_operator_name", mgr.getSimOperatorName());

            if (!Utils.isNullOrWhitespace(mgr.getSimOperator()))
                extras.put("sim_operator_mccmnc", mgr.getSimOperator());

            if (!Utils.isNullOrWhitespace(mgr.getSubscriberId()))
                extras.put("subscriber_id", mgr.getSubscriberId());

            if (!Utils.isNullOrWhitespace(mgr.getDeviceId()))
                extras.put("device_id", mgr.getDeviceId());

            //VERBOSE EXTRAS GO HERE!!
            if (bUseVerboseExtras)
                extras.put("running_apps", getRunningApps(context));


        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return extras;
    }


    public static JSONArray getRunningApps(Context context) {
        JSONArray apps = new JSONArray();

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();

        PackageManager pm = context.getPackageManager();

        RunningAppProcessInfo info = null;

        for (int i = 0; i < procInfos.size(); i++) {
            info = procInfos.get(i);
            if (info != null) {
                if (info.processName != null)
                    try {
                        //only add apps that are not system packages.. we are not really interested in these. Are we?
                        if (!isSystemPackage(pm.getPackageInfo(info.processName, PackageManager.GET_META_DATA))) {

                            apps.put(info.processName);
                            //TODO check an alternative to the below code to detect the current foregorund app
//            	            if(info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
//            	                    && !isRunningService(info.processName, activityManager)){
//
//            	            	JSONObject jsonForegroundApp = new JSONObject();
//            	            	try{
//                	            	jsonForegroundApp.put("foregroundapp", info.processName);
//                	            	apps.put(jsonForegroundApp);
//            	            	} catch (JSONException ex)
//            	            	{
//                    				apps.put(info.processName);
//            	            	}
//            	            }
//            	            else{
//                				apps.put(info.processName);
//            	            }
                        }
                    } catch (PackageManager.NameNotFoundException ex) {

                        //add it anyway! we need info, info, info
                        apps.put(info.processName);
                    }
            }
        }
        return apps;
    }

    /**
     * Return whether the given PackageInfo represents a system package or not.
     * User-installed packages (Market or otherwise) should not be denoted as
     * system packages.
     *
     * @param pkgInfo
     * @return
     */
    private static boolean isSystemPackage(PackageInfo pkgInfo) {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) ? true
                : false;
    }


    private static RunningAppProcessInfo getForegroundApp(Context context) {
        RunningAppProcessInfo result = null, info = null;

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<RunningAppProcessInfo> l = activityManager.getRunningAppProcesses();
        Iterator<RunningAppProcessInfo> i = l.iterator();
        while (i.hasNext()) {
            info = i.next();
            if (info.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND
                    && !isRunningService(info.processName, activityManager)) {
                result = info;
                break;
            }
        }
        return result;
    }

    private static boolean isRunningService(String processname, ActivityManager activityManager) {
        if (processname == null || TextUtils.isEmpty(processname))
            return false;

        RunningServiceInfo service;

        List<RunningServiceInfo> l = activityManager.getRunningServices(9999);
        Iterator<RunningServiceInfo> i = l.iterator();
        while (i.hasNext()) {
            service = i.next();
            if (service.process.equals(processname))
                return true;
        }

        return false;
    }

}
