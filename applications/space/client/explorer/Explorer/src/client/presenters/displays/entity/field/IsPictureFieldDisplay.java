package client.presenters.displays.entity.field;

import client.core.model.definition.entity.field.PictureFieldDefinition;
import client.core.model.types.Picture;

public interface IsPictureFieldDisplay extends IsFieldDisplay<Picture> {

	Picture createPicture(String id, String label);

	PictureFieldDefinition.SizeDefinition getSize();
    long getLimit();
	String getPictureLabel();
	String getPictureLink();
	String getThumbnail();

	void updateFilename(String filename);
	void savePicture(String filename, String data);

	void addHook(PictureFieldDisplay.Hook hook);
}
