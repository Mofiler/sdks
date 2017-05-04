using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class MofilerManager : MonoBehaviour {

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public void OnButtonTest()
	{


		MofilerBridge.SetAppKey ("myAppKey");
		MofilerBridge.SetAppName ("myAppName");
		MofilerBridge.SetURL ("mofiler.com/mock");
		MofilerBridge.SetUseVerboseContext (true);
		MofilerBridge.SetUserLocation (true);

		MofilerBridge.AddIdentity("username", "flash_jhondoe2");
		MofilerBridge.AddIdentity("pin", "12345");

		MofilerBridge.InjectValue("testKey0-1", "testValue0");
		MofilerBridge.InjectValue("testKey1-1", "testValue");
		MofilerBridge.InjectValue("testKey2-1", "testValue");


		MofilerBridge.FlushDataToMofiler();
		Debug.Log ("All Test performed");
	}
}






