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

package org.monet.space.backservice.control.actions;

import org.monet.space.backservice.control.constants.Actions;
import org.monet.space.backservice.core.constants.ErrorCode;
import org.monet.space.kernel.exceptions.SystemException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

public class ActionsFactory {
	private static ActionsFactory instance;
	private HashMap<String, Object> actions;

	private ActionsFactory() {
		this.actions = new HashMap<String, Object>();
		this.registerActions();
	}

	private void registerActions() {
		this.register(Actions.ADD_DATASTORE_CUBE_FACT, ActionAddDatastoreCubeFact.class);
		this.register(Actions.ADD_DATASTORE_CUBE_FACTS, ActionAddDatastoreCubeFacts.class);
		this.register(Actions.ADD_DATASTORE_DIMENSION_COMPONENT, ActionAddDatastoreDimensionComponent.class);
		this.register(Actions.ADD_NODE_FLAG, ActionAddNodeFlag.class);
		this.register(Actions.ADD_TASK_FACT, ActionAddTaskFact.class);
		this.register(Actions.ADD_TASK_FLAG, ActionAddTaskFlag.class);
		this.register(Actions.ADD_TASK_SHORTCUT, ActionAddTaskShortCut.class);
		this.register(Actions.CONSOLIDATE_NODE, ActionConsolidateNode.class);
		this.register(Actions.ADD_NODE_NOTE, ActionAddNodeNote.class);
		this.register(Actions.CREATE_ACCOUNT, ActionCreateAccount.class);
		this.register(Actions.CREATE_NODE, ActionCreateNode.class);
		this.register(Actions.CREATE_TASK, ActionCreateTask.class);
		this.register(Actions.DELETE_NODE_FLAG, ActionDeleteNodeFlag.class);
		this.register(Actions.DELETE_NODE_NOTE, ActionDeleteNodeNote.class);
		this.register(Actions.DELETE_TASK_FLAG, ActionDeleteTaskFlag.class);
		this.register(Actions.DELETE_TASK_SHORTCUT, ActionDeleteTaskShortCut.class);
		this.register(Actions.DOWNLOAD_DISTRIBUTION, ActionDownloadDistribution.class);
		this.register(Actions.EMPTY_TRASH, ActionEmptyTrash.class);
		this.register(Actions.EXECUTE_EXPORTER, ActionExecuteExporter.class);
		this.register(Actions.EXECUTE_NODE_COMMAND, ActionExecuteNodeCommand.class);
		this.register(Actions.EXPORT_NODE, ActionExportNode.class);
		this.register(Actions.EXPORT_NODES, ActionExportNodes.class);
		this.register(Actions.GET_NODE_ANCESTORS, ActionGetNodeAncestors.class);
		this.register(Actions.GET_NODE_FILE, ActionGetNodeFile.class);
		this.register(Actions.GET_NODE_DOCUMENT, ActionGetNodeDocument.class);
		this.register(Actions.GET_NODE_FLAGS, ActionGetNodeFlags.class);
		this.register(Actions.GET_NODE_LOCATION, ActionGetNodeLocation.class);
		this.register(Actions.GET_NODE_NOTES, ActionGetNodeNotes.class);
		this.register(Actions.GET_NODE_REFERENCE, ActionGetNodeReference.class);
		this.register(Actions.GET_NODE_REFERENCES, ActionGetNodeReferences.class);
		this.register(Actions.GET_NODE_REFERENCES_COUNT, ActionGetNodeReferencesCount.class);
		this.register(Actions.GET_TASKS, ActionGetTasks.class);
		this.register(Actions.GET_TASK_FACTS, ActionGetTaskFacts.class);
		this.register(Actions.GET_TASK_FLAGS, ActionGetTaskFlags.class);
		this.register(Actions.GET_TASK_PROCESS, ActionGetTaskProcess.class);
		this.register(Actions.GET_TASK_SHORTCUTS, ActionGetTaskShortCuts.class);
		this.register(Actions.GET_NODE_SCHEMA, ActionGetNodeSchema.class);
		this.register(Actions.GET_NODE_TASKS, ActionGetNodeTasks.class);
		this.register(Actions.GET_SOURCE_TERMS, ActionGetSourceTerms.class);
		this.register(Actions.GET_SOURCE_PARENT_TERM, ActionGetSourceParentTerm.class);
		this.register(Actions.GET_USER_LINKED_TO_NODE, ActionGetUserLinkedToNode.class);
		this.register(Actions.GET_USER_NODE, ActionGetUserNode.class);
		this.register(Actions.GET_USER_TASKS, ActionGetUserTasks.class);
		this.register(Actions.IMPORT_NODE, ActionImportNode.class);
		this.register(Actions.LOAD_USER, ActionLoadUser.class);
		this.register(Actions.LOCATE_NODE, ActionLocateNode.class);
		this.register(Actions.MAKE_NODE_PRIVATE, ActionMakeNodePrivate.class);
		this.register(Actions.MAKE_NODE_DELETEABLE, ActionMakeNodeDeleteable.class);
		this.register(Actions.MAKE_NODE_EDITABLE, ActionMakeNodeEditable.class);
		this.register(Actions.MAKE_NODE_PUBLIC, ActionMakeNodePublic.class);
		this.register(Actions.MAKE_NODE_UNDELETEABLE, ActionMakeNodeUnDeleteable.class);
		this.register(Actions.MAKE_NODE_UNEDITABLE, ActionMakeNodeUnEditable.class);
		this.register(Actions.OPEN_DATASTORE, ActionOpenDatastore.class);
		this.register(Actions.OPEN_NODE, ActionOpenNode.class);
		this.register(Actions.OPEN_TASK, ActionOpenTask.class);
		this.register(Actions.RECOVER_NODE, ActionRecoverNode.class);
		this.register(Actions.REMOVE_NODE, ActionRemoveNode.class);
		this.register(Actions.REMOVE_TASK, ActionRemoveTask.class);
		this.register(Actions.RESET_NODE_FORM, ActionResetNodeForm.class);
		this.register(Actions.RESUME_TASK, ActionResumeTask.class);
		this.register(Actions.RUN_TASK, ActionRunTask.class);
		this.register(Actions.SAVE_NODE, ActionSaveNode.class);
		this.register(Actions.SAVE_NODE_REFERENCE, ActionSaveNodeReference.class);
		this.register(Actions.SAVE_NODES_ATTRIBUTE, ActionSaveNodesAttribute.class);
		this.register(Actions.SAVE_TASK, ActionSaveTask.class);
		this.register(Actions.SAVE_USER, ActionSaveUser.class);
		this.register(Actions.SEARCH_EVENT, ActionSearchEvent.class);
		this.register(Actions.SEARCH_NODES, ActionSearchNodes.class);
		this.register(Actions.SHOW_API, ActionShowApi.class);
		this.register(Actions.SUBSCRIBE, ActionSubscribe.class);
		this.register(Actions.SAVE_NODE_PARENT, ActionSaveNodeParent.class);
		this.register(Actions.SAVE_NODE_PARTNER_CONTEXT, ActionSaveNodePartnerContext.class);
		this.register(Actions.SAVE_NODE_FILE, ActionSaveNodeFile.class);
		this.register(Actions.SAVE_NODE_PICTURE, ActionSaveNodePicture.class);
		this.register(Actions.SAVE_NODE_DOCUMENT, ActionSaveNodeDocument.class);
		this.register(Actions.EXISTS_NODE, ActionExistsNode.class);
		this.register(Actions.GET_NODE_DOCUMENT_CONTENT_TYPE, ActionGetNodeDocumentContentType.class);
		this.register(Actions.ADD_SOURCE_TERM, ActionAddSourceTerm.class);
		this.register(Actions.LOAD_SOURCE, ActionLoadSource.class);
		this.register(Actions.LOCATE_SOURCE, ActionLocateSource.class);
		this.register(Actions.UNLOCK_TASK, ActionUnLockTask.class);
		this.register(Actions.GOTO_PLACE_IN_TASK, ActionGotoPlaceInTask.class);
		this.register(Actions.HAS_PERMISSIONS, ActionHasPermissions.class);
	}

	public synchronized static ActionsFactory getInstance() {
		if (instance == null) instance = new ActionsFactory();
		return instance;
	}

	public String getOperation(String op) {
		if (this.actions.containsKey(op)) return op;
		return Actions.SHOW_API;
	}

	public Action get(String type, HttpServletRequest request, HttpServletResponse response, HashMap<String, Object> parameters) {
		Class<?> clazz;
		Action action = null;

		try {
			clazz = (Class<?>) this.actions.get(type);
			action = (Action) clazz.newInstance();
			action.setRequest(request);
			action.setResponse(response);
			action.setParameters(parameters);
			action.initialize();
		} catch (NullPointerException exception) {
			throw new SystemException(ErrorCode.ACTIONS_FACTORY, type, exception);
		} catch (Exception exception) {
			throw new SystemException(ErrorCode.ACTIONS_FACTORY, type, exception);
		}

		return action;
	}

	public Boolean register(String sType, Object cAction)
		throws IllegalArgumentException {

		if ((cAction == null) || (sType == null)) {
			return false;
		}
		this.actions.put(sType, cAction);

		return true;
	}

}
