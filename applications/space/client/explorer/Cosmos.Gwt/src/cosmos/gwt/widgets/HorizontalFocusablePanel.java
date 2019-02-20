package cosmos.gwt.widgets;

import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;

public class HorizontalFocusablePanel extends HorizontalPanel implements FocusableContainer {

    private final HorizontalNavigationController navigationController;

    public HorizontalFocusablePanel() {
        super();
        setStyleName(StyleName.HORIZONTAL_PANEL);
        navigationController = new HorizontalNavigationController();
    }

    @Override
    public void add(Widget widget) {
        super.add(widget);
        if (widget instanceof Focusable) navigationController.add((Focusable) widget);
    }

    @Override
    public void focusFirst() {
        navigationController.focusFirst();
    }

    @Override
    public void focusLast() {
        navigationController.focusLast();
    }

    @Override
    public void focus() {
    }

    @Override
    public boolean isNavigable() {
        return navigationController.isNavigable();
    }

    @Override
    public void setNavigationHandler(NavigationHandler navigationHandler) {
        navigationController.setNavigationHandler(navigationHandler);
    }

    public interface StyleName {
        String HORIZONTAL_PANEL = "horizontal-panel";
    }
}
