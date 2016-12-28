package functions;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.mofiler.Mofiler;
import com.mofiler.api.ApiListener;

import org.json.JSONObject;


public class GetValue implements FREFunction {

	private static final String TAG = "GetValueFunction";

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		Log.d(TAG, "called.");
//		FREObject result = null;
//		try {
//			Mofiler mof = Mofiler.getInstance(arg0.getActivity().getBaseContext());

//		} catch (Exception e) {
//			e.printStackTrace();
//			Log.e(TAG, "error", e);
//		}
//		return result;
		return null;
	}

}
