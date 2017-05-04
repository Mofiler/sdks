using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class MofilerManager : MonoBehaviour {


	public Text testText;

	public Text urlText;

	// Use this for initialization
	void Start () {
		
	}
	
	// Update is called once per frame
	void Update () {
		
	}

	public void OnButtonTest()
	{
		testText.text = "Test1";
		urlText.text = "Test2";

		MofilerBridge.SetAppKey ("myAppKey");
		urlText.text = MofilerBridge.SetURL ("mofiler.com/mock");
		MofilerBridge.SetAppName ("myApp");
		MofilerBridge.AddIdentity ("username", "mofUnityAndroid");
		MofilerBridge.InjectValue ("test-key0", "testvalueAndroid");
		MofilerBridge.SetUseVerboseContext(true);
		MofilerBridge.SetUseLocation(true);
		MofilerBridge.FlushDataToMofiler();






		/*
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
		*/
	}
}






