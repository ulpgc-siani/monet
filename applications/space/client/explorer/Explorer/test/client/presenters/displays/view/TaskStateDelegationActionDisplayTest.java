package client.presenters.displays.view;

import client.core.*;
import client.core.model.*;
import client.core.model.workmap.DelegationAction;
import client.core.model.workmap.DelegationAction.Message;
import client.core.model.workmap.DelegationAction.Step;
import client.core.model.workmap.LineAction;
import client.core.model.workmap.WaitAction;
import client.presenters.displays.entity.workmap.TaskStateDelegationActionDisplay;
import client.services.*;
import client.services.callback.*;
import cosmos.types.Date;
import cosmos.types.DatePrecision;
import cosmos.types.DayOfWeek;
import org.junit.Test;

import static client.core.model.Task.State;
import static client.core.model.Task.Type;
import static junit.framework.TestCase.assertEquals;

public class TaskStateDelegationActionDisplayTest {

	@Test
	public void delegationInSetupRoleStep() {
		TaskStateDelegationActionDisplay display = createDelegationViewWithSetupRoleStep();

		assertEquals(Step.SETUP_ROLE, display.getStep());
		assertEquals(Message.SETUP_ROLE, display.calculateMessage());

		display = createDelegationViewWithSetupRoleStepAndNoRoles();
		assertEquals(Message.NO_ROLES, display.calculateMessage());
	}

	@Test
	public void delegationInSetupOrderStep() {
		TaskStateDelegationActionDisplay display = createDelegationViewWithSetupOrderStep();

		assertEquals(Step.SETUP_ORDER, display.getStep());
		assertEquals(Message.SETUP_ORDER, display.calculateMessage());
	}

	@Test
	public void delegationInSendingOrderStep() {
		TaskStateDelegationActionDisplay display = createDelegationViewWithSendingStep();

		assertEquals(Step.SENDING, display.getStep());
		assertEquals(Message.SENDING, display.calculateMessage());
	}

	private TaskStateDelegationActionDisplay createDelegationViewWithSetupRoleStep() {
		Task task = createTask(State.PENDING, Step.SETUP_ROLE, null, null);
		TaskStateDelegationActionDisplay display = new TaskStateDelegationActionDisplay(task, (DelegationAction)task.getAction()) {{
			inject(createServices(ListBuilder.buildRoleList(new Role[]{
				EntityBuilder.buildRole("1", Role.Type.USER, "Role 1"),
				EntityBuilder.buildRole("2", Role.Type.USER, "Role 2")
			}), null));
		}};
		display.loadRoleList();
		return display;
	}

	private TaskStateDelegationActionDisplay createDelegationViewWithSetupRoleStepAndNoRoles() {
		Task task = createTask(State.PENDING, Step.SETUP_ROLE, null, null);
		TaskStateDelegationActionDisplay display = new TaskStateDelegationActionDisplay(task, (DelegationAction)task.getAction()) {{
			inject(createServices(ListBuilder.buildRoleList(new Role[0]), null));
		}};
		display.loadRoleList();
		return display;
	}

	private TaskStateDelegationActionDisplay createDelegationViewWithSetupOrderStep() {
		final Form orderNode = NodeBuilder.buildForm(null, null, "", ListBuilder.buildNodeViewList(new NodeView[] { ViewBuilder.buildFormView("001", "ejemplo", null, true, new Field[0]) }), new Field[0], false);
		Task task = createTask(State.PENDING, Step.SETUP_ORDER, EntityBuilder.buildRole("1", Role.Type.USER, "Role 1"), orderNode);
		TaskStateDelegationActionDisplay display = new TaskStateDelegationActionDisplay(task, (DelegationAction)task.getAction()) {{
			inject(createServices(ListBuilder.buildRoleList(new Role[0]), orderNode));
		}};
		display.loadRoleList();
		display.loadOrderNode();
		return display;
	}

	private TaskStateDelegationActionDisplay createDelegationViewWithSendingStep() {
		Task task = createTask(State.WAITING, Step.SENDING, EntityBuilder.buildRole("1", Role.Type.USER, "Role 1"), null);
		TaskStateDelegationActionDisplay display = new TaskStateDelegationActionDisplay(task, (DelegationAction)task.getAction()) {{
			inject(createServices(ListBuilder.buildRoleList(new Role[0]), null));
		}};
		display.loadRoleList();
		return display;
	}

