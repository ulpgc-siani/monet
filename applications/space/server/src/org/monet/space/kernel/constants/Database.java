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

public abstract class Database {

	public abstract class ConnectionTypes {
		public static final String MANUAL_COMMIT = "manual";
		public static final String AUTO_COMMIT = "auto";
	}

	public abstract class Types {
		public static final String MYSQL = "mysql";
		public static final String ORACLE = "oracle";
	}

	public abstract class Queries {
		public static final String INFO_LOAD_VERSION = "INFO_LOAD_VERSION";
		public static final String INFO_UPDATE_VERSION = "INFO_UPDATE_VERSION";
		public static final String INFO_LOAD_YEAR_SEQUENCE = "INFO_LOAD_YEAR_SEQUENCE";
		public static final String INFO_INSERT_YEAR_SEQUENCE = "INFO_INSERT_YEAR_SEQUENCE";
		public static final String INFO_UPDATE_YEAR_SEQUENCE = "INFO_UPDATE_YEAR_SEQUENCE";

		public static final String ACCOUNT_EXISTS = "ACCOUNT_EXISTS";
		public static final String ACCOUNT_LOAD = "ACCOUNT_LOAD";
		public static final String ACCOUNT_LOAD_BY_USERNAME = "ACCOUNT_LOAD_BY_USERNAME";
		public static final String ACCOUNT_LOCATE = "ACCOUNT_LOCATE";
		public static final String ACCOUNT_CREATE = "ACCOUNT_CREATE";
		public static final String ACCOUNT_SAVE = "ACCOUNT_SAVE";
		public static final String ACCOUNT_REMOVE = "ACCOUNT_REMOVE";
		public static final String ACCOUNT_LIST_LOAD_USERS = "ACCOUNT_LIST_LOAD_USERS";
		public static final String ACCOUNT_LIST_LOAD_USERS_IDS = "ACCOUNT_LIST_LOAD_USERS_IDS";
		public static final String ACCOUNT_LIST_LOAD_USERS_FILTERED = "ACCOUNT_LIST_LOAD_USERS_FILTERED";
		public static final String ACCOUNT_LIST_SEARCH_USERS = "ACCOUNT_LIST_SEARCH_USERS";
		public static final String ACCOUNT_LIST_SEARCH_USERS_COUNT = "ACCOUNT_LIST_SEARCH_USERS_COUNT";
		public static final String ACCOUNT_LINK_TO_NODE = "ACCOUNT_LINK_TO_NODE";
		public static final String ACCOUNT_UNLINK_FROM_NODE = "ACCOUNT_UNLINK_FROM_NODE";
		public static final String ACCOUNT_LOAD_LINKED_USER = "ACCOUNT_LOAD_LINKED_USER";
		public static final String ACCOUNT_LOAD_LINKED_NODES = "ACCOUNT_LOAD_LINKED_NODES";
		public static final String ACCOUNT_LOAD_IDS_FROM_CODES = "ACCOUNT_LOAD_IDS_FROM_CODES";
		public static final String ACCOUNT_LOAD_LINKED_DASHBOARDS = "ACCOUNT_LOAD_LINKED_DASHBOARDS";
		public static final String ACCOUNT_LINK_TO_DASHBOARD = "ACCOUNT_LINK_TO_DASHBOARD";
		public static final String ACCOUNT_UNLINK_FROM_DASHBOARD = "ACCOUNT_UNLINK_FROM_DASHBOARD";

		public static final String MASTER_EXISTS = "MASTER_EXISTS";
		public static final String MASTER_LOAD = "MASTER_LOAD";
		public static final String MASTER_CREATE = "MASTER_CREATE";
		public static final String MASTER_REMOVE = "MASTER_REMOVE";
		public static final String MASTER_LIST_LOAD = "MASTER_LIST_LOAD";
		public static final String MASTER_LIST_LOAD_COUNT = "MASTER_LIST_LOAD_COUNT";

		public static final String ROLE_LIST_LOAD = "ROLE_LIST_LOAD";
		public static final String ROLE_LIST_LOAD_COUNT = "ROLE_LIST_LOAD_COUNT";
		public static final String ROLE_LIST_LOAD_SUBQUERY_NON_EXPIRED = "ROLE_LIST_LOAD_SUBQUERY_NON_EXPIRED";
		public static final String ROLE_LIST_LOAD_SUBQUERY_NATURE_INTERNAL = "ROLE_LIST_LOAD_SUBQUERY_NATURE_INTERNAL";
		public static final String ROLE_LIST_LOAD_SUBQUERY_NATURE_EXTERNAL = "ROLE_LIST_LOAD_SUBQUERY_NATURE_EXTERNAL";
		public static final String ROLE_LIST_LOAD_SUBQUERY_CONDITION = "ROLE_LIST_LOAD_SUBQUERY_CONDITION";
		public static final String ROLE_LIST_LOAD_FOR_ACCOUNT = "ROLE_LIST_LOAD_FOR_ACCOUNT";
		public static final String ROLE_LIST_LOAD_FOR_TASKS = "ROLE_LIST_LOAD_FOR_TASKS";
		public static final String ROLE_LIST_LOAD_USERS_IDS = "ROLE_LIST_LOAD_USERS_IDS";
		public static final String ROLE_USER_EXISTS = "ROLE_USER_EXISTS";
		public static final String ROLE_SERVICE_EXISTS = "ROLE_SERVICE_EXISTS";
		public static final String ROLE_FEEDER_EXISTS = "ROLE_FEEDER_EXISTS";
		public static final String ROLE_LOAD = "ROLE_LOAD";
		public static final String ROLE_SAVE = "ROLE_SAVE";
		public static final String ROLE_CREATE = "ROLE_CREATE";
		public static final String ROLE_REMOVE = "ROLE_REMOVE";

		public static final String EVENT_LOG_CREATE = "EVENT_LOG_INSERT";
		public static final String EVENT_LOG_CLEAR = "EVENT_LOG_CLEAR";
		public static final String EVENT_LOG_LOAD = "EVENT_LOG_LOAD";
		public static final String EVENT_LOG_LOAD_COUNT = "EVENT_LOG_LOAD_COUNT";

		public static final String PROFILE_LOAD = "PROFILE_LOAD";

		public static final String SOURCE_EXISTS = "SOURCE_EXISTS";
		public static final String SOURCE_FEEDER_EXISTS = "SOURCE_FEEDER_EXISTS";
		public static final String SOURCE_LOAD = "SOURCE_LOAD";
		public static final String SOURCE_LOCATE = "SOURCE_LOCATE";
		public static final String SOURCE_FEEDER_LOCATE = "SOURCE_FEEDER_LOCATE";
		public static final String SOURCE_ADD = "SOURCE_ADD";
		public static final String SOURCE_SAVE = "SOURCE_SAVE";
		public static final String SOURCE_SAVE_UPDATE_DATE = "SOURCE_SAVE_UPDATE_DATE";
		public static final String SOURCE_DELETE = "SOURCE_DELETE";
		public static final String SOURCE_LIST_LOAD = "SOURCE_LIST_LOAD";
		public static final String SOURCE_LIST_LOAD_GLOSSARIES = "SOURCE_LIST_LOAD_GLOSSARIES";
		public static final String SOURCE_LIST_LOAD_COUNT = "SOURCE_LIST_LOAD_COUNT";
		public static final String SOURCE_LIST_LOAD_WITH_CODE = "SOURCE_LIST_LOAD_WITH_CODE";
		public static final String SOURCE_LIST_LOAD_WITH_CODE_AND_PARTNER = "SOURCE_LIST_LOAD_WITH_CODE_AND_PARTNER";
		public static final String SOURCE_LIST_LOAD_PARTNERS = "SOURCE_LIST_LOAD_PARTNERS";
		public static final String SOURCE_LIST_LOAD_PARTNERS_SUBQUERY_ONTOLOGIES = "SOURCE_LIST_LOAD_PARTNERS_SUBQUERY_ONTOLOGIES";

