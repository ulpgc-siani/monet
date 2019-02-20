package client.services.http;

import client.services.*;
import client.services.callback.NewsCallback;

public class NewsService extends HttpService implements client.services.NewsService {

	public NewsService(Stub stub, Services services) {
		super(stub, services);
	}

	@Override
	public void open(NewsCallback callback) {
		client.services.SpaceService spaceService = services.getSpaceService();
		client.services.TranslatorService translatorService = services.getTranslatorService();
		callback.success(spaceService.getEntityFactory().createNews(translatorService.translate(TranslatorService.Label.NEWS)));
	}
}
