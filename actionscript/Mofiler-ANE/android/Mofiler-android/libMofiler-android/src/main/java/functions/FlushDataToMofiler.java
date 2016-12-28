package functions;

import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.mofiler.Mofiler;

public class FlushDataToMofiler implements FREFunction {

	private static final String TAG = "FlushDataToMofilerFn";

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		Log.d(TAG, "called.");
		FREObject result = null;
		try {

			Log.d(TAG, "before mof.");
			Log.d(TAG, "before mof act:" + arg0.getActivity().getPackageName());
			Mofiler mof = Mofiler.getInstance(arg0.getActivity());
//			Log.d(TAG, "appKey"+mof);
			Log.d(TAG, "appKey:"+mof.getAppKey());
			Log.d(TAG, "appURL:"+mof.getURL());
			mof.flushDataToMofiler();
			result = FREObject.newObject(true);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(TAG, "error", e);
		}
		return result;
	}

}
