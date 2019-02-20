package client.widgets.popups.dialogs.picture.imagecropper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RootPanel;
import cosmos.gwt.widgets.CosmosHtmlPanel;

import java.util.ArrayList;
import java.util.List;

import static client.core.model.definition.entity.field.PictureFieldDefinition.SizeDefinition;

class AreaSelector extends CosmosHtmlPanel {

    private static final int HANDLE_WIDTH = 12;
    private int maxWidth;
    private int maxHeight;
    private SelectionChangedHandler selectionChangeHandler;
    private List<HandleWidget> handles;
    private HandlerRegistration mouseMoveHandlerRegistration;
    private HandlerRegistration mouseUpHandlerRegistration;
    private final SizeDefinition imageSize;

    public AreaSelector(SizeDefinition imageSize) {
        super();
        this.imageSize = imageSize;
        addStyleName(StyleName.AREA_SELECTOR);
        addDomHandler(new MouseHandler(getElement()), MouseDownEvent.getType());
        addHandles();
    }

    public void setMaxWidth(int maxWidth) {
        this.maxWidth = maxWidth;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setSelectionChangedHandler(SelectionChangedHandler selectionChangeHandler) {
        this.selectionChangeHandler = selectionChangeHandler;
        for (HandleWidget handle : handles)
            handle.setSizeChangeHandler(selectionChangeHandler);
    }

    private int getSelectedWidth() {
        return Integer.valueOf(getElement().getStyle().getWidth().replace("px", ""));
    }

    private void addHandles() {
        handles = new ArrayList<>();
        for (HandlePosition handlePosition : HandlePosition.getAvailablePositions())
            handles.add(createHandle(handlePosition));
        for (HandleWidget handle : handles)
            add(handle);
    }

    private HandleWidget createHandle(final HandlePosition handlePosition) {
        HandleWidget handle = new HandleWidget(getElement(), handlePosition, imageSize);
        if (imageSize != null)
            handle.setRatio((double) imageSize.getWidth() / imageSize.getHeight());
        return handle;
    }

    public void disableSizeChange() {
        for (HandleWidget handle : handles)
            handle.setVisible(false);
    }

    public void enableSizeChange() {
        for (HandleWidget handle : handles)
            handle.setVisible(true);
    }

    public interface StyleName {
        String AREA_SELECTOR = "area-selector";
    }

    public interface SelectionChangedHandler {
        void selectedArea(Area area);
    }

    private class MouseHandler implements MouseDownHandler, MouseMoveHandler, MouseUpHandler {

        private final Element element;
        private Position lastPosition;

        public MouseHandler(Element element) {
            this.element = element;
        }

        @Override
        public void onMouseDown(MouseDownEvent event) {
            if (clickOnHandle(event)) return;
            lastPosition = new Position(event.getRelativeX(element), event.getRelativeY(element));
            mouseMoveHandlerRegistration = RootPanel.get().addDomHandler(this, MouseMoveEvent.getType());
            mouseUpHandlerRegistration = RootPanel.get().addDomHandler(this, MouseUpEvent.getType());
        }

        private boolean clickOnHandle(MouseDownEvent event) {
            return event.getRelativeX(element) < HANDLE_WIDTH || getSelectedWidth() - event.getRelativeX(element) < HANDLE_WIDTH;
        }

        @Override
        public void onMouseMove(MouseMoveEvent event) {
            if (nextXPositionIsInsideArea(event.getRelativeX(element)))
                element.getStyle().setLeft(calculateNextXPosition(event.getRelativeX(element)), Unit.PX);
            if (nextYPositionIsInsideArea(event.getRelativeY(element)))
                element.getStyle().setTop(calculateNextYPosition(event.getRelativeY(element)), Unit.PX);
            lastPosition = new Position(event.getRelativeX(element), event.getRelativeY(element));
            selectionChangeHandler.selectedArea(new Area(element.getOffsetLeft(), element.getOffsetTop(), element.getOffsetWidth(), element.getOffsetHeight()));
        }

        @Override
        public void onMouseUp(MouseUpEvent event) {
            mouseMoveHandlerRegistration.removeHandler();
            mouseUpHandlerRegistration.removeHandler();
        }

        private boolean nextXPositionIsInsideArea(int relativeX) {
            return calculateNextXPosition(relativeX) >= 0 && maxWidth > calculateNextXPosition(relativeX) + AreaSelector.this.getOffsetWidth();
        }

        private boolean nextYPositionIsInsideArea(int relativeY) {
            return calculateNextYPosition(relativeY) >= 0 && maxHeight > calculateNextYPosition(relativeY) + AreaSelector.this.getOffsetHeight();
        }

        private int calculateNextXPosition(int relativeX) {
            return (relativeX - lastPosition.getLeft()) + element.getOffsetLeft();
        }

        private int calculateNextYPosition(int relativeY) {
            return (relativeY - lastPosition.getTop()) + element.getOffsetTop();
        }
    }
}
