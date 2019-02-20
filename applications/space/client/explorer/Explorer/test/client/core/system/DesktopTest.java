package client.core.system;

import client.ApplicationTestCase;
import client.core.model.definition.entity.DesktopDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.NodeBuilder;
import client.services.http.builders.definition.entity.NodeDefinitionBuilder;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class DesktopTest extends ApplicationTestCase {

	private static final String NODE = "{\"id\":\"1\",\"label\":\"Home\",\"type\":\"desktop\",\"definition\":{\"code\":\"mtvp02a\",\"name\":\"org.monet.explorerintegration.Home\",\"type\":\"desktop\"},\"singletons\":{\"totalCount\":2,\"items\":[{\"id\":\"3\",\"type\":\"container\",\"label\":\"ContainerSample\",\"definition\":{\"code\":\"miyqwmg\",\"name\":\"org.monet.explorerintegration.ContainerSample\",\"type\":\"container\"}},{\"id\":\"2\",\"type\":\"collection\",\"label\":\"CollectionSample\",\"definition\":{\"code\":\"myu5yzg\",\"name\":\"org.monet.explorerintegration.CollectionSample\",\"type\":\"collection\"}}]}}";
	private static final String DEFINITION = "{\"code\":\"mtvp02a\",\"name\":\"org.monet.explorerintegration.Home\",\"type\":\"desktop\",\"label\":\"Home\",\"views\":{\"totalCount\":1,\"items\":[{\"code\":\"mcticfw\",\"type\":\"desktop\",\"default\":false,\"show\":{\"links\":{\"totalCount\":2,\"items\":[{\"value\":\"org.monet.explorerintegration.ContainerSample\"},{\"value\":\"org.monet.explorerintegration.CollectionSample\"}]}}}]}}";

	@Test
	public void testViews() {
		NodeBuilder<client.core.model.Desktop> builder = new NodeBuilder<>();
		NodeDefinitionBuilder<DesktopDefinition> definitionBuilder = new NodeDefinitionBuilder<>();
		client.core.model.Desktop desktop = builder.build((HttpInstance) JsonUtils.safeEval(NODE));

		desktop.setDefinition(definitionBuilder.build((HttpInstance) JsonUtils.safeEval(DEFINITION)));

		assertEquals(1, desktop.getViews().size());
	}

}