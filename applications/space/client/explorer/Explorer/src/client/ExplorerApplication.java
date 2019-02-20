package client;

import client.core.messages.LoadingSpaceError;
import client.core.model.*;
import client.presenters.displays.*;
import client.presenters.operations.*;
import client.services.*;
import client.services.callback.SpaceCallback;
import client.services.http.HttpStub;
import client.services.http.Stub;
import client.utils.PathUtils;
import client.widgets.*;
import client.widgets.commands.CommandWidget;
import client.widgets.commands.MenuWidget;
import client.widgets.commands.SearchCommand;
import client.widgets.commands.ToolbarWidget;
import client.widgets.entity.HelperWidget;
import client.widgets.entity.NodeWidget;
import client.widgets.entity.TaskListWidget;
import client.widgets.entity.TaskWidget;
import client.widgets.entity.visiting.EntityVisitingWidget;
import client.widgets.entity.visiting.SearchVisitingWidget;
import client.widgets.index.IndexFilterToolsWidget;
import client.widgets.index.IndexToolbarWidget;
import client.widgets.index.IndexWidget;
import client.widgets.view.EntityViewListTabVerticalWidget;
import client.widgets.view.EntityViewWidget;
import client.widgets.view.NodeViewListTabWidget;
import client.widgets.view.workmap.TaskHistoryWidget;
import client.widgets.view.workmap.TaskStateActionWidget;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.RootPanel;
import cosmos.gwt.model.Theme;
import cosmos.gwt.presenters.PresenterMaker;
import cosmos.presenters.Presenter;
import cosmos.presenters.PresenterBodyMaker;
import cosmos.presenters.RootDisplay;

import static client.core.model.Space.Action;
import static client.core.model.Space.Tab;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toCombinedRule;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.presenters.Operation.Context;

public class ExplorerApplication implements Application, EntryPoint {
	protected Space space;
	protected Services services;
	protected PresenterMaker presenterMaker;
	protected ApplicationDisplay applicationDisplay;

	public void onModuleLoad() {
		init(createServices(getApiUrl()));
	}

	private String getApiUrl() {
		Element apiUrl = $(RootPanel.get()).find("#apiUrl").get(0);
		String result = apiUrl.getInnerHTML();
		apiUrl.removeFromParent();
		return result;
	}

	@Override
	public void init(final Services services) {
		this.services = services;

		this.services.getSpaceService().load(new SpaceCallback() {
            @Override
            public void success(final Space space) {
                init(space);
                start();
            }

            @Override
            public void failure(String error) {
                clearLoading();
                new LoadingSpaceError(error);
            }
        });
	}

	@Override
	public void start() {
		Action initialAction = space.getInitialAction();

		if (initialAction == null)
			initialAction = Action.SHOW_HOME;

		clearLoading();
		createDisplay();

		new ActionDispatcher(services).dispatch(initialAction, createContext());
	}

	@Override
	public RootDisplay getDisplay() {
		return applicationDisplay;
	}

	protected Services createServices(final String apiUrl) {
		return new Services() {
            private final TranslatorService translatorService = new client.services.http.TranslatorService(getStub(), this);
            private final NewsService newsService = new client.services.http.NewsService(getStub(), this);
            private final IndexService indexService = new client.services.http.IndexService(getStub(), this);
            private final SourceService sourceService = new client.services.http.SourceService(getStub(), this);
            private final AccountService accountService = new client.services.http.AccountService(getStub(), this);
            private final TaskService taskService = new client.services.http.TaskService(getStub(), this);
            private final NodeService nodeService = new client.services.http.NodeService(getStub(), this);
            private final SpaceService spaceService = new client.services.http.SpaceService(getStub(), this);
            private final NotificationService notificationService = new client.services.http.NotificationService(getStub(), this);
            private Stub stub = null;

			private Stub getStub() {
				if (stub == null)
					stub = new HttpStub(apiUrl, new HttpStub.ErrorHandler() {
						@Override
						public boolean onError(String error) {

							if (!error.contains("error-user-not-logged"))
								return false;

							String title = services.getTranslatorService().translate(TranslatorService.ErrorLabel.TITLE);
							String message = services.getTranslatorService().translate(TranslatorService.ErrorLabel.USER_NOT_LOGGED);
							applicationDisplay.getMessageDisplay().alert(title, message, new MessageDisplay.AlertCallback() {
								@Override
								public void close() {
									Window.Location.reload();
								}
							});

							return true;
						}
					});
				return stub;
			}

			@Override
			public SpaceService getSpaceService() {
                return spaceService;
			}

			@Override
			public NodeService getNodeService() {
				return nodeService;
			}

			@Override
			public TaskService getTaskService() {
				return taskService;
			}

			@Override
			public AccountService getAccountService() {
				return accountService;
			}

			@Override
			public SourceService getSourceService() {
				return sourceService;
			}

			@Override
			public IndexService getIndexService() {
				return indexService;
			}

			@Override
			public NewsService getNewsService() {
				return newsService;
			}

			@Override
			public TranslatorService getTranslatorService() {
				return translatorService;
			}

            @Override
            public NotificationService getNotificationService() {
                return notificationService;
            }
		};
	}