		public static final String SOURCE_STORE_CREATE = "SOURCE_STORE_CREATE";
		public static final String SOURCE_STORE_CREATE_ANCESTORS = "SOURCE_STORE_CREATE_ANCESTORS";
		public static final String SOURCE_STORE_CLEAN = "SOURCE_STORE_CLEAN";
		public static final String SOURCE_STORE_REMOVE = "SOURCE_STORE_REMOVE";
		public static final String SOURCE_STORE_REMOVE_ANCESTORS = "SOURCE_STORE_REMOVE_ANCESTORS";
		public static final String SOURCE_STORE_EXISTS_TERM = "SOURCE_STORE_EXISTS_TERM";
		public static final String SOURCE_STORE_ADD_TERM = "SOURCE_STORE_ADD_TERM";
		public static final String SOURCE_STORE_ADD_TERM_ANCESTORS = "SOURCE_STORE_ADD_TERM_ANCESTORS";
		public static final String SOURCE_STORE_CALCULATE_TERM_FLATTEN_LABEL = "SOURCE_STORE_CALCULATE_TERM_FLATTEN_LABEL";
		public static final String SOURCE_STORE_LOAD_TERM = "SOURCE_STORE_LOAD_TERM";
		public static final String SOURCE_STORE_SAVE_TERM = "SOURCE_STORE_SAVE_TERM";
		public static final String SOURCE_STORE_UPDATE_TERM_FLATTEN_LABEL = "SOURCE_STORE_UPDATE_TERM_FLATTEN_LABEL";
		public static final String SOURCE_STORE_DELETE_TERM = "SOURCE_STORE_DELETE_TERM";
		public static final String SOURCE_STORE_DELETE_TERM_ANCESTORS = "SOURCE_STORE_DELETE_TERM_ANCESTORS";
		public static final String SOURCE_STORE_PUBLISH_TERMS = "SOURCE_STORE_PUBLISH_TERMS";
		public static final String SOURCE_STORE_LOAD_DATA = "SOURCE_STORE_LOAD_DATA";
		public static final String SOURCE_STORE_LOAD_NEW_DATA = "SOURCE_STORE_LOAD_NEW_DATA";
		public static final String SOURCE_STORE_LOAD_ANCESTORS = "SOURCE_STORE_LOAD_ANCESTORS";
		public static final String SOURCE_STORE_LOAD_DATA_COUNT = "SOURCE_STORE_LOAD_DATA_COUNT";
		public static final String SOURCE_STORE_CONDITION = "SOURCE_STORE_CONDITION";
		public static final String SOURCE_STORE_FILTERS = "SOURCE_STORE_FILTERS";
		public static final String SOURCE_STORE_ENABLE = "SOURCE_STORE_ENABLE";
		public static final String SOURCE_STORE_NEW = "SOURCE_STORE_NEW";
		public static final String SOURCE_STORE_ANCESTOR_LEVEL = "SOURCE_STORE_ANCESTOR_LEVEL";
		public static final String SOURCE_STORE_ANCESTOR_LEVEL_FROM_TERM = "SOURCE_STORE_ANCESTOR_LEVEL_FROM_TERM";
		public static final String SOURCE_STORE_LEAF = "SOURCE_STORE_LEAF";
		public static final String SOURCE_STORE_TERMS_ONLY = "SOURCE_STORE_TERMS_ONLY";
		public static final String SOURCE_STORE_FROM_TERM = "SOURCE_STORE_FROM_TERM";
		public static final String SOURCE_STORE_SEARCH = "SOURCE_STORE_SEARCH";
		public static final String SOURCE_STORE_SEARCH_COUNT = "SOURCE_STORE_SEARCH_COUNT";
		public static final String SOURCE_STORE_SEARCH_CODE = "SOURCE_STORE_SEARCH_CODE";
		public static final String SOURCE_STORE_SEARCH_LABEL = "SOURCE_STORE_SEARCH_LABEL";

		public static final String HISTORY_STORE_EXISTS = "HISTORY_STORE_EXISTS";
		public static final String HISTORY_STORE_CREATE = "HISTORY_STORE_CREATE";
		public static final String HISTORY_STORE_CREATE_VIEW = "HISTORY_STORE_CREATE_VIEW";
		public static final String HISTORY_STORE_REMOVE = "HISTORY_STORE_REMOVE";
		public static final String HISTORY_STORE_LOAD_DATA = "HISTORY_STORE_LOAD_DATA";
		public static final String HISTORY_STORE_LOAD_DATA_COUNT = "HISTORY_STORE_LOAD_DATA_COUNT";
		public static final String HISTORY_STORE_EXIST_DATA = "HISTORY_STORE_EXIST_DATA";
		public static final String HISTORY_STORE_ADD_DATA = "HISTORY_STORE_ADD_DATA";
		public static final String HISTORY_STORE_UPDATE_FREQUENCY = "HISTORY_STORE_UPDATE_FREQUENCY";
		public static final String HISTORY_STORE_LOAD_DEFAULT_VALUES = "HISTORY_STORE_LOAD_DEFAULT_VALUES";
		public static final String HISTORY_STORE_LOAD_DEFAULT_VALUE = "HISTORY_STORE_LOAD_DEFAULT_VALUE";
		public static final String HISTORY_STORE_ADD_DEFAULT_VALUE = "HISTORY_STORE_ADD_DEFAULT_VALUE";
		public static final String HISTORY_STORE_UPDATE_DEFAULT_VALUE = "HISTORY_STORE_UPDATE_DEFAULT_VALUE";

		public static final String DATA_LINK_LOAD = "DATA_LINK_LOAD";
		public static final String DATA_LINK_LOAD_LOCATIONS = "DATA_LINK_LOAD_LOCATIONS";
		public static final String DATA_LINK_LOAD_LOCATIONS_COUNT = "DATA_LINK_LOAD_LOCATIONS_COUNT";
		public static final String DATA_LINK_LOAD_COUNT = "DATA_LINK_LOAD_COUNT";
		public static final String DATA_LINK_LOAD_SORTBY = "DATA_LINK_LOAD_SORTBY";
		public static final String DATA_LINK_CREATE_VIEW_PARAMETER = "DATA_LINK_CREATE_VIEW_PARAMETER";
		public static final String DATA_LINK_CREATE_VIEW_PARAMETER_WITH_EXTRA_DATA = "DATA_LINK_CREATE_VIEW_PARAMETER_WITH_EXTRA_DATA";
		public static final String DATA_LINK_CREATE_VIEW = "DATA_LINK_CREATE_VIEW";
		public static final String DATA_LINK_CREATE_VIEW_FROM_SYSTEM = "DATA_LINK_CREATE_VIEW_FROM_SYSTEM";
		public static final String DATA_LINK_CREATE_VIEW_ALL = "DATA_LINK_CREATE_VIEW_ALL";
		public static final String DATA_LINK_CREATE_VIEW_ALL_FROM_SYSTEM = "DATA_LINK_CREATE_VIEW_ALL_FROM_SYSTEM";
		public static final String DATA_LINK_REMOVE_VIEW = "DATA_LINK_REMOVE_VIEW";

		public static final String PUBLISH_ALL_SERVICES = "PUBLISH_ALL_SERVICES";
		public static final String UN_PUBLISH_ALL_SERVICES = "UN_PUBLISH_ALL_SERVICES";
		public static final String PUBLISH_SERVICE = "PUBLISH_SERVICE";
		public static final String UN_PUBLISH_SERVICE = "UN_PUBLISH_SERVICE";

		public static final String SERVICE_PROVIDER_LOAD_CODES = "SERVICE_PROVIDER_LOAD_CODES";

		public static final String REPORT_CREATE = "REPORT_CREATE";
		public static final String REPORT_LOAD = "REPORT_LOAD";
		public static final String REPORT_SAVE = "REPORT_SAVE";
		public static final String REPORT_REMOVE = "REPORT_REMOVE";
		public static final String REPORT_LIST_LOAD_ALL = "REPORT_LIST_LOAD_ALL";
		public static final String REPORT_LIST_LOAD_ITEMS = "REPORT_LIST_LOAD_ITEMS";
		public static final String REPORT_LIST_LOAD_ITEMS_COUNT = "REPORT_LIST_LOAD_ITEMS_COUNT";

		public static final String WORDS_LOAD = "WORDS_LOAD";
		public static final String WORDS_LOAD_WORD = "WORDS_LOAD_WORD";
		public static final String WORDS_CREATE = "WORDS_CREATE";

		public static final String WORDS_NODES_CREATE = "WORDS_NODES_CREATE";
		public static final String WORDS_NODES_REMOVE = "WORDS_NODES_REMOVE";

		public static final String WORDS_TASKS_CREATE = "WORDS_TASKS_CREATE";
		public static final String WORDS_TASKS_REMOVE = "WORDS_TASKS_REMOVE";

		public static final String DATA_LINK_LOAD_PARAMETER = "DATA_LINK_LOAD_PARAMETER";
		public static final String DATA_LINK_LOAD_PARAMETER_WITH_EXTRA_DATA = "DATA_LINK_LOAD_PARAMETER_WITH_EXTRA_DATA";
		public static final String DATA_LINK_LOAD_SEARCH = "DATA_LINK_LOAD_SEARCH";
		public static final String DATA_LINK_LOAD_OWNER = "DATA_LINK_LOAD_OWNER";

