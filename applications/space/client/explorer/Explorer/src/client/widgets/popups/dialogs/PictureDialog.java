package client.widgets.popups.dialogs;

import client.core.model.definition.entity.field.PictureFieldDefinition;
import client.services.TranslatorService;
import client.widgets.popups.dialogs.picture.ImageEditor;
import client.widgets.toolbox.FileUploaderWidget;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.Map;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class PictureDialog extends FileDialog {

    private static final String[] imageExtensions = new String[]{".jpg", ".jpeg", ".png", ".gif"};
    private final ImageEditor editor;
    private HandlerRegistration handlerRegistration;

    public PictureDialog(TranslatorService translator, long sizeLimit, PictureFieldDefinition.SizeDefinition imageMaxSize, String layout) {
        super(translator, sizeLimit, layout);
        editor = createImageEditor(translator, imageMaxSize);
        addSelectHandler();
        for (final Map.Entry<String, ImageEditor.Action> action : editor.getAvailableActions().entrySet()) {
            addOperation(action.getKey(), createOperation(action.getValue()));
        }
    }

    private FileUploaderWidget.Operation createOperation(final ImageEditor.Action action) {
        return new FileUploaderWidget.Operation() {
            @Override
            public void apply() {
                action.apply();
            }
        };
    }

    @Override
    public void deleteFile() {
        super.deleteFile();
        editor.deleteImage();
        $(this).closest(toRule(StyleName.POPUP)).removeClass(StyleName.WITH_IMAGE);
        editor.setVisible(false);
        fileSelector.submitFinished();
        if (handlerRegistration == null) return;
        handlerRegistration.removeHandler();
        handlerRegistration = null;
    }

    @Override
    protected void notifyRequest(final String filename, String data) {
        editor.getImage(new ImageEditor.SuccessCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    submitRequestHandler.request(filename, result);
                }
            });
    }

    private boolean isImage(String filename) {
        for (String extension : imageExtensions)
            if (filename.endsWith(extension)) return true;
        return false;
    }

    private void update(String data) {
        $(this).closest(toRule(StyleName.POPUP)).addClass(StyleName.WITH_IMAGE);
        editor.setImage(data);
        editor.setVisible(true);
    }

    private void addSelectHandler() {
        fileSelector.setFileSelectedHandler(new FileUploaderWidget.FileSelectedHandler() {
            @Override
            public void onSelect(String filename, String data) {
                if (isImage(filename.toLowerCase()))
                    update(data);
                else
                    deleteFile();
            }
        });
    }

    private ImageEditor createImageEditor(TranslatorService translator, PictureFieldDefinition.SizeDefinition imageMaxSize) {
        ImageEditor editor = new ImageEditor(translator, imageMaxSize);
        addComponent(toRule(StyleName.EDITOR), editor);
        return editor;
    }

    public interface StyleName {
        String EDITOR = "editor";
        String POPUP = "popup";
        String WITH_IMAGE = "with-image";
    }
}
