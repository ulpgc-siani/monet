package client.widgets.view.document;

import client.core.model.List;
import client.core.system.MonetList;
import client.services.TranslatorService;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class PagesViewerWidget extends ScrollPanel {

    private static final double ZOOM_FACTOR = 0.2;
    private final String loadingDocumentString;
    private VerticalPanel pagesContainer;
    private int currentPage;
    private int numberOfPages;
    private PageChangeHandler pageChangeHandler;
    private List<Image> pages;
    private int pageWidth;
    private int pageHeight;

    public PagesViewerWidget(TranslatorService translator) {
        super();
        create();
        currentPage = 0;
        numberOfPages = 0;
        pages = new MonetList<>();
        loadingDocumentString = translator.translate(TranslatorService.Label.DOCUMENT_LOAD_SECONDS_REMAINING);
    }

    public void addPage(String url) {
        final Image page = createPage(url);
        pagesContainer.add(page);
        pagesContainer.setCellHorizontalAlignment(page, HasHorizontalAlignment.ALIGN_CENTER);
    }

    @Override
    public void clear() {
        pagesContainer.clear();
        numberOfPages = 0;
    }

    public void showPage(int page) {
        currentPage = page;
        setVerticalScrollPosition(currentPage * calculatePageLength());
    }

    public void setPageChangeHandler(PageChangeHandler pageChangeHandler) {
        this.pageChangeHandler = pageChangeHandler;
    }

    private void create() {
        addStyleName(StyleName.PAGES_VIEWER);
        pagesContainer = new VerticalPanel();
        pagesContainer.addStyleName(StyleName.PAGES_CONTAINER);
        add(pagesContainer);
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                updateHeight();
            }
        });
        addScrollHandler(new ScrollHandler() {
            @Override
            public void onScroll(ScrollEvent event) {
                if (currentPage == getVerticalScrollPosition() / calculatePageLength()) return;
                currentPage = getVerticalScrollPosition() / calculatePageLength();
                if (pageChangeHandler != null) pageChangeHandler.pageChanged(currentPage);
            }
        });
    }

    private int calculatePageLength() {
        return pagesContainer.getOffsetHeight() / numberOfPages;
    }

    private Image createPage(String url) {
        final Image page = new Image(url);
        page.addLoadHandler(new LoadHandler() {
            @Override
            public void onLoad(LoadEvent event) {
                pageWidth = page.getWidth();
                pageHeight = page.getHeight();
            }
        });
        page.addStyleName(StyleName.PAGE);
        updateHeight();
        pages.add(page);
        numberOfPages++;
        return page;
    }

    private void updateHeight() {
        double height = Window.getClientHeight() - getAbsoluteTop() - 50;
        getElement().getStyle().setHeight(height, Style.Unit.PX);
    }

    public void setZoom(int value) {
        for (Image page : pages)
            zoomPage(page, zoomFactor(value));
    }

    private void zoomPage(Image page, double zoomFactor) {
        if (zoomFactor <= 0) return;
        final Style style = page.getElement().getStyle();
        style.setWidth(pageWidth * zoomFactor, Style.Unit.PX);
        style.setHeight(pageHeight * zoomFactor, Style.Unit.PX);
    }

    private double zoomFactor(int value) {
        return 1 + value * ZOOM_FACTOR;
    }

    public void showLoading(int secondsRemaining) {
        pagesContainer.add(new Label(loadingDocumentString.replace("::TIME::", String.valueOf(secondsRemaining))));
    }

    public void hideLoading() {
        pagesContainer.clear();
    }

    public interface PageChangeHandler {
        void pageChanged(int pageNumber);
    }

    public interface StyleName {
        String PAGE = "page";
        String PAGES_CONTAINER = "pages-container";
        String PAGES_VIEWER = "pages-viewer";
    }
}
