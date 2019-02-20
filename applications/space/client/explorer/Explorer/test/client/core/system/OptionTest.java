package client.core.system;

import client.ApplicationTestCase;
import client.core.model.List;
import client.services.http.HttpList;
import client.services.http.builders.FilterBuilder;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class OptionTest extends ApplicationTestCase {

	private static final String OPTIONS = "{\"totalCount\":2,\"items\":[\"item 1\",\"item 2\"]}";

	@Test
	public void testLoadOptions() {
		FilterBuilder.OptionBuilder builder = new FilterBuilder.OptionBuilder();
		List<client.core.model.Filter.Option> options = builder.buildList((HttpList) JsonUtils.safeEval(OPTIONS).cast());

		assertEquals(2, options.size());
		assertEquals("item 1", options.get(0).getValue());
		assertEquals("item 2", options.get(1).getValue());
	}

}