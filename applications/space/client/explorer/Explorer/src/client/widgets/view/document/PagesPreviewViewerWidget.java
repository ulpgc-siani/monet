package client.widgets.view.document;

import com.google.gwt.user.client.ui.VerticalPanel;

public class PagesPreviewViewerWidget extends VerticalPanel {

    private final ThumbsViewerWidget thumbsViewer;
    private final PagesControlsWidget controls;

    public PagesPreviewViewerWidget(ThumbsViewerWidget thumbsViewer) {
        super();
        controls = new PagesControlsWidget();
        this.thumbsViewer = thumbsViewer;
        create();
    }

    public void addThumb(String url) {
        thumbsViewer.addThumb(url);
    }

    public void setSelectThumbHandler(ThumbsViewerWidget.SelectThumbHandler selectThumbHandler) {
        thumbsViewer.setSelectThumbHandler(selectThumbHandler);
    }

    public void setPageChangeHandler(PagesControlsWidget.PageChangeHandler pageChangeHandler) {
        controls.setPageChangeHandler(pageChangeHandler);
    }

    public void setZoomPageHandler(PagesControlsWidget.ZoomPageHandler zoomPageHandler) {
        controls.setZoomPageHandler(zoomPageHandler);
    }

    public void setMaxNumberOfPages(int maxNumberOfPages) {
        controls.setMaxNumberOfPages(maxNumberOfPages);
    }

    private void create() {
        addStyleName(StyleName.PAGES_PREVIEW);
        add(controls);
        add(thumbsViewer);
    }

    public void selectThumb(int pageNumber) {
        thumbsViewer.selectThumb(pageNumber);
        controls.selectPage(pageNumber);
    }

    public interface StyleName {
        String PAGES_PREVIEW = "pages-preview";
    }
}
