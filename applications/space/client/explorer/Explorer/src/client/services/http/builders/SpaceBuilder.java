package client.services.http.builders;

import client.core.model.List;
import client.core.model.Space;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.definition.DictionaryBuilder;

public class SpaceBuilder implements Builder<Space, List<Space>> {
	@Override
	public client.core.model.Space build(HttpInstance instance) {
		if (instance == null)
			return null;

		client.core.system.Space space = new client.core.system.Space();
		initialize(space, instance);
		return space;
	}

	@Override
	public void initialize(client.core.model.Space object, HttpInstance instance) {
		client.core.system.Space space = (client.core.system.Space)object;

		space.setName(instance.getString("name"));
		space.setTitle(instance.getString("title"));
		space.setSubTitle(instance.getString("subTitle"));
		space.setModelLogoUrl(instance.getString("modelLogoUrl"));
		space.setLanguage(instance.getString("language"));
		space.setTheme(instance.getString("theme"));
		space.setInstanceId(instance.getString("instanceId"));
		space.setInitialAction(new SpaceActionBuilder().build(instance.getObject("initialAction")));
		space.setFederation(new FederationBuilder().build(instance.getObject("federation")));
		space.setAccount(new AccountBuilder().build(instance.getObject("account")));
		space.setConfiguration(new ConfigurationBuilder().build(instance.getObject("configuration")));
		space.setTabs(new TabBuilder().buildList(HttpInstance.createList(instance.getArray("tabs"))));
		space.setDictionary(new DictionaryBuilder().build(instance.getObject("dictionary")));
		space.setEntityFactory(new EntityBuilder<>());
	}

	@Override
	public List<client.core.model.Space> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
