package org.monet.mobile.service;

public enum ActionCode {
	Helo,
	Register,
	Unregister,
	LoadNewDefinitions,
	LoadNewGlossaries,
	DownloadGlossary,
	LoadNewAssignedTasks,
	LoadNewAvailableTasks,
	LoadAssignedTasksToDelete,
	LoadFinishedTasksToDelete,
	LoadUnassignedTasksToDelete,
	AssignTask,
	UnassignTask,
	DownloadTaskPacked,
	UploadTaskPacked,
	PrepareUploadTask,
	UploadTaskFile,
	UploadTaskSchema,
	SyncChats
}