	private Task createTask(final State state, final Step step, final Role role, final Node orderNode) {
		return TaskBuilder.buildActivity("tareacorrectiva1", "Tarea correctiva 1", state, TaskBuilder.buildWorkMap(
			TaskBuilder.buildDelegationAction("Iniciando encargod de actuación", step, role, orderNode)
		), ListBuilder.EmptyTaskViewList);
	}

	private Services createServices(final List<Role> roleList, final Node node) {
		return new Services() {
			@Override
			public SpaceService getSpaceService() {
				return null;
			}

			@Override
			public NodeService getNodeService() {
				return new NodeService() {
					@Override
					public void open(String id, NodeCallback callback) {
						callback.success(node);
					}

					@Override
					public void getLabel(Node node, Callback<String> callback) {

					}

					@Override
					public void saveNote(Node node, String name, String value, NoteCallback callback) {
					}

					@Override
					public void add(Node parent, String code, NodeCallback callback) {

					}

					@Override
					public void delete(Node node, VoidCallback callback) {

					}

					@Override
					public void delete(Node[] nodes, VoidCallback callback) {

					}

					@Override
					public void saveField(Node node, Field field, VoidCallback callback) {

					}

					@Override
					public void addField(Node node, MultipleField parent, Field field, int pos, VoidCallback callback) {

					}

					@Override
					public void deleteField(Node node, MultipleField parent, int pos, VoidCallback callback) {

					}

					@Override
					public void changeFieldOrder(Node node, MultipleField parent, int previousPos, int newPos, VoidCallback callback) {

					}

					@Override
					public void clearField(Node node, MultipleField parent, VoidCallback callback) {

					}

					@Override
					public void executeCommand(Node node, Command command, NodeCommandCallback callback) {

					}

                    @Override
                    public void focusNodeField(Node node, Field field) {
                    }

                    @Override
                    public void focusNodeView(Node node) {
                    }

                    @Override
                    public void blurNodeField(Node node, Field field) {
                    }

                    @Override
                    public void blurNodeView(Node node) {
                    }

                    @Override
					public String getNodePreviewBaseUrl(String documentId) {
						return null;
					}

					@Override
					public String getDownloadNodeUrl(Node node) {
						return null;
					}

					@Override
					public String getDownloadNodeImageUrl(Node node, String imageId) {
						return null;
					}

					@Override
					public String getDownloadNodeThumbnailUrl(Node node, String imageId) {
						return null;
					}

					@Override
					public String getDownloadNodeFileUrl(Node node, String fileId) {
						return null;
					}

					@Override
					public void searchFieldHistory(Field field, String dataStore, String filter, int start, int limit, TermListCallback callback) {

					}

					@Override
					public void getFieldHistory(Field field, String dataStore, int start, int limit, TermListCallback callback) {

					}

					@Override
					public void saveFile(String filename, String data, Node node, Callback<String> callback) {

					}

					@Override
					public void savePicture(String filename, String data, Node node, Callback<String> callback) {

					}

					@Override
					public void loadHelpPage(Node node, HtmlPageCallback callback) {
					}
				};
			}

			@Override
			public TaskService getTaskService() {
				return new TaskService() {
					@Override
					public void open(String id, TaskCallback callback) {

					}

					@Override
					public void saveUrgency(Task task, boolean value, VoidCallback callback) {

					}

					@Override
					public void saveOwner(Task task, User owner, String reason, VoidCallback callback) {

					}

					@Override
					public void abort(Task task, VoidCallback callback) {

					}

					@Override
					public void loadDelegationRoles(Task task, RoleListCallback callback) {
						callback.success(roleList);
					}

					@Override
					public void selectDelegationRole(Task task, Role role, TaskCallback callback) {

					}

					@Override
					public void setupDelegationOrder(Task task, TaskCallback callback) {

					}

					@Override
					public void solveLine(Task task, LineAction.Stop stop, TaskCallback callback) {

					}

					@Override
					public void solveEdition(Task task, TaskCallback callback) {

					}

					@Override
					public void setupWait(Task task, WaitAction.Scale scale, int value, TaskCallback callback) {
					}

					@Override
					public void loadHistory(Task task, int start, int limit, HistoryCallback callback) {

					}

					@Override
					public void open(TaskListCallback callback) {

					}

					@Override
					public void saveOwner(Task[] tasks, User owner, String reason, VoidCallback callback) {

					}

					@Override
					public void load(TaskList.Situation situation, TaskList.Inbox inbox, List<Filter> filters, List<Order> orders, int start, int limit, TaskListIndexEntriesCallback callback) {

					}

					@Override
					public void search(String condition, TaskList.Situation situation, TaskList.Inbox inbox, List<Filter> filters, List<Order> orders, int start, int limit, TaskListIndexEntriesCallback callback) {

					}

					@Override
					public void getFilterOptions(Filter filter, FilterOptionsCallback callback) {

					}

					@Override
					public void searchFilterOptions(Filter filter, String condition, FilterOptionsCallback callback) {
					}

				};
			}

			@Override
			public AccountService getAccountService() {
				return null;
			}

			@Override
			public SourceService getSourceService() {
				return null;
			}

			@Override
			public IndexService getIndexService() {
				return null;
			}

			@Override
			public NewsService getNewsService() {
				return null;
			}

			@Override
			public TranslatorService getTranslatorService() {
				return new TranslatorService() {
					@Override
					public String getLoadingLabel() {
						return null;
					}

					@Override
					public String getNoPhoto() {
						return null;
					}

					@Override
					public String getAddPhoto() {
						return null;
					}

					@Override
					public String translateSavedPeriodAgo(String message, Date date) {
						return null;
					}

					@Override
					public String translateSearchForCondition(String condition) {
						return null;
					}

					@Override
					public String translateCountDate(Date date) {
						return null;
					}

					@Override
					public int dayOfWeekToNumber(DayOfWeek dayOfWeek) {
						return 0;
					}

					@Override
					public int monthToNumber(String month) {
						return 0;
					}

					@Override
					public String monthNumberToString(Integer month) {
						return null;
					}

					@Override
					public String[] getDateSeparators() {
						return new String[0];
					}

					@Override
					public String translateDateWithPrecision(Date date, DatePrecision precision) {
						return null;
					}

					@Override
					public String translateFullDate(Date date) {
						return null;
					}

					@Override
					public String translateFullDateByUser(Date date, String user) {
						return null;
					}

					@Override
					public String getCountLabel(int count) {
						return null;
					}

					@Override
					public String getSelectionCountLabel(int selectionCount) {
						return null;
					}

					@Override
					public String getTaskAssignedToMeMessage() {
						return null;
					}

					@Override
					public String getTaskAssignedToMeBySenderMessage(User sender) {
						return null;
					}

					@Override
					public String getTaskAssignedToUserMessage(User Owner) {
						return null;
					}

					@Override
					public String getTaskAssignedToUserBySenderMessage(User owner, User sender) {
						return null;
					}

					@Override
					public String getTaskDateMessage(Date createDate, Date updateDate) {
						return null;
					}

					@Override
					public String getTaskNotificationsMessage(int count) {
						return null;
					}

					@Override
					public String getTaskDelegationMessage(Message message, Date failureDate, String roleTypeLabel) {
						if (message == Message.SETUP_ROLE)
							return "Selecciona el cliente al cual enviar el encargo:";

						if (message == Message.NO_ROLES)
							return "No se ha encontrado ningún role que permita realizar esta acción. Vaya a la vista de roles y asigne el role " + roleTypeLabel + " a algún usuario.";

						if (message == Message.FAILURE_INTERNAL)
							return "Esta tarea no soporta delegar el encargo a un usuario de la unidad de negocio";

						if (message == Message.FAILURE_EXTERNAL)
							return "Esta tarea no soporta delegar el encargo a un usuario externo";

						if (message == Message.SETUP_ORDER)
							return "Configura el encargo de " + roleTypeLabel;

						if (message == Message.SENDING)
							return "Enviando al proveedor seleccionado...";

						if (message == Message.SENDING_FAILURE)
							return "Intentando enviar el encargo. El último intento de envío fue realizado " + translateFullDate(failureDate) + ".";

						return null;
					}

					@Override
					public String getTaskLineTimeoutMessage(Date timeout, LineAction.Stop timeoutStop) {
						return null;
					}

					@Override
					public String getTaskWaitMessage(Date dueDate) {
						return null;
					}

					@Override
					public String getTaskStateLabel(State state) {
						return null;
					}

					@Override
					public String getTaskTypeIcon(Type type) {
						return null;
					}

					@Override
					public String getCurrentLanguage() {
						return null;
					}

					@Override
					public String translate(Object name) {
						return null;
					}

					@Override
					public String translateHTML(String html) {
						return null;
					}
				};
			}

            @Override
            public NotificationService getNotificationService() {
                return null;
            }
		};
	}

}