	private void registerWidgets(PresenterMaker presenterMaker) {
		presenterMaker.addBuilder(new ApplicationWidget.Builder());
		presenterMaker.addBuilder(new LayoutWidget.Builder());
		presenterMaker.addBuilder(new MessageWidget.Builder());
		presenterMaker.addBuilder(new SearchCommand.Builder());
		presenterMaker.addBuilder(new SpaceWidget.Builder());
		presenterMaker.addBuilder(new EntityVisitingWidget.Builder());
		presenterMaker.addBuilder(new SearchVisitingWidget.Builder());
		presenterMaker.addBuilder(new HelperWidget.Builder());

		presenterMaker.addBuilder(new AccountWidget.Builder());
		presenterMaker.addBuilder(new BusinessUnitListWidget.Builder());
		presenterMaker.addBuilder(new NotificationListWidget.Builder());

		presenterMaker.addBuilder(new TaskListWidget.Builder());
		presenterMaker.addBuilder(new TaskWidget.Builder());
		presenterMaker.addBuilder(new TaskStateActionWidget.Builder());
		presenterMaker.addBuilder(new TaskHistoryWidget.Builder());
		presenterMaker.addBuilder(new NodeWidget.Builder());

		presenterMaker.addBuilder(new EntityViewWidget.Builder());

		presenterMaker.addBuilder(new EntityViewListTabVerticalWidget.Builder());
		presenterMaker.addBuilder(new NodeViewListTabWidget.Builder());

		presenterMaker.addBuilder(new MenuWidget.Builder());
		presenterMaker.addBuilder(new ToolbarWidget.Builder());
		presenterMaker.addBuilder(new ExplorationWidget.Builder());

		presenterMaker.addBuilder(new IndexWidget.Builder());
		presenterMaker.addBuilder(new IndexToolbarWidget.Builder());
		presenterMaker.addBuilder(new IndexFilterToolsWidget.Builder());

		presenterMaker.addBuilder(new CommandWidget.Builder());
	}

	private void init(Space space) {
		this.space = space;
		this.presenterMaker = new PresenterMaker(services.getTranslatorService(), loadTheme());
		this.applicationDisplay = new ApplicationDisplay(services, space, new PresenterBodyMaker[]{ presenterMaker});

		registerWidgets(presenterMaker);
		initPushService();
        services.getNotificationService().registerListener(new NotificationService.UpdateAccountListener() {
            @Override
            public void notify(User user) {
                if (!services.getAccountService().userIsLogged(user)) return;

                RefreshAccountOperation operation = new RefreshAccountOperation(createContext(), user);
                operation.inject(services);
                operation.execute();
            }
        });
	}

	private void initPushService() {
		PushService pushService = new client.services.http.PushService(services.getSpaceService().load());
		pushService.init();
        services.getNotificationService().registerSource(pushService);
	}

	private void clearLoading() {
		$(RootPanel.get()).find("#main .loading").remove();
	}

	private void createDisplay() {
		Account account = space.getAccount();
		Theme theme = loadTheme();

		applicationDisplay.addChild(new MessageDisplay());
		applicationDisplay.addChild(new LayoutDisplay());
		applicationDisplay.addChild(new SearchOperation(createContext(), account.getRootNode()));
		applicationDisplay.addChild(new ToggleHelperOperation(createContext()));
		applicationDisplay.addChild(new SpaceDisplay(space, theme));
		applicationDisplay.addChild(createOperationList());
		applicationDisplay.addChild(new AccountDisplay(account, theme));
		applicationDisplay.addChild(createExplorationDisplay(account));
		applicationDisplay.addChild(new EntityVisitingDisplay(null, null));
		applicationDisplay.addChild(new HelperDisplay());
		applicationDisplay.makeBody();
	}

