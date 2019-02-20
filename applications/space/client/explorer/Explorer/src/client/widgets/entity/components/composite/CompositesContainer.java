package client.widgets.entity.components.composite;

import client.core.model.List;
import client.widgets.toolbox.ErasableListWidget;
import com.google.gwt.user.client.ui.IsWidget;

public interface CompositesContainer<Item> extends IsWidget {

    List<Item> getSelectedComposites();
    void addDeleteHandler(ErasableListWidget.DeleteHandler<Item> handler);
    void setItemSelectionChangeHandler(ItemSelectionChangeHandler handler);

    interface ItemSelectionChangeHandler {
        void select();
        void selectNone();
    }
}
