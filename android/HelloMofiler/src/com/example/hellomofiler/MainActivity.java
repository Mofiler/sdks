package com.example.hellomofiler;

import java.util.Vector;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.mofiler.Mofiler;
import com.mofiler.api.ApiListener;
import com.mofiler.api.RESTApi;

public class MainActivity extends ActionBarActivity {

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
     
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment{

    	private Mofiler mof;
    	private int iTestCounter;
    	private MofilerListener mofListenerExample = new MofilerListener();
    	
    	
        public PlaceholderFragment() {
        	
        }
        

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            Button btninit = (Button) rootView.findViewById(R.id.init_mofiler);
            btninit.setOnClickListener(new OnClickListener() {
     			@Override
    			public void onClick(View v) {
     	            mof = Mofiler.getInstance(getActivity());
     	            //mof.setURL("mofiler.com:8081");
     	            //mof.setURL("localhost:3000");
     	            mof.setURL("192.168.0.27:8081");
     	            mof.setAppKey("MY-APPKEY-HERE-ANDROID");
     	            mof.setAppName("MyAndroidTestApplication");
     	            mof.addIdentity("username", "johndoe");
//     	            mof.addIdentity("", "");
//     	            mof.addIdentity(" ", " ");
//     	            mof.addIdentity(" unake", " ");
//     	            mof.addIdentity("otrka ", " ");
//     	            mof.addIdentity("", " val1");
//     	            mof.addIdentity(" ", "val2 ");
//     	            mof.addIdentity(" kesp ", "val2 ");
     	            
     	            mof.setUseLocation(false); //defaults to true
     	            mof.setListener(mofListenerExample);
    			}
    		});
            
            Button btnsend = (Button) rootView.findViewById(R.id.send_data);
            btnsend.setOnClickListener(new OnClickListener() {
     			@Override
    			public void onClick(View v) {
     	        	try
     	        	{
     	                mof.injectValue("mykey" + iTestCounter, "myvalue");
     	                //mof.injectValue("mykey2", "myvalue2", System.currentTimeMillis() + (1000*60*60*24));
     	                iTestCounter++;
     	                ((Button)v).setText("Send Data to Mofiler - "+ iTestCounter);
     	                //mof.getValue("mykey");
     	        	}
     	        	catch(Exception ex)
     	        	{
     	        		System.err.println(ex.getMessage());
     	        		ex.printStackTrace();
     	        	}
     				
    			}
    		});

            Button btnrecv = (Button) rootView.findViewById(R.id.receive_data);
            btnrecv.setOnClickListener(new OnClickListener() {
     			@Override
    			public void onClick(View v) {
     	        	try
     	        	{
     	                mof.getValue("mykey0", "username", "johndoe");
     	        	}
     	        	catch(Exception ex)
     	        	{
     	        		System.err.println(ex.getMessage());
     	        		ex.printStackTrace();
     	        	}
     				
    			}
    		});

            Button btnflush = (Button) rootView.findViewById(R.id.flush_data);
            btnflush.setOnClickListener(new OnClickListener() {
     			@Override
    			public void onClick(View v) {
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
    		});

            
            
            return rootView;
        }
        
        
        private class MofilerListener implements ApiListener{
        	@Override
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
        
    }

    
}
