package org.monet.space.explorer.injection;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.multibindings.MapBinder;
import org.monet.space.explorer.configuration.Configuration;
import org.monet.space.explorer.control.ActionsFactory;
import org.monet.space.explorer.control.DialogsFactory;
import org.monet.space.explorer.control.RendersFactory;
import org.monet.space.explorer.control.actions.*;
import org.monet.space.explorer.control.dialogs.*;
import org.monet.space.explorer.control.dialogs.constants.Operation;
import org.monet.space.explorer.control.displays.*;
import org.monet.space.explorer.control.renders.Render;
import org.monet.space.explorer.control.renders.renders.HomeRender;
import org.monet.space.explorer.control.renders.renders.LoadingRender;
import org.monet.space.explorer.control.renders.renders.LogoutRender;
import org.monet.space.explorer.control.renders.renders.UpdatingSpaceRender;
import org.monet.space.explorer.model.ComponentProvider;
import org.monet.space.explorer.model.LayerProvider;
import org.monet.space.explorer.model.MonetComponentProvider;
import org.monet.space.explorer.model.MonetLayerProvider;
import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.agents.AgentUserClient;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.utils.MimeTypes;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletContext;

public class ApplicationModule extends AbstractModule {
	private Context context;
	ServletContext servletContext;

	public ApplicationModule(ServletContext servletContext) {
		super();
		this.servletContext = servletContext;
	}

	@Override
	protected void configure() {

		try {
			Context context = new InitialContext();
			this.context = (Context) context.lookup("java:comp/env");
		} catch (NamingException e) {
			throw new RuntimeException(e.getMessage(), e);
		}

		bind(Context.class).toInstance(this.context);
		bind(org.monet.space.kernel.model.Context.class).toInstance(org.monet.space.kernel.model.Context.getInstance());
		bind(Dictionary.class).toInstance(Dictionary.getInstance());
		bind(Configuration.class).toInstance(Configuration.getInstance());
		bind(org.monet.space.kernel.configuration.Configuration.class).toInstance(org.monet.space.kernel.configuration.Configuration.getInstance());
		bind(BusinessUnit.class).toInstance(BusinessUnit.getInstance());
		bind(AgentLogger.class).toInstance(AgentLogger.getInstance());
		bind(AgentUserClient.class).toInstance(AgentUserClient.getInstance());
		bind(LayerProvider.class).to(MonetLayerProvider.class);
		bind(ComponentProvider.class).to(MonetComponentProvider.class);
		bind(MimeTypes.class).toInstance(MimeTypes.getInstance());
		bind(AgentPushService.class).toInstance(AgentPushService.getInstance());
		bind(AgentFilesystem.class);

		bindRendersFactoryMap();
		bindActionsFactoryMap();
		bindDialogFactoryMap();
		bindDisplayFactoryMap();
	}

