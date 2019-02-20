package client.presenters.displays.entity.field;

import client.core.model.types.Link;
import client.presenters.displays.LinkFieldIndexDisplay;

public interface IsLinkFieldDisplay extends IsFieldDisplay<Link> {

	void loadOptions();
	void loadOptions(String condition);
    void showEntity();
	void addHook(LinkFieldDisplay.Hook hook);
    boolean allowEdit();

	LinkFieldIndexDisplay getLinkFieldIndexDisplay();

}
