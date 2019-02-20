package client.widgets.entity.components.composite;

import client.core.model.List;
import client.core.system.MonetList;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.FieldLabel;
import client.widgets.toolbox.ErasableListWidget;
import client.widgets.toolbox.HTMLListWidget;
import client.widgets.toolbox.SortableHTMLListController;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;
import cosmos.gwt.widgets.Focusable;
import cosmos.gwt.widgets.FocusableContainer;
import cosmos.gwt.widgets.HorizontalFocusablePanel;
import cosmos.gwt.widgets.VerticalFocusablePanel;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.gwt.utils.StyleUtils.toRuleCheckingTags;

public class CompositeErasableTableWidget extends VerticalPanel implements FocusableContainer, CompositesContainer<HorizontalFocusablePanel> {

    private final ErasableTableWidget table;
    private final SortableHTMLListController listController;
    private HorizontalPanel headerPanel;

    public CompositeErasableTableWidget(String labelLayout, List<String> headers, TranslatorService translator) {
        createHeaderRow(labelLayout, headers, translator);
        table = createTable(translator);
        listController = createSortablePanel(table);
    }

    public void addRow(HorizontalFocusablePanel row) {
        createItem(row);
        adjustWidth(row);
    }

    public void addChangePositionEvent(final ChangePositionEvent event) {
        listController.addChangePositionEvent(new SortableHTMLListController.ChangePositionEvent<ErasableListWidget.ListItem<HorizontalFocusablePanel>>() {
            @Override
            public void onPositionChange(ErasableListWidget.ListItem<HorizontalFocusablePanel> item, int newPosition) {
                event.onPositionChange(item.getValue(), newPosition);
            }
        });
    }

    @Override
    public List<HorizontalFocusablePanel> getSelectedComposites() {
        return table.getSelectedRows();
    }

    public void clearComposites() {
        table.clear();
    }

    @Override
    public void addDeleteHandler(ErasableListWidget.DeleteHandler<HorizontalFocusablePanel> handler) {
        table.addDeleteHandler(handler);
    }

    @Override
    public void setItemSelectionChangeHandler(ItemSelectionChangeHandler itemSelectionChangeHandler) {
        table.setItemSelectionChangeHandler(itemSelectionChangeHandler);
    }

    @Override
    public void focusFirst() {
        table.focusFirst();
    }

    @Override
    public void focusLast() {
        table.focusLast();
    }

    @Override
    public void focus() {
    }

    public void setNavigationHandler(Focusable.NavigationHandler navigationHandler) {
        table.setNavigationHandler(navigationHandler);
    }

    @Override
    public boolean isNavigable() {
        return table.isNavigable();
    }

    private void createHeaderRow(String labelLayout, List<String> headerValues, TranslatorService translator) {
        final HeaderWidget headers = new HeaderWidget(new TableRow.Builder<>(translator), translator);
        headers.addStyleName(StyleName.VALUES);
        headerPanel = createHeaderPanel(labelLayout, headerValues);
        final HTMLListWidget.ListItem<HorizontalPanel> item = headers.addItem(headerPanel);
        item.setStyleName(StyleName.HEADER_ROW);
        $(item).find(toRule(StyleName.OPERATIONS_PANEL)).parent().remove();
        createSortablePanel(headers);
    }

    private SortableHTMLListController<ErasableListWidget> createSortablePanel(ErasableListWidget content) {
        AbsolutePanel absolutePanel = new AbsolutePanel();
        SortableHTMLListController<ErasableListWidget> listController = new SortableHTMLListController<>(absolutePanel, content);
        absolutePanel.add(content);
        add(absolutePanel);
        return listController;
    }

    private HorizontalPanel createHeaderPanel(String labelLayout, List<String> headerValues) {
        final HorizontalPanel header = new HorizontalPanel();
        header.setStyleName(StyleName.HEADER);
        for (String label : headerValues)
            header.add(createFieldLabel(labelLayout, label));
        adjustWidth(header);
        return header;
    }

