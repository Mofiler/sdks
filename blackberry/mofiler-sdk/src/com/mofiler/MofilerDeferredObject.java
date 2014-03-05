package com.mofiler;

public class MofilerDeferredObject {
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

	
}