	private void bindActionsFactoryMap() {
		MapBinder<String, Class<? extends Action>> actionsMap = MapBinder.newMapBinder(binder(), new TypeLiteral<String>() {}, new TypeLiteral<Class<? extends Action>>() {});

		actionsMap.addBinding(Operation.HOME).toInstance(HomeAction.class);
		actionsMap.addBinding(Operation.LOGIN).toInstance(HomeAction.class);
		actionsMap.addBinding(Operation.LOAD_BUSINESS_UNITS).toInstance(LoadBusinessUnitsAction.class);
		actionsMap.addBinding(Operation.LOADING).toInstance(LoadingAction.class);
		actionsMap.addBinding(Operation.LOAD_NOTIFICATIONS).toInstance(LoadNotificationsAction.class);
		actionsMap.addBinding(Operation.LOAD_PENDING_TASKS_COUNT).toInstance(LoadPendingTasksCountAction.class);
		actionsMap.addBinding(Operation.LOAD_SPACE).toInstance(LoadSpaceAction.class);
		actionsMap.addBinding(Operation.LOAD_NODE).toInstance(LoadNodeAction.class);
		actionsMap.addBinding(Operation.LOAD_NODE_LABEL).toInstance(LoadNodeLabelAction.class);
		actionsMap.addBinding(Operation.LOAD_DEFINITION).toInstance(LoadDefinitionAction.class);
		actionsMap.addBinding(Operation.REGISTER_TOKENS).toInstance(RegisterTokensAction.class);
		actionsMap.addBinding(Operation.SAVE_FIELD).toInstance(SaveFieldAction.class);
		actionsMap.addBinding(Operation.SAVE_NOTE).toInstance(SaveNoteAction.class);
		actionsMap.addBinding(Operation.ADD_NODE).toInstance(AddNodeAction.class);
		actionsMap.addBinding(Operation.DELETE_NODE).toInstance(DeleteNodeAction.class);
		actionsMap.addBinding(Operation.DELETE_NODES).toInstance(DeleteNodesAction.class);
		actionsMap.addBinding(Operation.LOAD_HISTORY).toInstance(LoadHistoryAction.class);
		actionsMap.addBinding(Operation.ADD_FIELD).toInstance(AddFieldAction.class);
		actionsMap.addBinding(Operation.DELETE_FIELD).toInstance(DeleteFieldAction.class);
		actionsMap.addBinding(Operation.CHANGE_FIELD_ORDER).toInstance(ChangeFieldOrderAction.class);
		actionsMap.addBinding(Operation.LOAD_INDEX_ENTRIES).toInstance(LoadIndexEntriesAction.class);
		actionsMap.addBinding(Operation.LOAD_INDEX_ENTRY).toInstance(LoadIndexEntryAction.class);
		actionsMap.addBinding(Operation.LOAD_INDEX_FILTER_OPTIONS).toInstance(LoadIndexFilterOptionsAction.class);
		actionsMap.addBinding(Operation.LOAD_INDEX_FILTER_OPTIONS_REPORT).toInstance(LoadIndexFilterOptionsReportAction.class);
		actionsMap.addBinding(Operation.LOGOUT).toInstance(LogoutAction.class);
		actionsMap.addBinding(Operation.LOAD_SOURCE).toInstance(LoadSourceAction.class);
		actionsMap.addBinding(Operation.LOCATE_SOURCE).toInstance(LocateSourceAction.class);
		actionsMap.addBinding(Operation.LOAD_SOURCE_TERMS).toInstance(LoadSourceTermsAction.class);
		actionsMap.addBinding(Operation.LOAD_TASK).toInstance(LoadTaskAction.class);
		actionsMap.addBinding(Operation.SAVE_TASK_URGENCY).toInstance(SaveTaskUrgencyAction.class);
		actionsMap.addBinding(Operation.SAVE_TASK_OWNER).toInstance(SaveTaskOwnerAction.class);
		actionsMap.addBinding(Operation.SAVE_TASKS_OWNER).toInstance(SaveTaskOwnerAction.class);
		actionsMap.addBinding(Operation.ABORT_TASK).toInstance(AbortTaskAction.class);
		actionsMap.addBinding(Operation.LOAD_TASK_DELEGATION_ROLES).toInstance(LoadTaskDelegationRolesAction.class);
		actionsMap.addBinding(Operation.SELECT_TASK_DELEGATION_ROLE).toInstance(SelectTaskDelegationRoleAction.class);
		actionsMap.addBinding(Operation.SETUP_TASK_DELEGATION_ORDER).toInstance(SetupTaskDelegationOrderAction.class);
		actionsMap.addBinding(Operation.SOLVE_TASK_LINE).toInstance(SolveTaskLineAction.class);
		actionsMap.addBinding(Operation.SOLVE_TASK_EDITION).toInstance(SolveTaskEditionAction.class);
		actionsMap.addBinding(Operation.SETUP_TASK_WAIT).toInstance(SetupTaskWaitAction.class);
		actionsMap.addBinding(Operation.LOAD_TASK_LIST_INDEX_ENTRIES).toInstance(LoadTaskListIndexEntriesAction.class);
		actionsMap.addBinding(Operation.LOAD_TASK_TIME_LINE).toInstance(LoadTaskTimeLineAction.class);
		actionsMap.addBinding(Operation.LOAD_TASK_LIST_INDEX_FILTER_OPTIONS).toInstance(LoadTaskListIndexFilterOptionsAction.class);
		actionsMap.addBinding(Operation.LOAD_MODEL_FILE).toInstance(LoadModelFileAction.class);
		actionsMap.addBinding(Operation.REDIRECT).toInstance(RedirectAction.class);
		actionsMap.addBinding(Operation.EXECUTE_COMMAND).toInstance(ExecuteCommandAction.class);
		actionsMap.addBinding(Operation.LOAD_TASK_HISTORY).toInstance(LoadTaskHistoryAction.class);
		actionsMap.addBinding(Operation.PREVIEW_NODE).toInstance(PreviewNodeAction.class);
		actionsMap.addBinding(Operation.DOWNLOAD_NODE).toInstance(DownloadNodeAction.class);
		actionsMap.addBinding(Operation.DOWNLOAD_NODE_FILE).toInstance(DownloadNodeFileAction.class);
		actionsMap.addBinding(Operation.UPLOAD_NODE_FILE).toInstance(UploadNodeFileAction.class);
		actionsMap.addBinding(Operation.DOWNLOAD_NODE_IMAGE).toInstance(DownloadNodeImageAction.class);
		actionsMap.addBinding(Operation.UPLOAD_NODE_IMAGE).toInstance(UploadNodeImageAction.class);
		actionsMap.addBinding(Operation.SAVE_ACCOUNT_PHOTO).toInstance(SaveAccountPhotoAction.class);
		actionsMap.addBinding(Operation.SEARCH_NODE_ENTRIES_ACTION).toInstance(SearchNodeEntriesAction.class);
		actionsMap.addBinding(Operation.LOAD_NODE_HELP_PAGE).toInstance(LoadNodeHelpPageAction.class);

        actionsMap.addBinding(Operation.FOCUS_NODE_FIELD).toInstance(FocusNodeFieldAction.class);
        actionsMap.addBinding(Operation.FOCUS_NODE_VIEW).toInstance(FocusNodeViewAction.class);
        actionsMap.addBinding(Operation.BLUR_NODE_FIELD).toInstance(BlurNodeFieldAction.class);
        actionsMap.addBinding(Operation.BLUR_NODE_VIEW).toInstance(BlurNodeViewAction.class);

        actionsMap.addBinding(Operation.LOAD_INDEX_HISTORY).toInstance(LoadIndexHistoryAction.class);

		bind(ActionsFactory.class);
	}

