package client.widgets.view.document;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.ValueBoxBase.TextAlignment;

public class PagesControlsWidget extends HorizontalPanel {

    private static final int ZOOM_LIMIT = 5;
    private int currentPage;
    private int zoom;
    private PageChangeHandler pageChangeHandler;
    private int maxNumberOfPages;
    private IntegerBox pageBox;
    private ZoomPageHandler zoomPageHandler;

    public PagesControlsWidget() {
        create();
    }

    public void setPageChangeHandler(PageChangeHandler pageChangeHandler) {
        this.pageChangeHandler = pageChangeHandler;
    }

    public void setZoomPageHandler(ZoomPageHandler zoomPageHandler) {
        this.zoomPageHandler = zoomPageHandler;
    }

    public void setMaxNumberOfPages(int maxNumberOfPages) {
        this.maxNumberOfPages = maxNumberOfPages;
    }

    private void create() {
        currentPage = 1;
        zoom = 1;
        createComponents();
        addStyleName(StyleName.PREVIEW_CONTROLS);
    }

    private void createComponents() {
        createPageSelector();
        createZoomControls();
    }

    private void createPageSelector() {
        HorizontalPanel pageSelector = new HorizontalPanel();
        pageSelector.add(createPrevPage());
        pageSelector.add(createPageBox());
        pageSelector.add(createNextPage());
        add(pageSelector);
    }

    private Anchor createPrevPage() {
        final Anchor previous = new Anchor();
        previous.addStyleName(StyleName.CONTROL);
        previous.addStyleName(StyleName.PREVIOUS_PAGE);
        previous.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (currentPage == 1) return;
                pageChangeHandler.selectedPage(--currentPage - 1);
            }
        });
        return previous;
    }

    private IsWidget createPageBox() {
        pageBox = new IntegerBox();
        pageBox.setValue(currentPage);
        pageBox.addStyleName(StyleName.PAGE_BOX);
        pageBox.addValueChangeHandler(new ValueChangeHandler<Integer>() {
            @Override
            public void onValueChange(ValueChangeEvent<Integer> event) {
                if (event.getValue() < 1 || event.getValue() > maxNumberOfPages) return;
                currentPage = event.getValue();
                pageChangeHandler.selectedPage(currentPage - 1);
            }
        });
        pageBox.setAlignment(TextAlignment.CENTER);
        return pageBox;
    }

    private Anchor createNextPage() {
        final Anchor next = new Anchor();
        next.addStyleName(StyleName.CONTROL);
        next.addStyleName(StyleName.NEXT_PAGE);
        next.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (currentPage == maxNumberOfPages) return;
                pageChangeHandler.selectedPage(++currentPage - 1);
            }
        });
        return next;
    }

    private void createZoomControls() {
        HorizontalPanel zoomControls = new HorizontalPanel();
        zoomControls.add(createZoomIn());
        zoomControls.add(createRestoreZoom());
        zoomControls.add(createZoomOut());
        add(zoomControls);
        setCellHorizontalAlignment(zoomControls, HasHorizontalAlignment.ALIGN_CENTER);
    }

    private Anchor createZoomIn() {
        final Anchor zoomIn = new Anchor();
        zoomIn.addStyleName(StyleName.CONTROL);
        zoomIn.addStyleName(StyleName.ZOOM_IN);
        zoomIn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (zoom == ZOOM_LIMIT) return;
                zoom++;
                zoomPageHandler.zoom(zoom);
            }
        });
        return zoomIn;
    }

    private Anchor createRestoreZoom() {
        final Anchor zoomIn = new Anchor();
        zoomIn.addStyleName(StyleName.CONTROL);
        zoomIn.addStyleName(StyleName.RESTORE_ZOOM);
        zoomIn.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                zoom = 0;
                zoomPageHandler.zoom(zoom);
            }
        });
        return zoomIn;
    }

    private Anchor createZoomOut() {
        final Anchor zoomOut = new Anchor();
        zoomOut.addStyleName(StyleName.CONTROL);
        zoomOut.addStyleName(StyleName.ZOOM_OUT);
        zoomOut.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (zoom == -ZOOM_LIMIT) return;
                zoom--;
                zoomPageHandler.zoom(zoom);
            }
        });
        return zoomOut;
    }

    public void selectPage(int pageNumber) {
        currentPage = pageNumber + 1;
        pageBox.setValue(currentPage);
    }

    public interface PageChangeHandler {
        void selectedPage(int page);
    }

    public interface ZoomPageHandler {
        void zoom(int value);
    }

    public interface StyleName {
        String CONTROL = "control";
        String NEXT_PAGE = "next-page";
        String PAGE_BOX = "page-box";
        String PREVIEW_CONTROLS = "preview-controls";
        String PREVIOUS_PAGE = "previous-page";
        String RESTORE_ZOOM = "restore-zoom";
        String ZOOM_IN = "zoom-in";
        String ZOOM_OUT = "zoom-out";
    }
}
