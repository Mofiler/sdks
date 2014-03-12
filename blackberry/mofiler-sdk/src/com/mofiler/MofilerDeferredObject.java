package com.mofiler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.sun.lwuit.io.Externalizable;
import com.sun.lwuit.io.util.Util;

public class MofilerDeferredObject extends Object implements Externalizable{
	private String deferredKey, deferredValue;
	private long deferredMs;
	private boolean bDeferredMsSet;
	
	public MofilerDeferredObject(String deferredKey, String deferredValue,
			long deferredMs) {
		super();
		this.deferredKey = deferredKey;
		this.deferredValue = deferredValue;
		this.deferredMs = deferredMs;
		this.bDeferredMsSet = true;
	}
	public MofilerDeferredObject(String deferredKey, String deferredValue
			) {
		super();
		this.deferredKey = deferredKey;
		this.deferredValue = deferredValue;
		this.bDeferredMsSet = false;
	}
	public String getDeferredKey() {
		return deferredKey;
	}
	public void setDeferredKey(String deferredKey) {
		this.deferredKey = deferredKey;
	}
	public String getDeferredValue() {
		return deferredValue;
	}
	public void setDeferredValue(String deferredValue) {
		this.deferredValue = deferredValue;
	}
	public long getDeferredMs() {
		return deferredMs;
	}
	public void setDeferredMs(long deferredMs) {
		this.deferredMs = deferredMs;
	}
	public boolean isbDeferredMsSet() {
		return bDeferredMsSet;
	}
	public void setbDeferredMsSet(boolean bDeferredMsSet) {
		this.bDeferredMsSet = bDeferredMsSet;
	}
	
	public void externalize(DataOutputStream stream) throws IOException {
		// TODO Auto-generated method stub
        Util.writeUTF(deferredKey, stream);
        Util.writeUTF(deferredValue, stream);
        stream.writeLong(deferredMs);
        stream.writeBoolean(bDeferredMsSet);
		
	}
	public String getObjectId() {
		// TODO Auto-generated method stub
		return "MofilerDeferredObject";
	}
	public int getVersion() {
		// TODO Auto-generated method stub
		return 1;
	}
	public void internalize(int i, DataInputStream stream) throws IOException {
		// TODO Auto-generated method stub
        this.deferredKey = Util.readUTF(stream);
        this.deferredValue = Util.readUTF(stream);
		this.bDeferredMsSet = stream.readBoolean();
        this.deferredMs = stream.readLong();

	}

	
}
