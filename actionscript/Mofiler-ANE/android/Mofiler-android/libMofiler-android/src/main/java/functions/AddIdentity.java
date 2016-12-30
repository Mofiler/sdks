package functions;

import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.adobe.fre.FREArray;
import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;
import com.adobe.fre.FREObject;
import com.mofiler.Mofiler;

import utils.AdvertisingIdClient;

public class AddIdentity implements FREFunction {

	private static final String TAG = "AddIdentityFunction";

	@Override
	public FREObject call(FREContext arg0, FREObject[] arg1) {
		Log.d(TAG, "called.");
		FREObject result = null;
		try {

			Mofiler mof = Mofiler.getInstance(arg0.getActivity());

			//patch until we have this implemented in the SDK
			try {
				if(mof.getIdentity("advertisingIdentifier")==null){
					final Mofiler _mof = mof;
					final Context _context = arg0.getActivity();
					new Thread(new Runnable() {
						public void run() {
							try {
								_mof.addIdentity("dvertisingIdentifier", AdvertisingIdClient.getAdvertisingIdInfo(_context).getId());
							} catch (Exception e) {
								Log.e(TAG, "dvertisingIdentifier error", e);
							}
						}
					}).start();
				}
			} catch (Exception e) {
				Log.e(TAG, "dvertisingIdentifier error", e);
			}
			try {
				if(mof.getIdentity("android_id")==null){
					mof.addIdentity("android_id", Settings.Secure.getString(arg0.getActivity().getContentResolver(), Settings.Secure.ANDROID_ID));
				}
			} catch (Exception e) {
				Log.e(TAG, "android_id error", e);
			}

			FREArray arr = (FREArray)arg1[0];
			mof.addIdentity(arr.getObjectAt(0).getAsString(), arr.getObjectAt(1).getAsString());
			result = FREObject.newObject(true);
		} catch (Exception e) {
//			e.printStackTrace();
			Log.e(TAG, "error", e);
		}
		return result;
	}

}
