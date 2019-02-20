package cosmos.gwt.widgets;

import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.ui.*;
import cosmos.gwt.widgets.parser.Element;
import cosmos.gwt.widgets.parser.ElementList;
import cosmos.gwt.widgets.parser.LayoutParser;
import cosmos.gwt.widgets.renderer.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.gwt.dom.client.Style.Unit;

public class LayoutRendererWidget extends LayoutWidget {

    private final List<HTMLPanel> widgetsToReplace;
    private final ParentsWithChildrenMap widgets;
    private final VerticalPanel content;
    private final Map<IsWidget, Element> widgetsToElement;

    public LayoutRendererWidget(String layout) {
        this.widgetsToElement = new HashMap<>();
        widgetsToReplace = new ArrayList<>();
        widgets = new ParentsWithChildrenMap();
        setStyleName(StyleName.LAYOUT_RENDER);
        content = createContent(layout, new LayoutParser());
        super.add(content);
    }

    @Override
    public void add(Widget widget) {
        if (!widgetsToElement.containsKey(widgetsToReplace.get(0))) return;
        super.add(widget);
        widgetsToElement.put(widget, widgetsToElement.get(widgetsToReplace.get(0)));
        widget.getElement().setAttribute("style", widgetStyle((Widget) widget));
        addWidget(widget);
    }

    @Override
    public void insert(Widget widget, int beforeIndex) {
        content.insert(widget, content.getWidgetCount() == 0 ? 0 : beforeIndex);
    }

    @Override
    public void show(Widget widget) {
        widget.setVisible(true);
        widgets.showParent(widget);
    }

    @Override
    public void hide(Widget widget) {
        widget.setVisible(false);
        widgets.hideParent(widget);
    }

    @Override
    public void clear() {
        for (HTMLPanel panel : widgetsToReplace)
            panel.clear();
    }

    private void addWidget(Widget widget) {
        widgets.replace(widgetsToReplace.get(0), widget);
        remove(widgetsToReplace.get(0));
        widgetsToReplace.get(0).add(widget);
        moveFirstWidgetToLastPosition();
    }

    private void moveFirstWidgetToLastPosition() {
        widgetsToReplace.add(widgetsToReplace.remove(0));
    }

    private VerticalPanel createContent(String layout, LayoutParser parser) {
        final VerticalPanel content = (VerticalPanel) renderTable(renderElements(parser.process(layout)));
        content.addStyleName(StyleName.ROOT);
        return content;
    }

    private VerticalTable renderElements(ElementList elements) {
        VerticalTable table = new VerticalTable();
        for (Integer row : elements.getRowNumbers())
            table.add(elements.getElements(row));
        return table;
    }

    private String widgetStyle(Widget widget) {
        return widget.getElement().getAttribute("style") + widgetsToReplace.get(0).getElement().getAttribute("style");
    }

    private CellPanel renderTable(MultipleElement table) {
        CellPanel panel = table.isVertical() ? createVerticalTable(table) : createHorizontalPanel(table);
        panel.setWidth(100 + "%");
        for (Table element : table)
            addCell(createWidget(element.getWidth(), element), element, panel);
        return panel;
    }

    private VerticalPanel createVerticalTable(MultipleElement table) {
        return table.isSpace() ? new VerticalPanel() : new VerticalFocusablePanel();
    }

    private HorizontalPanel createHorizontalPanel(MultipleElement table) {
        return table.isSpace() ? new HorizontalPanel() : new HorizontalFocusablePanel();
    }

    private void addCell(Widget widget, Table element, CellPanel panel) {
        panel.add(widget);
        panel.setCellWidth(widget, width(element) * 100 + "%");
        widgets.addParentWithChild(panel, widget);
    }

    private float width(Table element) {
        return (element.isMultiple() ? ((MultipleElement)element).getTotalWidth() : element.getWidth());
    }

    private Widget createWidget(float parentWidth, Table element) {
        if (element.isMultiple())
            return renderTable((MultipleElement) element);
        return renderElement(parentWidth, (SingleElement) element);
    }

    private Widget renderElement(float parentWidth, SingleElement element) {
        return createWidget(element.getElement(), parentWidth);
    }

    private Widget createWidget(Element element, float parentWidth) {
        final float elementWidth = element.getWidth() * (1 / parentWidth) * 100;
        HTMLPanel panel = element.isSpace() ? new HTMLPanel("") : new FocusableElement();
        getStyle(panel).setWidth(elementWidth, Unit.PCT);
        if (!element.isSpace()) {
            widgetsToReplace.add(panel);
            widgetsToElement.put(panel, element);
        }
        return panel;
    }

    private Style getStyle(Widget widget) {
        return widget.getElement().getStyle();
    }

    public interface StyleName extends LayoutWidget.StyleName {
        String LAYOUT_RENDER = "layout-render";
        String ROOT = "root";
    }

    private class FocusableElement extends HTMLPanel implements Focusable {

        private NavigationHandler navigationHandler;
        private Focusable widget;

        public FocusableElement() {
            super("");
        }

        @Override
        public void add(Widget widget) {
            super.add(widget);
            if (!(widget instanceof Focusable)) return;
            this.widget = (Focusable) widget;
            this.widget.setNavigationHandler(navigationHandler);
        }

        @Override
        public void focus() {
            if (widget != null)
                widget.focus();
        }

        @Override
        public void setNavigationHandler(NavigationHandler navigationHandler) {
            this.navigationHandler = navigationHandler;
        }

        @Override
        public boolean isNavigable() {
            return widget == null || widget.isNavigable();
        }
    }
}
