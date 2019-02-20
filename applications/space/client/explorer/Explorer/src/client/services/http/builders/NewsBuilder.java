package client.services.http.builders;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.News;
import client.services.http.HttpInstance;
import client.services.http.HttpList;

public class NewsBuilder extends EntityBuilder<News, client.core.model.News, List<client.core.model.News>> {
	@Override
	public client.core.model.News build(HttpInstance instance) {
		if (instance == null)
			return null;

		News news = new News();
		initialize(news, instance);
		return news;
	}

	@Override
	public void initialize(client.core.model.News object, HttpInstance instance) {
	}

	@Override
	public List<client.core.model.News> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
