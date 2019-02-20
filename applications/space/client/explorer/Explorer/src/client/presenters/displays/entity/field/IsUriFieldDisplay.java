package client.presenters.displays.entity.field;

import client.core.model.types.Uri;

public interface IsUriFieldDisplay extends IsFieldDisplay<Uri> {

	Uri createUri(String value);
	void addHook(UriFieldDisplay.Hook hook);

}
