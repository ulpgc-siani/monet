package client.widgets.popups.dialogs.picture.imagecropper;

import client.core.model.definition.entity.field.PictureFieldDefinition.SizeDefinition;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

public class ImageCropper extends AbsolutePanel {

    private static final int SELECTOR_BORDER = 2;
    private final Image image;
    private final Image selectedImage;
    private final AreaSelector selector;
    private final SizeDefinition imageSize;
    private final Label imageNeedsToBeCroppedLabel;
    private Area selectedArea;

    public ImageCropper(SizeDefinition imageSize, String imageNeedsToBeCroppedMessage) {
        super();
        this.imageSize = imageSize;
        this.image = new Image();
        this.selectedImage = new Image();
        this.selector = createSelector();
        imageNeedsToBeCroppedLabel = new Label(imageNeedsToBeCroppedMessage);
	    imageNeedsToBeCroppedLabel.addStyleName(StyleName.MESSAGE);
        add(createImageContainer());
        add(selector);
        add(imageNeedsToBeCroppedLabel);
        addStyleName(StyleName.CROPPER);
    }

    public int getSelectedLeft() {
        return selectedArea.getLeft();
    }

    public int getSelectedTop() {
        return selectedArea.getTop();
    }

    public int getSelectedWidth() {
        return selectedArea.getWidth();
    }

    public int getSelectedHeight() {
        return selectedArea.getHeight();
    }

    public Area getSelectedArea() {
        return selectedArea;
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return selector.addDomHandler(handler, ClickEvent.getType());
    }

    public void setImage(final String image) {
        this.image.setUrl(image);
        this.image.addLoadHandler(new LoadHandler() {
            @Override
            public void onLoad(LoadEvent event) {
                updateSelectedImage(image);
                updateSelector();
                checkImageSize();
            }
        });
    }

    private void checkImageSize() {
        if (imageSize == null) return;
        if (imageSize.getWidth() < image.getWidth() || imageSize.getHeight() < image.getHeight())
            imageNeedsToBeCroppedLabel.setVisible(true);
        else
            imageNeedsToBeCroppedLabel.setVisible(false);
    }

    private AbsolutePanel createImageContainer() {
        AbsolutePanel imageContainer = new AbsolutePanel();
        imageContainer.setStyleName(StyleName.IMAGE_CONTAINER);
        imageContainer.add(image);
        return imageContainer;
    }

    private AreaSelector createSelector() {
        AreaSelector selector = new AreaSelector(imageSize);
        selector.setSelectionChangedHandler(new AreaSelector.SelectionChangedHandler() {
            @Override
            public void selectedArea(Area area) {
                updateSelectedArea(area);
            }
        });
        return selector;
    }

    private void updateSelector() {
        selector.getElement().getStyle().setLeft(selectedArea.getLeft(), Unit.PX);
        selector.getElement().getStyle().setTop(selectedArea.getTop(), Unit.PX);
        selector.setMaxWidth(image.getWidth());
        selector.setMaxHeight(image.getHeight());
        selector.setWidth((selectedArea.getWidth() - SELECTOR_BORDER) + Unit.PX.toString());
        selector.setHeight((selectedArea.getHeight() - SELECTOR_BORDER) + Unit.PX.toString());
    }

    private boolean imageHasRightRatio() {
        return Math.abs((double) image.getWidth() / image.getHeight() - calculateRatio()) <= 0.1;
    }

    private double calculateRatio() {
        return (double) imageSize.getWidth() / imageSize.getHeight();
    }

    private boolean imageIsGreaterThanRequired() {
        return image.getWidth() >= imageSize.getWidth() && image.getHeight() >= imageSize.getHeight();
    }

    private void updateSelectedImage(String image) {
        selectedImage.setUrl(image);
        if (!selectedImage.isAttached())
            add(selectedImage);
        if (imageSize != null)
            selectWithRatio();
        else
            updateSelectedArea(new Area(0, 0, this.image.getWidth() / 2, this.image.getHeight() / 2));
        selectedImage.setStyleName(StyleName.SELECTED_IMAGE);
    }

    private void selectWithRatio() {
        if (imageIsGreaterThanRequired())
            selectArea((int) imageSize.getWidth(), (int) imageSize.getHeight());
        else if (imageHasRightRatio())
            selectArea(image.getWidth(), image.getHeight());
        else if (image.getWidth() > imageSize.getWidth())
            selectArea((int) (image.getHeight() * calculateRatio()), image.getHeight());
        else if (image.getHeight() > imageSize.getHeight())
            selectArea(image.getWidth(), (int) ((double) image.getWidth() / calculateRatio()));
        else
            selectArea(image.getWidth(), (int) ((double) image.getHeight() / calculateRatio()));
    }

    private void selectArea(int width, int height) {
        updateSelectedArea(new Area(0, 0, width, height));
    }

    private void updateSelectedArea(Area area) {
        selectedImage.getElement().getStyle().setLeft(area.getLeft(), Unit.PX);
        selectedImage.getElement().getStyle().setTop(area.getTop(), Unit.PX);
        selectedImage.setVisibleRect(area.getLeft(), area.getTop(), area.getWidth(), area.getHeight());
        selectedArea = area;
    }

    public void enableSizeChange() {
        selector.enableSizeChange();
    }

    public void disableSizeChange() {
        selector.disableSizeChange();
    }

    public interface StyleName {
        String CROPPER = "cropper";
        String MESSAGE = "message";
        String IMAGE_CONTAINER = "image-container";
        String SELECTED_IMAGE = "selected-image";
    }
}
