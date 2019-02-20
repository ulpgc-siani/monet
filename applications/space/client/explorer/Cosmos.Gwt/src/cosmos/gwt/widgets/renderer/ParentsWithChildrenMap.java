package cosmos.gwt.widgets.renderer;

import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.gwt.query.client.GQuery.$;

public class ParentsWithChildrenMap {

    private final Map<Widget, List<Widget>> container;

    public ParentsWithChildrenMap() {
        container = new HashMap<>();
    }

    public void addParentWithChild(Widget parent, Widget child) {
        if (!container.containsKey(parent)) container.put(parent, new ArrayList<Widget>());
        container.get(parent).add(child);
    }

    public void replace(Widget oldWidget, Widget newWidget) {
        List<Widget> siblings = getSiblings(oldWidget);
        siblings.remove(oldWidget);
        siblings.add(newWidget);
    }

    public void showParent(Widget widget) {
        if (getParent(widget) != null)
            getParent(widget).show();
    }

    public void hideParent(Widget widget) {
        if (siblingsAreVisible(getSiblings(widget)))
            getParent(widget).hide();
    }

    public GQuery getParent(Widget widget) {
        for (Map.Entry<Widget, List<Widget>> entry : container.entrySet())
            if (entry.getValue().contains(widget)) return $(entry.getKey()).parent("td");
        return null;
    }

    private List<Widget> getSiblings(Widget widget) {
        for (Map.Entry<Widget, List<Widget>> entry : container.entrySet())
            if (entry.getValue().contains(widget)) return entry.getValue();
        return new ArrayList<>();
    }

    private boolean siblingsAreVisible(List<Widget> siblings) {
        for (Widget sibling : siblings)
            if (sibling.isVisible()) return true;
        return false;
    }
}
