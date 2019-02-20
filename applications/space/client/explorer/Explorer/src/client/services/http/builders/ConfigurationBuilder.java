package client.services.http.builders;

import client.core.model.Space;
import client.core.system.Configuration;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class ConfigurationBuilder implements Builder<Space.Configuration, MonetList<Space.Configuration>> {
	@Override
	public client.core.model.Space.Configuration build(HttpInstance instance) {
		if (instance == null)
			return null;

		Configuration configuration = new Configuration();
		initialize(configuration, instance);
		return configuration;
	}

	@Override
	public void initialize(Space.Configuration object, HttpInstance instance) {
		Configuration configuration = (Configuration)object;
		configuration.setDomain(instance.getString("domain"));
		configuration.setUrl(instance.getString("url"));
		configuration.setApiUrl(instance.getString("apiUrl"));
		configuration.setPushUrl(instance.getString("pushUrl"));
		configuration.setAnalyticsUrl(instance.getString("analyticsUrl"));
		configuration.setDigitalSignatureUrl(instance.getString("digitalSignatureUrl"));
		configuration.setImagesPath(instance.getString("imagesPath"));
	}

	@Override
	public MonetList<Space.Configuration> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
