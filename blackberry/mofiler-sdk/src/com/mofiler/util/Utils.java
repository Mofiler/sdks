package com.mofiler.util;

public class Utils {

    public static boolean isNullOrWhitespace(String strtocheck){
        return !(strtocheck != null && strtocheck.trim().length() > 0 );
    }
    
    public static String getSafeString(String strtocheck){
    	if (strtocheck == null)
    		return "";
    	else
    		return strtocheck.trim();
    }
    

/*	public static boolean isMyServiceRunning(Context context, Class<?> ServiceClass) {
		ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		for (RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
			if (ServiceClass.getName().equals(service.service.getClassName())) {
				return true;
			}
		}
		return false;
	}
*/
    
}
