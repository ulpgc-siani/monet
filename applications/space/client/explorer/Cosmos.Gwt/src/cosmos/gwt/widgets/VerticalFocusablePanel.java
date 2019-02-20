package cosmos.gwt.widgets;

import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class VerticalFocusablePanel extends VerticalPanel implements FocusableContainer {

    private final VerticalNavigationController navigationController;

    public VerticalFocusablePanel() {
        super();
        setStyleName(StyleName.VERTICAL_PANEL);
        navigationController = new VerticalNavigationController();
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
        String VERTICAL_PANEL = "vertical-panel";
    }
}
