package functions;

import android.os.Build;
import android.util.Log;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.adobe.fre.FREWrongThreadException;
import com.mofiler.Mofiler;

public class SetURL implements FREFunction {

	private static final String TAG = "SetUrlFunction";

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		Log.d(TAG, "called.");
		FREObject result = null;
		try {

			Mofiler mof = Mofiler.getInstance(arg0.getActivity());
			mof.setURL(arg1[0].getAsString());
			result = FREObject.newObject(true);
		} catch (Exception e) {
//			e.printStackTrace();
			Log.e(TAG, "error", e);
		}
		return result;
	}

}
