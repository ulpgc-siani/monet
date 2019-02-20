package cosmos.gwt.widgets;

import com.google.gwt.user.client.ui.IsWidget;

public interface Focusable extends IsWidget {

    void focus();
    void setNavigationHandler(NavigationHandler navigationHandler);
    boolean isNavigable();

    interface NavigationHandler {
        void onNavigate(Key key);

        enum Key {
            ARROW_UP, ARROW_DOWN, ARROW_LEFT, ARROW_RIGHT
        }
    }
}