	private Theme loadTheme() {
		Theme theme = new Theme() {
			private Element widgetContainer = null;

			@Override
			public String getName() {
				return space.getTheme();
			}

			public void initFromHostElement() {
				widgetContainer = $(RootPanel.getBodyElement()).find(toCombinedRule(THEME, getName())).get(0);

				if (widgetContainer == null)
					widgetContainer = $(RootPanel.getBodyElement()).find(toRule(THEME)).get(0);
			}

			@Override
			public String getLayout(String name) {
				Element[] elements = getElements(name);

				if (elements.length == 0)
					return "";

				return elements[0].getInnerHTML().replaceAll("\n", "");
			}

			@Override
			public boolean existsLayout(String name) {
				return getElements(name).length > 0;
			}

			@Override
			public String getLogoPath() {
				return PathUtils.getThemeImagesPath(getName(), "logo.png");
			}

			@Override
			public String getLogoReducedPath() {
				return PathUtils.getThemeImagesPath(getName(), "logo_reduced.png");
			}

			@Override
			public String getAddPhotoPath() {
				return PathUtils.getThemeImagesPath(getName(), "addphoto.png");
			}

			@Override
			public String getAddPhotoReducedPath() {
				return PathUtils.getThemeImagesPath(getName(), "addphoto_reduced.png");
			}

			private Element[] getElements(String name) {
				if (widgetContainer == null)
					return new Element[0];

				return $(widgetContainer).find(".layout." + name.replaceAll(" ", ".")).elements();
			}
		};

		theme.initFromHostElement();

		return theme;
	}

	private Operation createEnvironmentOperation(Tab tab, Account account, Context context) {
		Node rootNode = account.getRootNode();
		Node entity = tab.getEntity();
		NodeView view = (NodeView) tab.getView();

		if (tab.getEntity().equals(rootNode)) {
			entity = rootNode;
			view = (NodeView) rootNode.getViews().get(tab.getView().getKey());
		}

		return new ShowEnvironmentOperation(context, entity, view);
	}

	private Operation createNewsOperation(Context context) {
		return new ShowNewsOperation(context);
	}

	private Operation createTaskBoardOperation(Tab tab, Context context) {
		return new ShowTaskListOperation(context, (TaskList)tab.getEntity(), null);
	}

	private Operation createTaskTrayOperation(Tab tab, Context context) {
		return new ShowTaskListOperation(context, (TaskList)tab.getEntity(), null);
	}

	private Operation createTrashOperation(Context context) {
		return new ShowTrashOperation(context, null);
	}

	private Context createContext() {
		return new Context() {
			@Override
			public Presenter getCanvas() {
				return applicationDisplay;
			}

			@Override
			public Operation getReferral() {
				return applicationDisplay.getVisitingDisplay() != null ? applicationDisplay.getVisitingDisplay().toOperation() : null;
			}
		};
	}

	private Presenter createExplorationDisplay(Account account) {
		ExplorationDisplay display = new ExplorationDisplay();
		Context context = createContext();
		ExplorationDisplay.ExplorationItemDisplay rootItem = null;
		int spaced = 0;

		for (Tab tab : space.getTabs()) {
			Operation operation = null;

			switch (tab.getType()) {
				case ENVIRONMENT: operation = createEnvironmentOperation(tab, account, context); break;
				//case DASHBOARD: operation = createDashboardOperation(tab, context); spaced++; break;
				case NEWS: operation = createNewsOperation(context); spaced++; break;
				//case ROLES: operation = createRolesOperation(tab, context); spaced++; break;
				case TASK_BOARD: operation = createTaskBoardOperation(tab, context); spaced++; break;
				case TASK_TRAY: operation = createTaskTrayOperation(tab, context); spaced++; break;
				case TRASH: operation = createTrashOperation(context); spaced++; break;
			}

			if (operation == null)
				continue;

			ExplorationDisplay.ExplorationItemDisplay item = display.add(operation, spaced==1);
			if (tab.isActive())
				rootItem = item;
		}

		if (rootItem == null)
			return display;

		display.setRootOperation(rootItem);
		display.activate(rootItem);

		return display;
	}

	private Presenter createOperationList() {
		OperationListDisplay operationListDisplay = new OperationListDisplay();
        List<Command> commands = space.getAccount().getRootNode().getCommands();

		for (Command command : commands)
			addOperation(command, operationListDisplay);

		return operationListDisplay;
	}

	private void addOperation(Command command, Presenter container) {
		if (command instanceof GroupCommand)
			addCompositeOperation((GroupCommand) command, container);
		else
			addExecuteNodeCommandOperation(command, container);
	}

	private void addCompositeOperation(GroupCommand command, Presenter container) {
		CompositeOperation operation = new CompositeOperation(createContext());
		operation.setLabel(command.getLabel());

		for (Command childCommand : command.getCommandList())
			addOperation(childCommand, operation);

		container.addChild(operation);
	}

	private void addExecuteNodeCommandOperation(Command command, Presenter container) {
		ExecuteNodeCommandOperation operation = new ExecuteNodeCommandOperation(createContext(), command, space.getAccount().getRootNode());
		operation.inject(services);
		container.addChild(operation);
	}

}
