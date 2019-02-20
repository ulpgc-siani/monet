package client.presenters.displays.entity.field;

import client.core.model.types.Picture;
import client.presenters.displays.IsMultipleDisplay;

public interface IsMultiplePictureFieldDisplay extends IsPictureFieldDisplay, IsMultipleDisplay<Picture> {

    String getThumbnail(Picture picture);
    String getPictureLink(Picture picture);
}
