package client.widgets.popups.dialogs.picture.imagecropper;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;

import java.util.ArrayList;
import java.util.List;

import static client.core.model.definition.entity.field.PictureFieldDefinition.*;

abstract class HandlePosition {

    public static final int BORDER_WIDTH = 1;

    public static final HandlePosition TOP_LEFT = new HandlePosition() {
        @Override
        public void setHandlePositionValue(Element handle) {
            handle.getStyle().setTop(-BORDER_WIDTH, Style.Unit.PX);
            handle.getStyle().setLeft(-BORDER_WIDTH, Style.Unit.PX);
        }

        @Override
        public String getStyleName() {
            return StyleName.TOP_LEFT;
        }

        @Override
        public HandleResizeHandler createHandleResizeHandler(Element areaSelector, SizeDefinition imageSize) {
            return new TopLeftHandleResizeHandler(areaSelector, imageSize);
        }
    };

    public static final HandlePosition TOP_RIGHT = new HandlePosition() {
        @Override
        public void setHandlePositionValue(Element handle) {
            handle.getStyle().setTop(-BORDER_WIDTH, Style.Unit.PX);
            handle.getStyle().setRight(-BORDER_WIDTH, Style.Unit.PX);
        }

        @Override
        public String getStyleName() {
            return StyleName.TOP_RIGHT;
        }

        @Override
        public HandleResizeHandler createHandleResizeHandler(Element areaSelector, SizeDefinition imageSize) {
            return new TopRightHandleResizeHandler(areaSelector, imageSize);
        }
    };

    public static final HandlePosition BOTTOM_LEFT = new HandlePosition() {
        @Override
        public void setHandlePositionValue(Element handle) {
            handle.getStyle().setBottom(-BORDER_WIDTH, Style.Unit.PX);
            handle.getStyle().setLeft(-BORDER_WIDTH, Style.Unit.PX);
        }

        @Override
        public String getStyleName() {
            return StyleName.BOTTOM_LEFT;
        }

        @Override
        public HandleResizeHandler createHandleResizeHandler(Element areaSelector, SizeDefinition imageSize) {
            return new BottomLeftHandleResizeHandler(areaSelector, imageSize);
        }
    };

    public static final HandlePosition BOTTOM_RIGHT = new HandlePosition() {
        @Override
        public void setHandlePositionValue(Element handle) {
            handle.getStyle().setBottom(-BORDER_WIDTH, Style.Unit.PX);
            handle.getStyle().setRight(-BORDER_WIDTH, Style.Unit.PX);
        }

        @Override
        public String getStyleName() {
            return StyleName.BOTTOM_RIGHT;
        }

        @Override
        public HandleResizeHandler createHandleResizeHandler(Element areaSelector, SizeDefinition imageSize) {
            return new BottomRightHandleResizeHandler(areaSelector, imageSize);
        }
    };

    abstract String getStyleName();
    abstract void setHandlePositionValue(Element handle);
    abstract HandleResizeHandler createHandleResizeHandler(Element areaSelector, SizeDefinition imageSize);

    public static List<HandlePosition> getAvailablePositions() {
        List<HandlePosition> positions = new ArrayList<>();
        positions.add(TOP_LEFT);
        positions.add(TOP_RIGHT);
        positions.add(BOTTOM_LEFT);
        positions.add(BOTTOM_RIGHT);
        return positions;
    }

    public interface StyleName {
        String TOP_LEFT = "top-left";
        String TOP_RIGHT = "top-right";
        String BOTTOM_LEFT = "bottom-left";
        String BOTTOM_RIGHT = "bottom-right";
    }
}
