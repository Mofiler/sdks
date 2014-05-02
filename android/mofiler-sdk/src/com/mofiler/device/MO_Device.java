package com.mofiler.device;

import java.util.Locale;

import android.content.Context;
import android.graphics.Point;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.view.Display;

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
	

	public static String getDisplaySize(final Display display) {
		String strDimensions = "";
	    final Point point = new Point();
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
	
	
	public static String getDeviceManufacturer()
	{
		return Build.MANUFACTURER;
	}
	
	public static String getDeviceModelName()
	{
        return Build.MODEL;
	}
	
	public static String getLocale()
	{
        return Locale.getDefault().getLanguage();
	}
	
    /**
     * Determines what connection type to use and returns the necessary string to use it.
     * @return A string with the connection info
     */
    public static String getConnectionString(Context context)
    {
    	String connectionString = "";
    	
    	TelephonyManager telephonyManager =((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
    	
    	//CARRIER NAME
    	connectionString = connectionString + "Carrier name: " + getCarrierName(telephonyManager);
    	connectionString = connectionString + "; SIM Carrier name: " + getSIMCarrierName(telephonyManager);

    	ConnectivityManager cm =
    	        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	NetworkInfo currentNetwork = cm.getActiveNetworkInfo();
    	
    	connectionString = connectionString + "; NetworkType: " + currentNetwork.getTypeName();
    	connectionString = connectionString + "; NetworkSubType: " + currentNetwork.getSubtypeName();
    	connectionString = connectionString + "; isRoaming: " + currentNetwork.isRoaming();
    	connectionString = connectionString + "; isFailover: " + currentNetwork.isFailover();
    	
    	
        return connectionString;
    }

    private static String getCarrierName(TelephonyManager telephonyManager)
    {
    	String operatorName = telephonyManager.getNetworkOperatorName();    	

        return operatorName;
    }
    
    private static String getSIMCarrierName(TelephonyManager telephonyManager)
    {
    	String operatorName = telephonyManager.getSimOperatorName();
    	return operatorName;
    }
    
	
}
