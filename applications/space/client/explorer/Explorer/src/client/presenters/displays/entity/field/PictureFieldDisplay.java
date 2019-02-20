package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.PictureFieldDefinition;
import client.core.model.fields.PictureField;
import client.core.model.types.Picture;
import client.presenters.displays.entity.FieldDisplay;
import client.services.TranslatorService;
import client.services.callback.Callback;

public class PictureFieldDisplay extends FieldDisplay<PictureField, PictureFieldDefinition, Picture> implements IsPictureFieldDisplay {

	public static final Type TYPE = new Type("PictureFieldDisplay", FieldDisplay.TYPE);

	public PictureFieldDisplay(Node node, PictureField field) {
		super(node, field);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public String getValueAsString() {
		return getPictureLabel();
	}

	@Override
	protected Picture format(Picture picture) {
		return picture;
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
	public void addHook(Hook hook) {
		super.addHook(hook);
	}

	@Override
	public String getThumbnail() {
		return hasValue() ? services.getNodeService().getDownloadNodeThumbnailUrl(node, getValue().getId()) : services.getTranslatorService().getNoPhoto();
	}

	@Override
	public String getPictureLabel() {
		return getValue().getLabel();
	}

	@Override
	public String getPictureLink() {
		return hasValue() ? getProfilePictureUrl(getValue()) : services.getTranslatorService().getNoPhoto();
	}

	@Override
	public void setValue(Picture picture) {
		if (picture == null)
			super.setValue(null);
		else
			super.setValue(getNewValue(picture));
		if (getDefinition().isProfilePhoto())
			updateProfilePhoto(picture == null ? null : getNewValue(picture));
	}

	private Picture getNewValue(Picture picture) {
		return (!hasValue() || labelIsSameAsFilename()) ? picture : createPicture(picture.getId(), getPictureLabel());
	}

	private void updateProfilePhoto(Picture profilePhoto) {
		services.getAccountService().saveProfilePhoto(getProfilePictureUrl(profilePhoto));
	}

	private String getProfilePictureUrl(Picture profilePhoto) {
		if (profilePhoto == null)
			return "";
		return services.getNodeService().getDownloadNodeImageUrl(node, profilePhoto.getId());
	}

	@Override
	public void updateFilename(String filename) {
		if (hasValue())
			super.setValue(createPicture(getValue().getId(), filename));
	}

	@Override
	public void savePicture(final String filename, String data) {
		services.getNodeService().savePicture(filename, data, node, new Callback<String>() {
			@Override
			public void success(String id) {
				setValue(createPicture(id, filename));
			}

			@Override
			public void failure(String error) {
				notifyError(error, TranslatorService.ErrorLabel.SAVE_VALUE);
			}
		});
	}

	private boolean labelIsSameAsFilename() {
		return getName(getPictureLabel()).equals(getName(getValue().getId()));
	}

	private String getName(String label) {
		return label.substring(label.lastIndexOf("/") + 1).replaceAll("\\s", "");
	}
}
