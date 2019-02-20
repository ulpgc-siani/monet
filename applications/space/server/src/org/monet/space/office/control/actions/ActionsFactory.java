/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

package org.monet.space.office.control.actions;

import org.monet.space.kernel.exceptions.BaseException;
import org.monet.space.kernel.exceptions.SystemException;
import org.monet.space.office.control.constants.Actions;
import org.monet.space.office.core.constants.ErrorCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class ActionsFactory {
	private static ActionsFactory oInstance;
	private HashMap<String, Object> hmActions;

	private ActionsFactory() {
		this.hmActions = new HashMap<String, Object>();
		this.registerActions();
	}

	private Boolean registerActions() {

		this.register(Actions.SHOW_LOADING, ActionShowLoading.class);
		this.register(Actions.SHOW_APPLICATION, ActionShowApplication.class);
		this.register(Actions.REDIRECT, ActionRedirect.class);
		this.register(Actions.LOGIN, ActionShowApplication.class);
		this.register(Actions.LOGOUT, ActionLogout.class);
		this.register(Actions.LOAD_ACCOUNT, ActionLoadAccount.class);
		this.register(Actions.LOAD_UNITS, ActionLoadUnits.class);
        this.register(Actions.LOAD_ACCOUNT_PENDING_TASKS, ActionLoadAccountPendingTasks.class);
		this.register(Actions.LOAD_NODES, ActionLoadNodes.class);
		this.register(Actions.LOAD_NODE, ActionLoadNode.class);
		this.register(Actions.LOAD_NODE_REVISION, ActionLoadNodeRevision.class);
		this.register(Actions.LOAD_CURRENT_NODE_REVISION, ActionLoadCurrentNodeRevision.class);
		this.register(Actions.LOAD_NODE_TYPE, ActionLoadNodeType.class);
		this.register(Actions.LOAD_NODE_TEMPLATE, ActionLoadNodeTemplate.class);
		this.register(Actions.LOAD_NODE_FROM_DATA, ActionLoadNodeFromData.class);
		this.register(Actions.LOAD_NODE_REFERENCE, ActionLoadNodeReference.class);
		this.register(Actions.LOAD_NODE_NOTES, ActionLoadNodeNotes.class);
		this.register(Actions.LOAD_NODE_ITEMS, ActionLoadNodeItems.class);
		this.register(Actions.LOAD_NODE_LOCATION, ActionLoadNodeLocation.class);
		this.register(Actions.LOAD_NODE_ATTRIBUTE, ActionLoadNodeAttribute.class);
		this.register(Actions.LOAD_LOCATION_LAYER, ActionLoadLocationLayer.class);
		this.register(Actions.LOAD_SET_ITEMS, ActionLoadSetItems.class);
		this.register(Actions.LOAD_LINK_NODE_ITEMS, ActionLoadLinkNodeItems.class);
		this.register(Actions.LOAD_LINK_NODE_ITEMS_LOCATIONS, ActionLoadLinkNodeItemsLocations.class);
		this.register(Actions.LOAD_LINK_NODE_ITEMS_LOCATIONS_COUNT, ActionLoadLinkNodeItemsLocationsCount.class);
		this.register(Actions.SAVE_NODE, ActionSaveNode.class);
		this.register(Actions.SAVE_NODE_ATTRIBUTE, ActionSaveNodeAttribute.class);
		this.register(Actions.SAVE_EMBEDDED_NODE, ActionSaveEmbeddedNode.class);
		this.register(Actions.LOAD_NODE_DESCRIPTOR, ActionLoadNodeDescriptor.class);
		this.register(Actions.SAVE_NODE_DESCRIPTOR, ActionSaveNodeDescriptor.class);
		this.register(Actions.ADD_NODE, ActionAddNode.class);
		this.register(Actions.ADD_PROTOTYPE, ActionAddPrototype.class);
		this.register(Actions.COPY_NODE, ActionCopyNode.class);
		this.register(Actions.DELETE_NODES, ActionDeleteNodes.class);
		this.register(Actions.DELETE_NODE, ActionDeleteNode.class);
		this.register(Actions.LOAD_ATTRIBUTES, ActionLoadAttributes.class);
		this.register(Actions.LOAD_SOURCE_LIST, ActionLoadSourceList.class);
		this.register(Actions.LOAD_SOURCE_TERMS, ActionLoadSourceTerms.class);
		this.register(Actions.EXISTS_SOURCE_TERM, ActionExistsSourceTerm.class);
		this.register(Actions.ADD_SOURCE_TERM, ActionAddSourceTerm.class);
		this.register(Actions.SAVE_SOURCE_TERM, ActionSaveSourceTerm.class);
		this.register(Actions.SAVE_SOURCE_TERM_ATTRIBUTE, ActionSaveSourceTermAttribute.class);
		this.register(Actions.DELETE_SOURCE_TERM, ActionDeleteSourceTerm.class);
		this.register(Actions.LOAD_HISTORY_TERMS, ActionLoadHistoryTerms.class);
		this.register(Actions.LOAD_TASK, ActionLoadTask.class);
		this.register(Actions.LOAD_TASKS, ActionLoadTasks.class);
		this.register(Actions.LOAD_NODE_HELPER_PAGE, ActionLoadNodeHelperPage.class);
		this.register(Actions.LOAD_HELPER_PAGE, ActionLoadHelperPage.class);
		this.register(Actions.SEARCH_NODES, ActionSearchNodes.class);
		this.register(Actions.LOAD_TEMPLATE, ActionLoadTemplate.class);
		this.register(Actions.IMPORT_NODE, ActionImportNode.class);
		this.register(Actions.EXPORT_NODE, ActionExportNode.class);
		this.register(Actions.DOWNLOAD_EXPORTED_NODE_FILE, ActionDownloadExportedNodeFile.class);
		this.register(Actions.UPLOAD_NODE_CONTENT, ActionUploadNodeContent.class);
		this.register(Actions.SAVE_ACCOUNT, ActionSaveAccount.class);
		this.register(Actions.SEARCH_USERS, ActionSearchUsers.class);
		this.register(Actions.SHARE_NODE, ActionShareNode.class);
		this.register(Actions.LOAD_NODES_FROM_TRASH, ActionLoadNodesFromTrash.class);
		this.register(Actions.EMPTY_TRASH, ActionEmptyTrash.class);
		this.register(Actions.RECOVER_NODES_FROM_TRASH, ActionRecoverNodesFromTrash.class);
		this.register(Actions.RECOVER_NODE_FROM_TRASH, ActionRecoverNodeFromTrash.class);
		this.register(Actions.ABORT_TASK, ActionAbortTask.class);
		this.register(Actions.LOAD_BUSINESS_MODEL_FILE, ActionLoadBusinessModelFile.class);
		this.register(Actions.LOAD_THEME_FILE, ActionLoadThemeFile.class);
		this.register(Actions.LOAD_BUSINESS_MODEL_DEFINITION, ActionLoadBusinessModelDefinition.class);
		this.register(Actions.LOAD_BUSINESS_UNIT_FILE, ActionLoadBusinessUnitFile.class);
		this.register(Actions.SEND_MAIL, ActionSendMail.class);
		this.register(Actions.SEND_SUGGESTION, ActionSendSuggestion.class);
		this.register(Actions.DISCARD_NODE, ActionDiscardNode.class);
		this.register(Actions.LOAD_DEFAULT_VALUES, ActionLoadDefaultValues.class);
		this.register(Actions.LOAD_DEFAULT_VALUE, ActionLoadDefaultValue.class);
		this.register(Actions.ADD_DEFAULT_VALUE, ActionAddDefaultValue.class);
		this.register(Actions.CREATE_TASK, ActionCreateTask.class);
		this.register(Actions.SET_TASK_TITLE, ActionSetTaskTitle.class);
		this.register(Actions.PREVIEW_NODE, ActionPreviewNode.class);
		this.register(Actions.DOWNLOAD_NODE, ActionDownloadNode.class);
		this.register(Actions.GENERATE_REPORT, ActionGenerateReport.class);
		this.register(Actions.PING, ActionPing.class);
		this.register(Actions.REGISTER_EXCEPTION, ActionRegisterException.class);
		this.register(Actions.CREATE_SEQUENCE_VALUE, ActionCreateSequenceValue.class);
		this.register(Actions.SELECT_TASK_DELEGATION_ROLE, ActionSelectTaskDelegationRole.class);
		this.register(Actions.SETUP_TASK_DELEGATION, ActionSetupTaskDelegation.class);
		this.register(Actions.SETUP_TASK_ENROLL, ActionSetupTaskEnroll.class);
		this.register(Actions.SETUP_TASK_WAIT, ActionSetupTaskWait.class);
		this.register(Actions.SOLVE_TASK_LINE, ActionSolveTaskLine.class);
		this.register(Actions.SOLVE_TASK_EDITION, ActionSolveTaskEdition.class);
		this.register(Actions.EXECUTE_NODE_COMMAND, ActionExecuteNodeCommand.class);
		this.register(Actions.EXECUTE_NODE_COMMAND_ON_ACCEPT, ActionExecuteNodeCommandOnAccept.class);
		this.register(Actions.EXECUTE_NODE_COMMAND_ON_CANCEL, ActionExecuteNodeCommandOnCancel.class);
		this.register(Actions.DOWNLOAD_NODE_COMMAND_FILE, ActionDownloadNodeCommandFile.class);
		this.register(Actions.LOAD_TASK_FILTERS, ActionLoadTaskFilters.class);
		this.register(Actions.EXITING, ActionExiting.class);
		this.register(Actions.LOAD_SYSTEM_TEMPLATE, ActionLoadSystemTemplate.class);
		this.register(Actions.LOAD_DASHBOARD_TEMPLATE, ActionLoadDashboardTemplate.class);
		this.register(Actions.LOAD_NOTIFICATIONS, ActionLoadNotifications.class);
		this.register(Actions.NOTIFICATIONS_READ, ActionNotificationsRead.class);
		this.register(Actions.NOTIFICATIONS_READ_ALL, ActionNotificationsReadAll.class);
		this.register(Actions.LOAD_TASK_HISTORY, ActionLoadTaskHistory.class);
		this.register(Actions.FOCUS_NODE_VIEW, ActionFocusNodeView.class);
		this.register(Actions.FOCUS_TASK_VIEW, ActionFocusTaskView.class);
		this.register(Actions.FOCUS_NODE_FIELD, ActionFocusNodeField.class);
		this.register(Actions.BLUR_NODE_FIELD, ActionBlurNodeField.class);
		this.register(Actions.CHANGE_ROLE, ActionChangeRole.class);
		this.register(Actions.PRINT_NODE, ActionPrintNode.class);
		this.register(Actions.PRINT_NODE_TIME_CONSUMPTION, ActionPrintNodeTimeConsumption.class);
		this.register(Actions.DOWNLOAD_PRINTED_NODE, ActionDownloadPrintedNode.class);
		this.register(Actions.PRINT_TASK_LIST, ActionPrintTaskList.class);
		this.register(Actions.UPDATE_NODE_LOCATION, ActionUpdateNodeLocation.class);
		this.register(Actions.CLEAN_NODE_LOCATION, ActionCleanNodeLocation.class);
		this.register(Actions.ADD_COMMENT_TO_POST, ActionAddCommentToPost.class);
		this.register(Actions.ADD_POST, ActionAddPost.class);
		this.register(Actions.LOAD_NEWS_NEXT_PAGE, ActionLoadNewsNextPage.class);
		this.register(Actions.ADD_FILTER, ActionAddFilter.class);
		this.register(Actions.SEARCH_ROLES, ActionSearchRoles.class);
		this.register(Actions.LOAD_TEAM, ActionLoadTeam.class);
		this.register(Actions.LOAD_USERS, ActionLoadUsers.class);
		this.register(Actions.LOAD_FEDERATION_USERS, ActionLoadFederationUsers.class);
		this.register(Actions.REPLACE_NODE_DOCUMENT, ActionReplaceNodeDocument.class);
		this.register(Actions.LOAD_NODE_FIELD_COMPOSITE_ITEM, ActionLoadNodeFieldCompositeItem.class);
		this.register(Actions.LOAD_NODE_REVISION_ITEMS, ActionLoadNodeRevisionItems.class);
		this.register(Actions.RESTORE_NODE_REVISION, ActionRestoreNodeRevision.class);
		this.register(Actions.LOAD_SOURCE, ActionLoadSource.class);
		this.register(Actions.BLUR_NODE_VIEW, ActionBlurNodeView.class);
		this.register(Actions.PREPARE_NODE_DOCUMENT_SIGNATURE, ActionPrepareNodeDocumentSignature.class);
		this.register(Actions.DELETE_NODE_DOCUMENT_SIGNATURE, ActionDeleteNodeDocumentSignature.class);
		this.register(Actions.STAMP_NODE_DOCUMENT_SIGNATURE, ActionStampNodeDocumentSignature.class);
		this.register(Actions.LOAD_SIGNATURE_ITEMS, ActionLoadSignatureItems.class);
		this.register(Actions.LOAD_ROLE_DEFINITION_LIST, ActionLoadRoleDefinitionList.class);
		this.register(Actions.LOAD_ROLE_LIST, ActionLoadRoleList.class);
		this.register(Actions.LOAD_GROUPED_ROLE_LIST, ActionLoadGroupedRoleList.class);
		this.register(Actions.ADD_USER_ROLE, ActionAddUserRole.class);
		this.register(Actions.ADD_SERVICE_ROLE, ActionAddServiceRole.class);
		this.register(Actions.ADD_FEEDER_ROLE, ActionAddFeederRole.class);
		this.register(Actions.SAVE_USER_ROLE, ActionSaveUserRole.class);
		this.register(Actions.SAVE_SERVICE_ROLE, ActionSaveServiceRole.class);
		this.register(Actions.SAVE_FEEDER_ROLE, ActionSaveFeederRole.class);
		this.register(Actions.PUBLISH_SOURCE_TERMS, ActionPublishSourceTerms.class);
		this.register(Actions.LOAD_SOURCE_NEW_TERMS, ActionLoadSourceNewTerms.class);
		this.register(Actions.LOAD_ATTACHMENT_ITEMS, ActionLoadAttachmentItems.class);
		this.register(Actions.PREVIEW_ATTACHMENT, ActionPreviewAttachment.class);
		this.register(Actions.TOGGLE_TASK_URGENCY, ActionToggleTaskUrgency.class);
		this.register(Actions.SET_TASK_OWNER, ActionSetTaskOwner.class);
		this.register(Actions.SET_TASKS_OWNER, ActionSetTasksOwner.class);
		this.register(Actions.UNSET_TASK_OWNER, ActionUnsetTaskOwner.class);
		this.register(Actions.UNSET_TASKS_OWNER, ActionUnsetTasksOwner.class);
		this.register(Actions.LOAD_TASK_ORDER_ITEMS, ActionLoadTaskOrderItems.class);
		this.register(Actions.LOAD_TASK_ORDER_CHAT_ITEMS, ActionLoadTaskOrderChatItems.class);
		this.register(Actions.SEND_TASK_ORDER_CHAT_MESSAGE, ActionSendTaskOrderChatMessage.class);
		this.register(Actions.LOAD_PARTNERS, ActionLoadPartners.class);
		this.register(Actions.SETUP_TASK_SEND_JOB, ActionSetupTaskSendJob.class);
		this.register(Actions.SELECT_TASK_SEND_JOB_ROLE, ActionSelectTaskSendJobRole.class);
		this.register(Actions.SAVE_NODE_PARTNER_CONTEXT, ActionSaveNodePartnerContext.class);
		this.register(Actions.LOAD_NODE_FIELD_CHECK_OPTIONS, ActionLoadNodeFieldCheckOptions.class);
		this.register(Actions.RESET_TASK_ORDER_NEW_MESSAGES, ActionResetTaskOrderNewMessages.class);
		this.register(Actions.LOAD_NODE_STATE, ActionLoadNodeState.class);
		this.register(Actions.RENDER_TASK_TIMELINE, ActionRenderTaskTimeline.class);
		this.register(Actions.ADD_NODE_NOTE, ActionAddNodeNote.class);
		this.register(Actions.DELETE_NODE_NOTE, ActionDeleteNodeNote.class);
		this.register(Actions.LOAD_NODE_CONTEXT, ActionLoadNodeContext.class);
		this.register(Actions.FILL_NODE_DOCUMENT, ActionFillNodeDocument.class);
		this.register(Actions.LOAD_MAIN_NODE, ActionLoadMainNode.class);
		this.register(Actions.SEND_TASK_REQUEST, ActionSendTaskRequest.class);
		this.register(Actions.SEND_TASK_RESPONSE, ActionSendTaskResponse.class);
		this.register(Actions.LOAD_NODE_GROUP_BY_OPTIONS, ActionLoadNodeGroupByOptions.class);
		this.register(Actions.LOAD_NODE_PRINT_ATTRIBUTES, ActionLoadNodePrintAttributes.class);
		this.register(Actions.LOAD_TASKLIST_PRINT_ATTRIBUTES, ActionLoadTaskListPrintAttributes.class);
		this.register(Actions.LOAD_NODE_ITEMS_LOCATIONS, ActionLoadNodeItemsLocations.class);
		this.register(Actions.LOAD_NODE_ITEMS_LOCATIONS_COUNT, ActionLoadNodeItemsLocationsCount.class);
		this.register(Actions.LOAD_TASK_COMMENTS, ActionLoadTaskComments.class);

		return true;
	}

	public synchronized static ActionsFactory getInstance() {
		if (oInstance == null) oInstance = new ActionsFactory();
		return oInstance;
	}

	public Action get(String sType, HttpServletRequest oRequest, HttpServletResponse oResponse) {
		Class<?> cAction;
		Action oAction = null;

		try {
			cAction = (Class<?>) this.hmActions.get(sType);
			oAction = (Action) cAction.newInstance();
			oAction.setRequest(oRequest);
			oAction.setResponse(oResponse);
			oAction.initialize();
		} catch (NullPointerException oException) {
			throw new SystemException(ErrorCode.ACTIONS_FACTORY, sType, oException);
		} catch (BaseException oException) {
			throw oException;
		} catch (Exception oException) {
			throw new SystemException(ErrorCode.ACTIONS_FACTORY, sType, oException);
		}

		return oAction;
	}

	public Boolean register(String sType, Object cAction)
		throws IllegalArgumentException {

		if ((cAction == null) || (sType == null)) {
			return false;
		}
		this.hmActions.put(sType, cAction);

		return true;
	}

}
