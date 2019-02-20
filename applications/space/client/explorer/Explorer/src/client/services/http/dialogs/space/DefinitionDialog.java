package client.services.http.dialogs.space;

import client.services.http.dialogs.HttpDialog;

public class DefinitionDialog extends HttpDialog {
    private final String code;

    public DefinitionDialog(String code) {
        this.code = code;
    }

    @Override
    public String getOperation() {
        return "definition";
    }

    @Override
    public String getEntityId() {
        return code;
    }

}