	private void bindRendersFactoryMap() {
		MapBinder<String, Class<? extends Render>> rendersMap = MapBinder.newMapBinder(binder(), new TypeLiteral<String>() {}, new TypeLiteral<Class<? extends Render>>() {});

		rendersMap.addBinding(HomeRender.NAME).toInstance(HomeRender.class);
		rendersMap.addBinding(LogoutRender.NAME).toInstance(LogoutRender.class);
		rendersMap.addBinding(UpdatingSpaceRender.NAME).toInstance(UpdatingSpaceRender.class);
		rendersMap.addBinding(LoadingRender.NAME).toInstance(LoadingRender.class);

		bind(RendersFactory.class);
	}

	private void bindDialogFactoryMap() {
		MapBinder<Class<? extends Action>, Class<? extends Dialog>> dialogsMap = MapBinder.newMapBinder(binder(), new TypeLiteral<Class<? extends Action>>() {}, new TypeLiteral<Class<? extends Dialog>>() {});

		dialogsMap.addBinding(HomeAction.class).toInstance(HomeDialog.class);
		dialogsMap.addBinding(RegisterTokensAction.class).toInstance(RegisterTokensDialog.class);
		dialogsMap.addBinding(LoadSpaceAction.class).toInstance(HttpDialog.class);
		dialogsMap.addBinding(LoadNodeAction.class).toInstance(LoadNodeDialog.class);
		dialogsMap.addBinding(LoadNodeLabelAction.class).toInstance(LoadNodeDialog.class);
		dialogsMap.addBinding(LoadDefinitionAction.class).toInstance(DefinitionDialog.class);
		dialogsMap.addBinding(SaveFieldAction.class).toInstance(SaveFieldDialog.class);
		dialogsMap.addBinding(SaveNoteAction.class).toInstance(NoteDialog.class);
		dialogsMap.addBinding(AddNodeAction.class).toInstance(AddNodeDialog.class);
		dialogsMap.addBinding(DeleteNodeAction.class).toInstance(DeleteNodeDialog.class);
		dialogsMap.addBinding(DeleteNodesAction.class).toInstance(DeleteNodesDialog.class);
		dialogsMap.addBinding(LoadHistoryAction.class).toInstance(HistoryDialog.class);
		dialogsMap.addBinding(AddFieldAction.class).toInstance(AddFieldDialog.class);
		dialogsMap.addBinding(DeleteFieldAction.class).toInstance(DeleteFieldDialog.class);
		dialogsMap.addBinding(ChangeFieldOrderAction.class).toInstance(ChangeFieldOrderDialog.class);
		dialogsMap.addBinding(LoadIndexEntriesAction.class).toInstance(LoadIndexEntriesDialog.class);
		dialogsMap.addBinding(LoadIndexEntryAction.class).toInstance(LoadIndexEntryDialog.class);
		dialogsMap.addBinding(LoadIndexFilterOptionsAction.class).toInstance(LoadIndexFilterDialog.class);
		dialogsMap.addBinding(LoadIndexFilterOptionsReportAction.class).toInstance(LoadIndexFilterDialog.class);
		dialogsMap.addBinding(LoadNotificationsAction.class).toInstance(LoadNotificationsDialog.class);
		dialogsMap.addBinding(LoadBusinessUnitsAction.class).toInstance(HttpDialog.class);
		dialogsMap.addBinding(LogoutAction.class).toInstance(LogoutDialog.class);
		dialogsMap.addBinding(LoadSourceAction.class).toInstance(LoadSourceDialog.class);
		dialogsMap.addBinding(LocateSourceAction.class).toInstance(LocateSourceDialog.class);
		dialogsMap.addBinding(LoadSourceTermsAction.class).toInstance(LoadSourceTermsDialog.class);
		dialogsMap.addBinding(LoadTaskAction.class).toInstance(LoadTaskDialog.class);
		dialogsMap.addBinding(SaveTaskUrgencyAction.class).toInstance(SaveTaskUrgencyDialog.class);
		dialogsMap.addBinding(SaveTaskOwnerAction.class).toInstance(SaveTaskOwnerDialog.class);
		dialogsMap.addBinding(SaveTasksOwnerAction.class).toInstance(SaveTasksOwnerDialog.class);
		dialogsMap.addBinding(AbortTaskAction.class).toInstance(TaskDialog.class);
		dialogsMap.addBinding(LoadTaskDelegationRolesAction.class).toInstance(TaskDialog.class);
		dialogsMap.addBinding(SelectTaskDelegationRoleAction.class).toInstance(SelectTaskDelegationRoleDialog.class);
		dialogsMap.addBinding(SetupTaskDelegationOrderAction.class).toInstance(TaskDialog.class);
		dialogsMap.addBinding(SolveTaskLineAction.class).toInstance(SolveTaskLineDialog.class);
		dialogsMap.addBinding(SolveTaskEditionAction.class).toInstance(TaskDialog.class);
		dialogsMap.addBinding(SetupTaskWaitAction.class).toInstance(SetupTaskWaitDialog.class);
		dialogsMap.addBinding(LoadTaskListIndexEntriesAction.class).toInstance(LoadTaskListIndexEntriesDialog.class);
		dialogsMap.addBinding(LoadTaskTimeLineAction.class).toInstance(TaskDialog.class);
		dialogsMap.addBinding(LoadTaskListIndexFilterOptionsAction.class).toInstance(LoadTaskListIndexFilterOptionsDialog.class);
		dialogsMap.addBinding(LoadModelFileAction.class).toInstance(FileDialog.class);
		dialogsMap.addBinding(RedirectAction.class).toInstance(RedirectDialog.class);
		dialogsMap.addBinding(ExecuteCommandAction.class).toInstance(CommandDialog.class);
		dialogsMap.addBinding(LoadTaskHistoryAction.class).toInstance(LoadTaskHistoryDialog.class);
		dialogsMap.addBinding(PreviewNodeAction.class).toInstance(HttpDialog.class);
		dialogsMap.addBinding(DownloadNodeAction.class).toInstance(HttpDialog.class);
		dialogsMap.addBinding(DownloadNodeFileAction.class).toInstance(DownloadNodeResourceDialog.class);
		dialogsMap.addBinding(UploadNodeFileAction.class).toInstance(UploadNodeResourceDialog.class);
		dialogsMap.addBinding(DownloadNodeImageAction.class).toInstance(DownloadNodeResourceDialog.class);
		dialogsMap.addBinding(UploadNodeImageAction.class).toInstance(UploadNodeResourceDialog.class);
		dialogsMap.addBinding(SaveAccountPhotoAction.class).toInstance(AccountPhotoDialog.class);
		dialogsMap.addBinding(SearchNodeEntriesAction.class).toInstance(SearchNodeEntriesDialog.class);
		dialogsMap.addBinding(LoadNodeHelpPageAction.class).toInstance(NodeHelpDialog.class);
        dialogsMap.addBinding(FocusNodeFieldAction.class).toInstance(NodeFieldDialog.class);
        dialogsMap.addBinding(BlurNodeFieldAction.class).toInstance(NodeFieldDialog.class);
        dialogsMap.addBinding(FocusNodeViewAction.class).toInstance(NodeDialog.class);
        dialogsMap.addBinding(BlurNodeViewAction.class).toInstance(NodeDialog.class);
        dialogsMap.addBinding(LoadIndexHistoryAction.class).toInstance(IndexHistoryDialog.class);

		bind(DialogsFactory.class);
	}

