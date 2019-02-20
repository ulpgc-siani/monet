package client.widgets.popups.dialogs;

import cosmos.gwt.widgets.CosmosHtmlPanel;

public abstract class NumberControlsDialog extends CosmosHtmlPanel {

    public NumberControlsDialog(String html) {
        super(html);
    }

    public abstract void refreshValue(Number value);

    public void draw() {
    }

    public void focus() {
    }
}
