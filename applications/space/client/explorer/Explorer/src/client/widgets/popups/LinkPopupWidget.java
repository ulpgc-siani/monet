package client.widgets.popups;

import client.presenters.displays.LinkFieldIndexDisplay;
import client.widgets.popups.dialogs.LinkFieldIndexDialog;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextBoxBase;

public class LinkPopupWidget extends FieldPopupWidget<TextBoxBase, LinkFieldIndexDialog> {

    private final LinkFieldIndexDisplay display;
    private final LinkFieldIndexDialog.Builder dialogBuilder;

    public LinkPopupWidget(String layout, LinkFieldIndexDisplay display, LinkFieldIndexDialog.Builder dialogBuilder, TextBoxBase input) {
        super(layout, input);
        this.display = display;
        this.dialogBuilder = dialogBuilder;
        init();
    }

	@Override
	protected LinkFieldIndexDialog createDialog(Element container) {
        content = (LinkFieldIndexDialog) dialogBuilder.build(display, "filter-tools", container.getInnerHTML());
		bindKeepingStyles(content, container);
        content.setClosePopupHandler(new LinkFieldIndexDialog.ClosePopupHandler() {
            @Override
            public void onClose() {
                hide();
                input.setFocus(true);
            }
        });
		return content;
	}

	@Override
    public void show() {
        super.show();
        content.show();
    }

    public void refresh() {
        content.refreshEntriesState();
    }
}
