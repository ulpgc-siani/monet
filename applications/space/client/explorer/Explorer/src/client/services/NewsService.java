package client.services;

import client.services.callback.NewsCallback;

public interface NewsService extends Service {
	void open(final NewsCallback callback);
}
