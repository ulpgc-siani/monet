package client.core.model;

import static client.core.model.Instance.*;

public interface HtmlPage {
	ClassName CLASS_NAME = new ClassName("HtmlPage");

	String getContent();
}
