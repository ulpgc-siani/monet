package client.widgets.toolbox;

import com.allen_sauer.gwt.dnd.client.DragContext;
import com.allen_sauer.gwt.dnd.client.drop.AbstractInsertPanelDropController;
import com.allen_sauer.gwt.dnd.client.util.DOMUtil;
import com.allen_sauer.gwt.dnd.client.util.DragClientBundle;
import com.allen_sauer.gwt.dnd.client.util.LocationWidgetComparator;
import com.google.gwt.user.client.ui.*;

public class HorizontalDropController extends AbstractInsertPanelDropController {

    private final Label DUMMY_LABEL_IE_QUIRKS_MODE_OFFSET_HEIGHT = new Label("x");
    private final InsertPanel dropContainer;

    public HorizontalDropController(InsertPanel dropContainer) {
        super(dropContainer);
        this.dropContainer = dropContainer;
    }

    boolean isRtl() {
        return DOMUtil.isRtl((Widget) dropContainer);
    }

    @Override
    protected LocationWidgetComparator getLocationWidgetComparator() {
        return isRtl() ? LocationWidgetComparator.BOTTOM_LEFT_COMPARATOR
                : LocationWidgetComparator.BOTTOM_RIGHT_COMPARATOR;
    }

    @Override
    protected Widget newPositioner(DragContext context) {
        SimplePanel outer = new SimplePanel();
        outer.addStyleName(DragClientBundle.INSTANCE.css().flowPanelPositioner());

        RootPanel.get().add(outer, -500, -500);
        outer.setWidget(DUMMY_LABEL_IE_QUIRKS_MODE_OFFSET_HEIGHT);

        int width = 0;
        int height = 0;
        for (Widget widget : context.selectedWidgets) {
            width += widget.getOffsetWidth();
            height = Math.max(height, widget.getOffsetHeight());
        }

        SimplePanel inner = new SimplePanel();
        inner.setPixelSize(width - DOMUtil.getHorizontalBorders(outer), height - DOMUtil.getVerticalBorders(outer));

        outer.setWidget(inner);

        return outer;
    }

}
