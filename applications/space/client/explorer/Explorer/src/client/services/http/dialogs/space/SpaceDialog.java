package client.services.http.dialogs.space;

import client.services.http.dialogs.HttpDialog;
import client.utils.DateUtils;

public class SpaceDialog extends HttpDialog {

    public SpaceDialog() {
        add("timezone", DateUtils.getCurrentTimeZone());
    }

    @Override
    public String getOperation() {
        return "space";
    }

    @Override
    public String getEntityId() {
        return null;
    }
}
