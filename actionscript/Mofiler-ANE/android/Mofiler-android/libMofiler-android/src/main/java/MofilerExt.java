
import java.util.HashMap;
import java.util.Map;

import com.adobe.fre.FREContext;
import com.adobe.fre.FREFunction;

import functions.AddIdentity;
import functions.FlushDataToMofiler;
import functions.GetValue;
import functions.InjectValue;
import functions.SetAppKey;
import functions.SetAppName;
import functions.SetURL;
import functions.SetUseLocation;
import functions.SetUseVerboseContext;

public class MofilerExt extends FREContext {

	
	public MofilerExt() {}
	
	@Override
	public void dispose() {
	}

	@Override
	public Map<String, FREFunction> getFunctions() {
		Map<String, FREFunction> functions = new HashMap<String, FREFunction>();
		functions.put("setURL", new SetURL());
		functions.put("setAppKey", new SetAppKey());
		functions.put("setAppName", new SetAppName());
		functions.put("addIdentity", new AddIdentity());
		functions.put("setUseVerboseContext", new SetUseVerboseContext());
		functions.put("setUseLocation", new SetUseLocation());
		functions.put("injectValue", new InjectValue());
		functions.put("flushDataToMofiler", new FlushDataToMofiler());
		functions.put("getValue", new GetValue());

		return functions;
	}

}
