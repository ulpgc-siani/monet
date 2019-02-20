package client.widgets.popups.dialogs.picture.imagecropper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.HTML;

import static client.core.model.definition.entity.field.PictureFieldDefinition.*;

class HandleWidget extends HTML {

    private final HandleResizeHandler handleResizeHandler;

    public HandleWidget(Element areaSelector, HandlePosition position, SizeDefinition imageSize) {
        addStyleName(StyleName.HANDLE);
        addStyleName(position.getStyleName());
        handleResizeHandler = position.createHandleResizeHandler(areaSelector, imageSize);
        addMouseDownHandler(handleResizeHandler);
        position.setHandlePositionValue(getElement());
    }

    public void setSizeChangeHandler(final AreaSelector.SelectionChangedHandler handler) {
        handleResizeHandler.setResizeListener(new HandleResizeHandler.ResizeListener() {
            @Override
            public void onResize(int left, int top, int width, int height) {
                handler.selectedArea(new Area(left, top, width, height));
            }
        });
    }

    public void setRatio(double ratio){
        handleResizeHandler.setRatio(ratio);
    }

    public interface StyleName {
        String HANDLE = "handle";
    }
}
