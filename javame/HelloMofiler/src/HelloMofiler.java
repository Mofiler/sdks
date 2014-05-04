/*
 * @(#)HelloMofiler		v1.0 11-16-2008
 *

    Copyright (C) 2006-2008  Mario Zorz email me at marionetazorz at yahoo dot com
    The Prosciutto Project website: http://www.prosciuttoproject.org
    The Prosciutto Project blog:   http://prosciutto.boutiquestartups.com

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


import javax.microedition.lcdui.*;
import javax.microedition.midlet.*;

import com.mofiler.Mofiler;

public class HelloMofiler extends MIDlet
{
    public Navigation nav;
    private Mofiler mof;


    public boolean midPaused;

    public HelloMofiler ()
    {
        midPaused = false;
        mof = Mofiler.getInstance();
        nav = new Navigation(mof);

    }


    protected void destroyApp( boolean unconditional )throws MIDletStateChangeException
    {
        mof.onDestroyApp();
        exitMIDlet();
    }

    protected void pauseApp()
    {
        midPaused = true;
    }

    protected void startApp()
    {
        initMIDlet();
    }

    private void initMIDlet()
    {
//#ifndef UsePersistenceFramework
        //#ifdef FORMQTY
        //#ifdef BUTTONQTY
        //#expand nav.za_UI_api_Initialize(%FORMQTY%, %BUTTONQTY%, -1, -1, -1, -1, -1, -1, -1, -1);
        //#else
        //#expand nav.za_UI_api_Initialize(%FORMQTY%, -1, -1, -1, -1, -1, -1, -1, -1, -1);
        //#endif
        //#else
        nav.za_UI_api_Initialize(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
        //#endif
//#else
        //#ifdef FORMQTY
        //#ifdef BUTTONQTY
        //#expand nav.za_UI_api_Initialize(%FORMQTY%, %BUTTONQTY%, -1, -1, -1, -1, -1, -1, -1, -1, -1);
        //#else
        //#expand nav.za_UI_api_Initialize(%FORMQTY%, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
        //#endif
        //#else
        nav.za_UI_api_Initialize(-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1);
        //#endif
//#endif
        //nav.setZAMAEDescriptor(ZA_App_GetVersionString());
        nav.setNotificationListener(this);
        //nav.setPreventRemoteImagesFromBeingDeletedFlag(true);

//#ifndef UsePortuguese
        nav.setLanguage(com.zasysdev.zamae.Constants.ZA_LANGUAGE_ENGLISH_IDX);
        //nav.setZAJAGUrl("http://prosciuttohome.boutiquestartups.com.ar/zajag_demo/zajag.php", true);
//#else
        nav.setLanguage(com.zasysdev.zamae.Constants.ZA_LANGUAGE_PORTUGUESE_IDX);
        //nav.setZAJAGUrl("http://prosciuttohome.boutiquestartups.com.ar/zajag_demo/zajag_pr.php", true);
//#endif

        nav.setXMLModelFilename("/model.xml");
        nav.setZAUIAPIFullScreenMode(true);
        try
        {

            nav.startUI(Display.getDisplay(this));
        } catch ( Exception ex )
        {
            System.err.println("Could not start Prosciutto UI engine: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public void exitMIDlet()
    {
        nav.exitZAMAE();
        mof.onDestroyApp();
        notifyDestroyed();
    }

    private String ZA_App_GetVersionString()
    {
        String strVendor = null;
        String strAppName = null;
        String strVersion = null;
        String strMEConf = null;
        String strMEProf = null;
        String strZAMAEVer = null;
        String strFullString = null;

        strVendor = getAppProperty("MIDlet-Vendor");
        if (strVendor == null || strVendor.length() == 0)
        {
            strVendor = "VendorNameUndef";
        }
        strVersion = getAppProperty("MIDlet-Version");
        if (strVersion == null || strVersion.length() == 0)
        {
            strVersion = "AppVersionUndef";
        }
        strAppName = getAppProperty("MIDlet-Name");
        if (strAppName == null || strAppName.length() == 0)
        {
            strAppName = "AppNameUndef";
        }
        strMEConf = getAppProperty("MicroEdition-Configuration");
        if (strMEConf == null || strMEConf.length() == 0)
        {
            strMEConf = "CLDCLVersionUndef";
        }
        strMEProf = getAppProperty("MicroEdition-Profile");
        if (strMEProf == null || strMEProf.length() == 0)
        {
            strMEProf = "MIDPVersionUndef";
        }

//#ifdef ZAMAEUA
        //#expand strZAMAEVer = "%ZAMAEUA%";
//#else
        strZAMAEVer = getAppProperty("ZAMAE-Version");
        if (strZAMAEVer == null || strZAMAEVer.length() == 0)
        {
            strZAMAEVer = "ZAMAEVerUndef";
        }
//#endif


        strFullString = new String(strVendor + ":" + "ZAMAE" + ":" + strAppName + ":" + strVersion + "/" + strMEConf + ":" + strMEProf + "/ZAMAEVer:" + strZAMAEVer);

        return strFullString;
    }


}//End Class
