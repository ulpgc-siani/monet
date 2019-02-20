package client.presenters.displays.entity.field;

import client.core.model.types.File;
import client.presenters.displays.IsMultipleDisplay;

public interface IsMultipleFileFieldDisplay extends IsFileFieldDisplay, IsMultipleDisplay<File> {

    String getFileId(File file);
}
