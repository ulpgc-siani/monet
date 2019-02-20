package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.PictureFieldDefinition;
import client.core.model.fields.MultiplePictureField;
import client.core.model.types.Picture;
import client.presenters.displays.entity.MultipleFieldDisplay;
import client.services.TranslatorService;
import client.services.callback.Callback;

public class MultiplePictureFieldDisplay extends MultipleFieldDisplay<MultiplePictureField, PictureFieldDefinition, Picture> implements IsMultiplePictureFieldDisplay {

    public static final Type TYPE = new Type("MultiplePictureFieldDisplay", MultipleFieldDisplay.TYPE);

    public MultiplePictureFieldDisplay(Node node, MultiplePictureField field) {
        super(node, field);
    }

    @Override
    public Type getType() {
        return TYPE;
    }

    @Override
    public String getValueAsString() {
        return "";
    }

    @Override
    public Picture createPicture(String id, String label) {
        return getTypeFactory().createPicture(id, label);
    }

    @Override
    public PictureFieldDefinition.SizeDefinition getSize() {
        return getDefinition().getSize();
    }

    @Override
    public long getLimit() {
        return megaBytesToBytes();
    }

    private long megaBytesToBytes() {
        return getDefinition().getLimit() * 1024 * 1024;
    }

    @Override
    public String getPictureLabel() {
        return null;
    }

    @Override
    public String getPictureLink() {
        return hasValue() ? services.getNodeService().getDownloadNodeImageUrl(node, getValue().getId()) : services.getTranslatorService().getNoPhoto();
    }

    @Override
    public String getPictureLink(Picture picture) {
        return services.getNodeService().getDownloadNodeImageUrl(node, picture.getId());
    }

    @Override
    public void addHook(PictureFieldDisplay.Hook hook) {
        super.addHook(hook);
    }

    @Override
    public String getThumbnail() {
        return services.getTranslatorService().getAddPhoto();
    }

    @Override
    public String getThumbnail(Picture picture) {
        return services.getNodeService().getDownloadNodeThumbnailUrl(node, picture.getId());
    }

    @Override
    public void updateFilename(String filename) {
    }

    @Override
    public void savePicture(final String filename, String data) {
        services.getNodeService().saveFile(filename, data, node, new Callback<String>() {
            @Override
            public void success(String id) {
                add(createPicture(id, filename));
            }

            @Override
            public void failure(String error) {
                notifyError(error, TranslatorService.ErrorLabel.SAVE_VALUE);
            }
        });
    }
}
