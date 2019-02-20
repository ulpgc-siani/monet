package client.services.http.dialogs.account;

import client.services.http.dialogs.HttpDialog;

public class LoadBusinessUnitsDialog extends HttpDialog {

    @Override
    public String getOperation() {
        return "businessunits";
    }

    @Override
    public String getEntityId() {
        return null;
    }
}
