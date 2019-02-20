package cosmos.gwt.widgets;

import cosmos.gwt.widgets.Focusable.NavigationHandler.Key;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigationController {

    private final List<Focusable> elements;
    private final Map<Key, NavigationAction> actions;
    private Focusable.NavigationHandler navigationHandler;

    public NavigationController() {
        elements = new ArrayList<>();
        actions = new HashMap<>();
    }

    public Focusable getChildAt(int position) {
        return elements.get(position);
    }

    public Focusable getPrevTo(Focusable widget) {
        return getChildAt(elements.indexOf(widget) - 1);
    }

    public Focusable getNextTo(Focusable widget) {
        return getChildAt(elements.indexOf(widget) + 1);
    }

    public boolean isNavigable() {
        for (Focusable focusable : elements)
            if (focusable.isNavigable()) return true;
        return false;
    }

    public boolean isFirst(Focusable widget) {
        return elements.indexOf(widget) == 0;
    }

    public boolean isLast(Focusable widget) {
        return elements.indexOf(widget) == elements.size() - 1;
    }

    public void add(final Focusable focusable) {
        elements.add(focusable);
        focusable.setNavigationHandler(new Focusable.NavigationHandler() {
            @Override
            public void onNavigate(Key key) {
                if (actions.containsKey(key))
                    actions.get(key).execute(focusable);
            }
        });
    }

    public void setNavigationHandler(Focusable.NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    public void addActionForKey(Key key, NavigationAction action) {
        actions.put(key, action);
    }

    public void focusFirst() {
        if (elements.isEmpty()) return;
        final Focusable first = elements.get(0);
        if (first instanceof FocusableContainer)
            ((FocusableContainer) first).focusFirst();
        else
            first.focus();
    }

    public void focusLast() {
        if (elements.isEmpty()) return;
        final Focusable last = elements.get(elements.size() - 1);
        if (last instanceof FocusableContainer)
            ((FocusableContainer) last).focusLast();
        else
            last.focus();
    }

    public void focusPrevTo(Focusable widget) {
        if (getPrevTo(widget) instanceof FocusableContainer)
            ((FocusableContainer) getPrevTo(widget)).focusLast();
        else
            getPrevTo(widget).focus();
    }

    public void focusNextTo(Focusable widget) {
        if (getNextTo(widget) instanceof FocusableContainer)
            ((FocusableContainer) getNextTo(widget)).focusFirst();
        else
            getNextTo(widget).focus();
    }

    public void delegate(Key key) {
        if (navigationHandler != null) navigationHandler.onNavigate(key);
    }

    public interface NavigationAction {
        void execute(Focusable widget);
    }
}
