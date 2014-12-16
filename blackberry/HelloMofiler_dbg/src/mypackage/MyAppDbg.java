package mypackage;

import net.rim.device.api.ui.UiApplication;

import com.mofiler.Mofiler;

/**
 * This class extends the UiApplication class, providing a
 * graphical user interface.
 */
public class MyAppDbg extends UiApplication
{
    /**
     * Entry point for application
     * @param args Command line arguments (not used)
     */ 
    public static void main(String[] args)
    {
        // Create a new instance of the application and make the currently
        // running thread the application's event dispatch thread.
        MyAppDbg theApp = new MyAppDbg();
        
        Mofiler.getInstance().onStart(args);

        theApp.enterEventDispatcher();
    }
    

    /**
     * Creates a new MyApp object
     */
    public MyAppDbg()
    {        
        // Push a screen onto the UI stack for rendering.
        pushScreen(new MyScreen());
    }    
}
