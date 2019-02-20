package client.core.system;

import client.ApplicationTestCase;
import client.services.http.HttpList;
import client.services.http.builders.BusinessUnitBuilder;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class BusinessUnitTest extends ApplicationTestCase {

	private static final String BUSINESS_UNITS = "{\"totalCount\":1,\"items\":[{\"name\":\"monet\",\"url\":\"http://localhost:8080/monet\",\"type\":\"MEMBER\",\"active\":true,\"disabled\":false}]}";

	@Test
	public void testLoadBusinessUnits() {
		BusinessUnitBuilder builder = new BusinessUnitBuilder();
		client.core.model.BusinessUnitList businessUnitList = builder.buildList((HttpList) JsonUtils.safeEval(BUSINESS_UNITS).cast());

		assertEquals(1, businessUnitList.size());
		assertEquals("monet", businessUnitList.get(0).getName());
		assertEquals("http://localhost:8080/monet", businessUnitList.get(0).getUrl());
		assertTrue(businessUnitList.get(0).isMember());
		assertTrue(businessUnitList.get(0).isActive());
		assertFalse(businessUnitList.get(0).isDisabled());
	}

}