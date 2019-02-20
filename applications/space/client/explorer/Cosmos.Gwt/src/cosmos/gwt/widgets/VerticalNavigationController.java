package cosmos.gwt.widgets;

import static cosmos.gwt.widgets.Focusable.NavigationHandler.*;

public class VerticalNavigationController extends NavigationController {

    public VerticalNavigationController() {
        super();
        addActions();
    }

    private void addActions() {
        addActionForKey(Key.ARROW_UP, new NavigationController.NavigationAction() {
            @Override
            public void execute(Focusable widget) {
                focusPrevElement(widget);
            }
        });
        addActionForKey(Key.ARROW_DOWN, new NavigationController.NavigationAction() {
            @Override
            public void execute(Focusable widget) {
                focusNextElement(widget);
            }
        });
        addActionForKey(Key.ARROW_LEFT, new NavigationController.NavigationAction() {
            @Override
            public void execute(Focusable widget) {
                delegate(Key.ARROW_LEFT);
            }
        });
        addActionForKey(Key.ARROW_RIGHT, new NavigationController.NavigationAction() {
            @Override
            public void execute(Focusable widget) {
                delegate(Key.ARROW_RIGHT);
            }
        });
    }

    private void focusPrevElement(Focusable widget) {
        if (isFirst(widget))
            delegate(Key.ARROW_UP);
        else if (getPrevTo(widget).isNavigable())
            focusPrevTo(widget);
        else
            focusPrevElement(getPrevTo(widget));
    }

    private void focusNextElement(Focusable widget) {
        if (isLast(widget))
            delegate(Key.ARROW_DOWN);
        else if (getNextTo(widget).isNavigable())
            focusNextTo(widget);
        else
            focusNextElement(getNextTo(widget));
    }
}