		public static final String EVENT_CREATE = "EVENT_CREATE";
		public static final String EVENT_LOAD = "EVENT_LOAD";
		public static final String EVENT_SAVE_FIRED = "EVENT_SAVE_FIRED";
		public static final String EVENT_REMOVE = "EVENT_REMOVE";
		public static final String EVENT_LIST_LOAD = "EVENT_LIST_LOAD";

		public static final String DATASTORE_QUEUE_CREATE = "DATASTORE_QUEUE_CREATE";
		public static final String DATASTORE_QUEUE_LOAD = "DATASTORE_QUEUE_LOAD";
		public static final String DATASTORE_QUEUE_REMOVE = "DATASTORE_QUEUE_REMOVE";

		public static final String LINK_SOURCES = "LINK_SOURCES";
		public static final String LINK_COUNT_SOURCES = "LINK_COUNT_SOURCES";
		public static final String LINK_TARGETS = "LINK_TARGETS";
		public static final String LINK_COUNT_TARGETS = "LINK_COUNT_TARGETS";
		public static final String LINK_ADD = "LINK_ADD";
		public static final String LINK_SAVE = "LINK_SAVE";
		public static final String LINK_DELETE = "LINK_DELETE";

		public static final String ATTACHMENT_ADD = "ATTACHMENT_ADD";
		public static final String ATTACHMENT_DELETE = "ATTACHMENT_DELETE";
		public static final String ATTACHMENT_LOAD_ITEMS = "ATTACHMENT_LOAD_ITEMS";
		public static final String ATTACHMENT_LOAD_ITEMS_COUNT = "ATTACHMENT_LOAD_ITEMS_COUNT";

		public static final String LOCATION_CREATE = "LOCATION_CREATE";
		public static final String LOCATION_SAVE = "LOCATION_SAVE";
		public static final String LOCATION_EXISTS = "LOCATION_EXISTS";
		public static final String LOCATION_LOAD_BY_NODE_ID = "LOCATION_LOAD_BY_NODE_ID";
		public static final String LOCATION_LOAD_REFERENCE = "LOCATION_LOAD_REFERENCE";
		public static final String LOCATION_LOAD_REFERENCE_ATTRIBUTES = "LOCATION_LOAD_REFERENCE_ATTRIBUTES";
		public static final String LOCATION_LIST_LOAD_IN_NODE_WITHIN_BOX = "LOCATION_LIST_LOAD_IN_NODE_WITHIN_BOX";
		public static final String LOCATION_LIST_LOAD_IN_NODE_WITH_LOCATION = "LOCATION_LIST_LOAD_IN_NODE_WITH_LOCATION";
		public static final String LOCATION_LIST_LOAD_IN_NODE_WITH_PARENT = "LOCATION_LIST_LOAD_IN_NODE_WITH_PARENT";
		public static final String LOCATION_LIST_LOAD_IN_NODE = "LOCATION_LIST_LOAD_IN_NODE";

		public static final String POST_LOAD = "POST_LOAD";
		public static final String POST_SAVE = "POST_SAVE";
		public static final String POST_CREATE = "POST_CREATE";
		public static final String POST_LOAD_LIST = "POST_LOAD_LIST";
		public static final String POST_LOAD_LIST_IDS = "POST_LOAD_LIST_IDS";
		public static final String POST_LOAD_COMMENTS = "POST_LOAD_COMMENTS";
		public static final String POST_LOAD_POSTS_COMMENTS = "POST_LOAD_POSTS_COMMENTS";
		public static final String POST_LOAD_POSTS_COMMENTS_SUBQUERY_IDS = "POST_LOAD_POSTS_COMMENTS_SUBQUERY_IDS";
		public static final String POST_COMMENT_CREATE = "POST_COMMENT_CREATE";
		public static final String POST_FILTER_POST_ADD = "POST_FILTER_POST_ADD";
		public static final String POST_FILTER_AUTHOR_ADD = "POST_FILTER_AUTHOR_ADD";

