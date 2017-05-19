using UnityEngine;
using System.Collections;
using System.Runtime.InteropServices;


/// <summary>
/// Mofiler Bridge to native plugin.
/// For iOS Build, You will have to add the mofiler.framework to the embed libraries group in order to work. 
/// </summary>
public class MofilerBridge {


	#if UNITY_EDITOR

	public static string SetURL(string urlString)
	{
		return urlString;
	}

	public static void SetAppKey(string key)
	{
	}

	public static void SetAppName(string name)
	{

	}

	public static void AddIdentity(string key, string value)
	{
		
	}

	public static void InjectValue(string key, string value)
	{
		
	}

	public static void SetUseVerboseContext(bool value)
	{
		
	}

	public static void SetUseLocation(bool value)
	{

	}

	public static void FlushDataToMofiler()
	{
	}

	#elif UNITY_ANDROID

	[DllImport("__Internal")]
	extern static public string _SetURL(string urlString);
	public static string SetURL(string urlString)
	{
		AndroidJavaClass pluginClass = new AndroidJavaClass("com.mofiler.mofiler.MofilerBridge");	
		string urlback = pluginClass.CallStatic<string> ("_SetURL", urlString);
		Debug.Log("GOT URL BACK " + urlback);
		return urlback;
	}

	[DllImport("__Internal")]
	extern static public string _SetAppKey(string key);
	public static void SetAppKey(string key)
	{
		AndroidJavaClass pluginClass = new AndroidJavaClass("com.mofiler.mofiler.MofilerBridge");	
		string localKey = pluginClass.CallStatic<string> ("_SetAppKey", key);
		Debug.Log ("Set App Key end" + localKey);
	}

	[DllImport("__Internal")]
	extern static public string _SetAppName(string name);
	public static void SetAppName(string name)
	{
		AndroidJavaClass pluginClass = new AndroidJavaClass("com.mofiler.mofiler.MofilerBridge");	
		string localName = pluginClass.CallStatic<string> ("_SetAppName", name);
		Debug.Log ("Set App name" + localName);

	}

	[DllImport("__Internal")]
	extern static public string _AddIdentity(string key, string value);
	public static void AddIdentity(string key, string value)
	{
		AndroidJavaClass pluginClass = new AndroidJavaClass("com.mofiler.mofiler.MofilerBridge");	
		pluginClass.CallStatic<string> ("_AddIdentity", key,value);
		Debug.Log ("Add Identity " + value);
	}

	[DllImport("__Internal")]
	extern static public string _InjectValue(string key, string value);
	public static void InjectValue(string key, string value)
	{
		AndroidJavaClass pluginClass = new AndroidJavaClass("com.mofiler.mofiler.MofilerBridge");	
		pluginClass.CallStatic<string> ("_InjectValue", key,value);
		Debug.Log ("InjectValue " + value);
	}

	[DllImport("__Internal")]
	extern static public string _SetUseVerboseContext(bool value);
	public static void SetUseVerboseContext(bool value)
	{
		AndroidJavaClass pluginClass = new AndroidJavaClass("com.mofiler.mofiler.MofilerBridge");	
		pluginClass.CallStatic<string> ("_SetUseVerboseContext", value);
		Debug.Log ("SetUseVerboseContext " + value);
	}

	[DllImport("__Internal")]
	extern static public string _SetUseLocation(bool value);
	public static void SetUseLocation(bool value)
	{
		AndroidJavaClass pluginClass = new AndroidJavaClass("com.mofiler.mofiler.MofilerBridge");	
		pluginClass.CallStatic<string> ("_SetUseLocation", value);
		Debug.Log ("setUseLocation " + value);
	}

	[DllImport("__Internal")]
	extern static public string _FlushDataToMofiler();
	public static void FlushDataToMofiler()
	{
		AndroidJavaClass pluginClass = new AndroidJavaClass("com.mofiler.mofiler.MofilerBridge");	
		pluginClass.CallStatic<string> ("_FlushDataToMofiler");
		Debug.Log ("FlushDataToMofiler");
	}



	#elif UNITY_IOS


	[DllImport ("__Internal")]
	private static extern void _SetURL (string urlString);
	public static string SetURL(string urlString)
	{
		// Call plugin only when running on real device
		if (Application.platform != RuntimePlatform.OSXEditor)
			_SetURL (urlString);
		return urlString;
	}

	[DllImport ("__Internal")]
	private static extern void _SetAppKey (string appKey);
	public static void SetAppKey(string appKey)
	{
		// Call plugin only when running on real device
		if (Application.platform != RuntimePlatform.OSXEditor)
			_SetAppKey (appKey);
		else
			Debug.Log ("SETURL: Working on Editor");
	}

	[DllImport ("__Internal")]
	private static extern void _FlushDataToMofiler ();
	public static void FlushDataToMofiler()
	{
		// Call plugin only when running on real device
		if (Application.platform != RuntimePlatform.OSXEditor)
			_FlushDataToMofiler ();
		else
			Debug.Log ("SETURL: Working on Editor");
	}

	[DllImport ("__Internal")]
	private static extern void _SetAppName (string appName);
	public static void SetAppName(string appName)
	{
		// Call plugin only when running on real device
		if (Application.platform != RuntimePlatform.OSXEditor)
			_SetAppName (appName);
		else
			Debug.Log ("SETURL: Working on Editor");
	}

	[DllImport ("__Internal")]
	private static extern void _AddIdentity (string key, string value);
	public static void AddIdentity(string key, string value)
	{
		// Call plugin only when running on real device
		if (Application.platform != RuntimePlatform.OSXEditor)
			_AddIdentity (key, value);
		else
			Debug.Log ("SETURL: Working on Editor");
	}

	[DllImport ("__Internal")]
	private static extern void _InjectValue (string key, string value);
	public static void InjectValue(string key, string value)
	{
		// Call plugin only when running on real device
		if (Application.platform != RuntimePlatform.OSXEditor)
			_InjectValue (key, value);
		else
			Debug.Log ("SETURL: Working on Editor");
	}

	[DllImport ("__Internal")]
	private static extern void _SetUseVerboseContext (bool state);
	public static void SetUseVerboseContext(bool state)
	{
		// Call plugin only when running on real device
		if (Application.platform != RuntimePlatform.OSXEditor)
			_SetUseVerboseContext (state);
		else
			Debug.Log ("SETURL: Working on Editor");
	}

	[DllImport ("__Internal")]
	private static extern void _SetUseLocation (bool state);
	public static void SetUseLocation(bool state)
	{
		// Call plugin only when running on real device
		if (Application.platform != RuntimePlatform.OSXEditor)
			_SetUseLocation (state);
		else
			Debug.Log ("SETURL: Working on Editor");
	}
	#endif

}