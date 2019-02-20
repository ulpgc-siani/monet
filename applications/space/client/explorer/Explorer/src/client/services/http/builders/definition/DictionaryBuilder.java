package client.services.http.builders.definition;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.definition.Dictionary;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

public class DictionaryBuilder implements Builder<client.core.model.definition.Dictionary, List<client.core.model.definition.Dictionary>> {
	@Override
	public client.core.model.definition.Dictionary build(HttpInstance instance) {
		if (instance == null)
			return null;

		Dictionary dictionary = new Dictionary();
		initialize(dictionary, instance);
		return dictionary;
	}

	@Override
	public void initialize(client.core.model.definition.Dictionary object, HttpInstance instance) {
		Dictionary dictionary = (Dictionary)object;
		dictionary.setDefinitions(new DefinitionBuilder<>().buildList(instance.getList("definitions")));
	}

	@Override
	public List<client.core.model.definition.Dictionary> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
