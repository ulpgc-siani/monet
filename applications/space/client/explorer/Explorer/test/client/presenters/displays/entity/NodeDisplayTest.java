package client.presenters.displays.entity;

import client.ApplicationTestCase;
import client.core.model.Desktop;
import client.core.model.DesktopView;
import client.core.model.definition.entity.DesktopDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.NodeBuilder;
import client.services.http.builders.definition.entity.NodeDefinitionBuilder;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class NodeDisplayTest extends ApplicationTestCase {

	private static final String DESKTOP = "{\"id\":\"1\",\"label\":\"Home\",\"type\":\"desktop\",\"definition\":{\"code\":\"mtvp02a\",\"name\":\"org.monet.explorerintegration.Home\",\"type\":\"desktop\"},\"singletons\":{\"totalCount\":2,\"items\":[{\"id\":\"3\",\"type\":\"container\",\"label\":\"ContainerSample\",\"definition\":{\"code\":\"miyqwmg\",\"name\":\"org.monet.explorerintegration.ContainerSample\",\"type\":\"container\"}},{\"id\":\"2\",\"type\":\"collection\",\"label\":\"CollectionSample\",\"definition\":{\"code\":\"myu5yzg\",\"name\":\"org.monet.explorerintegration.CollectionSample\",\"type\":\"collection\"}}]}}";
	private static final String DESKTOP_DEFINITION = "{\"code\":\"mtvp02a\",\"name\":\"org.monet.explorerintegration.Home\",\"type\":\"desktop\",\"label\":\"Home\",\"views\":{\"totalCount\":1,\"items\":[{\"code\":\"mcticfw\",\"type\":\"desktop\",\"default\":false,\"show\":{\"links\":{\"totalCount\":2,\"items\":[{\"value\":\"org.monet.explorerintegration.ContainerSample\"},{\"value\":\"org.monet.explorerintegration.CollectionSample\"}]}}}]}}";

	@Test
	public void testLoadView() {
		NodeDisplay<Desktop, DesktopView> display = createDisplay();

		assertEquals(1, display.getViewsCount());
		assertEquals(2, display.getView().getShows().size());
		assertEquals("mcticfw", display.getCurrentView().getKey().getCode());
	}

	private NodeDisplay createDisplay() {
		Desktop desktop = createDesktop();
		return new DesktopDisplay(desktop, (DesktopView) desktop.getViews().get(0));
	}

	private client.core.model.Desktop createDesktop() {
		NodeBuilder<Desktop> builder = new NodeBuilder<>();
		NodeDefinitionBuilder<DesktopDefinition> definitionBuilder = new NodeDefinitionBuilder<>();
		Desktop desktop = builder.build((HttpInstance) JsonUtils.safeEval(DESKTOP));

		desktop.setDefinition(definitionBuilder.build((HttpInstance) JsonUtils.safeEval(DESKTOP_DEFINITION)));

		return desktop;
	}

}