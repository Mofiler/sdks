package com.mofiler;

import org.json.JSONArray;
import org.json.JSONException;

public class MofilerValueStack {
    private String jsonstack;

    public MofilerValueStack(String a_jsonStack) {
        super();
        this.jsonstack = a_jsonStack;
    }

    public void setJsonStack(JSONArray a_jsonStack) {
        this.jsonstack = a_jsonStack.toString();
    }

    public void setJsonStack(String a_jsonStack) {
        this.jsonstack = a_jsonStack;
    }

    public JSONArray getJsonStack() throws JSONException {
        return new JSONArray(this.jsonstack);
    }
	
}
