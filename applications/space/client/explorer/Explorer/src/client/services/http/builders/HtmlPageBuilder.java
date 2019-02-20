package client.services.http.builders;

import client.core.model.List;
import client.core.system.HtmlPage;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import com.google.gwt.core.client.JsArray;

public class HtmlPageBuilder implements Builder<client.core.model.HtmlPage, List<client.core.model.HtmlPage>> {
	@Override
	public client.core.model.HtmlPage build(HttpInstance instance) {
		if (instance == null)
			return null;

		HtmlPage page = new HtmlPage();
		initialize(page, instance);
		return page;
	}

	@Override
	public void initialize(client.core.model.HtmlPage object, HttpInstance instance) {
        ((HtmlPage)object).setContent(instance.getString("content"));
	}

	@Override
	public List<client.core.model.HtmlPage> buildList(HttpList instances) {
		List<client.core.model.HtmlPage> result = new MonetList<>();
		JsArray<HttpInstance> items = instances.getItems();

		for (int i = 0; i < items.length(); i++)
			result.add(build(items.get(i)));

		return result;
	}
}
