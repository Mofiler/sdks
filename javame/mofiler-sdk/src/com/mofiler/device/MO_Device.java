package com.mofiler.device;


public class MO_Device {


    private int iWidth;
    private int iHeight;

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
	

        public void setDisplaySize(int iWidth, int iHeight){
            this.iWidth = iWidth;
            this.iHeight = iHeight;
        }

	public String getDisplaySize()
	{
            return iWidth + "x" + iHeight;
	}
	
	
	public static String getDeviceManufacturer()
	{
		return "JavaME";
	}
	
	public static String getDeviceModelName()
	{
            String devModelName = System.getProperty("microedition.platform");
            if (devModelName == null)
                devModelName = "";
            return devModelName;
	}
	
	public static String getLocale()
	{
            String devLocale = System.getProperty("microedition.locale");
            if (devLocale == null)
                devLocale = "";
            return devLocale;
	}
	
    /**
     * Determines what connection type to use and returns the necessary string to use it.
     * @return A string with the connection info
     */
    public static String getConnectionString()
    {
        String connectionString = "";

        return connectionString;
    }
	
}
