package client.widgets.popups.dialogs.picture.imagecropper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.MouseMoveEvent;

import static client.core.model.definition.entity.field.PictureFieldDefinition.SizeDefinition;

class BottomRightHandleResizeHandler extends HandleResizeHandler {

    public BottomRightHandleResizeHandler(Element areaSelector, SizeDefinition imageSize) {
        super(areaSelector, imageSize);
    }

    @Override
    protected boolean isOutOfBounds(MouseMoveEvent event) {
        return isMouseBelowBottom(event) || isMouseAfterRightEdge(event);
    }

    @Override
    protected int getLeft() {
        return Integer.valueOf(areaSelector.getStyle().getLeft().replace("px", ""));
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
            currentPosition = new Position(getContainerWidth(), currentPosition.getTop());
        }
        return new Area(left, top, width, height);
    }

    @Override
    protected Area createAreaPreservingRatioOnYAxis(int left, int top, int width, int height) {
        width = (int) (height * ratio);
        if ((width + left) >= getContainerWidth()) {
            width = getContainerWidth() - left;
            height = (int) (width / ratio);
            currentPosition = new Position(currentPosition.getLeft(), getContainerHeight());
        }
        return new Area(left, top, width, height);
    }

    @Override
    protected SizeCalculator getSizeCalculator() {
        return new SizeCalculator(currentPosition.getLeft() - lastPosition.getLeft(), currentPosition.getTop() - lastPosition.getTop());
    }

    @Override
    protected void updateLastPosition(Area area) {
        lastPosition = new Position(area.getLeft() + area.getWidth(), area.getTop() + area.getHeight());
    }
}
