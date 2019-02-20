package client.core.system;

import client.ApplicationTestCase;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.SourceBuilder;
import client.services.http.builders.types.TermBuilder;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class SourceTest extends ApplicationTestCase {

	private static final String SOURCE = "{\"id\":\"1\",\"label\":\"Fuente de datos\",\"type\":\"thesaurus\",\"definition\":{\"code\":\"m99hbmw\",\"name\":\"org.monet.explorerintegration.FuenteDatos\",\"type\":\"thesaurus\"}}";
	private static final String SOURCE_TERMS = "{\"totalCount\":2,\"items\":[{\"value\":\"001\",\"label\":\"Termino 1\"},{\"value\":\"002\",\"label\":\"Termino 2\"}]}";

	@Test
	public void testLoadSource() {
		SourceBuilder builder = new SourceBuilder();
		client.core.model.Source source = builder.build((HttpInstance) JsonUtils.safeEval(SOURCE));

		assertEquals("1", source.getId());
		assertEquals("Fuente de datos", source.getLabel());
		assertEquals("m99hbmw", source.getDefinition().getCode());
	}

	@Test
	public void testLoadSourceTerms() {
		TermBuilder builder = new TermBuilder();
		client.core.model.types.TermList terms = builder.buildList((HttpList) JsonUtils.safeEval(SOURCE_TERMS).cast());

		assertEquals(2, terms.size());
		assertEquals(2, terms.getTotalCount());
		assertEquals("001", terms.get(0).getValue());
		assertEquals("Termino 1", terms.get(0).getLabel());
		assertEquals("002", terms.get(1).getValue());
		assertEquals("Termino 2", terms.get(1).getLabel());
	}

}