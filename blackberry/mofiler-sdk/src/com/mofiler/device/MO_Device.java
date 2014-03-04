package com.mofiler.device;

import net.rim.device.api.servicebook.ServiceBook;
import net.rim.device.api.servicebook.ServiceRecord;
import net.rim.device.api.system.CoverageInfo;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.Display;
import net.rim.device.api.system.WLANInfo;

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
                System.err.println("Device is a simulator and USE_MDS_IN_SIMULATOR is false");
                connectionString = ";deviceside=true";
            }
        }

        // Wifi is the preferred transmission method
        else if (WLANInfo.getWLANState() == WLANInfo.WLAN_STATE_CONNECTED)
        {
            System.err.println("Device is connected via Wifi.");
            connectionString = ";interface=wifi";
        }

        // Is the carrier network the only way to connect?
        else if ((CoverageInfo.getCoverageStatus() & CoverageInfo.COVERAGE_DIRECT) == CoverageInfo.COVERAGE_DIRECT)
        {
            System.err.println("Carrier coverage.");

            String carrierUid = getCarrierBIBSUid();
            if (carrierUid == null)
            {
                // Has carrier coverage, but not BIBS.  So use the carrier's TCP network
                System.err.println("No Uid");
                connectionString = ";deviceside=true";
            } else
            {
                // otherwise, use the Uid to construct a valid carrier BIBS request
                System.err.println("uid is: " + carrierUid);
                connectionString = ";deviceside=false;connectionUID="+carrierUid + ";ConnectionType=mds-public";
            }
        }

        // Check for an MDS connection instead (BlackBerry Enterprise Server)
        else if ((CoverageInfo.getCoverageStatus() & CoverageInfo.COVERAGE_MDS) == CoverageInfo.COVERAGE_MDS)
        {
            System.err.println("MDS coverage found");
            connectionString = ";deviceside=false";
        }

        // If there is no connection available abort to avoid bugging the user unnecssarily.
        else if (CoverageInfo.getCoverageStatus() == CoverageInfo.COVERAGE_NONE)
        {
            System.err.println("There is no available connection.");
        }

        // In theory, all bases are covered so this shouldn't be reachable.
        else
        {
            System.err.println("no other options found, assuming device.");
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
	
}