		public static final String NODE_EXISTS = "NODE_EXISTS";
		public static final String NODE_LOAD = "NODE_LOAD";
		public static final String NODE_LOAD_DATA = "NODE_LOAD_DATA";
		public static final String NODE_LOAD_FLAGS = "NODE_LOAD_FLAGS";
		public static final String NODE_LOAD_NOTES = "NODE_LOAD_NOTES";
		public static final String NODE_LOAD_PARENTS_LABELS = "NODE_LOAD_PARENTS_LABELS";
		public static final String NODE_LOAD_PARTNER_CONTEXT = "NODE_LOAD_PARTNER_CONTEXT";
		public static final String NODE_LOAD_PROTOTYPES_INFO = "NODE_LOAD_PROTOTYPES_INFO";
		public static final String NODE_LOAD_DESCENDANTS = "NODE_LOAD_DESCENDANTS";
		public static final String NODE_LOAD_CHILDREN = "NODE_LOAD_CHILDREN";
		public static final String NODE_SAVE = "NODE_SAVE";
		public static final String NODE_SAVE_PARENT = "NODE_SAVE_PARENT";
		public static final String NODE_SAVE_DATA = "NODE_SAVE_DATA";
		public static final String NODE_SAVE_FLAGS = "NODE_SAVE_FLAGS";
		public static final String NODE_SAVE_NOTES = "NODE_SAVE_NOTES";
		public static final String NODE_SAVE_PROTOTYPE = "NODE_SAVE_PROTOTYPE";
		public static final String NODE_SAVE_PARTNER_CONTEXT = "NODE_SAVE_PARTNER_CONTEXT";
		public static final String NODE_CREATE = "NODE_CREATE";
		public static final String NODE_TRASH_SUBQUERY = "NODE_TRASH_SUBQUERY";
		public static final String NODE_MOVE_TO_TRASH = "NODE_MOVE_TO_TRASH";
		public static final String NODE_RECOVER_FROM_TRASH = "NODE_RECOVER_FROM_TRASH";
		public static final String NODE_REMOVE_LOAD_ANCESTORS = "NODE_REMOVE_LOAD_ANCESTORS";
		public static final String NODE_REMOVE = "NODE_REMOVE";
		public static final String NODE_ANCESTORS_CLEAN = "NODE_ANCESTORS_CLEAN";
		public static final String NODE_ANCESTORS_INSERT_FROM_PARENT = "NODE_ANCESTORS_INSERT_FROM_PARENT";
		public static final String NODE_ANCESTORS_INSERT = "NODE_ANCESTORS_INSERT";
		public static final String NODE_MAKE_PUBLIC = "NODE_MAKE_PUBLIC";
		public static final String NODE_MAKE_PRIVATE = "NODE_MAKE_PRIVATE";
		public static final String NODE_MAKE_PROTOTYPE = "NODE_MAKE_PROTOTYPE";
		public static final String NODE_SUPER_DATA_LOAD = "NODE_SUPER_DATA_LOAD";
		public static final String NODE_SUPER_DATA_LOAD_FOR_NODES = "NODE_SUPER_DATA_LOAD_FOR_NODES";
		public static final String NODE_SUPER_DATA_ADD = "NODE_SUPER_DATA_ADD";
		public static final String NODE_SUPER_DATA_ADD_REVISION = "NODE_SUPER_DATA_ADD_REVISION";
		public static final String NODE_SUPER_DATA_SAVE = "NODE_SUPER_DATA_SAVE";
		public static final String NODE_SUPER_DATA_DELETE = "NODE_SUPER_DATA_DELETE";
		public static final String NODE_REVISION_LOAD = "NODE_REVISION_LOAD";
		public static final String NODE_REVISION_CREATE = "NODE_REVISION_CREATE";
		public static final String NODE_REVISION_LIST_LOAD_NODE_IDS = "NODE_REVISION_LIST_LOAD_NODE_IDS";
		public static final String NODE_REVISION_LIST_LOAD_ITEMS = "NODE_REVISION_LIST_LOAD_ITEMS";
		public static final String NODE_REVISION_LIST_LOAD_ITEMS_COUNT = "NODE_REVISION_LIST_LOAD_ITEMS_COUNT";
		public static final String NODE_LIST_LOCATE = "NODE_LIST_LOCATE";
		public static final String NODE_LIST_SEARCH_ITEMS = "NODE_LIST_SEARCH_ITEMS";
		public static final String NODE_LIST_SEARCH_ITEMS_COUNT = "NODE_LIST_SEARCH_ITEMS_COUNT";
		public static final String NODE_LIST_SEARCH_PART_1 = "NODE_LIST_SEARCH_PART_1";
		public static final String NODE_LIST_SEARCH_PART_1_IN_NODES_DESCRIPTORS = "NODE_LIST_SEARCH_PART_1_IN_NODES_DESCRIPTORS";
		public static final String NODE_LIST_SEARCH_PART_1_IN_TITLE = "NODE_LIST_SEARCH_PART_1_IN_TITLE";
		public static final String NODE_LIST_SEARCH_DATE_FILTER = "NODE_LIST_SEARCH_DATE_FILTER";
		public static final String NODE_LIST_SEARCH_ANCESTORS = "NODE_LIST_SEARCH_ANCESTORS";
		public static final String NODE_LIST_SEARCH_CONDITION = "NODE_LIST_SEARCH_CONDITION";
		public static final String NODE_LIST_LOAD = "NODE_LIST_LOAD";
		public static final String NODE_LIST_LOAD_IDS = "NODE_LIST_LOAD_IDS";
		public static final String NODE_LIST_LOAD_IDS_WITH_CODES = "NODE_LIST_LOAD_IDS_WITH_CODES";
		public static final String NODE_LIST_LOAD_ITEMS_CODENODES = "NODE_LIST_LOAD_ITEMS_CODENODES";
		public static final String NODE_LIST_LOAD_ITEMS_SORTBY = "NODE_LIST_LOAD_ITEMS_SORTBY";
		public static final String NODE_LIST_LOAD_ITEMS_GROUPBY = "NODE_LIST_LOAD_ITEMS_GROUPBY";
		public static final String NODE_LIST_LOAD_ITEMS_GROUPBY_OPTION = "NODE_LIST_LOAD_ITEMS_GROUPBY_OPTION";
		public static final String NODE_LIST_LOAD_ITEMS_GROUPBY_DATE_GT_OPTION = "NODE_LIST_LOAD_ITEMS_GROUPBY_DATE_GT_OPTION";
		public static final String NODE_LIST_LOAD_ITEMS_GROUPBY_DATE_LT_OPTION = "NODE_LIST_LOAD_ITEMS_GROUPBY_DATE_LT_OPTION";
		public static final String NODE_LIST_LOAD_ITEMS_GROUPBY_OPTION_WITH_NULL = "NODE_LIST_LOAD_ITEMS_GROUPBY_OPTION_WITH_NULL";
		public static final String NODE_LIST_LOAD_ITEMS_PARAMETERS = "NODE_LIST_LOAD_ITEMS_PARAMETERS";
		public static final String NODE_LIST_LOAD_ITEMS_PARAMETER = "NODE_LIST_LOAD_ITEMS_PARAMETER";
		public static final String NODE_LIST_LOAD_ITEMS_PARAMETER_WITH_EXTRA_DATA = "NODE_LIST_LOAD_ITEMS_PARAMETER_WITH_EXTRA_DATA";
		public static final String NODE_LIST_LOAD_ITEMS = "NODE_LIST_LOAD_ITEMS";
		public static final String NODE_LIST_LOAD_ITEMS_VIEW = "NODE_LIST_LOAD_ITEMS_VIEW";
		public static final String NODE_LIST_LOAD_ITEMS_VIEW_EXISTS = "NODE_LIST_LOAD_ITEMS_VIEW_EXISTS";
		public static final String NODE_LIST_LOAD_ITEMS_VIEW_DELETE = "NODE_LIST_LOAD_ITEMS_VIEW_DELETE";
		public static final String NODE_LIST_LOAD_ITEMS_LOCATIONS = "NODE_LIST_LOAD_ITEMS_LOCATIONS";
		public static final String NODE_LIST_LOAD_ITEMS_LOCATIONS_COUNT = "NODE_LIST_LOAD_ITEMS_LOCATIONS_COUNT";
		public static final String NODE_LIST_LOAD_ITEMS_COUNT = "NODE_LIST_LOAD_ITEMS_COUNT";
		public static final String NODE_LIST_LOAD_ITEMS_PARENT_SUBQUERY = "NODE_LIST_LOAD_ITEMS_PARENT_SUBQUERY";
		public static final String NODE_LIST_LOAD_ITEMS_VIEW_PARENT_SUBQUERY = "NODE_LIST_LOAD_ITEMS_VIEW_PARENT_SUBQUERY";
		public static final String NODE_LIST_LOAD_SET_LINKS_IN_ITEMS = "NODE_LIST_LOAD_SET_LINKS_IN_ITEMS";
		public static final String NODE_LIST_LOAD_SET_LINKS_IN_ITEMS_COUNT = "NODE_LIST_LOAD_SET_LINKS_IN_ITEMS_COUNT";
		public static final String NODE_LIST_LOAD_SET_LINKS_OUT_ITEMS = "NODE_LIST_LOAD_SET_LINKS_OUT_ITEMS";
		public static final String NODE_LIST_LOAD_SET_LINKS_OUT_ITEMS_COUNT = "NODE_LIST_LOAD_SET_LINKS_OUT_ITEMS_COUNT";
		public static final String NODE_LIST_LOAD_SET_OWNED_PROTOTYPES_ITEMS = "NODE_LIST_LOAD_SET_OWNED_PROTOTYPES_ITEMS";
		public static final String NODE_LIST_LOAD_SET_OWNED_PROTOTYPES_ITEMS_COUNT = "NODE_LIST_LOAD_SET_OWNED_PROTOTYPES_ITEMS_COUNT";
		public static final String NODE_LIST_LOAD_SET_SHARED_PROTOTYPES_ITEMS = "NODE_LIST_LOAD_SET_SHARED_PROTOTYPES_ITEMS";
		public static final String NODE_LIST_LOAD_SET_SHARED_PROTOTYPES_ITEMS_COUNT = "NODE_LIST_LOAD_SET_SHARED_PROTOTYPES_ITEMS_COUNT";
		public static final String NODE_LIST_LOAD_REFERENCE_SUBQUERY = "NODE_LIST_LOAD_REFERENCE_SUBQUERY";
		public static final String NODE_LIST_LOAD_REFERENCE_SUBQUERY_FOR_LOCATIONS = "NODE_LIST_LOAD_REFERENCE_SUBQUERY_FOR_LOCATIONS";
		public static final String NODE_LIST_LOAD_REFERENCE_SUBQUERY_CONDITION_FOR_LOCATIONS = "NODE_LIST_LOAD_REFERENCE_SUBQUERY_CONDITION_FOR_LOCATIONS";
		public static final String NODE_LIST_LOAD_DESCRIPTOR_ATTRIBUTES = "NODE_LIST_LOAD_DESCRIPTOR_ATTRIBUTES";
		public static final String NODE_LIST_LOAD_REFERENCE_ATTRIBUTES = "NODE_LIST_LOAD_REFERENCE_ATTRIBUTES";
		public static final String NODE_LIST_LOAD_FROM_SYSTEM = "NODE_LIST_LOAD_FROM_SYSTEM";
		public static final String NODE_LIST_LOAD_ALL_FROM_SYSTEM = "NODE_LIST_LOAD_ALL_FROM_SYSTEM";
		public static final String NODE_LIST_LOAD_FROM_TRASH = "NODE_LIST_LOAD_FROM_TRASH";
		public static final String NODE_LIST_LOAD_FROM_TRASH_COUNT = "NODE_LIST_LOAD_FROM_TRASH_COUNT";
		public static final String NODE_LIST_LOAD_FROM_TRASH_OFPARENT = "NODE_LIST_LOAD_FROM_TRASH_OFPARENT";
		public static final String NODE_PERMISSION_LIST_LOAD = "NODE_PERMISSION_LIST_LOAD";
		public static final String NODE_PERMISSION_LIST_LOAD_SUBQUERY = "NODE_PERMISSION_LIST_LOAD_SUBQUERY";
		public static final String NODE_PERMISSION_LIST_ADD = "NODE_PERMISSION_LIST_ADD";
		public static final String NODE_PERMISSION_LIST_DELETE = "NODE_PERMISSION_LIST_DELETE";
		public static final String NODE_SCHEMA_LOAD = "NODE_SCHEMA_LOAD";
		public static final String NODE_SCHEMA_ADD = "NODE_SCHEMA_ADD";
		public static final String NODE_SCHEMA_DELETE = "NODE_SCHEMA_DELETE";
		public static final String CHECK_NODE_SCHEMA_COUNT = "CHECK_NODE_SCHEMA_COUNT";
		public static final String NODE_REFERENCE_EXISTS = "NODE_REFERENCE_EXISTS";
		public static final String NODE_REFERENCE_LOAD = "NODE_REFERENCE_LOAD";
		public static final String NODE_REFERENCE_LOAD_FROM_SYSTEM = "NODE_REFERENCE_LOAD_FROM_SYSTEM";
		public static final String NODE_REFERENCE_SAVE = "NODE_REFERENCE_SAVE";
		public static final String NODE_REFERENCE_UPDATE_PARENT = "NODE_REFERENCE_UPDATE_PARENT";
		public static final String NODE_REFERENCE_DELETE = "NODE_REFERENCE_DELETE";
		public static final String NODE_REFERENCE_MAKE_PUBLIC = "NODE_REFERENCE_MAKE_PUBLIC";
		public static final String NODE_REFERENCE_MAKE_PRIVATE = "NODE_REFERENCE_MAKE_PRIVATE";
		public static final String NODE_REFERENCE_SAVE_UPDATE_DATE = "NODE_REFERENCE_SAVE_UPDATE_DATE";

