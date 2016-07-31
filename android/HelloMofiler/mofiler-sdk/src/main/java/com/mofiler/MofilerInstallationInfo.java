package com.mofiler;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Random;

public class MofilerInstallationInfo{// extends Object implements Externalizable{
	private String _installation_id;
	
	public MofilerInstallationInfo() {
		super();
	}
	
	public void generateId(boolean a_bForceNew)
	{
		if ((_installation_id == null) || a_bForceNew){
			Random rndNbr = new Random();
			_installation_id = rndNbr.nextLong() + "";
		}
	}
	
	public void setInstallationId(String id){
		this._installation_id = id;
	}
	
	public String getInstallationId()
	{
		return this._installation_id;
	}
	
/*	public void externalize(DataOutputStream stream) throws IOException {
		// TODO Auto-generated method stub
        Util.writeUTF(_installation_id, stream);
	}
	public String getObjectId() {
		// TODO Auto-generated method stub
		return "MofilerInstallationInfo";
	}
	public int getVersion() {
		// TODO Auto-generated method stub
		return 1;
	}
	public void internalize(int i, DataInputStream stream) throws IOException {
		// TODO Auto-generated method stub
        this._installation_id = Util.readUTF(stream);
	}
*/
	
}
