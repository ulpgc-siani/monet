package cosmos.gwt.widgets;

import static cosmos.gwt.widgets.Focusable.NavigationHandler.Key;

public class HorizontalNavigationController extends NavigationController {

    public HorizontalNavigationController() {
        super();
        addActions();
    }

    private void addActions() {
        addActionForKey(Key.ARROW_UP, new NavigationController.NavigationAction() {
            @Override
            public void execute(Focusable widget) {
                delegate(Key.ARROW_UP);
            }
        });
        addActionForKey(Key.ARROW_DOWN, new NavigationController.NavigationAction() {
            @Override
            public void execute(Focusable widget) {
                delegate(Key.ARROW_DOWN);
            }
        });
        addActionForKey(Key.ARROW_LEFT, new NavigationController.NavigationAction() {
            @Override
            public void execute(Focusable widget) {
                focusPrevElement(widget);
            }
        });
        addActionForKey(Key.ARROW_RIGHT, new NavigationController.NavigationAction() {
            @Override
            public void execute(Focusable widget) {
                focusNextElement(widget);
            }
        });
    }

    private void focusPrevElement(Focusable widget) {
        if (isFirst(widget))
            delegate(Key.ARROW_LEFT);
        else if (getPrevTo(widget).isNavigable())
            focusPrevTo(widget);
        else
            focusPrevElement(getPrevTo(widget));
    }

    private void focusNextElement(Focusable widget) {
        if (isLast(widget))
            delegate(Key.ARROW_RIGHT);
        else if (getNextTo(widget).isNavigable())
            focusNextTo(widget);
        else
            focusNextElement(getNextTo(widget));
    }
}
