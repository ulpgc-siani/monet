package client;

import com.google.gwt.junit.client.GWTTestCase;

public abstract class ApplicationTestCase extends GWTTestCase {

	@Override
	public String getModuleName() {
		return "Application";
	}
}
