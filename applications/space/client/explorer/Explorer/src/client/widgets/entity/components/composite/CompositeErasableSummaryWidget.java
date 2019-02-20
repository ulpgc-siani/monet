package client.widgets.entity.components.composite;

import client.core.model.List;
import client.core.system.MonetList;
import client.services.TranslatorService;
import client.widgets.toolbox.ErasableListWidget;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.dom.client.Style.Position;
import static com.google.gwt.dom.client.Style.Unit;
import static com.google.gwt.query.client.GQuery.$;
import static com.google.gwt.query.client.GQuery.window;
import static cosmos.gwt.utils.StyleUtils.toCombinedRule;

public class CompositeErasableSummaryWidget extends ErasableListWidget<List<String>> {

    private static final int SCROLL_BAR_WIDTH = 15;
    private boolean selectedItemWasVisible = true;
    private int itemHeight = 0;
    private int selectedItemPosition;
    private CompositeErasableItem stickyWidget;
    private CompositesContainer.ItemSelectionChangeHandler itemSelectionChangeHandler;

    public CompositeErasableSummaryWidget(CompositeErasableItem.Builder builder, TranslatorService translator) {
        super(builder, translator);
        getScrollPanel().addScrollHandler(new ScrollHandler() {

            @Override
            public void onScroll(ScrollEvent event) {
                checkVisible();
            }
        });
        $(toCombinedRule(StyleName.DESIGN_PANEL, StyleName.MAIN)).scroll(new Function() {
            @Override
            public void f() {
                if (getSelectedItem() != null && !selectedItemIsVisible()) fixedStyle();
            }
        });
        $(window).resize(new Function() {
            @Override
            public void f() {
                if (getSelectedItem() != null && !selectedItemIsVisible()) fixedStyle();
            }
        });
        selectedItemPosition = -1;
    }

    @Override
    public HTMLListWidget.ListItem<List<String>> addItem(int index, List<String> values) {
        final HTMLListWidget.ListItem<List<String>> item = super.addItem(index, values);
        item.addClickHandler(new HTMLListWidget.ListItem.ClickHandler<List<String>>() {
            @Override
            public void onClick(HTMLListWidget.ListItem<List<String>> item) {
                selectItem(item);
            }
        });
        calculateHeightOfItemDeferred(item);
        getScrollPanel().scrollToBottom();
        item.addValueChangeHandler(createValueChangeHandler());
        return item;
    }

    public HTMLListWidget.ListItem<List<String>> addItemSelected(int index, List<String> values) {
        final HTMLListWidget.ListItem<List<String>> item = addItem(index, values);
        selectItem(item);
        return item;
    }

    public List<List<String>> getSelectedValues() {
        final List<List<String>> values = new MonetList<>();
        for (CompositeErasableItem widget : getCompositeItems())
            if ((widget.isSelected())) values.add(widget.getValue());
        return values;
    }

    public void refreshItem(List<String> values) {
        getSelectedItem().refreshValue(values);
    }

    public void setItemSelectionChangeHandler(final CompositesContainer.ItemSelectionChangeHandler handler) {
        itemSelectionChangeHandler = handler;
        for (CompositeErasableItem item : getCompositeItems())
            item.addValueChangeHandler(createValueChangeHandler());
    }

    public void unselectAll() {
        for (Widget widget : getItems())
            widget.removeStyleName(StyleName.SELECTED);
    }

    private ValueChangeHandler<List<String>> createValueChangeHandler() {
        return new ValueChangeHandler<List<String>>() {
            @Override
            public void onValueChange(ValueChangeEvent<List<String>> event) {
                if (getSelectedValues().isEmpty())
                    itemSelectionChangeHandler.selectNone();
                else
                    itemSelectionChangeHandler.select();
            }
        };
    }

