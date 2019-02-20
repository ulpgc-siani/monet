package client.widgets.view.document;

import com.google.gwt.user.client.ui.IsWidget;

public interface ThumbsViewerWidget extends IsWidget {

    void addThumb(String url);
    void setSelectThumbHandler(SelectThumbHandler selectThumbHandler);
    void clear();

    void selectThumb(int number);

    interface SelectThumbHandler {
        void selected(int number);
    }

    interface StyleName {
        String SELECTED = "selected";
        String THUMB = "thumb";
        String THUMBS_CONTAINER = "thumbs-container";
        String THUMBS_VIEWER = "thumbs-viewer";
    }
}
