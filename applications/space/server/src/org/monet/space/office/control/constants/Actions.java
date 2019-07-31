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

package org.monet.space.office.control.constants;

public class Actions {

	public static final String SHOW_LOADING = "servletoffice.load";
	public static final String SHOW_APPLICATION = "servletoffice.app";
	public static final String REDIRECT = "redirect";
	public static final String LOGIN = "login";
	public static final String LOGOUT = "logout";
	public static final String LOAD_ACCOUNT = "loadaccount";
	public static final String LOAD_UNITS = "loadunits";
    public static final String LOAD_ACCOUNT_PENDING_TASKS = "loadaccountpendingtasks";
	public static final String LOAD_NODES = "loadnodes";
	public static final String LOAD_NODE = "loadnode";
	public static final String LOAD_NODE_REVISION = "loadnoderevision";
	public static final String LOAD_CURRENT_NODE_REVISION = "loadcurrentnoderevision";
	public static final String LOAD_NODE_TYPE = "loadnodetype";
	public static final String LOAD_NODE_FROM_DATA = "loadnodefromdata";
	public static final String LOAD_NODE_TEMPLATE = "loadnodetemplate";
	public static final String LOAD_NODE_ITEMS = "loadnodeitems";
	public static final String LOAD_NODE_LOCATION = "loadnodelocation";
	public static final String LOAD_SET_ITEMS = "loadsetitems";
	public static final String LOAD_LINK_NODE_ITEMS = "loadlinknodeitems";
	public static final String LOAD_LINK_NODE_ITEMS_LOCATIONS = "loadlinknodeitemslocations";
	public static final String LOAD_LINK_NODE_ITEMS_LOCATIONS_COUNT = "loadlinknodeitemslocationscount";
	public static final String LOAD_NODE_REFERENCE = "loadnodereference";
	public static final String LOAD_NODE_ATTRIBUTE = "loadnodeattribute";
	public static final String LOAD_ANCESTOR_CHILD_ID = "loadancestorchildid";
	public static final String LOAD_NODE_NOTES = "loadnodenotes";
	public static final String SAVE_ACCOUNT = "saveaccount";
	public static final String SAVE_NODE = "savenode";
	public static final String SAVE_NODE_ATTRIBUTE = "savenodeattribute";
	public static final String SAVE_EMBEDDED_NODE = "saveembeddednode";
	public static final String UPDATE_NODE_LOCATION = "updatenodelocation";
	public static final String CLEAN_NODE_LOCATION = "cleannodelocation";
	public static final String LOAD_NODE_DESCRIPTOR = "loadnodedescriptor";
	public static final String SAVE_NODE_DESCRIPTOR = "savenodedescriptor";
	public static final String ADD_NODE = "addnode";
	public static final String ADD_PROTOTYPE = "addprototype";
	public static final String GENERATE_REPORT = "generatereport";
	public static final String COPY_NODE = "copynode";
	public static final String DELETE_NODES = "deletenodes";
	public static final String DELETE_NODE = "deletenode";
	public static final String LOAD_ATTRIBUTES = "loadattributes";
	public static final String EXISTS_SOURCE_TERM = "existssourceterm";
	public static final String ADD_SOURCE_TERM = "addsourceterm";
	public static final String SAVE_SOURCE_TERM = "savesourceterm";
	public static final String SAVE_SOURCE_TERM_ATTRIBUTE = "savesourcetermattribute";
	public static final String DELETE_SOURCE_TERM = "deletesourceterm";
	public static final String LOAD_SOURCE_LIST = "loadsourcelist";
	public static final String LOAD_SOURCE_TERMS = "loadsourceterms";
	public static final String LOAD_HISTORY_TERMS = "loadhistoryterms";
	public static final String LOAD_TASK = "loadtask";
	public static final String LOAD_TASKS = "loadtasks";
	public static final String LOAD_NODE_HELPER_PAGE = "loadnodehelperpage";
	public static final String LOAD_HELPER_PAGE = "loadhelperpage";
	public static final String SEARCH_NODES = "searchnodes";
	public static final String LOAD_TEMPLATE = "loadtemplate";
	public static final String IMPORT_NODE = "importnode";
	public static final String EXPORT_NODE = "exportnode";
	public static final String DOWNLOAD_EXPORTED_NODE_FILE = "downloadexportednodefile";
	public static final String UPLOAD_NODE_CONTENT = "uploadnodecontent";
	public static final String SEARCH_USERS = "searchusers";
	public static final String SHARE_NODE = "sharenode";
	public static final String LOAD_NODES_FROM_TRASH = "loadnodesfromtrash";
	public static final String EMPTY_TRASH = "emptytrash";
	public static final String RECOVER_NODES_FROM_TRASH = "recovernodesfromtrash";
	public static final String RECOVER_NODE_FROM_TRASH = "recovernodefromtrash";
	public static final String ABORT_TASK = "aborttask";
	public static final String LOAD_BUSINESS_MODEL_FILE = "loadbusinessmodelfile";
	public static final String LOAD_THEME_FILE = "loadthemefile";
	public static final String LOAD_BUSINESS_MODEL_DEFINITION = "loadbusinessmodeldefinition";
	public static final String LOAD_BUSINESS_UNIT_FILE = "loadbusinessunitfile";
	public static final String SEND_MAIL = "sendmail";
	public static final String SEND_SUGGESTION = "sendsuggestion";
	public static final String DISCARD_NODE = "discardnode";
	public static final String LOAD_DEFAULT_VALUES = "loaddefaultvalues";
	public static final String LOAD_DEFAULT_VALUE = "loaddefaultvalue";
	public static final String ADD_DEFAULT_VALUE = "adddefaultvalue";
	public static final String CREATE_TASK = "createtask";
	public static final String SET_TASK_TITLE = "settasktitle";
	public static final String PREVIEW_NODE = "previewnode";
	public static final String DOWNLOAD_NODE = "downloadnode";
	public static final String PING = "ping";
	public static final String REGISTER_EXCEPTION = "registerexception";
	public static final String CREATE_SEQUENCE_VALUE = "createsequencevalue";
	public static final String SOLVE_RETRY_LOCK = "retrytasklock";
	public static final String SELECT_TASK_DELEGATION_ROLE = "selecttaskdelegationrole";
	public static final String SETUP_TASK_DELEGATION = "setuptaskdelegation";
	public static final String SELECT_TASK_SEND_JOB_ROLE = "selecttasksendjobrole";
	public static final String SETUP_TASK_SEND_JOB = "setuptasksendjob";
	public static final String SOLVE_TASK_LINE = "solvetaskline";
	public static final String SOLVE_TASK_EDITION = "solvetaskedition";
	public static final String SETUP_TASK_ENROLL = "setuptaskenroll";
	public static final String SETUP_TASK_WAIT = "setuptaskwait";
	public static final String EXECUTE_NODE_COMMAND = "executenodecommand";
	public static final String EXECUTE_NODE_COMMAND_ON_ACCEPT = "executenodecommandonaccept";
	public static final String EXECUTE_NODE_COMMAND_ON_CANCEL = "executenodecommandoncancel";
	public static final String DOWNLOAD_NODE_COMMAND_FILE = "downloadnodecommandfile";
	public static final String LOAD_TASK_FILTERS = "loadtaskfilters";
	public static final String EXITING = "exiting";
	public static final String LOAD_SYSTEM_TEMPLATE = "loadsystemtemplate";
	public static final String LOAD_DASHBOARD_TEMPLATE = "loaddashboardtemplate";
	public static final String LOAD_NOTIFICATIONS = "loadnotifications";
	public static final String NOTIFICATIONS_READ = "notificationsread";
	public static final String LOAD_TASK_HISTORY = "loadtaskhistory";
	public static final String FOCUS_NODE_VIEW = "focusnodeview";
	public static final String FOCUS_TASK_VIEW = "focustaskview";
	public static final String FOCUS_NODE_FIELD = "focusnodefield";
	public static final String BLUR_NODE_FIELD = "blurnodefield";
	public static final String CHANGE_ROLE = "changerole";
	public static final String PRINT_NODE = "printnode";
	public static final String PRINT_NODE_TIME_CONSUMPTION = "printnodetimeconsumption";
	public static final String DOWNLOAD_PRINTED_NODE = "downloadprintednode";
	public static final String PRINT_TASK_LIST = "printtasklist";
	public static final String LOAD_LOCATION_LAYER = "loadlocationlayer";
	public static final String ADD_COMMENT_TO_POST = "addcommenttopost";
	public static final String ADD_POST = "addpost";
	public static final String LOAD_NEWS_NEXT_PAGE = "loadnewsnextpage";
	public static final String ADD_FILTER = "addfilter";
	public static final String SEARCH_ROLES = "searchroles";
	public static final String NOTIFICATIONS_READ_ALL = "notificationsreadall";
	public static final String LOAD_TEAM = "loadteam";
	public static final String LOAD_USERS = "loadusers";
	public static final String LOAD_FEDERATION_USERS = "loadfederationusers";
	public static final String REPLACE_NODE_DOCUMENT = "replacenodedocument";
	public static final String LOAD_NODE_FIELD_COMPOSITE_ITEM = "loadnodefieldcompositeitem";
	public static final String LOAD_NODE_REVISION_ITEMS = "loadnoderevisionitems";
	public static final String RESTORE_NODE_REVISION = "restorenoderevision";
	public static final String LOAD_SOURCE = "loadsource";
	public static final String BLUR_NODE_VIEW = "blurnodeview";
	public static final String PREPARE_NODE_DOCUMENT_SIGNATURE = "preparenodedocumentsignature";
	public static final String DELETE_NODE_DOCUMENT_SIGNATURE = "deletenodedocumentsignature";
	public static final String STAMP_NODE_DOCUMENT_SIGNATURE = "stampnodedocumentsignature";
	public static final String LOAD_SIGNATURE_ITEMS = "loadsignatureitems";
	public static final String LOAD_ROLE_DEFINITION_LIST = "loadroledefinitionlist";
	public static final String LOAD_ROLE_LIST = "loadrolelist";
	public static final String LOAD_GROUPED_ROLE_LIST = "loadgroupedrolelist";
	public static final String ADD_USER_ROLE = "adduserrole";
	public static final String ADD_SERVICE_ROLE = "addservicerole";
	public static final String ADD_FEEDER_ROLE = "addfeederrole";
	public static final String SAVE_USER_ROLE = "saveuserrole";
	public static final String SAVE_SERVICE_ROLE = "saveservicerole";
	public static final String SAVE_FEEDER_ROLE = "savefeederrole";
	public static final String PUBLISH_SOURCE_TERMS = "publishsourceterms";
	public static final String LOAD_SOURCE_NEW_TERMS = "loadsourcenewterms";
	public static final String LOAD_ATTACHMENT_ITEMS = "loadattachmentitems";
	public static final String PREVIEW_ATTACHMENT = "previewattachment";
	public static final String TOGGLE_TASK_URGENCY = "toggletaskurgency";
	public static final String SET_TASK_OWNER = "settaskowner";
	public static final String UNSET_TASK_OWNER = "unsettaskowner";
	public static final String SET_TASKS_OWNER = "settasksowner";
	public static final String UNSET_TASKS_OWNER = "unsettasksowner";
	public static final String LOAD_TASK_ORDER_ITEMS = "loadtaskorderitems";
	public static final String LOAD_TASK_ORDER_CHAT_ITEMS = "loadtaskorderchatitems";
	public static final String SEND_TASK_ORDER_CHAT_MESSAGE = "sendtaskorderchatmessage";
	public static final String LOAD_PARTNERS = "loadpartners";
	public static final String SAVE_NODE_PARTNER_CONTEXT = "savenodepartnercontext";
	public static final String LOAD_NODE_FIELD_CHECK_OPTIONS = "loadnodefieldcheckoptions";
	public static final String RESET_TASK_ORDER_NEW_MESSAGES = "resettaskordernewmessages";
	public static final String LOAD_NODE_STATE = "loadnodestate";
	public static final String RENDER_TASK_TIMELINE = "rendertasktimeline";
	public static final String ADD_NODE_NOTE = "addnodenote";
	public static final String DELETE_NODE_NOTE = "deletenodenote";
	public static final String LOAD_NODE_CONTEXT = "loadnodecontext";
	public static final String FILL_NODE_DOCUMENT = "fillnodedocument";
	public static final String LOAD_MAIN_NODE = "loadmainnode";
	public static final String SEND_TASK_REQUEST = "sendtaskrequest";
	public static final String SEND_TASK_RESPONSE = "sendtaskresponse";
	public static final String LOAD_NODE_GROUP_BY_OPTIONS = "loadnodegroupbyoptions";
	public static final String LOAD_NODE_PRINT_ATTRIBUTES = "loadnodeprintattributes";
	public static final String LOAD_TASKLIST_PRINT_ATTRIBUTES = "loadtasklistprintattributes";
	public static final String LOAD_NODE_ITEMS_LOCATIONS = "loadnodeitemslocations";
	public static final String LOAD_NODE_ITEMS_LOCATIONS_COUNT = "loadnodeitemslocationscount";
	public static final String LOAD_TASK_COMMENTS = "loadtaskcomments";
}