		public static final String NOTIFICATION_EXISTS = "NOTIFICATION_EXISTS";
		public static final String NOTIFICATION_CREATE = "NOTIFICATION_CREATE";
		public static final String NOTIFICATION_PRIORIZE = "NOTIFICATION_PRIORIZE";
		public static final String NOTIFICATION_MARK_READ = "NOTIFICATION_MARK_READ";
		public static final String NOTIFICATION_MARK_ALL_READ = "NOTIFICATION_MARK_ALL_READ";
		public static final String NOTIFICATION_LIST_LOAD = "NOTIFICATION_LIST_LOAD";
		public static final String NOTIFICATION_LIST_LOAD_WITH_PUBLICATION = "NOTIFICATION_LIST_LOAD_WITH_PUBLICATION";
		public static final String NOTIFICATION_LIST_LOAD_COUNT = "NOTIFICATION_LIST_LOAD_COUNT";

		public static final String REFERENCE_TABLE_EXISTS = "REFERENCE_TABLE_EXISTS";
		public static final String REFERENCE_TABLE_CREATE = "REFERENCE_TABLE_CREATE";
		public static final String REFERENCE_TABLE_CREATE_TRIGGER = "REFERENCE_TABLE_CREATE_TRIGGER";
		public static final String REFERENCE_TABLE_CREATE_TRIGGER_COLUMN = "REFERENCE_TABLE_CREATE_TRIGGER_COLUMN";
		public static final String REFERENCE_TABLE_CREATE_INDEX = "REFERENCE_TABLE_CREATE_INDEX";
		public static final String REFERENCE_TABLE_CREATE_TEXT_INDEX = "REFERENCE_TABLE_CREATE_TEXT_INDEX";
		public static final String REFERENCE_TABLE_CREATE_FULLTEXT_INDEX = "REFERENCE_TABLE_CREATE_FULLTEXT_INDEX";
		public static final String REFERENCE_TABLE_DELETE = "REFERENCE_TABLE_DELETE";
		public static final String REFERENCE_TABLE_DELETE_INDEX = "REFERENCE_TABLE_DELETE_INDEX";
		public static final String REFERENCE_TABLE_REFRESH_PREPARE = "REFERENCE_TABLE_REFRESH_PREPARE";
		public static final String REFERENCE_TABLE_REFRESH_MIGRATE = "REFERENCE_TABLE_REFRESH_MIGRATE";
		public static final String REFERENCE_EXISTS = "REFERENCE_EXISTS";
		public static final String REFERENCE_ADD = "REFERENCE_ADD";
		public static final String REFERENCE_LOAD_ATTRIBUTE_VALUES = "REFERENCE_LOAD_ATTRIBUTE_VALUES";
		public static final String REFERENCE_LOAD_ATTRIBUTE_VALUES_FOR_OWNER = "REFERENCE_LOAD_ATTRIBUTE_VALUES_FOR_OWNER";
		public static final String REFERENCE_LOAD_ATTRIBUTE_VALUES_CODES = "REFERENCE_LOAD_ATTRIBUTE_VALUES_CODES";
		public static final String REFERENCE_LOAD_ATTRIBUTE_VALUES_CODES_FOR_OWNER = "REFERENCE_LOAD_ATTRIBUTE_VALUES_CODES_FOR_OWNER";
		public static final String REFERENCE_LOAD_ATTRIBUTE_VALUES_COUNT = "REFERENCE_LOAD_ATTRIBUTE_VALUES_COUNT";
		public static final String REFERENCE_LOAD_ATTRIBUTE_VALUES_COUNT_FOR_OWNER = "REFERENCE_LOAD_ATTRIBUTE_VALUES_COUNT_FOR_OWNER";
		public static final String REFERENCE_LIST_LOAD = "REFERENCE_LIST_LOAD";
		public static final String REFERENCE_LIST_LOAD_COUNT = "REFERENCE_LIST_LOAD_COUNT";
		public static final String REFERENCE_LIST_LOAD_NODE_ID = "REFERENCE_LIST_LOAD_NODE_ID";

		public static final String REFERENCE_AS_SOURCE = "REFERENCE_AS_SOURCE";
		public static final String REFERENCE_AS_SOURCE_COUNT = "REFERENCE_AS_SOURCE_COUNT";
		public static final String REFERENCE_AS_SOURCE_SEARCH = "REFERENCE_AS_SOURCE_SEARCH";
		public static final String REFERENCE_AS_SOURCE_SEARCH_COUNT = "REFERENCE_AS_SOURCE_SEARCH_COUNT";
		public static final String REFERENCE_AS_SOURCE_ANCESTORS = "REFERENCE_AS_SOURCE_ANCESTORS";
		public static final String REFERENCE_AS_SOURCE_CONDITION = "REFERENCE_AS_SOURCE_CONDITION";
		public static final String REFERENCE_AS_SOURCE_FILTERS = "REFERENCE_AS_SOURCE_FILTERS";
		public static final String REFERENCE_AS_SOURCE_ANCESTOR_LEVEL = "REFERENCE_AS_SOURCE_ANCESTOR_LEVEL";
		public static final String REFERENCE_AS_SOURCE_ANCESTOR_LEVEL_FROM_TERM = "REFERENCE_AS_SOURCE_ANCESTOR_LEVEL_FROM_TERM";
		public static final String REFERENCE_AS_SOURCE_FROM_TERM = "REFERENCE_AS_SOURCE_FROM_TERM";
		public static final String REFERENCE_AS_SOURCE_TERMS_ONLY = "REFERENCE_AS_SOURCE_TERMS_ONLY";

		public static final String SUBSCRIPTION_SAVE = "SUBSCRIPTION_SAVE";
		public static final String SUBSCRIPTION_LOAD = "SUBSCRIPTION_LOAD";

		public static final String SEQUENCE_EXISTS = "SEQUENCE_EXISTS";
		public static final String SEQUENCE_CREATE = "SEQUENCE_CREATE";
		public static final String SEQUENCE_RESET_VALUE = "SEQUENCE_RESET_VALUE";
		public static final String SEQUENCE_CREATE_VALUE = "SEQUENCE_CREATE_VALUE";
		public static final String SEQUENCE_SELECT_LAST_VALUE = "SEQUENCE_SELECT_LAST_VALUE";

		public static final String SERVICE_LOAD = "SERVICE_LOAD";
		public static final String SERVICE_LOAD_FOR_TASK = "SERVICE_LOAD_FOR_TASK";
		public static final String SERVICE_LOAD_BY_REQUEST_ID = "SERVICE_LOAD_BY_REQUEST_ID";
		public static final String SERVICE_CREATE = "SERVICE_CREATE";
		public static final String SERVICE_SAVE = "SERVICE_SAVE";
		public static final String SERVICE_REMOVE = "SERVICE_REMOVE";

		public static final String CUBE_LOAD = "CUBE_LOAD";
		public static final String CUBE_LOCATE = "CUBE_LOCATE";
		public static final String CUBE_CREATE = "CUBE_CREATE";
		public static final String CUBE_REMOVE = "CUBE_REMOVE";
		public static final String CUBE_REPORT_LOAD = "CUBE_REPORT_LOAD";
		public static final String CUBE_REPORT_SAVE = "CUBE_REPORT_SAVE";
		public static final String CUBE_REPORT_CREATE = "CUBE_REPORT_CREATE";
		public static final String CUBE_REPORT_REMOVE = "CUBE_REPORT_REMOVE";
		public static final String CUBE_REPORT_LIST_LOAD_ITEMS = "CUBE_REPORT_LIST_LOAD_ITEMS";
		public static final String CUBE_REPORT_LIST_LOAD_ITEMS_COUNT = "CUBE_REPORT_LIST_LOAD_ITEMS_COUNT";
		public static final String CUBE_FILTERGROUP_CREATE = "CUBE_FILTERGROUP_CREATE";
		public static final String CUBE_FILTERGROUP_LOAD = "CUBE_FILTERGROUP_LOAD";
		public static final String CUBE_FILTERGROUP_SAVE = "CUBE_FILTERGROUP_SAVE";
		public static final String CUBE_FILTERGROUP_REMOVE = "CUBE_FILTERGROUP_REMOVE";
		public static final String CUBE_FILTERGROUP_LIST_LOAD = "CUBE_FILTERGROUP_LIST_LOAD";

