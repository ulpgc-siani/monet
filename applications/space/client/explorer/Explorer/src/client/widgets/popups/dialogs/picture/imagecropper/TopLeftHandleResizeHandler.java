package client.widgets.popups.dialogs.picture.imagecropper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseMoveEvent;

import static client.core.model.definition.entity.field.PictureFieldDefinition.SizeDefinition;

class TopLeftHandleResizeHandler extends HandleResizeHandler {

    public TopLeftHandleResizeHandler(Element areaSelector, SizeDefinition imageSize) {
        super(areaSelector, imageSize);
    }

    @Override
    protected boolean isOutOfBounds(MouseMoveEvent event) {
        return isMouseBeforeLeftEdge(event) || isMouseAboveTop(event);
    }

    @Override
    protected int getLeft() {
        return currentPosition.getLeft();
    }

    @Override
    protected int getTop() {
        return currentPosition.getTop();
    }

    @Override
    protected SizeCalculator getSizeCalculator() {
        return new SizeCalculator(lastPosition.getLeft() - currentPosition.getLeft(), lastPosition.getTop() - currentPosition.getTop());
    }

    @Override
    protected Area createAreaPreservingRatioOnXAxis(int left, int top, int width, int height) {
        int newHeight = (int) (width / ratio);
        top -= newHeight - height;
        if (top < 0) {
            top = 0;
            newHeight = currentPosition.getTop() + height;
            width = (int) (newHeight * ratio);
            left = currentPosition.getLeft() - (int) (currentPosition.getTop() * ratio);
        }
        return new Area(left, top, width, newHeight);
    }

    @Override
    protected Area createAreaPreservingRatioOnYAxis(int left, int top, int width, int height) {
        int newWidth = (int) (height * ratio);
        left -= newWidth - width;
        if (left < 0) {
            left = 0;
            newWidth = currentPosition.getTop() + width;
            height = (int) (newWidth / ratio);
            top = currentPosition.getTop() - (int) (currentPosition.getLeft() / ratio);
        }
        return new Area(left, top, newWidth, height);
    }

    @Override
    protected void updateLastPosition(Area area) {
        lastPosition = new Position(area.getLeft(), area.getTop());
    }
}
