package client.core.model;

import client.core.model.definition.views.DesktopViewDefinition;

public interface DesktopView extends NodeView<DesktopViewDefinition> {

	ClassName CLASS_NAME = new ClassName("DesktopView");

	ClassName getClassName();
	List<Entity> getShows();

}
