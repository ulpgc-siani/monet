package client.services.http.dialogs.account;

import client.services.http.dialogs.HttpDialog;

public class ProfilePhotoDialog extends HttpDialog {

    private final String accountId;

    public ProfilePhotoDialog(String accountId, String photoUrl) {
        this.accountId = accountId;
        add("photo",photoUrl);
    }

    @Override
    public String getOperation() {
        return "account$save_photo";
    }

    @Override
    public String getEntityId() {
        return accountId;
    }
}
