package client.presenters.displays.entity.field;

import client.core.model.types.File;

public interface IsFileFieldDisplay extends IsFieldDisplay<File> {

	String getFileLabel();
	String getFileId();
    long getLimit();
	boolean fileHasLabel();

	void updateFilename(String filename);
	void addHook(FileFieldDisplay.Hook hook);

	void saveFile(String filename, String data);
}
