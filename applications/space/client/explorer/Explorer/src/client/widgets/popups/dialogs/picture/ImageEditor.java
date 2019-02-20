package client.widgets.popups.dialogs.picture;

import client.core.model.definition.entity.field.PictureFieldDefinition.SizeDefinition;
import client.services.TranslatorService;
import client.widgets.popups.dialogs.picture.imagecropper.Area;
import client.widgets.popups.dialogs.picture.imagecropper.ImageCropper;
import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.ImageElement;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.VerticalPanel;
import cosmos.gwt.utils.ImageUtils;

import java.util.HashMap;
import java.util.Map;

import static client.services.TranslatorService.OperationLabel;

public class ImageEditor extends VerticalPanel {

    private static final int WIDTH = 0;
    private static final int HEIGHT = 1;
    private static final int MAX_HEIGHT = 300;
    private final SizeDefinition imageSize;
    private final ImageCropper cropper;
    private String initialImage;
    private double scale;
    private String enlargedImage;
    private Element unscaledImage;
    private Map<String, Action> actions;

    public ImageEditor(final TranslatorService translator, SizeDefinition imageSize) {
        this.imageSize = imageSize;
        this.cropper = new ImageCropper(imageSize, translator.translate(TranslatorService.Label.IMAGE_TOO_BIG));
        createActions(translator);
        enlargedImage = "";
        scale = 1;
    }

    private void createActions(TranslatorService translator) {
        actions = new HashMap<>();
        actions.put(translator.translate(OperationLabel.RESTORE), new Action() {
            @Override
            public void apply() {
                setImage(initialImage);
            }
        });
        actions.put(translator.translate(OperationLabel.CROP_IMAGE), new Action() {
            @Override
            public void apply() {
                updateImage(getSelectedImage());
            }
        });
    }

    public Map<String, Action> getAvailableActions() {
        return actions;
    }

    public HandlerRegistration addClickHandler(ClickHandler handler) {
        return cropper.addClickHandler(handler);
    }

    public void getImage(SuccessCallback<String> callback) {
        if (imageSize != null)
            ImageUtils.scaleImage(enlargedImage.isEmpty() ? getSelectedImage() : enlargedImage, imageSize.getWidth(), imageSize.getHeight(), callback);
        else
            callback.onSuccess(getSelectedImage());
    }

    private String getSelectedImage() {
        Context2d context = createCanvas(cropper.getSelectedArea()).getContext2d();
        context.drawImage(ImageElement.as(unscaledImage), cropper.getSelectedLeft() * scale, cropper.getSelectedTop() * scale,
                cropper.getSelectedWidth() * scale, cropper.getSelectedHeight() * scale,
                0, 0, cropper.getSelectedWidth() * scale, cropper.getSelectedHeight() * scale);
        return ImageUtils.getImageAsBase64(context.getCanvas());
    }

    public void deleteImage() {
        this.initialImage = "";
        cropper.setVisible(false);
    }

    public void setImage(final String image) {
        initialImage = image;
        updateImage(image);
    }

    private void updateImage(final String image) {
        ImageUtils.getImageDimension(image, new SuccessCallback<JsArrayInteger>() {
            @Override
            public void onSuccess(final JsArrayInteger dimension) {
                ImageUtils.getImageElement(image, new SuccessCallback<Element>() {
                    @Override
                    public void onSuccess(Element element) {
                        unscaledImage = element;
                        scale = calculateScale(dimension);
                        showImage(dimension, image);
                    }
                });
            }
        });
    }

    private void showImage(JsArrayInteger size, String image) {
        if (scale > 1)
            showScaledImage(image, size);
        else if (imageSize != null && (imageSize.getWidth() > size.get(WIDTH) || imageSize.getHeight() > size.get(HEIGHT)))
            showEnlargedImage(image);
        else
            showImage(image);
    }

    private void showEnlargedImage(String image) {
        ImageUtils.scaleImage(image, imageSize.getWidth(), imageSize.getHeight(), new SuccessCallback<String>() {
            @Override
            public void onSuccess(String result) {
                enlargedImage = result;
                showImage(result);
            }
        });
    }

    private double calculateScale(JsArrayInteger size) {
        if (size.get(WIDTH) < getOffsetWidth() && size.get(HEIGHT) < MAX_HEIGHT)
            return 1;
        if (size.get(WIDTH) > size.get(HEIGHT))
            return Math.max(1.0, (double) size.get(WIDTH) / (MAX_HEIGHT * ((double)imageSize.getWidth() / imageSize.getHeight())));
        return Math.max(1.0, (double) size.get(HEIGHT) / MAX_HEIGHT);
    }

    private void showScaledImage(String image, JsArrayInteger size) {
        ImageUtils.scaleImage(image, size.get(WIDTH) / scale, size.get(HEIGHT) / scale, new SuccessCallback<String>() {
            @Override
            public void onSuccess(String result) {
                showImage(result);
            }
        });
    }

    private void showImage(String image) {
        cropper.setImage(image);
        cropper.setVisible(true);
        if (!cropper.isAttached()) insert(cropper, 0);
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                updateCropperState();
            }
        });
    }

    private void updateCropperState() {
        if (imageCanBeResized())
            cropper.enableSizeChange();
        else
            cropper.disableSizeChange();
    }

    private boolean imageCanBeResized() {
        return imageSize == null;
    }

    private CanvasElement createCanvas(Area area) {
        CanvasElement canvas= Canvas.createIfSupported().getCanvasElement();
        canvas.setWidth(area.getScaledWidth(scale));
        canvas.setHeight(area.getScaledHeight(scale));
        return canvas;
    }

    public static abstract class SuccessCallback<Type> implements Callback<Type, String> {
        @Override
        public void onFailure(String reason) {
        }
    }

    public interface Action {
        void apply();
    }
}
