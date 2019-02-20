package client.widgets.view;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTMLPanel;

public abstract class NodeViewWidget extends HTMLPanel {

    public NodeViewWidget(String html) {
        super(html);
    }

    public NodeViewWidget(SafeHtml safeHtml) {
        super(safeHtml);
    }

    public NodeViewWidget(String tag, String html) {
        super(tag, html);
    }

	public static class Builder extends EntityViewWidget.Builder {
	}

	protected interface Style {
	}
}