		public static final String TASK_EXISTS = "TASK_EXISTS";
		public static final String TASK_LOAD = "TASK_LOAD";
		public static final String TASK_LOAD_DATA = "TASK_LOAD_DATA";
		public static final String TASK_LOAD_SHORTCUTS = "TASK_LOAD_SHORTCUTS";
		public static final String TASK_CREATE = "TASK_CREATE";
		public static final String TASK_SAVE = "TASK_SAVE";
		public static final String TASK_SAVE_GEOMETRY = "TASK_SAVE_GEOMETRY";
		public static final String TASK_SAVE_URGENCY = "TASK_SAVE_URGENCY";
		public static final String TASK_SAVE_STATE = "TASK_SAVE_STATE";
		public static final String TASK_SAVE_OWNER = "TASK_SAVE_OWNER";
		public static final String TASK_SAVE_END_DATE = "TASK_SAVE_END_DATE";
		public static final String TASK_SAVE_ABORT_DATE = "TASK_SAVE_ABORT_DATE";
		public static final String TASK_SAVE_PROCESS = "TASK_SAVE_PROCESS";
		public static final String TASK_UPDATE_NEW_MESSAGES = "TASK_UPDATE_NEW_MESSAGES";
		public static final String TASK_REMOVE = "TASK_REMOVE";
		public static final String TASK_FIND_WITH_CLASSIFICATOR = "TASK_FIND_WITH_CLASSIFICATOR";
		public static final String TASK_FIND = "TASK_FIND";
		public static final String TASK_FIND_CURRENT_INITIALIZER = "TASK_FIND_CURRENT_INITIALIZER";
		public static final String TASK_LOCKS_LOAD = "TASK_LOCKS_LOAD";
		public static final String TASK_LIST_LOAD_TYPES = "TASK_LIST_LOAD_TYPES";
		public static final String TASK_LIST_LOAD_ACTIVE_TYPES = "TASK_LIST_LOAD_ACTIVE_TYPES";
		public static final String TASK_LIST_LOAD_STATES = "TASK_LIST_LOAD_STATES";
		public static final String TASK_LIST_LOAD_ROLES = "TASK_LIST_LOAD_ROLES";
		public static final String TASK_LIST_LOAD_OWNERS = "TASK_LIST_LOAD_OWNERS";
		public static final String TASK_LIST_LOAD_SENDERS = "TASK_LIST_LOAD_SENDERS";
		public static final String TASK_LIST_LOAD_SENDERS_OF_OWNER = "TASK_LIST_LOAD_SENDERS_OF_OWNER";
		public static final String TASK_LIST_LOAD_SUBQUERY_TASK = "TASK_LIST_LOAD_SUBQUERY_TASK";
		public static final String TASK_LIST_LOAD_SUBQUERY_TYPE = "TASK_LIST_LOAD_SUBQUERY_TYPE";
		public static final String TASK_LIST_LOAD_SUBQUERY_ROLE = "TASK_LIST_LOAD_SUBQUERY_ROLE";
		public static final String TASK_LIST_LOAD_SUBQUERY_URGENT = "TASK_LIST_LOAD_SUBQUERY_URGENT";
		public static final String TASK_LIST_LOAD_SUBQUERY_OWNER = "TASK_LIST_LOAD_SUBQUERY_OWNER";
		public static final String TASK_LIST_LOAD_SUBQUERY_OWNER_NULL = "TASK_LIST_LOAD_SUBQUERY_OWNER_NULL";
		public static final String TASK_LIST_LOAD_SUBQUERY_SENDER = "TASK_LIST_LOAD_SUBQUERY_SENDER";
		public static final String TASK_LIST_LOAD_SUBQUERY_BACKGROUND = "TASK_LIST_LOAD_SUBQUERY_BACKGROUND";
		public static final String TASK_LIST_LOAD_SUBQUERY_SITUATION_ALIVE = "TASK_LIST_LOAD_SUBQUERY_SITUATION_ALIVE";
		public static final String TASK_LIST_LOAD_SUBQUERY_SITUATION_ACTIVE = "TASK_LIST_LOAD_SUBQUERY_SITUATION_ACTIVE";
		public static final String TASK_LIST_LOAD_SUBQUERY_SITUATION_PENDING = "TASK_LIST_LOAD_SUBQUERY_SITUATION_PENDING";
		public static final String TASK_LIST_LOAD_SUBQUERY_SITUATION_FINISHED = "TASK_LIST_LOAD_SUBQUERY_SITUATION_FINISHED";
		public static final String TASK_LIST_LOAD_SUBQUERY_DESCRIPTION = "TASK_LIST_LOAD_SUBQUERY_DESCRIPTION";
		public static final String TASK_LIST_LOAD = "TASK_LIST_LOAD";
		public static final String TASK_LIST_LOAD_COUNT = "TASK_LIST_LOAD_COUNT";
		public static final String TASK_LIST_LOAD_NODE_ALL = "TASK_LIST_LOAD_NODE_ALL";
		public static final String TASK_LIST_LOAD_NODE_ACTIVE = "TASK_LIST_LOAD_NODE_ACTIVE";
		public static final String TASK_LIST_LOAD_LINKED_WITH_NODE_COUNT = "TASK_LIST_LOAD_LINKED_WITH_NODE_COUNT";
		public static final String TASK_LIST_LOAD_LINKED_WITH_NODE = "TASK_LIST_LOAD_LINKED_WITH_NODE";
		public static final String TASK_LIST_DELETE = "TASK_LIST_DELETE";
		public static final String TASK_DEFINITION_CLEAN = "TASK_DEFINITION_CLEAN";
		public static final String TASK_DEFINITION_INSERT = "TASK_DEFINITION_INSERT";

		public static final String TASK_JOBS_LOAD_NOT_READ = "TASK_JOBS_LOAD_NOT_READ";
		public static final String TASK_JOBS_LOAD_AVAILABLE_NOT_READ = "TASK_JOBS_LOAD_AVAILABLE_NOT_READ";
		public static final String TASK_JOBS_LOAD_ASSIGNED_TO_DELETE = "TASK_JOBS_LOAD_ASSIGNED_TO_DELETE";
		public static final String TASK_JOBS_LOAD_FINISHED_TO_DELETE = "TASK_JOBS_LOAD_FINISHED_TO_DELETE";
		public static final String TASK_JOBS_LOAD_UNASSIGNED_TO_DELETE = "TASK_JOBS_LOAD_UNASSIGNED_TO_DELETE";
		public static final String TASK_JOBS_LOAD_AVAILABLE = "TASK_JOBS_LOAD_AVAILABLE";
		public static final String TASK_JOBS_LOAD_REQUEST = "TASK_JOBS_LOAD_REQUEST";
		public static final String TASK_JOBS_LOAD_RESPONSE = "TASK_JOBS_LOAD_RESPONSE";
		public static final String TASK_JOB_CREATE = "TASK_JOB_CREATE";
		public static final String TASK_JOB_REMOVE = "TASK_JOB_REMOVE";
		public static final String TASK_JOB_SAVE_RESPONSE = "TASK_JOB_SAVE_RESPONSE";
		public static final String TASK_JOB_ADD_ATTACHMENT = "TASK_JOB_ADD_ATTACHMENT";
		public static final String TASK_JOB_LOAD_ATTACHMENTS = "TASK_JOB_LOAD_ATTACHMENTS";
		public static final String TASK_JOBS_LOAD_CALLBACK_TASK_ID = "TASK_JOBS_LOAD_CALLBACK_TASK_ID";
		public static final String TASK_JOB_REFRESH_TIME_MARK = "TASK_JOB_REFRESH_TIME_MARK";
		public static final String TASK_JOB_ASSIGN = "TASK_JOB_ASSIGN";
		public static final String TASK_JOB_UNASSIGN = "TASK_JOB_UNASSIGN";
		public static final String TASK_JOB_LOAD_ORDER_ID = "TASK_JOB_LOAD_ORDER_ID";
		public static final String TASK_JOB_LOAD_NEW_CHAT_LIST_ITEM = "TASK_JOB_LOAD_NEW_CHAT_LIST_ITEM";
		public static final String TASK_JOB_LOAD_USER_ID_FROM_ORDER = "TASK_JOB_LOAD_USER_ID_FROM_ORDER";

