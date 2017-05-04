using UnityEngine;
using System.Collections;
using System.Runtime.InteropServices;

public class MofilerBridge {


	[DllImport ("__Internal")]
	private static extern void _SetURL (string urlString);
	public static void SetURL(string urlString)
	{
		// Call plugin only when running on real device
		if (Application.platform != RuntimePlatform.OSXEditor)
			_SetURL (urlString);
		else
			Debug.Log ("SETURL: Working on Editor");
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
	private static extern void _SetUserLocation (bool state);
	public static void SetUserLocation(bool state)
	{
		// Call plugin only when running on real device
		if (Application.platform != RuntimePlatform.OSXEditor)
			_SetUserLocation (state);
		else
			Debug.Log ("SETURL: Working on Editor");
	}
		

}