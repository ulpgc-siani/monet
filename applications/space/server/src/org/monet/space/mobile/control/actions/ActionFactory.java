package org.monet.space.mobile.control.actions;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.errors.UnknownActionError;

import java.util.HashMap;
import java.util.Map;

import static org.monet.mobile.service.ActionCode.*;

public class ActionFactory {
	private static ActionFactory instance;
	private final Map<ActionCode, Class<? extends Action<?>>> actionsMap = new HashMap<>();

	private ActionFactory() {
		registerActions();
	}

	private void registerActions() {
		actionsMap.put(Helo, ActionDoHelo.class);
		actionsMap.put(Register, ActionDoRegister.class);
		actionsMap.put(Unregister, ActionDoUnregister.class);
		actionsMap.put(LoadNewDefinitions, ActionDoLoadNewDefinitions.class);
		actionsMap.put(LoadNewGlossaries, ActionDoLoadNewGlossaries.class);
		actionsMap.put(DownloadGlossary, ActionDoDownloadGlossary.class);
		actionsMap.put(LoadNewAssignedTasks, ActionDoLoadNewAssignedTasks.class);
		actionsMap.put(LoadNewAvailableTasks, ActionDoLoadNewAvailableTasks.class);
		actionsMap.put(LoadAssignedTasksToDelete, ActionDoLoadAssignedTasksToDelete.class);
		actionsMap.put(LoadFinishedTasksToDelete, ActionDoLoadFinishedTasksToDelete.class);
		actionsMap.put(LoadUnassignedTasksToDelete, ActionDoLoadUnassignedTasksToDelete.class);
		actionsMap.put(AssignTask, ActionDoAssignTask.class);
		actionsMap.put(UnassignTask, ActionDoUnassignTask.class);
		actionsMap.put(DownloadTaskPacked, ActionDoDownloadTaskPacked.class);
		actionsMap.put(UploadTaskPacked, ActionDoUploadTaskPacked.class);
		actionsMap.put(PrepareUploadTask, ActionDoPrepareUploadTask.class);
		actionsMap.put(UploadTaskFile, ActionDoUploadTaskFile.class);
		actionsMap.put(UploadTaskSchema, ActionDoUploadTaskSchema.class);
		actionsMap.put(SyncChats, ActionDoSyncChats.class);
	}

	public synchronized static ActionFactory getInstance() {
		if (instance == null)
			instance = new ActionFactory();
		return instance;
	}

	public Action<?> get(ActionCode actionCode) throws Exception {
		Class<? extends Action<?>> actionClass = actionsMap.get(actionCode);
		if (actionClass == null)
			throw new ActionException(new UnknownActionError());
		return actionClass.newInstance();
	}
}
