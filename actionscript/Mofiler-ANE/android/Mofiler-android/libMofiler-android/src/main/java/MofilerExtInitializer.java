
import android.provider.Settings;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREExtension;


public class MofilerExtInitializer implements FREExtension {

	private static final String TAG = "MofilerExtInitializer";

	public static MofilerExt context;
	
	public FREContext createContext(String arg0) {
		Log.i(TAG, "called.");
		return context = new MofilerExt();
	}

	public void dispose() {
		context = null;
	}

	public void initialize() {

	}

	public static void log(String message)
	{
		Log.d(TAG, message);
	}
}
