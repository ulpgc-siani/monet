package client.widgets.view.document;

import client.core.model.List;
import client.core.system.MonetList;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;

public class VerticalThumbsViewerWidget extends ScrollPanel implements ThumbsViewerWidget {

    private VerticalPanel thumbsContainer;
    private SelectThumbHandler selectThumbHandler;
    private List<Image> thumbs;

    public VerticalThumbsViewerWidget() {
        super();
        create();
    }

    @Override
    public void addThumb(String url) {
        final IsWidget thumb = createThumb(url);
        thumbsContainer.add(thumb);
        thumbsContainer.setCellHorizontalAlignment(thumb, HasHorizontalAlignment.ALIGN_CENTER);
    }

    @Override
    public void setSelectThumbHandler(SelectThumbHandler selectThumbHandler) {
        this.selectThumbHandler = selectThumbHandler;
    }

    @Override
    public void clear() {
        thumbsContainer.clear();
    }

    @Override
    public void selectThumb(int number) {
        for (Image thumb : thumbs)
            thumb.removeStyleName(StyleName.SELECTED);
        thumbs.get(number).addStyleName(StyleName.SELECTED);
    }

    private void create() {
        addStyleName(StyleName.THUMBS_VIEWER);
        createComponent();
    }

    private void createComponent() {
        thumbsContainer = new VerticalPanel();
        thumbsContainer.addStyleName(StyleName.THUMBS_CONTAINER);
        thumbsContainer.setWidth("100%");
        add(thumbsContainer);
        thumbs = new MonetList<>();
        Window.addResizeHandler(new ResizeHandler() {
            @Override
            public void onResize(ResizeEvent event) {
                updateHeight();
            }
        });
    }

    private IsWidget createThumb(final String url) {
        final Image thumb = new Image(url);
        thumb.addStyleName(StyleName.THUMB);
        thumb.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                selectCurrentThumb(thumb);
            }
        });
        thumbs.add(thumb);
        updateHeight();
        return thumb;
    }

    private void selectCurrentThumb(Image thumb) {
        if (selectThumbHandler == null) return;
        selectThumbHandler.selected(thumbs.indexOf(thumb));
        selectThumb(thumbs.indexOf(thumb));
    }

    private void updateHeight() {
        double height = Window.getClientHeight() - getAbsoluteTop() - 50;
        getElement().getStyle().setHeight(height, Style.Unit.PX);
    }

    public interface StyleName extends ThumbsViewerWidget.StyleName { }
}
