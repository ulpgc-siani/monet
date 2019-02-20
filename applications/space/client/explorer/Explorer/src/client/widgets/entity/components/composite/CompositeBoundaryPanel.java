package client.widgets.entity.components.composite;

import client.core.model.List;
import client.services.TranslatorService;
import client.widgets.toolbox.ErasableListWidget;
import client.widgets.toolbox.HTMLListWidget;
import client.widgets.toolbox.SortableHTMLListController;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.AbsolutePanel;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class CompositeBoundaryPanel extends AbsolutePanel implements CompositesContainer<List<String>> {

    private final CompositeErasableSummaryWidget tableWidget;
    private final SortableHTMLListController listController;

    public CompositeBoundaryPanel(TranslatorService translator) {
        super();
        tableWidget = new CompositeErasableSummaryWidget(new CompositeErasableItem.Builder(translator), translator);
        tableWidget.addStyleName(StyleName.VALUES);
        listController = new SortableHTMLListController<>(this, tableWidget);
        add(tableWidget);
    }

    public HTMLListWidget.ListItem<List<String>> addItem(int index, List<String> values) {
        return tableWidget.addItem(index, values);
    }

    public HTMLListWidget.ListItem<List<String>> addItemSelected(int index, List<String> values) {
        return tableWidget.addItemSelected(index, values);
    }

    public void addClickHandler(HTMLListWidget.ClickHandler<List<String>> handler) {
        tableWidget.addClickHandler(handler);
    }

    public void addChangePositionEvent(SortableHTMLListController.ChangePositionEvent<CompositeErasableItem> event) {
        listController.addChangePositionEvent(event);
    }

    public void addDeleteHandler(ErasableListWidget.DeleteHandler<List<String>> handler) {
        tableWidget.addDeleteHandler(handler);
    }

    @Override
    public void clear() {
        tableWidget.clear();
    }

    public CompositeErasableItem getItem(List<String> value) {
        return (CompositeErasableItem) tableWidget.getItem(value);
    }

    public List<List<String>> getSelectedComposites() {
        return tableWidget.getSelectedValues();
    }

    @Override
    public void setItemSelectionChangeHandler(ItemSelectionChangeHandler handler) {
        tableWidget.setItemSelectionChangeHandler(handler);
    }

    public void unSelectAll() {
        tableWidget.unselectAll();
    }

    public void refresh() {
        if (tableWidget.getItems().size() == 0)
            hideComponents();
        else
            showComponents();
    }

    private void showComponents() {
        setVisible(true);
        getHorizontalSeparator().show();
    }

    private void hideComponents() {
        setVisible(false);
        getHorizontalSeparator().hide();
    }

    private GQuery getHorizontalSeparator() {
        return $(getElement()).closest(toRule(StyleName.COMPONENT)).find(toRule(StyleName.HORIZONTAL_SEPARATOR)).first();
    }

    public void refreshComposite(List<String> values) {
        tableWidget.refreshItem(values);
    }

    public interface StyleName {
        String COMPONENT = "component";
        String HORIZONTAL_SEPARATOR = "horizontal-separator";
        String VALUES = "values";
    }
}
