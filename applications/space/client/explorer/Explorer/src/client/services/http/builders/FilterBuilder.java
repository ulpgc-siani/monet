package client.services.http.builders;

import client.core.model.List;
import client.core.system.Filter;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import com.google.gwt.core.client.JsArray;

public class FilterBuilder implements Builder<client.core.model.Filter, List<client.core.model.Filter>> {
	@Override
	public client.core.model.Filter build(HttpInstance instance) {
		if (instance == null)
			return null;

		Filter filter = new Filter();
		initialize(filter, instance);
		return filter;
	}

	@Override
	public void initialize(client.core.model.Filter object, HttpInstance instance) {
		Filter filter = (Filter)object;
		filter.setName(instance.getString("name"));
		filter.setLabel(instance.getString("label"));
		filter.setOptions(new OptionBuilder().buildList(instance.getList("options")));
	}

	@Override
	public List<client.core.model.Filter> buildList(HttpList instance) {
		List<client.core.model.Filter> result = new MonetList<>();

		for (int i = 0; i < instance.getItems().length(); i++)
			result.add(build(instance.getItems().get(i)));

		return result;
	}

	public static class OptionBuilder implements Builder<client.core.model.Filter.Option, List<client.core.model.Filter.Option>> {

		@Override
		public client.core.model.Filter.Option build(HttpInstance instance) {
			if (instance == null)
				return null;

			Filter.Option option = new Filter.Option();
			initialize(option, instance);
			return option;
		}

		@Override
		public void initialize(client.core.model.Filter.Option object, HttpInstance instance) {
			Filter.Option option = (Filter.Option)object;
			option.setValue(instance.getString("value"));
			option.setLabel(instance.getString("label"));
		}

		@Override
		public List<client.core.model.Filter.Option> buildList(HttpList instance) {
			List<client.core.model.Filter.Option> result = new MonetList<>();
			JsArray<HttpInstance> items = instance.getItems();

			for (int i = 0; i < items.length(); i++)
				result.add(build(items.get(i)));

			return result;
		}
	}

}