		public static final String TASK_TIMER_LOAD = "TASK_TIMER_LOAD";
		public static final String TASK_TIMER_CREATE = "TASK_TIMER_CREATE";
		public static final String TASK_TIMER_DELETE = "TASK_TIMER_DELETE";

		public static final String TASK_PROCESS_SNAPSHOT_LOAD_LAST = "TASK_PROCESS_SNAPSHOT_LOAD_LAST";
		public static final String TASK_PROCESS_SNAPSHOT_CREATE = "TASK_PROCESS_SNAPSHOT_CREATE";
		public static final String TASK_PROCESS_SNAPSHOT_DELETE = "TASK_PROCESS_SNAPSHOT_DELETE";
		public static final String TASK_PROCESS_SNAPSHOT_COUNT = "TASK_PROCESS_SNAPSHOT_COUNT";

		public static final String TASK_ORDER_LOAD = "TASK_ORDER_LOAD";
		public static final String TASK_ORDER_LOAD_BY_CODE = "TASK_ORDER_LOAD_BY_CODE";
		public static final String TASK_ORDER_CREATE = "TASK_ORDER_CREATE";
		public static final String TASK_ORDER_SAVE = "TASK_ORDER_SAVE";
		public static final String TASK_ORDER_CLOSE_ALL_OF_TASK = "TASK_ORDER_CLOSE_ALL_OF_TASK";
		public static final String TASK_ORDER_RESET_NEW_MESSAGES = "TASK_ORDER_RESET_NEW_MESSAGES";
		public static final String TASK_ORDER_INCREMENT_NEW_MESSAGES = "TASK_ORDER_INCREMENT_NEW_MESSAGES";
		public static final String TASK_ORDER_CHAT_LOAD_ENTRIES = "TASK_ORDER_CHAT_LOAD_ENTRIES";
		public static final String TASK_ORDER_CHAT_LOAD_ENTRIES_COUNT = "TASK_ORDER_CHAT_LOAD_ENTRIES_COUNT";
		public static final String TASK_ORDER_CHAT_ADD_ENTRY = "TASK_ORDER_CHAT_ADD_ENTRY";
		public static final String TASK_ORDER_CHAT_UPDATE_ENTRY_STATE_TO_SENT = "TASK_ORDER_CHAT_UPDATE_ENTRY_STATE_TO_SENT";
		public static final String TASK_ORDER_LIST_LOAD_ITEMS = "TASK_ORDER_LIST_LOAD_ITEMS";
		public static final String TASK_ORDER_LIST_LOAD_ITEMS_COUNT = "TASK_ORDER_LIST_LOAD_ITEMS_COUNT";
		public static final String TASK_ORDER_LIST_LOAD_ROLES = "TASK_ORDER_LIST_LOAD_ROLES";

		public static final String MAILBOX_EXISTS = "MAILBOX_EXISTS";
		public static final String MAILBOX_LOAD = "MAILBOX_LOAD";
		public static final String MAILBOX_CREATE = "MAILBOX_CREATE";
		public static final String MAILBOX_REMOVE = "MAILBOX_REMOVE";
		public static final String MAILBOX_REMOVE_WITH_TASK_ID = "MAILBOX_REMOVE_WITH_TASK_ID";

		public static final String MAILBOX_PERMISSION_LOAD = "MAILBOX_PERMISSION_LOAD";
		public static final String MAILBOX_PERMISSION_CREATE = "MAILBOX_PERMISSION_CREATE";
		public static final String MAILBOX_PERMISSION_REMOVE = "MAILBOX_PERMISSION_REMOVE";

		public static final String MESSAGEQUEUE_LIST_LOAD_PENDING = "MESSAGEQUEUE_LIST_LOAD_PENDING";
		public static final String MESSAGEQUEUE_LOAD_CONTENT = "MESSAGEQUEUE_LOAD_CONTENT";
		public static final String MESSAGEQUEUE_INSERT = "MESSAGEQUEUE_INSERT";
		public static final String MESSAGEQUEUE_REMOVE = "MESSAGEQUEUE_REMOVE";
		public static final String MESSAGEQUEUE_UPDATE = "MESSAGEQUEUE_UPDATE";

		public static final String LOGBOOKNODE_CREATE_ENTRY = "LOGBOOKNODE_CREATE_ENTRY";
		public static final String LOGBOOKNODE_SEARCH = "LOGBOOKNODE_SEARCH";
		public static final String LOGBOOKNODE_SUBSCRIBER_LIST_LOAD = "LOGBOOKNODE_SUBSCRIBER_LIST_LOAD";
		public static final String LOGBOOKNODE_SUBSCRIBER_LIST_ADD = "LOGBOOKNODE_SUBSCRIBER_LIST_ADD";
		public static final String LOGBOOKNODE_SUBSCRIBER_LIST_DELETE = "LOGBOOKNODE_SUBSCRIBER_LIST_DELETE";

		public static final String TASK_FACTS_CREATE_ENTRY = "TASK_FACTS_CREATE_ENTRY";
		public static final String TASK_FACTS_LOAD = "TASK_FACTS_LOAD";
		public static final String TASK_FACTS_LOAD_ENTRIES = "TASK_FACTS_LOAD_ENTRIES";
		public static final String TASK_FACTS_LOAD_ENTRIES_COUNT = "TASK_FACTS_LOAD_ENTRIES_COUNT";

		public static final String WORKQUEUE_LIST_LOAD_PENDING = "WORKQUEUE_LIST_LOAD_PENDING";
		public static final String WORKQUEUE_ADD = "WORKQUEUE_ADD";
		public static final String WORKQUEUE_REMOVE = "WORKQUEUE_REMOVE";
		public static final String WORKQUEUE_LOAD_TARGET = "WORKQUEUE_LOAD_TARGET";
		public static final String WORKQUEUE_UPDATE_WITH_ERROR = "WORKQUEUE_UPDATE_WITH_ERROR";
		public static final String WORKQUEUE_UPDATE_FINISHED = "WORKQUEUE_UPDATE_FINISHED";
	}

	public abstract class QueryProperties {
		public static final String TABLE_NODES_DESCRIPTORS = "TABLE_NODES_DESCRIPTORS";
		public static final String TABLE_REFERENCES_PREFIX = "TABLE_REFERENCES_PREFIX";
	}

