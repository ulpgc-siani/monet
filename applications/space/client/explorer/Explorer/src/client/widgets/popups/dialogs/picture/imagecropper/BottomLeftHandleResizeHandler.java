package client.widgets.popups.dialogs.picture.imagecropper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseMoveEvent;

import static client.core.model.definition.entity.field.PictureFieldDefinition.SizeDefinition;

class BottomLeftHandleResizeHandler extends HandleResizeHandler {

    public BottomLeftHandleResizeHandler(Element areaSelector, SizeDefinition imageSize) {
        super(areaSelector, imageSize);
    }

    @Override
    protected boolean isOutOfBounds(MouseMoveEvent event) {
        return isMouseBelowBottom(event) || isMouseBeforeLeftEdge(event);
    }

    @Override
    protected int getLeft() {
        return currentPosition.getLeft();
    }

    @Override
    protected int getTop() {
        return Integer.valueOf(areaSelector.getStyle().getTop().replace("px", ""));
    }

    @Override
    protected Area createAreaPreservingRatioOnXAxis(int left, int top, int width, int height) {
        height = (int) (width / ratio);
        if ((height + top) >= getContainerHeight()) {
            height = getContainerHeight() - top;
            width = (int) (height * ratio);
            top = getContainerHeight() - height;
            left = currentPosition.getLeft() - (int) ((getContainerHeight() - currentPosition.getTop()) * ratio);
        }
        return new Area(left, top, width, height);
    }

    @Override
    protected Area createAreaPreservingRatioOnYAxis(int left, int top, int width, int height) {
        int newWidth = (int) (height * ratio);
        left -= newWidth - width;
        if (left < 0) {
            newWidth = width + currentPosition.getLeft();
            height = (int) (newWidth / ratio);
            left = 0;
        }
        return new Area(left, top, newWidth, height);
    }

    @Override
    protected SizeCalculator getSizeCalculator() {
        return new SizeCalculator(lastPosition.getLeft() - currentPosition.getLeft(), currentPosition.getTop() - lastPosition.getTop());
    }

    @Override
    protected void updateLastPosition(Area area) {
        lastPosition = new Position(area.getLeft(), area.getTop() + area.getHeight());
    }
}
