package client.services.http.builders.definition.entity;

import client.ApplicationTestCase;
import client.core.model.definition.entity.DesktopDefinition;
import client.core.model.definition.entity.DocumentDefinition;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.definition.entity.IndexDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class EntityDefinitionBuilderTest extends ApplicationTestCase {

	private static final String DESKTOP_DEFINITION = "{\"code\":\"mtvp02a\",\"name\":\"org.monet.explorerintegration.Home\",\"type\":\"desktop\",\"label\":\"Home\",\"views\":{\"totalCount\":1,\"items\":[{\"code\":\"mcticfw\",\"type\":\"desktop\",\"default\":false,\"show\":{\"links\":{\"totalCount\":2,\"items\":[{\"value\":\"org.monet.explorerintegration.ContainerSample\"},{\"value\":\"org.monet.explorerintegration.CollectionSample\"}]}}}]}}";
	private static final String INDEX_DEFINITION = "{\"code\":\"mnntusg\",\"name\":\"org.monet.explorerintegration.CollectionSample.Index\",\"type\":\"index\",\"label\":\"Índice\",\"reference\":[{\"code\":\"mwkjusw\",\"label\":\"Campo texto\",\"description\":\"\",\"type\":\"STRING\"},{\"code\":\"mcjv7dg\",\"label\":\"Campo memo\",\"description\":\"\",\"type\":\"STRING\"},{\"code\":\"mdgnaug\",\"label\":\"Campo boolean\",\"description\":\"\",\"type\":\"BOOLEAN\"}],\"views\":{\"totalCount\":1,\"items\":[{\"code\":\"myqq8ia\",\"name\":\"Default\",\"type\":\"index\",\"show\":{\"title\":{\"value\":\"FieldText\",\"definition\":\"mnntusg\"},\"lines\":{\"totalCount\":2,\"items\":[{\"value\":\"FieldMemo\",\"definition\":\"mnntusg\"},{\"value\":\"FieldBoolean\",\"definition\":\"mnntusg\"}]},\"linesBelow\":{\"totalCount\":0,\"items\":[]},\"highlight\":{\"totalCount\":0,\"items\":[]},\"footer\":{\"totalCount\":0,\"items\":[]}}}]}}";

	@Test
	public void testLoadDesktopDefinition() {
		EntityDefinitionBuilder builder = new EntityDefinitionBuilder();
		EntityDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(DESKTOP_DEFINITION));
		assertTrue(definition instanceof DesktopDefinition);
	}

	@Test
	public void testLoadIndexDefinition() {
		IndexDefinitionBuilder indexBuilder = new IndexDefinitionBuilder();
		IndexDefinition definition = indexBuilder.build((HttpInstance) JsonUtils.safeEval(INDEX_DEFINITION));
		assertTrue(definition instanceof IndexDefinition);
	}

	@Test
	public void testLoadDocumentDefinition() {
		EntityDefinitionBuilder indexBuilder = new EntityDefinitionBuilder();
		EntityDefinition definition = indexBuilder.build((HttpInstance) JsonUtils.safeEval("{\"code\":\"mfnof4a\",\"name\":\"micv.documentos.Fecyt\",\"type\":\"document\",\"label\":\"Fecyt\"}"));
		assertTrue(definition instanceof DocumentDefinition);
	}

}