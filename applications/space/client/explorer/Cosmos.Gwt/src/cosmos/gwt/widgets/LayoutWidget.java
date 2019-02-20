package cosmos.gwt.widgets;

import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.query.client.GQuery.$;

public class LayoutWidget extends VerticalFocusablePanel {

    public void show(Widget widget) {
        widget.setVisible(true);
        getParentColumn(widget).show();
    }

    public void hide(Widget widget) {
        widget.setVisible(false);
        getParentColumn(widget).hide();
    }

    protected GQuery getParentColumn(Widget widget) {
        return $(widget).parent(StyleName.TD);
    }

    public interface StyleName extends VerticalFocusablePanel.StyleName {
        String TD = "td";
    }
}
