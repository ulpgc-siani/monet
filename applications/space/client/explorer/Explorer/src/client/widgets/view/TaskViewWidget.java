package client.widgets.view;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.ui.HTMLPanel;

public abstract class TaskViewWidget extends HTMLPanel {

    public TaskViewWidget(String html) {
        super(html);
    }

    public TaskViewWidget(SafeHtml safeHtml) {
        super(safeHtml);
    }

    public TaskViewWidget(String tag, String html) {
        super(tag, html);
    }

	public static class Builder extends EntityViewWidget.Builder {
	}

	protected interface Style {
	}
}
