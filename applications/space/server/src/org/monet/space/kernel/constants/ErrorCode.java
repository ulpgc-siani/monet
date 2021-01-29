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

package org.monet.space.kernel.constants;

public abstract class ErrorCode {

	public static final String UNDEFINED = "ERR_UNDEFINED";
	public static final String UNSERIALIZE_NODE = "ERR_UNSERIALIZE_NODE";
	public static final String UNSERIALIZE_TASK_FROM_XML = "ERR_UNSERIALIZE_TASK_FROM_XML";
	public static final String CREATE_NODE = "ERR_CREATE_NODE";
	public static final String CREATE_REPORT = "ERR_CREATE_REPORT";
	public static final String CREATE_TASK = "ERR_CREATE_TASK";
	public static final String CREATE_SERVICE = "ERR_CREATE_SERVICE";
	public static final String CREATE_SERVICE_LINK = "ERR_CREATE_SERVICE_LINK";
	public static final String CREATE_SERVICE_USE = "ERR_CREATE_SERVICE_USE";
	public static final String EXIST_NODE = "ERR_EXIST_NODE";
	public static final String EXIST_TASK = "ERR_EXIST_TASK";
	public static final String LOAD_NODE = "ERR_LOAD_NODE";
	public static final String LOAD_NODELIST = "ERR_LOAD_NODELIST";
	public static final String LOAD_MESSAGE_QUEUE = "ERR_LOAD_MESSAGE_QUEUE";
	public static final String LOAD_SERVICE = "ERR_LOAD_SERVICE";
	public static final String LOAD_SERVICE_FOR_TASK = "ERR_LOAD_SERVICE_FOR_TASK";
	public static final String LOAD_SERVICE_BY_REQUEST_ID = "ERR_LOAD_SERVICE_BY_REQUEST_ID";
	public static final String LOAD_SERVICELINK = "ERR_LOAD_SERVICE_LINK";
	public static final String LOAD_SERVICELINKLIST = "ERR_LOAD_SERVICE_LINK_LIST";
	public static final String LOAD_SERVICEUSE = "ERR_LOAD_SERVICE_USE";
	public static final String LOAD_LOCK = "ERR_LOAD_LOCK";
	public static final String LOAD_REPORT = "ERR_LOAD_REPORT";
	public static final String LOAD_REPORTLIST = "ERR_LOAD_REPORTLIST";
	public static final String LOAD_TASK = "ERR_LOAD_TASK";
	public static final String LOAD_TASKLIST = "ERR_LOAD_TASKLIST";
	public static final String LOAD_DOCUMENT = "ERR_LOAD_DOCUMENT";
	public static final String LOAD_DOCUMENTLIST = "ERR_LOAD_DOCUMENTLIST";
	public static final String LOAD_USER = "ERR_LOAD_USER";
	public static final String LOAD_LOGBOOK_SUBSCRIBERLIST = "ERR_LOAD_LOGBOOK_SUBSCRIBERLIST";
	public static final String LOAD_ACCOUNT = "ERR_LOAD_ACCOUNT";
	public static final String LOAD_FACTS = "ERR_LOAD_FACTS";
	public static final String LOAD_POST = "ERR_LOAD_POST";
	public static final String LOAD_POST_LIST = "ERR_LOAD_POST_LIST";
	public static final String SAVE_ACCOUNT = "ERR_SAVE_ACCOUNT";
	public static final String SEARCH_LOGBOOK = "ERR_SEARCH_LOGBOOK";
	public static final String LOAD_USERS = "ERR_LOAD_USERS";
	public static final String GET_NODESSORTING = "ERR_GET_NODESSORTING";
	public static final String SET_NODESSORTING = "ERR_SET_NODESSORTING";
	public static final String SET_NODESFILTERING = "ERR_SET_NODESFILTERING";
	public static final String DOWNLOAD_DOCUMENT = "ERR_DOWNLOAD_DOCUMENT";
	public static final String INVALID_OPERATION = "ERR_INVALID_OPERATION";
	public static final String CHANGE_PASSWORD = "ERR_CHANGE_PASSWORD";
	public static final String READ_NODE_PERMISSIONS = "ERR_READ_NODE_PERMISSIONS";
	public static final String READ_TASK_PERMISSIONS = "ERR_READ_TASK_PERMISSIONS";
	public static final String READ_CUBE_PERMISSIONS = "ERR_READ_CUBE_PERMISSIONS";
	public static final String READ_NODE_LIST_PERMISSIONS = "ERR_READ_NODE_LIST_PERMISSIONS";
	public static final String WRITE_NODE_PERMISSIONS = "ERR_WRITE_NODE_PERMISSIONS";
	public static final String WRITE_TASK_PERMISSIONS = "ERR_WRITE_TASK_PERMISSIONS";
	public static final String CREATE_DELETE_NODE_PERMISSIONS = "ERR_CREATE_DELETE_NODE_PERMISSIONS";
	public static final String CREATE_DELETE_TASK_PERMISSIONS = "ERR_CREATE_DELETE_TASK_PERMISSIONS";
	public static final String LOAD_NODELIST_FROM_TRASH = "ERR_LOAD_NODELIST_FROM_TRASH";
	public static final String EMPTY_NODES_TRASH = "ERR_EMPTY_NODES_TRASH";
	public static final String EMPTY_TASKS_TRASH = "ERR_EMPTY_TASKS_TRASH";
	public static final String FILESYSTEM_READ_FILE = "ERR_FILESYSTEM_READ_FILE";
	public static final String FILESYSTEM_WRITE_FILE = "ERR_FILESYSTEM_WRITE_FILE";
	public static final String DATABASE_SELECT_QUERY = "ERR_DATABASE_SELECT_QUERY";
	public static final String DATABASE_UPDATE_QUERY = "ERR_DATABASE_UPDATE_QUERY";
	public static final String DATABASE_CONNECTION = "ERR_DATABASE_CONNECTION";
	public static final String DATABASE_DISCONNECTION = "ERR_DATABASE_DISCONNECTION";
	public static final String SAVE_NODE = "ERR_SAVE_NODE";
	public static final String SAVE_NODE_REFERENCE = "ERR_SAVE_NODE_REFERENCE";
	public static final String CALCULATE_NODE_REFERENCE = "ERR_CALCULATE_NODE_REFERENCE";
	public static final String CREATE_NODE_REFERENCE = "ERR_CREATE_NODE_REFERENCE";
	public static final String REMOVE_NODE_REFERENCE = "ERR_REMOVE_NODE_REFERENCE";
	public static final String GENERATE_NODE_SUPER_DATA_ID = "ERR_GENERATE_NODE_SUPER_DATA_ID";
	public static final String LOAD_TASKLIST_FROM_TRASH = "ERR_LOAD_TASKLIST_FROM_TRASH";
	public static final String LOAD_NODE_REFERENCE = "ERR_LOAD_NODE_REFERENCE";
	public static final String NODE_LOAD_REVISIONS = "ERR_LOAD_NODE_REVISIONS";
	public static final String GET_QUERY_PARAMETER_VALUE = "ERR_GET_QUERY_PARAMETER_VALUE";
	public static final String UNSERIALIZE_DEFINITION_FROM_XML = "ERR_UNSERIALIZE_NODE_FROM_XML";
	public static final String UNSERIALIZE_ATTRIBUTE_LIST_FROM_XML = "ERR_UNSERIALIZE_ATTRIBURE_LIST_FROM_XML";
	public static final String UNKOWN_DATABASE_QUERY = "ERR_UNKOWN_DATABASE_QUERY";
	public static final String UNKOWN_PROPERTY = "ERR_UNKOWN_DATABASE_TABLE";
	public static final String LOAD_WORDS = "ERR_LOAD_WORDS";
	public static final String LOAD_SIMILAR_WORDS = "ERR_LOAD_SIMILAR_WORDS";
	public static final String SEARCH = "ERR_SEARCH";
	public static final String SOURCE_STORE_LOAD = "ERR_SOURCE_STORE_LOAD";
	public static final String SOURCE_INDEX_LOAD = "ERR_SOURCE_INDEX_LOAD";
	public static final String SOURCE_INDEX_SEARCH_CODE = "ERR_SOURCE_INDEX_SEARCH_CODE";
	public static final String SOURCE_INDEX_SEARCH_LABEL = "ERR_SOURCE_INDEX_SEARCH_LABEL";
	public static final String HISTORY_STORE_CREATE = "ERR_HISTORY_STORE_CREATE";
	public static final String HISTORY_STORE_LOAD = "ERR_HISTORY_STORE_LOAD";
	public static final String HISTORY_STORE_LOAD_DEFAULT_VALUES = "ERR_HISTORY_STORE_LOAD_DEFAULT_VALUES";
	public static final String HISTORY_STORE_LOAD_DEFAULT_VALUE = "ERR_HISTORY_STORE_LOAD_DEFAULT_VALUE";
	public static final String HISTORY_STORE_EXIST = "ERR_HISTORY_STORE_EXIST";
	public static final String COMPLETE_NODE = "ERR_COMPLETE_NODE";
	public static final String BUSINESS_MODEL_NOT_FOUND = "ERR_BUSINESS_MODEL_NOT_FOUND";
	public static final String BUSINESS_MODEL_DEFINITION_ERROR = "ERR_BUSINESS_MODEL_DEFINITION_ERROR";
	public static final String BUSINESS_UNIT_NOT_FOUND = "ERR_BUSINESS_UNIT_NOT_FOUND";
	public static final String DATABASE_COMMIT = "ERR_DATABASE_COMMIT";
	public static final String SEARCH_USERS = "ERR_SEARCH_USERS";
	public static final String LOAD_ROOT_NODE = "ERR_LOAD_ROOT_NODE";
	public static final String ACTIONS_FACTORY = "ERR_ACTIONS_FACTORY";
	public static final String PRODUCERS_FACTORY = "ERR_PRODUCERS_FACTORY";
	public static final String VIEWS_FACTORY = "ERR_VIEWS_FACTORY";
	public static final String TRANSLATORS_FACTORY = "ERR_TRANSLATORS_FACTORY";
	public static final String READ_LANGUAGE_FILE = "ERR_READ_LANGUAGE_FILE";
	public static final String GENERATE_ID = "ERR_GENERATE_ID";
	public static final String BUSINESS_MODEL_FILE_NOT_FOUND = "ERR_BUSINESS_MODEL_FILE_NOT_FOUND";
	public static final String UNSERIALIZE_PROJECT_FROM_XML = "ERR_UNSERIALIZE_PROJECT_FROM_XML";
	public static final String UNSERIALIZE_DICTIONARY_FROM_XML = "ERR_UNSERIALIZE_DICTIONARY_FROM_XML";
	public static final String LOAD_BUSINESS_MODEL_LIST = "ERR_LOAD_BUSINESS_MODEL_LIST";
	public static final String QUERY_FAILED = "ERR_QUERY_FAILED";
	public static final String BUSINESS_UNIT_STOPPED = "ERR_BUSINESS_UNIT_STOPPED";
	public static final String UNSERIALIZE_APPLICATION_FROM_XML = "ERR_UNSERIALIZE_APPLICATION_FROM_XML";
	public static final String UNSERIALIZE_COMPONENT_FROM_XML = "ERR_UNSERIALIZE_COMPONENT_FROM_XML";
	public static final String UNSERIALIZE_TRANSLATOR_FROM_XML = "ERR_UNSERIALIZE_TRANSLATOR_FROM_XML";
	public static final String UNSERIALIZE_BUSINESS_UNIT_FROM_XML = "ERR_UNSERIALIZE_BUSINESS_UNIT_FROM_XML";
	public static final String BUSINESS_UNIT_COULD_NOT_START = "ERR_BUSINESS_UNIT_COULD_NOT_START";
	public static final String BUSINESS_UNIT_COULD_NOT_STOP = "ERR_BUSINESS_UNIT_COULD_NOT_STOP";
	public static final String COMPONENT_NOT_INITIALIZED = "ERR_COMPONENT_NOT_INITIALIZED";
	public static final String APPLICATION_NOT_FOUND = "ERR_APPLICATION_NOT_FOUND";
	public static final String COMPONENT_NOT_FOUND = "ERR_COMPONENT_NOT_FOUND";
	public static final String TRANSLATOR_NOT_FOUND = "ERR_TRANSLATOR_NOT_FOUND";
	public static final String EXPORT_FORMAT_NOT_FOUND = "ERR_EXPORT_FORMAT_NOT_FOUND";
	public static final String EXPORT_FORMAT = "ERR_EXPORT_FORMAT";
	public static final String IMPORT_FORMAT_NOT_FOUND = "ERR_IMPORT_FORMAT_NOT_FOUND";
	public static final String IMPORT_FORMAT = "ERR_IMPORT_FORMAT";
	public static final String IMPORT_DATA_WRONG = "ERR_IMPORT_DATA_WRONG";
	public static final String IMPORT_NODE_TYPE_INCORRECT = "ERR_IMPORT_NODE_TYPE_INCORRECT";
	public static final String BUSINESS_MODEL_DEFINITION_NOT_FOUND = "ERR_BUSINESS_MODEL_DEFINITION_NOT_FOUND";
	public static final String BUSINESS_MODEL_DEFINITION_NO_IMPLEMENTATION_FOUND = "BUSINESS_MODEL_DEFINITION_NO_IMPLEMENTATION_FOUND";
	public static final String BUSINESS_MODEL_DEFINITION_DICTIONARY_NOT_FOUND = "ERR_BUSINESS_MODEL_DEFINITION_DICTIONARY_NOT_FOUND";
	public static final String BUSINESS_MODEL_BEHAVIOUR_NOT_FOUND = "ERR_BUSINESS_MODEL_BEHAVIOUR_NOT_FOUND";
	public static final String CREATE_USER = "ERR_CREATE_USER";
	public static final String CLOSE_QUERY = "ERR_CLOSE_QUERY";
	public static final String POPULATE_SOURCE = "ERR_POPULATE_SOURCE";
	public static final String LOAD_SOURCE = "ERR_LOAD_SOURCE";
	public static final String CREATE_SOURCE = "ERR_CREATE_SOURCE";
	public static final String REMOVE_SOURCE = "ERR_REMOVE_SOURCE";
	public static final String REQUEST_LOGBOOKTASK_ENTRIES = "ERR_REQUEST_LOGBOOKTASK_ENTRIES";
	public static final String REQUEST_LOGBOOKTASK_ENTRIES_COUNT = "ERR_REQUEST_LOGBOOKTASK_ENTRIES_COUNT";
	public static final String LOAD_ROLE_LIST = "ERR_LOAD_ROLE_LIST";
	public static final String LOAD_TASK_GRANTED_USERS_FOR_TARGET = "ERR_LOAD_TASK_GRANTED_USERS_FOR_TARGET";
	public static final String LOAD_TASK_GRANTED_USERS_FOR_TASK = "ERR_LOAD_TASK_GRANTED_USERS_FOR_TASK";
	public static final String LINK_COUNT_SOURCES = "ERR_LINK_COUNT_SOURCES";
	public static final String NODE_LINKS_FROM_TASK_COUNT = "ERR_NODE_LINKS_FROM_TASK_COUNT";
	public static final String LOAD_RECOMMENDATIONS = "ERR_LOAD_RECOMMENDATIONS";
	public static final String LOAD_RECOMMENDATION = "ERR_LOAD_RECOMMENDATION";
	public static final String DEFINITION_TEMPLATE_NOT_DECLARED = "ERR_DEFINITION_TEMPLATE_NOT_DECLARED";
	public static final String DEFINITION_TEMPLATE_NOT_FOUND = "ERR_DEFINITION_TEMPLATE_NOT_FOUND";
	public static final String UPLOAD_DOCUMENT_TEMPLATE = "ERR_UPLOAD_DOCUMENT_TEMPLATE";
	public static final String CONSOLIDATE_DOCUMENT = "ERR_CONSOLIDATE_DOCUMENT";
	public static final String EXISTS_DOCUMENT = "ERR_EXISTS_DOCUMENT";
	public static final String UPLOAD_DOCUMENT = "ERR_UPLOAD_DOCUMENT";
	public static final String CREATE_DOCUMENT = "ERR_CREATE_DOCUMENT";
	public static final String CREATE_DOCUMENT_INTEROPERABLE = "ERR_CREATE_DOCUMENT_INTEROPERABLE";
	public static final String UPDATE_DOCUMENT = "ERR_UPDATE_DOCUMENT";
	public static final String REMOVE_DOCUMENT = "ERR_REMOVE_DOCUMENT";
	public static final String GET_DOCUMENT = "ERR_GET_DOCUMENT";
	public static final String COPY_NODE = "ERR_COPY_NODE";
	public static final String REMOVE_NODE = "ERR_REMOVE_NODE";
	public static final String ADD_NODE = "ERR_ADD_NODE";
	public static final String BPI_BEHAVIOR_NOT_FOUND = "ERR_BPI_BEHAVIOR_NOT_FOUND";
	public static final String BPI_FIELD_FACTORY = "ERR_BPI_FIELD_FACTORY";
	public static final String CREATE_SEQUENCE_VALUE = "ERR_CREATE_SEQUENCE_VALUE";
	public static final String LOAD_SOURCELIST = "ERR_LOAD_SOURCELIST";
	public static final String LOAD_SOURCELIST_COUNT = "ERR_LOAD_SOURCELIST_COUNT";
	public static final String REQUEST_FACTBOOKTASK_ENTRIES = "ERR_REQUEST_FACTBOOKTASK_ENTRIES";
	public static final String TASK_FACTS_ENTRIES_COUNT = "ERR_REQUEST_FACTBOOKTASK_ENTRIES_COUNT";
	public static final String SERIALIZE_TO_XML = "ERR_SERIALIZE_TO_XML";
	public static final String CREATE_TASK_LOCK = "ERR_CREATE_TASK_LOCK";
	public static final String CALL_SERVICE = "ERR_CALL_SERVICE";
	public static final String CALL_SERVICE_CALLBACK = "ERR_CALL_SERVICE_CALLBACK";
	public static final String PROCESS_STEP = "ERR_PROCESS_STEP";
	public static final String LOAD_EVENT_LOGS = "ERR_LOAD_EVENT_LOGS";
	public static final String ATTRIBUTE_NOT_FOUND = "ERR_ATTRIBUTE_NOT_FOUND";
	public static final String NODE_NOT_FOUND = "ERR_NODE_NOT_FOUND";
	public static final String LOAD_NOTIFICATIONLIST = "ERR_LOAD_NOTIFICATIONLIST";
	public static final String LOAD_REFERENCELIST = "ERR_LOAD_REFERENCELIST";
	public static final String LOAD_REFERENCE_ATTRIBUTE_VALUES = "ERR_LOAD_REFERENCE_ATTRIBUTE_VALUES";
	public static final String LOAD_REFERENCE_ATTRIBUTE_VALUES_COUNT = "ERR_LOAD_REFERENCE_ATTRIBUTE_VALUES_COUNT";
	public static final String COMPILE = "ERR_COMPILE";
	public static final String LOAD_NODE_PROTOTYPES = "ERR_LOAD_NODE_PROTOTYPES";
	public static final String FEDERATION_CONNECTION = "ERR_FEDERATION_CONNECTION";
	public static final String LOAD_SERVICE_PROVIDER_CODES = "ERR_LOAD_SERVICE_PROVIDER_CODES";
	public static final String WORKQUEUE_ITEM = "ERR_WORKQUEUE_ITEM";
	public static final String LOAD_CUBE = "ERR_LOAD_CUBE";
	public static final String LOCATE_CUBE = "ERR_LOCATE_CUBE";
	public static final String LOAD_NODE_PARENTS = "ERR_LOAD_NODE_PARENTS";
	public static final String LOAD_LOCATIONLIST = "ERR_LOAD_LOCATIONLIST";
	public static final String OVERWRITE_DOCUMENT = "ERR_OVERWRITE_DOCUMENT";
	public static final String LOAD_FILTERGROUPLIST = "ERR_LOAD_FILTERGROUPLIST";
	public static final String LOAD_FILTERGROUP = "ERR_LOAD_FILTERGROUP";
	public static final String LOAD_ROLELIST = "ERR_LOAD_ROLELIST";
	public static final String LOAD_ROLE = "ERR_LOAD_ROLE";
	public static final String LOAD_USERLIST = "ERR_LOAD_USERLIST";
	public static final String LOAD_USERS_OF_ROLE = "ERR_LOAD_USERS_OF_ROLE";
	public static final String LOAD_ATTACHMENT_ITEMS = "ERR_LOAD_ATTACHMENT_ITEMS";
	public static final String LOAD_MAILBOX = "ERR_LOAD_MAILBOX";
	public static final String SAVE_SNAPSHOT = "ERR_SAVE_SNAPSHOT";
	public static final String LOAD_CHAT_ENTRIES = "ERR_LOAD_CHAT_ENTRIES";
	public static final String LOAD_TASKORDERLIST = "ERR_LOAD_TASKORDERLIST";
	public static final String LOAD_PARTNER = "ERR_LOAD_PARTNER";
	public static final String LOAD_MASTERLIST = "ERR_LOAD_MASTERLIST";
	public static final String SEARCH_REFERENCE = "ERR_SEARCH_REFERENCE";
	public static final String LOAD_GLOSSARY = "ERR_LOAD_GLOSSARY";
	public static final String LOAD_NODE_IDS = "ERR_LOAD_NODE_IDS";
	public static final String LOAD_TASK_IDS = "ERR_LOAD_TASK_IDS";
	public static final String LOAD_NODE_PARTNER_CONTEXT = "ERR_LOAD_PARTNER_CONTEXT";
    public static final String LOAD_DATABASE_VERSION = "ERR_LOAD_DATABASE_VERSION";
    public static final String LOAD_EVENT_LIST = "ERR_LOAD_EVENT_LIST";
    public static final String LOAD_DATASTORE_QUEUE = "ERR_LOAD_DATASTORE_QUEUE";
    public static final String COULD_NODE_REMOVE_LINKED_NODE = "ERR_COULD_NODE_REMOVE_LINKED_NODE";
}
