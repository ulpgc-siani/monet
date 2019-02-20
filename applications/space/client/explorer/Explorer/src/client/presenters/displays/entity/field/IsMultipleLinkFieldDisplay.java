package client.presenters.displays.entity.field;

import client.core.model.types.Link;
import client.presenters.displays.IsMultipleDisplay;

public interface IsMultipleLinkFieldDisplay extends IsLinkFieldDisplay, IsMultipleDisplay<Link> {

    Link getLinkByLabel(String label);
}
