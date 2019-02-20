package org.monet.space.explorer.model;

public interface Error {
	String BUSINESS_UNIT_NOT_INSTALLED = "business-unit-not-installed";
	String BUSINESS_UNIT_STOPPED = "business-unit-stopped";
	String USER_NOT_LOGGED = "user-not-logged";
	String TASK_EDITION = "task-edition";
	String TASK_DELEGATION_PLACE_INCORRECT = "task-delegation-place-incorrect";
	String TASK_DELEGATION_NO_NATURE = "task-delegation-no-nature";
	String TASK_LINE_PLACE_INCORRECT = "task-line-place-incorrect";
	String TASK_EDITION_PLACE_INCORRECT = "task-edition-place-incorrect";
	String TASK_WAIT_PLACE_INCORRECT = "task-wait-place-incorrect";
	String NODE_FIELD_NOT_FOUND = "node-parent-field-not-found";
	String NODE_PARENT_FIELD_NOT_FOUND = "node-parent-field-not-found";
	String TASK_COULD_NOT_CREATE_PROGRESS_IMAGE = "task-could-not-create-progress-image";
	String PAGE_NOT_FOUND = "page-not-found";
	String NODE_DOCUMENT_REQUIRED = "node-document-required";
	String PREVIEW_NOT_SUPPORTED = "preview-not-supported";
	String PREVIEW_NODE = "preview-node";
	String DOWNLOAD_NOT_SUPPORTED = "download-not-supported";
	String DOWNLOAD_NODE = "download-node";
	String UPLOAD_NOT_SUPPORTED = "upload-not-supported";
	String UPLOAD_NODE = "upload-node";
}