    private void calculateHeightOfItemDeferred(final HTMLListWidget.ListItem<List<String>> item) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (itemHeight == 0 && item.getOffsetHeight() != 0)
                    itemHeight = item.getOffsetHeight() + calculateMarginTop(item);
            }
        });
    }

    private void checkVisible() {
        if (getSelectedItem() == null) return;
        if (selectedItemIsVisible() && !selectedItemWasVisible)
            restoreStyle();
        else if (!selectedItemIsVisible() && selectedItemWasVisible)
            fixedStyle();
        selectedItemWasVisible = selectedItemIsVisible();
    }

    private void restoreStyle() {
        if (stickyWidget == null) return;
        items.remove(stickyWidget);
        stickyWidget = null;
    }

    private void fixedStyle() {
        createSticky();
        setupStickyStyle(stickyWidget.getElement().getStyle());
    }

    private void setupStickyStyle(Style style) {
        style.setPosition(Position.FIXED);
        if (isUpperHidden())
            style.setTop(getScrollPanel().getAbsoluteTop() - calculateMarginTop(getSelectedItem()), Unit.PX);
        else
            style.setTop(getScrollPanel().getAbsoluteTop() + getScrollPanel().getOffsetHeight() - itemHeight, Unit.PX);
        style.setLeft(getScrollPanel().getAbsoluteLeft() - calculateMarginLeft(getSelectedItem()), Unit.PX);
        style.setWidth(getScrollPanel().getOffsetWidth() - SCROLL_BAR_WIDTH, Unit.PX);
        style.setZIndex(100);
    }

    private void createSticky() {
        if (stickyWidget != null) return;
        stickyWidget = CompositeErasableItem.clone(getSelectedItem());
        stickyWidget.addStyleName(StyleName.STICKY);
        stickyWidget.addValueChangeHandler(new ValueChangeHandler<List<String>>() {
            @Override
            public void onValueChange(ValueChangeEvent<List<String>> event) {
                getSelectedItem().select(stickyWidget.isSelected());
            }
        });
        items.add(stickyWidget);
    }

    private boolean selectedItemIsVisible() {
        return getSelectedItem() != null && !isUpperHidden() && !isBottomHidden();
    }

    private boolean isUpperHidden() {
        int itemPosition = itemHeight * selectedItemPosition;
        int scrollPosition = getScrollPanel().getVerticalScrollPosition();
        return (itemPosition < scrollPosition);
    }

    private boolean isBottomHidden() {
        int itemPosition = itemHeight * selectedItemPosition;
        int scrollEndPosition = getScrollPanel().getVerticalScrollPosition() + getScrollPanel().getElement().getClientHeight();
        return (itemPosition + itemHeight > scrollEndPosition);
    }

    private Integer calculateMarginTop(Widget widget) {
        return calculateMargin("top", widget);
    }

    private Integer calculateMarginLeft(Widget widget) {
        return calculateMargin("left", widget);
    }

    private Integer calculateMargin(String position, Widget widget) {
        return Integer.valueOf($(widget).css("margin-" + position).replace("px", ""));
    }

    private CompositeErasableItem getSelectedItem() {
        for (CompositeErasableItem widget : getCompositeItems())
            if ($(widget).hasClass(StyleName.SELECTED)) return widget;
        return null;
    }

    private void selectItem(HTMLListWidget.ListItem<List<String>> listItem) {
        if (getSelectedItem() != null) restoreStyle();
        unselectAll();
        listItem.addStyleName(StyleName.SELECTED);
        selectedItemPosition = getItems().indexOf(listItem);
    }

    private List<CompositeErasableItem> getCompositeItems() {
        final List<CompositeErasableItem> items = new MonetList<>();
        for (Widget widget : getItems())
            if (widget instanceof CompositeErasableItem) items.add((CompositeErasableItem) widget);
        return items;
    }

    public interface StyleName {
        String SELECTED = "selected";
        String DESIGN_PANEL = "design-panel";
        String MAIN = "main";
        String STICKY = "sticky";
    }
}
