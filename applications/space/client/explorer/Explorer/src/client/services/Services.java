package client.services;

public interface Services extends cosmos.services.Services {

	SpaceService getSpaceService();
	NodeService getNodeService();
	TaskService getTaskService();
    AccountService getAccountService();
	SourceService getSourceService();
	IndexService getIndexService();
	NewsService getNewsService();
	TranslatorService getTranslatorService();
    NotificationService getNotificationService();
}
