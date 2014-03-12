package com.mofiler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.json.me.JSONArray;
import org.json.me.JSONException;

import com.sun.lwuit.io.Externalizable;
import com.sun.lwuit.io.util.Util;

public class MofilerValueStack extends Object implements Externalizable{
	private String jsonstack;
	
	public MofilerValueStack(String a_jsonStack) {
		super();
		this.jsonstack = a_jsonStack;
	}
	
	/*public String getJsonStack() {
		return this.jsonstack;
	}*/
	
	public void setJsonStack(JSONArray a_jsonStack){
		this.jsonstack = a_jsonStack.toString();
	}

	public void setJsonStack(String a_jsonStack){
		this.jsonstack = a_jsonStack;
	}
	
	public JSONArray getJsonStack() throws JSONException {
		return new JSONArray(this.jsonstack);
	}
	
	
	public void externalize(DataOutputStream stream) throws IOException {
		// TODO Auto-generated method stub
        Util.writeUTF(jsonstack, stream);
	}
	public String getObjectId() {
		// TODO Auto-generated method stub
		return "MofilerValueStack";
	}
	public int getVersion() {
		// TODO Auto-generated method stub
		return 1;
	}
	public void internalize(int i, DataInputStream stream) throws IOException {
		// TODO Auto-generated method stub
        this.jsonstack = Util.readUTF(stream);
	}

	
}
