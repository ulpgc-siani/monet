package client.core.system.definition.entity.field;

import client.core.model.Instance;

public class PictureFieldDefinition extends FileFieldDefinition implements client.core.model.definition.entity.field.PictureFieldDefinition {
	private String defaultValue;
	private boolean isProfilePhoto;
	private client.core.model.definition.entity.field.PictureFieldDefinition.SizeDefinition size;

    @Override
	public Instance.ClassName getClassName() {
		return client.core.model.definition.entity.field.PictureFieldDefinition.CLASS_NAME;
	}

	@Override
	public String getDefault() {
		return defaultValue;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	@Override
	public boolean isProfilePhoto() {
		return this.isProfilePhoto;
	}

	public void setIsProfilePhoto(boolean isProfilePhoto) {
		this.isProfilePhoto = isProfilePhoto;
	}

	@Override
	public client.core.model.definition.entity.field.PictureFieldDefinition.SizeDefinition getSize() {
		return size;
	}

	public void setSize(client.core.model.definition.entity.field.PictureFieldDefinition.SizeDefinition size) {
		this.size = size;
	}

    public static class SizeDefinition implements client.core.model.definition.entity.field.PictureFieldDefinition.SizeDefinition {
		private long width;
		private long height;

		@Override
		public long getWidth() {
			return width;
		}

		public void setWidth(long width) {
			this.width = width;
		}

		@Override
		public long getHeight() {
			return height;
		}

		public void setHeight(long height) {
			this.height = height;
		}
	}
}
