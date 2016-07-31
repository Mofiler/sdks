package com.mofiler;

import java.util.Random;

public class MofilerInstallationInfo {
    private String _installation_id;

    public MofilerInstallationInfo() {
        super();
    }

    public void generateId(boolean a_bForceNew) {
        if ((_installation_id == null) || a_bForceNew) {
            Random rndNbr = new Random();
            _installation_id = rndNbr.nextLong() + "";
        }
    }

    public void setInstallationId(String id) {
        this._installation_id = id;
    }

    public String getInstallationId() {
        return this._installation_id;
    }


}