    private FieldLabel createFieldLabel(String labelLayout, String label) {
        final FieldLabel fieldLabel = new FieldLabel(labelLayout, label);
        fieldLabel.addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                fieldLabel.showDescription();
            }
        }, MouseOverEvent.getType());
        fieldLabel.addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                fieldLabel.hideDescription();
            }
        }, MouseOutEvent.getType());
        return fieldLabel;
    }

    private void adjustWidth(ComplexPanel container) {
        for (Widget widget : container)
            $(widget).parent(StyleName.TD).get(0).getStyle().setWidth(100 / container.getWidgetCount(), Unit.PCT);
    }

    private ErasableTableWidget createTable(TranslatorService translator) {
        final ErasableTableWidget rows = new ErasableTableWidget(new TableRow.Builder(translator), translator);
        rows.addStyleName(StyleName.VALUES);
        return rows;
    }

    private void createItem(HorizontalFocusablePanel row) {
        for (int i = 0; i < row.getWidgetCount(); i++) {
            ((FieldWidget)row.getWidget(i)).setLabel((FieldLabel) headerPanel.getWidget(i));
        }
        $(table.addItem(row)).find(toRuleCheckingTags(StyleName.CONTROL_OPERATIONS_PANEL, StyleName.TD)).css("vertical-align", "middle");
    }

    private static class ErasableTableWidget extends ErasableListWidget<HorizontalFocusablePanel> implements FocusableContainer {

        private final VerticalFocusablePanel rows;
        private ItemSelectionChangeHandler itemSelectionChangeHandler;

        public ErasableTableWidget(ListItem.Builder builder, TranslatorService translator) {
            super(builder, translator);
            rows = new VerticalFocusablePanel();
        }

        @Override
        public HTMLListWidget.ListItem<HorizontalFocusablePanel> addItem(HorizontalFocusablePanel item) {
            rows.add(item);
            final HTMLListWidget.ListItem<HorizontalFocusablePanel> widgets = super.addItem(item);
            widgets.addValueChangeHandler(new ValueChangeHandler<HorizontalFocusablePanel>() {
                @Override
                public void onValueChange(ValueChangeEvent<HorizontalFocusablePanel> event) {
                    if (itemSelectionChangeHandler == null) return;
                    if (getSelectedRows().isEmpty())
                        itemSelectionChangeHandler.selectNone();
                    else
                        itemSelectionChangeHandler.select();
                }
            });
            return widgets;
        }

        public List<HorizontalFocusablePanel> getSelectedRows() {
            List<HorizontalFocusablePanel> rows = new MonetList<>();
            for (Widget item : items)
                if (item instanceof TableRow && ((TableRow)item).isSelected()) rows.add(((TableRow<HorizontalFocusablePanel>)item).getValue());
            return rows;
        }

        @Override
        public void focusFirst() {
            rows.focusFirst();
        }

        @Override
        public void focusLast() {
            rows.focusLast();
        }

        @Override
        public void focus() {
        }

        public void setNavigationHandler(Focusable.NavigationHandler navigationHandler) {
            rows.setNavigationHandler(navigationHandler);
        }

        @Override
        public boolean isNavigable() {
            return rows.isNavigable();
        }

        public void setItemSelectionChangeHandler(ItemSelectionChangeHandler itemSelectionChangeHandler) {
            this.itemSelectionChangeHandler = itemSelectionChangeHandler;
        }
    }

    private static class HeaderWidget extends ErasableListWidget<HorizontalPanel> {

        public HeaderWidget(ListItem.Builder builder, TranslatorService translator) {
            super(builder, translator);
        }
    }

    private static class TableRow<T extends HorizontalPanel> extends ErasableListWidget.SelectableListItem<T>{

        public TableRow(T value, TranslatorService translator) {
            super(value, translator);
        }

        @Override
        protected Widget createComponent() {
            return value;
        }

        private static class Builder<T extends HorizontalPanel> extends ErasableListWidget.WidgetListItem.Builder<T> {

            public Builder(TranslatorService translator) {
                super(translator);
            }

            @Override
            public HTMLListWidget.ListItem<T> build(T value, HTMLListWidget.Mode mode) {
                return new TableRow<>(value, translator);
            }
        }
    }

    public interface ChangePositionEvent {
        void onPositionChange(HorizontalPanel item, int newPosition);
    }

    public interface StyleName {
        String HEADER = "header";
        String HEADER_ROW = "header-row";
        String VALUES = "values";
        String CONTROL_OPERATIONS_PANEL = "control-operations-panel";
        String OPERATIONS_PANEL = "operations-panel";
        String TD = "td";
    }
}
