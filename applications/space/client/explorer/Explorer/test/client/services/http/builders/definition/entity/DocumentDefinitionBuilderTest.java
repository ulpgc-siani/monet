package client.services.http.builders.definition.entity;

import client.ApplicationTestCase;
import client.core.model.definition.entity.DocumentDefinition;
import client.core.model.definition.entity.NodeDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class DocumentDefinitionBuilderTest extends ApplicationTestCase {

	@Test
	public void testLoad() {
		NodeDefinitionBuilder builder = new NodeDefinitionBuilder();
		NodeDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval("{\"code\":\"mfnof4a\",\"name\":\"micv.documentos.Fecyt\",\"type\":\"document\",\"label\":\"Fecyt\"}"));

		assertTrue(definition instanceof DocumentDefinition);
	}

}