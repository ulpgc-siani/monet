package client.widgets.entity.components;

import client.widgets.toolbox.ErasableListWidget;
import client.widgets.toolbox.HTMLListWidget.ListItem;
import client.widgets.toolbox.SortableHTMLListController;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.AbsolutePanel;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class MultipleFieldSortableValueListWidget<T> extends AbsolutePanel {

    private final ErasableListWidget<T> listWidget;
    private ChangePositionHandler<T> changePositionHandler;
    private DeleteHandler<T> deleteHandler;

    public MultipleFieldSortableValueListWidget(ErasableListWidget<T> listWidget) {
        this.listWidget = listWidget;
        listWidget.addStyleName(StyleName.VALUES);
        final SortableHTMLListController sortableHTMLListController = new SortableHTMLListController<>(this, listWidget);
        addHandlers(sortableHTMLListController);
        add(listWidget);
    }

    public void refresh() {
        if (listWidget.getItems().size() == 0)
            hideComponents();
        else
            showComponents();
    }

    public void setChangePositionHandler(ChangePositionHandler<T> changePositionHandler) {
        this.changePositionHandler = changePositionHandler;
    }

    public void setDeleteHandler(DeleteHandler<T> deleteHandler) {
        this.deleteHandler = deleteHandler;
    }

    public void addItem(T item) {
        listWidget.addItem(item);
    }

    public ErasableListWidget.ListItem<T> getItem(T value) {
        return (ErasableListWidget.ListItem<T>) listWidget.getItem(value);
    }

    @Override
    public void clear() {
        listWidget.clear();
    }

    private void addHandlers(SortableHTMLListController sortableHTMLListController) {
        sortableHTMLListController.addChangePositionEvent(new SortableHTMLListController.ChangePositionEvent<ListItem<T>>() {
            @Override
            public void onPositionChange(ListItem<T> item, int newPosition) {
                if (changePositionHandler != null) changePositionHandler.onPositionChange(item.getValue(), newPosition);
            }
        });
        listWidget.addDeleteHandler(new ErasableListWidget.DeleteHandler<T>() {
	        @Override
            public void onDelete(T value) {
                if (deleteHandler != null) deleteHandler.onDelete(value);
            }
        });
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

    public interface ChangePositionHandler<T> {
        void onPositionChange(T item, int newPosition);
    }

    public interface DeleteHandler<T> {
        void onDelete(T item);
    }

    public interface StyleName {
        String COMPONENT = "component";
        String HORIZONTAL_SEPARATOR = "horizontal-separator";
        String VALUES = "values";
    }
}
