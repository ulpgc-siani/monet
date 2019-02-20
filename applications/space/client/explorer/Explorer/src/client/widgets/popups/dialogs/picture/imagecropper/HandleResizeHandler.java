package client.widgets.popups.dialogs.picture.imagecropper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RootPanel;

import static client.core.model.definition.entity.field.PictureFieldDefinition.SizeDefinition;

abstract class HandleResizeHandler implements MouseDownHandler, MouseMoveHandler, MouseUpHandler{

    private static final int HANDLE_WIDTH = 10;
    protected final Element areaSelector;
    private final SizeDefinition imageSize;
    private HandlerRegistration mouseMoveHandlerRegistration;
    private HandlerRegistration mouseUpHandlerRegistration;
    protected Position lastPosition;
    protected Position currentPosition;
    private ResizeListener resizeListener;
    protected double ratio;

    public HandleResizeHandler(Element areaSelector, SizeDefinition imageSize) {
        this.areaSelector = areaSelector;
        this.imageSize = imageSize;
        this.ratio = 0;
        this.lastPosition = new Position(0, 0);
        this.currentPosition = lastPosition;
    }

    public void setResizeListener(ResizeListener resizeListener) {
        this.resizeListener = resizeListener;
    }

    public void setRatio(double ratio) {
        this.ratio = ratio;
    }

    @Override
    public void onMouseDown(MouseDownEvent event) {
        lastPosition = new Position(event.getRelativeX(areaSelector.getPreviousSiblingElement()), event.getRelativeY(areaSelector.getPreviousSiblingElement()));
        currentPosition = lastPosition;
        mouseMoveHandlerRegistration = RootPanel.get().addDomHandler(this, MouseMoveEvent.getType());
        mouseUpHandlerRegistration = RootPanel.get().addDomHandler(this, MouseUpEvent.getType());
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
        if (isOutOfBounds(event)) return;
        currentPosition = getNewPosition(event);
        SizeCalculator sizeCalculator = getSizeCalculator();

        if (sizeCalculator.getWidth() < HANDLE_WIDTH || sizeCalculator.getHeight() < HANDLE_WIDTH ||
           (imageSize != null && (sizeCalculator.getWidth() > imageSize.getWidth() || sizeCalculator.getHeight() > imageSize.getHeight()))) return;

        update(createAreaWithRatio(getLeft(), getTop(), sizeCalculator.getWidth(), sizeCalculator.getHeight()));
        resizeListener.onResize(areaSelector.getOffsetLeft(), areaSelector.getOffsetTop(), areaSelector.getOffsetWidth(), areaSelector.getOffsetHeight());
        event.preventDefault();
        event.stopPropagation();
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        mouseMoveHandlerRegistration.removeHandler();
        mouseUpHandlerRegistration.removeHandler();
    }

    private Area createAreaWithRatio(int left, int top, int width, int height) {
        if (ratio == 0) return new Area(left, top, width, height);
        if (Math.abs(lastPosition.getLeft() - left) > Math.abs(lastPosition.getTop() - top))
            return createAreaPreservingRatioOnXAxis(left, top, width, height);
        return createAreaPreservingRatioOnYAxis(left, top, width, height);
    }

    private void update(Area area) {
        updateSelectedArea(area);
        updateLastPosition(area);
    }

    protected int borderWidth() {
        return areaSelector.getOffsetHeight() - areaSelector.getClientHeight();
    }

    protected int getContainerWidth() {
        return areaSelector.getPreviousSiblingElement().getOffsetWidth() - borderWidth();
    }

    protected int getContainerHeight() {
        return areaSelector.getPreviousSiblingElement().getOffsetHeight() - borderWidth();
    }

    protected boolean isMouseAboveTop(MouseMoveEvent event) {
        return event.getRelativeY(areaSelector.getPreviousSiblingElement()) + borderWidth() <= 0;
    }

    protected boolean isMouseBeforeLeftEdge(MouseMoveEvent event) {
        return event.getRelativeX(areaSelector.getPreviousSiblingElement()) + borderWidth() <= 0;
    }

    protected boolean isMouseBelowBottom(MouseMoveEvent event) {
        return event.getRelativeY(areaSelector.getPreviousSiblingElement()) + borderWidth() > areaSelector.getPreviousSiblingElement().getOffsetHeight();
    }

    protected boolean isMouseAfterRightEdge(MouseMoveEvent event) {
        return event.getRelativeX(areaSelector.getPreviousSiblingElement()) + borderWidth() > areaSelector.getPreviousSiblingElement().getOffsetWidth();
    }

    protected Position getNewPosition(MouseMoveEvent event) {
        Element element = areaSelector.getPreviousSiblingElement();
        return new Position(event.getRelativeX(element), event.getRelativeY(element));
    }

    protected void updateSelectedArea(Area area) {
        areaSelector.getStyle().setLeft(area.getLeft(), Unit.PX);
        areaSelector.getStyle().setTop(area.getTop(), Unit.PX);
        areaSelector.getStyle().setWidth(area.getWidth(), Unit.PX);
        areaSelector.getStyle().setHeight(area.getHeight(), Unit.PX);
    }

    protected abstract Area createAreaPreservingRatioOnXAxis(int left, int top, int width, int height);

    protected abstract Area createAreaPreservingRatioOnYAxis(int left, int top, int width, int height);

    protected abstract SizeCalculator getSizeCalculator();

    protected abstract int getTop();

    protected abstract void updateLastPosition(Area area);

    protected abstract int getLeft();

    protected abstract boolean isOutOfBounds(MouseMoveEvent event);

    public interface ResizeListener {
        void onResize(int left, int top, int width, int height);
    }

    protected class SizeCalculator {
        private final int offsetWidth;
        private final int offsetHeight;

        public SizeCalculator(int offsetWidth, int offsetHeight) {
            this.offsetWidth = offsetWidth;
            this.offsetHeight = offsetHeight;
        }

        public int getWidth() {
            return areaSelector.getOffsetWidth() + offsetWidth - borderWidth();
        }

        public int getHeight() {
            return areaSelector.getOffsetHeight() + offsetHeight - borderWidth();
        }
    }

}
