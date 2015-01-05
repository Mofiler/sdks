package mypackage;

import java.util.Vector;

import org.json.me.JSONException;
import org.json.me.JSONObject;

import net.rim.blackberry.api.mail.Message.Icons;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.container.MainScreen;

import com.mofiler.Mofiler;
import com.mofiler.api.ApiListener;
import com.mofiler.api.RESTApi;
import com.mofiler.exception.AppKeyNotSetException;
import com.mofiler.exception.IdentityNotSetException;

/**
 * A class extending the MainScreen class, which provides default standard
 * behavior for BlackBerry GUI applications.
 */
public final class MyScreen extends MainScreen implements FieldChangeListener, ApiListener
{
	private ButtonField mybutton;
	private ButtonField myrecvbutton;
	private ButtonField myflushbutton;
	private Mofiler mof;
	private int iTestCounter;
    /**
     * Creates a new MyScreen object
     */
    public MyScreen()
    {        
        // Set the displayed title of the screen       
        setTitle("MyTitle");
        
        mybutton = new ButtonField("Send Data to Mofiler value stack");
        mybutton.setChangeListener(this);
        this.add(mybutton);
        
        myrecvbutton = new ButtonField("Receive Data from Mofiler!");
        myrecvbutton.setChangeListener(this);
        this.add(myrecvbutton);

        myflushbutton = new ButtonField("Flush unsent data to Mofiler!");
        myflushbutton.setChangeListener(this);
        this.add(myflushbutton);
        
        mof = Mofiler.getInstance();
        //mof.setURL("mofiler.com:8081");
        //mof.setURL("localhost:8081");
        mof.setURL("192.168.0.21:8081");
        mof.setAppKey("MY-APPKEY-HERE");
        mof.setAppName("HelloMofiler");
        mof.addIdentity("username", "johndoe");
        mof.setListener(this);
    }
    
    public boolean onClose() {
    	// TODO Auto-generated method stub
    	//Call Mofiler.onDestroyApp() when you know your main screen is going off and your app is going to be finished
    	System.err.println("CLOSING SCREEN 2 ");
    	mof.onDestroyApp();
    	return super.onClose();
    }
    
    public void fieldChanged(Field field, int context) {
        // TODO Auto-generated method stub
        if(field == mybutton)
        {
        	try
        	{
                mof.injectValue("mykey" + iTestCounter, "myvalue");
                //mof.injectValue("mykey2", "myvalue2", System.currentTimeMillis() + (1000*60*60*24));
                iTestCounter++;
                mybutton.setLabel("Send Data to Mofiler - "+ iTestCounter);
                //mof.getValue("mykey");
        	}
        	catch(Exception ex)
        	{
        		System.err.println(ex.getMessage());
        		ex.printStackTrace();
        	}
        }
        else
        if(field == myrecvbutton)
        {
        	try
        	{
                mof.getValue("mykey", "username", "johndoe");
        	}
        	catch(Exception ex)
        	{
        		System.err.println(ex.getMessage());
        		ex.printStackTrace();
        	}
        }
        else
        if(field == myflushbutton)
        {
        	try
        	{
                mof.flushDataToMofiler();
        	}
        	catch(Exception ex)
        	{
        		System.err.println(ex.getMessage());
        		ex.printStackTrace();
        	}
        }
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

    			try {
    				
    				System.out.println("HELLOMOFILER: resulting JSON object is: " + jsonResult.toString());
    				
	                if (jsonResult.has("result")){
	                	String strResult = (String) jsonResult.getString("result");
	                	if (strResult.equals("ok")){
	                		
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
	                } else if (jsonResult.has("error")){
	                	
	                	//TODO: HANDLE YOUR ERROR HERE
	                	//TODO: HANDLE YOUR ERROR HERE
	                	//TODO: HANDLE YOUR ERROR HERE
	                	//TODO: HANDLE YOUR ERROR HERE

	                	String strError = (String) jsonResult.getString("error");
    	        		System.err.println(strError);
    	        		System.err.println("HELLOMOFILER ERROR HANDLER");
	                }
	                	
    			}
    			catch(JSONException ex)
    			{
    				ex.printStackTrace();
    			}
    			
    		}
    	}

    }
    
}
