package client.presenters.displays.entity.field;

import client.core.model.Node;

public interface IsNodeFieldDisplay extends IsFieldDisplay<Node> {

	void addHook(NodeFieldDisplay.Hook hook);

}
