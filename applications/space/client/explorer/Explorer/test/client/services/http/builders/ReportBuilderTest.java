package client.services.http.builders;

import client.ApplicationTestCase;
import client.core.model.Report;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class ReportBuilderTest extends ApplicationTestCase {

	@Test
	public void testLoadReport() {
		Report report = new ReportBuilder().build((HttpInstance) JsonUtils.safeEval("{\"values\":[{\"code\":\"ic.020.060\",\"value\":2},{\"code\":\"ic.020.010.020\",\"value\":1}]}"));
		assertEquals(2, report.get("ic.020.060"));
		assertEquals(1, report.get("ic.020.010.020"));
	}

}