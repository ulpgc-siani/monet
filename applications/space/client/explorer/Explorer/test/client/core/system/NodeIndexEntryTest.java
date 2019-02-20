package client.core.system;

import client.ApplicationTestCase;
import client.core.adapters.NodeIndexEntriesAdapter;
import client.core.model.List;
import client.core.model.definition.entity.IndexDefinition;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.NodeIndexEntryBuilder;
import client.services.http.builders.definition.entity.IndexDefinitionBuilder;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class NodeIndexEntryTest extends ApplicationTestCase {

	private static final String DESCRIPTOR_ITEMS = "{\"totalCount\":1,\"items\":[{\"label\":\"valor del campo texto\",\"entity\":{\"id\":\"7\",\"label\":\"valor del campo texto\",\"type\":\"form\",\"definition\":{\"code\":\"m6p3yvw\",\"name\":\"org.monet.explorerintegration.CollectionSample.Item\",\"type\":\"form\"}}}]}";
	private static final String ITEMS = "{\"totalCount\":2,\"items\":[{\"label\":\"item 1\",\"entity\":{\"id\":\"8\",\"label\":\"item 1\",\"type\":\"form\",\"definition\":{\"code\":\"m6p3yvw\",\"name\":\"org.monet.explorerintegration.CollectionSample.Item\",\"type\":\"form\"}},\"geoReferenced\":false,\"title\":{\"code\":\"mwkjusw\",\"value\":\"item 1\"},\"highlights\":{\"totalCount\":0,\"items\":[]},\"lines\":{\"totalCount\":2,\"items\":[{\"code\":\"mcjv7dg\",\"value\":\"Hola campo 1\"},{\"code\":\"mdgnaug\",\"value\":\"false\"}]},\"linesBelow\":{\"totalCount\":0,\"items\":[]},\"footers\":{\"totalCount\":0,\"items\":[]}},{\"label\":\"item 2\",\"entity\":{\"id\":\"9\",\"label\":\"item 2\",\"type\":\"form\",\"definition\":{\"code\":\"m6p3yvw\",\"name\":\"org.monet.explorerintegration.CollectionSample.Item\",\"type\":\"form\"}},\"geoReferenced\":false,\"title\":{\"code\":\"mwkjusw\",\"value\":\"item 2\"},\"highlights\":{\"totalCount\":0,\"items\":[]},\"lines\":{\"totalCount\":2,\"items\":[{\"code\":\"mcjv7dg\",\"value\":\"Hola campo 2\"},{\"code\":\"mdgnaug\",\"value\":\"false\"}]},\"linesBelow\":{\"totalCount\":0,\"items\":[]},\"footers\":{\"totalCount\":0,\"items\":[]}}]}";
	private static final String DEFINITION = "{\"code\":\"mnntusg\",\"name\":\"org.monet.explorerintegration.CollectionSample.Index\",\"type\":\"index\",\"label\":\"Índice\",\"reference\":[{\"code\":\"mwkjusw\",\"label\":\"Campo texto\",\"description\":\"\",\"type\":\"STRING\"},{\"code\":\"mcjv7dg\",\"label\":\"Campo memo\",\"description\":\"\",\"type\":\"STRING\"},{\"code\":\"mdgnaug\",\"label\":\"Campo boolean\",\"description\":\"\",\"type\":\"BOOLEAN\"}],\"views\":{\"totalCount\":1,\"items\":[{\"code\":\"myqq8ia\",\"name\":\"Default\",\"type\":\"index\",\"show\":{\"title\":{\"value\":\"FieldText\",\"definition\":\"mnntusg\"},\"lines\":{\"totalCount\":2,\"items\":[{\"value\":\"FieldMemo\",\"definition\":\"mnntusg\"},{\"value\":\"FieldBoolean\",\"definition\":\"mnntusg\"}]},\"linesBelow\":{\"totalCount\":0,\"items\":[]},\"highlight\":{\"totalCount\":0,\"items\":[]},\"footer\":{\"totalCount\":0,\"items\":[]}}}]}}";

	@Test
	public void testDescriptorItems() {
		NodeIndexEntryBuilder builder = new NodeIndexEntryBuilder();
		List<client.core.model.NodeIndexEntry> entries = builder.buildList((HttpList)JsonUtils.safeEval(DESCRIPTOR_ITEMS).cast());

		assertEquals(1, entries.getTotalCount());
		assertEquals("valor del campo texto", entries.get(0).getLabel());
		assertEquals("7", entries.get(0).getEntity().getId());
		assertEquals("m6p3yvw", entries.get(0).getEntity().getDefinition().getCode());
	}

	@Test
	public void testIndexItems() {
		NodeIndexEntryBuilder builder = new NodeIndexEntryBuilder();
		List<client.core.model.NodeIndexEntry> entries = builder.buildList((HttpList)JsonUtils.safeEval(ITEMS).cast());

		assertEquals(2, entries.getTotalCount());
		assertEquals("item 1", entries.get(0).getLabel());
		assertEquals("item 1", entries.get(0).getTitle().getValue());
		assertFalse(entries.get(0).isGeoReferenced());
		assertEquals("Hola campo 1", entries.get(0).getLines().get(0).getValue());
		assertEquals("false", entries.get(0).getLines().get(1).getValue());
		assertEquals(0, entries.get(0).getHighlights().size());
		assertEquals(0, entries.get(0).getLinesBelow().size());
		assertEquals(0, entries.get(0).getFooters().size());
		assertEquals("item 2", entries.get(1).getLabel());
		assertEquals("item 2", entries.get(1).getTitle().getValue());

		new NodeIndexEntriesAdapter().adapt(entries, createDefinition().getReference());
	}

	@Test
	public void testAdaptIndexItems() {
		NodeIndexEntryBuilder builder = new NodeIndexEntryBuilder();
		List<client.core.model.NodeIndexEntry> entries = builder.buildList((HttpList)JsonUtils.safeEval(ITEMS).cast());

		new NodeIndexEntriesAdapter().adapt(entries, createDefinition().getReference());
	}

	private IndexDefinition createDefinition() {
		IndexDefinitionBuilder builder = new IndexDefinitionBuilder();
		return builder.build((HttpInstance) JsonUtils.safeEval(DEFINITION));
	}

}