	public abstract class QueryFields {
		public static final String CONDITION = "condition";
		public static final String CONDITION_SUBQUERY = "conditionsubquery";
		public static final String CONDITION_DATA = "conditiondata";
		public static final String CONDITION_DESCRIPTOR = "conditiondescriptor";
		public static final String OWNER = "owner";
		public static final String NON_EXPIRED = "nonexpired";
		public static final String FILTERS = "filters";
		public static final String USERNAME = "username";
		public static final String ID = "id";
		public static final String IDS = "ids";
		public static final String ID_PARENT = "idparent";
		public static final String ID_USER = "iduser";
		public static final String ID_PUBLICATION = "idpublication";
		public static final String ID_NODE = "idnode";
		public static final String DATABASE_NAME = "database";
		public static final String ID_DASHBOARD = "iddashboard";
		public static final String ID_NODES = "idnodes";
		public static final String ID_SUPER_DATA = "idsuperdata";
		public static final String ID_OWNER = "idowner";
		public static final String ID_ATTACHMENT = "idattachment";
		public static final String OWNER_FULLNAME = "ownerfullname";
		public static final String ID_SENDER = "idsender";
		public static final String SENDER_FULLNAME = "senderfullname";
		public static final String ID_PROTOTYPE = "idprototype";
		public static final String ID_TASK = "idtask";
		public static final String ID_CUBE = "idcube";
		public static final String ID_REQUEST = "idrequest";
		public static final String ID_POST = "idpost";
		public static final String ID_ORDER = "idorder";
		public static final String ID_ROLE = "idrole";
		public static final String ID_SETUP_NODE = "idsetupnode";
		public static final String CODE = "code";
		public static final String CODE_PARENT = "codeparent";
		public static final String CODE_ATTRIBUTE = "codeattribute";
		public static final String ORDERING = "ordering";
		public static final String FULLNAME = "fullname";
		public static final String PHOTO = "photo";
		public static final String PREFERENCES = "preferences";
		public static final String EMAIL = "email";
		public static final String LANGUAGE = "language";
		public static final String NAME = "name";
		public static final String OPERATOR = "operator";
		public static final String FIRED = "fired";
		public static final String LABEL = "label";
		public static final String FLATTEN_LABEL = "flattenlabel";
		public static final String TAGS = "tags";
		public static final String TITLE = "title";
		public static final String SUBTITLE = "subtitle";
		public static final String DESCRIPTION = "description";
		public static final String SHORTCUTS = "shortcuts";
		public static final String DATE = "date";
		public static final String DATE_FILTER = "datefilter";
		public static final String ANCESTORS = "ancestors";
		public static final String BEGIN_DATE = "begindate";
		public static final String EXPIRE_DATE = "expiredate";
		public static final String CREATE_DATE = "createdate";
		public static final String FINISH_DATE = "finishdate";
		public static final String DELETE_DATE = "deletedate";
		public static final String UPDATE_DATE = "updatedate";
		public static final String START_DATE = "startdate";
		public static final String SUGGESTED_START_DATE = "suggestedstartdate";
		public static final String SUGGESTED_END_DATE = "suggestedenddate";
		public static final String END_DATE = "enddate";
		public static final String FROM_DATE = "fromdate";
		public static final String TO_DATE = "todate";
		public static final String REGISTER_DATE = "registerdate";
		public static final String FIELDS = "fields";
		public static final String DATA = "data";
		public static final String DATA_PREFIX = "dataprefix";
		public static final String DUE_DATE = "duedate";
		public static final String TYPE = "type";
		public static final String STATE = "state";
		public static final String COMMENTS = "comments";
		public static final String URGENT = "urgent";
		public static final String CLOSED = "closed";
		public static final String FIELD = "field";
		public static final String FIELD_KEY = "fieldkey";
		public static final String FIELD_LABEL = "fieldlabel";
		public static final String FIELD_TAG = "fieldtag";
		public static final String MODE = "mode";
		public static final String SENDER = "sender";
		public static final String DEPTH = "depth";
		public static final String PART_1 = "part1";
		public static final String SOURCE = "source";
		public static final String SERVER_NAME = "servername";
		public static final String SERVER_HOST = "serverhost";
		public static final String SERVER_PORT = "serverport";
		public static final String HOST = "host";
		public static final String LAYER = "layer";
		public static final String VISITED = "visited";
		public static final String CREATED = "created";
		public static final String DELETED = "deleted";
		public static final String MODIFIED = "modified";
		public static final String NODES = "nodes";
		public static final String TASK = "task";
		public static final String START_POS = "startpos";
		public static final String LIMIT = "limit";
		public static final String ROLE = "role";
		public static final String ROLES = "roles";
		public static final String FREQUENCY = "frequency";
		public static final String PROPERTY = "property";
		public static final String CODE_NODE = "codenode";
		public static final String CODE_TARGET = "codetarget";
		public static final String OPERATION = "operation";
		public static final String BODY_TARGET = "bodytarget";
		public static final String ID_AUTHOR = "idauthor";
		public static final String AUTHOR = "author";
		public static final String BODY = "body";
		public static final String TARGET_CODE = "targetcode";
		public static final String TAG = "tag";
		public static final String ID_SOURCE = "idsource";
		public static final String ID_SOURCE_COMPONENT = "idsourcecomponent";
		public static final String ID_TARGET = "idtarget";
		public static final String TYPE_SOURCE = "typesource";
		public static final String TYPE_TARGET = "typetarget";
		public static final String TASK_SUBQUERY = "tasksubquery";
		public static final String TYPE_SUBQUERY = "typesubquery";
		public static final String STATE_SUBQUERY = "statesubquery";
		public static final String SITUATION_SUBQUERY = "situationsubquery";
		public static final String ROLE_SUBQUERY = "rolesubquery";
		public static final String URGENT_SUBQUERY = "urgentsubquery";
		public static final String OWNER_SUBQUERY = "ownersubquery";
		public static final String SENDER_SUBQUERY = "sendersubquery";
		public static final String BACKGROUND_SUBQUERY = "backgroundsubquery";
		public static final String ATTRIBUTES = "attributes";
		public static final String ATTRIBUTE = "attribute";
		public static final String VALUES = "values";
		public static final String INDEX = "index";
		public static final String REFERENCE_TABLE = "referencetable";
		public static final String TABLE_NAME = "tablename";
		public static final String CODE_NODES = "codenodes";
		public static final String NODES_DESCRIPTORS_TABLE = "nodesdescriptorstable";
		public static final String CODE_NODES_SUBQUERY = "codenodessubquery";
		public static final String REFERENCE = "reference";
		public static final String PARAMETERS = "parameters";
		public static final String CODE_SUBSEQUENCE = "codesubsequence";
		public static final String VALUE = "value";
		public static final String LOCATION = "location";
		public static final String TARGET = "target";
		public static final String READ = "read";
		public static final String FLAGS = "flags";
		public static final String NOTES = "notes";
		public static final String USERS = "users";
		public static final String PARTNER_ID = "partnerid";
		public static final String PARTNER_SERVICE_NAME = "partnerservicename";
		public static final String MERGED = "merged";
		public static final String REVISION_DATE = "revisiondate";
		public static final String TEMPLATE = "template";
		public static final String LOGGER = "logger";
		public static final String PRIORITY = "priority";
		public static final String MESSAGE = "message";
		public static final String URI = "uri";
		public static final String STACKTRACE = "stacktrace";
		public static final String CREATION_TIME = "creationtime";
		public static final String VALID = "valid";
		public static final String WHERE = "where";
		public static final String ORDERBY = "orderby";
		public static final String ICON = "icon";
		public static final String SORTS_BY = "sortsby";
		public static final String GROUPS_BY = "groupsby";
		public static final String STORE = "store";
		public static final String EXTRA_DATA = "extradata";
		public static final String ENABLE = "enable";
		public static final String IS_ENABLE = "isenable";
		public static final String IS_NEW = "isnew";
		public static final String SCHEMA = "schema";
		public static final String LAST_ERROR = "lasterror";
		public static final String IS_VALID = "isvalid";
		public static final String ID_LOCATION = "idlocation";
		public static final String GEOMETRY = "geometry";
		public static final String GEOMETRY_LABEL = "geometrylabel";
		public static final String BOUNDING_BOX = "boundingbox";
		public static final String IS_LEAF = "isleaf";
		public static final String ANCESTOR_LEVEL = "ancestorlevel";
		public static final String FROM = "from";
		public static final String POSTS_IDS = "postsids";
		public static final String POSTS_IDS_SUBQUERY = "postsidssubquery";
		public static final String TEXT = "text";
		public static final String TERMS_ONLY = "termsonly";
		public static final String REMOTE_UNIT_LABEL = "remoteunitlabel";
		public static final String WALL_USER_ID = "walluserid";
		public static final String PARENT = "parent";
		public static final String DEFINITION_TYPE = "definitiontype";
		public static final String PARTNER_CONTEXT = "partnercontext";
		public static final String REFERENCE_ATTRIBUTES = "referenceattributes";
		public static final String REFERENCE_SUBQUERY = "referencesubquery";
		public static final String REFERENCE_SUBQUERY_CONDITION = "referencesubquerycondition";
		public static final String COLUMNS = "columns";
		public static final String TERMS = "terms";
		public static final String CODES = "codes";
		public static final String DELAY = "delay";
		public static final String LOCAL_MAILBOX = "localmailbox";
		public static final String REPLY_MAILBOX = "replymailbox";
		public static final String CLASSIFICATOR = "classificator";
		public static final String INITIALIZER = "initializer";
		public static final String BACKGROUND = "background";
		public static final String NEW_MESSAGES = "newmessages";
		public static final String MAILBOX = "mailbox";
		public static final String HASH = "hash";
		public static final String RETRIES = "retries";
		public static final String LAST_UPDATE_TIME = "lastupdatetime";
		public static final String CERTIFICATE_AUTHORITY = "certificateauthority";
		public static final String COLONIZER = "colonizer";
		public static final String CACHE = "cache";
		public static final String REQUEST = "request";
		public static final String RESPONSE = "response";
		public static final String CALLBACK_TASK_ID = "callbacktaskid";
		public static final String CALLBACK_CODE = "callbackcode";
		public static final String CALLBACK_ORDER_ID = "callbackorderid";
		public static final String SYNC_MARK = "syncmark";
		public static final String ENABLED = "enabled";
		public static final String URL = "url";
		public static final String PARTNER_NAME = "partnername";
		public static final String PARTNER_LABEL = "partnerlabel";
		public static final String NATURE = "nature";
		public static final String JOBS = "jobs";
		public static final String ONTOLOGIES = "ontologies";
		public static final String ONTOLOGIES_SUBQUERY = "ontologiessubquery";
		public static final String ONTOLOGY = "ontology";
		public static final String OR = "or";
		public static final String AVAILABLE_DATE = "availabledate";
		public static final String OPTIONS = "options";
		public static final String DATASTORE = "datastore";
	}

}
