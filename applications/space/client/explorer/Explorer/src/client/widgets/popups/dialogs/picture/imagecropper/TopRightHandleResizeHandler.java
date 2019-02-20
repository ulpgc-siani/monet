package client.widgets.popups.dialogs.picture.imagecropper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseMoveEvent;

import static client.core.model.definition.entity.field.PictureFieldDefinition.SizeDefinition;

class TopRightHandleResizeHandler extends HandleResizeHandler {

    public TopRightHandleResizeHandler(Element areaSelector, SizeDefinition imageSize) {
        super(areaSelector, imageSize);
    }

    @Override
    protected boolean isOutOfBounds(MouseMoveEvent event) {
        return isMouseAfterRightEdge(event) || isMouseAboveTop(event);
    }

    @Override
    protected int getLeft() {
        return Integer.valueOf(areaSelector.getStyle().getLeft().replace("px", ""));
    }

    @Override
    protected int getTop() {
        return currentPosition.getTop();
    }

    @Override
    protected Area createAreaPreservingRatioOnXAxis(int left, int top, int width, int height) {
        int newHeight = (int) (width / ratio);
        top -= newHeight - height;
        if (top < 0) {
            top = 0;
            newHeight = (currentPosition.getTop() + height);
            width = (int) (newHeight * ratio);
        }
        return new Area(left, top, width, newHeight);
    }

    @Override
    protected Area createAreaPreservingRatioOnYAxis(int left, int top, int width, int height) {
        width = (int) (height * ratio);
        if ((width + left) >= getContainerWidth()) {
            width = getContainerWidth() - left;
            height = (int) (width / ratio);
            top = currentPosition.getTop() - (int) ((getContainerWidth() - left - width) / ratio);
            currentPosition = new Position(currentPosition.getLeft(), top);
        }
        return new Area(left, top, width, height);
    }

    @Override
    protected SizeCalculator getSizeCalculator() {
        return new SizeCalculator(currentPosition.getLeft() - lastPosition.getLeft(), lastPosition.getTop() - currentPosition.getTop());
    }

    @Override
    protected void updateLastPosition(Area area) {
        lastPosition = new Position(area.getLeft() + area.getWidth(), area.getTop());
    }
}