	private void bindDisplayFactoryMap() {
		MapBinder<Class<? extends Action>, Class<? extends Display>> displaysMap = MapBinder.newMapBinder(binder(), new TypeLiteral<Class<? extends Action>>() {}, new TypeLiteral<Class<? extends Display>>() {});

		displaysMap.addBinding(HomeAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(RegisterTokensAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(LoadSpaceAction.class).toInstance(SpaceDisplay.class);
		displaysMap.addBinding(LoadNodeAction.class).toInstance(NodeDisplay.class);
		displaysMap.addBinding(LoadNodeLabelAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(LoadDefinitionAction.class).toInstance(DefinitionDisplay.class);
		displaysMap.addBinding(SaveFieldAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(SaveNoteAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(AddNodeAction.class).toInstance(NodeDisplay.class);
		displaysMap.addBinding(DeleteNodeAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(DeleteNodesAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(LoadHistoryAction.class).toInstance(HistoryDisplay.class);
		displaysMap.addBinding(AddFieldAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(DeleteFieldAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(ChangeFieldOrderAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(LoadIndexEntriesAction.class).toInstance(NodeIndexEntryDisplay.class);
		displaysMap.addBinding(LoadIndexEntryAction.class).toInstance(NodeIndexEntryDisplay.class);
		displaysMap.addBinding(LoadIndexFilterOptionsAction.class).toInstance(FilterOptionDisplay.class);
		displaysMap.addBinding(LoadIndexFilterOptionsReportAction.class).toInstance(ReportDisplay.class);
		displaysMap.addBinding(LoadNotificationsAction.class).toInstance(NotificationDisplay.class);
		displaysMap.addBinding(LoadBusinessUnitsAction.class).toInstance(BusinessUnitDisplay.class);
		displaysMap.addBinding(LogoutAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(LoadSourceAction.class).toInstance(SourceDisplay.class);
		displaysMap.addBinding(LocateSourceAction.class).toInstance(SourceDisplay.class);
		displaysMap.addBinding(LoadSourceTermsAction.class).toInstance(TermDisplay.class);
		displaysMap.addBinding(LoadTaskAction.class).toInstance(TaskDisplay.class);
		displaysMap.addBinding(SaveTaskUrgencyAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(SaveTaskOwnerAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(SaveTasksOwnerAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(AbortTaskAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(LoadTaskDelegationRolesAction.class).toInstance(RoleDisplay.class);
		displaysMap.addBinding(SelectTaskDelegationRoleAction.class).toInstance(TaskDisplay.class);
		displaysMap.addBinding(SetupTaskDelegationOrderAction.class).toInstance(TaskDisplay.class);
		displaysMap.addBinding(SolveTaskLineAction.class).toInstance(TaskDisplay.class);
		displaysMap.addBinding(SolveTaskEditionAction.class).toInstance(TaskDisplay.class);
		displaysMap.addBinding(SetupTaskWaitAction.class).toInstance(TaskDisplay.class);
		displaysMap.addBinding(LoadTaskListIndexEntriesAction.class).toInstance(TaskListIndexEntryDisplay.class);
		displaysMap.addBinding(LoadTaskTimeLineAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(LoadTaskListIndexFilterOptionsAction.class).toInstance(FilterOptionDisplay.class);
		displaysMap.addBinding(LoadModelFileAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(RedirectAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(ExecuteCommandAction.class).toInstance(CommandDisplay.class);
		displaysMap.addBinding(LoadTaskHistoryAction.class).toInstance(FactDisplay.class);
		displaysMap.addBinding(PreviewNodeAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(DownloadNodeAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(DownloadNodeFileAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(UploadNodeFileAction.class).toInstance(JsonElementDisplay.class);
		displaysMap.addBinding(DownloadNodeImageAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(UploadNodeImageAction.class).toInstance(JsonElementDisplay.class);
		displaysMap.addBinding(SaveAccountPhotoAction.class).toInstance(DefaultDisplay.class);
		displaysMap.addBinding(SearchNodeEntriesAction.class).toInstance(NodeIndexEntryDisplay.class);
		displaysMap.addBinding(LoadNodeHelpPageAction.class).toInstance(PageDisplay.class);
        displaysMap.addBinding(FocusNodeFieldAction.class).toInstance(DefaultDisplay.class);
        displaysMap.addBinding(FocusNodeViewAction.class).toInstance(DefaultDisplay.class);
        displaysMap.addBinding(BlurNodeFieldAction.class).toInstance(DefaultDisplay.class);
        displaysMap.addBinding(BlurNodeViewAction.class).toInstance(DefaultDisplay.class);
        displaysMap.addBinding(LoadIndexHistoryAction.class).toInstance(NodeIndexEntryDisplay.class);

		bind(DisplaysFactory.class);
	}

}