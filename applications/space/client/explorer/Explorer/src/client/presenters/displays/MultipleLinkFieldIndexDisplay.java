package client.presenters.displays;

import client.core.model.Index;
import client.core.model.List;
import client.core.model.NodeIndexEntry;
import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.core.model.types.Link;
import client.core.system.MonetList;

public class MultipleLinkFieldIndexDisplay extends LinkFieldIndexDisplay {

    private final List<Link> activatedLinks;

    public MultipleLinkFieldIndexDisplay(LinkFieldDefinition linkFieldDefinition, Index index, Handler handler) {
        super(linkFieldDefinition, index, handler);
        this.activatedLinks = new MonetList<>();
    }

    @Override
    public void activate(NodeIndexEntry entry) {
        if (activatedLinks.contains(entry.getEntity().toLink())) return;
        activatedLinks.add(entry.getEntity().toLink());
        if (handler != null)
            handler.onActivate(this, entry);
    }

    public void deactive(Link link) {
        activatedLinks.remove(link);
    }

}
