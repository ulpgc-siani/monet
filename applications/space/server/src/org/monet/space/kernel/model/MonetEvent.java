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

package org.monet.space.kernel.model;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MonetEvent extends BaseObject {
	private String property;
	private Object sender;
	private Map<String, Object> parameters;

	public static final String ACCOUNT_MODIFIED = "accountmodified";

	public static final String SERVICE_CREATED = "servicecreated";
	public static final String SERVICE_STARTED = "servicestarted";
	public static final String SERVICE_FINISHED = "servicefinished";

	public static final String NODE_VISITED = "nodevisited";
	public static final String NODE_CREATED = "nodecreated";
	public static final String NODE_EXECUTE_COMMAND = "nodexecutecommand";
	public static final String NODE_EXECUTE_COMMAND_CONFIRMATION_WHEN = "nodexecutecommandwhen";
	public static final String NODE_EXECUTE_COMMAND_CONFIRMATION_ON_CANCEL = "nodexecutecommandoncancel";
	public static final String NODE_FIELD_CHANGED = "nodefieldchanged";
	public static final String BEFORE_MODIFY_NODE = "beforemodifynode";
	public static final String NODE_MODIFIED = "nodemodified";
	public static final String NODE_SAVED = "nodesaved";
	public static final String NODE_REFRESH_STATE = "noderefreshstate";
	public static final String NODE_REMOVED = "noderemoved";
	public static final String NODE_MOVED_TO_TRASH = "nodemovedtotrash";
	public static final String NODE_RECOVERED_FROM_TRASH = "noderecoveredfromtrash";
	public static final String NODE_ADDED_TO_COLLECTION = "nodeaddedtocollection";
	public static final String NODE_REMOVED_FROM_COLLECTION = "noderemovedfromcollection";
	public static final String NODE_IMPORT = "nodeimport";
	public static final String NODE_REFRESH_MAPPING = "noderefreshmapping";
	public static final String NODE_REFRESH_PROPERTIES = "noderefreshproperties";
	public static final String NODE_SIGNED = "nodesignature";
	public static final String NODE_SIGNS_COMPLETED = "nodesignscompleted";

	public static final String TASK_CREATED = "taskcreated";
	public static final String TASK_REMOVED = "taskremoved";
	public static final String TASK_MOVED_TO_TRASH = "taskmovedtotrash";
	public static final String TASK_RECOVERED_FROM_TRASH = "taskrecoveredfromtrash";
	public static final String TASK_STATE_UPDATED = "taskupdatestate";

	public static final String TASK_ORDER_CREATED = "taskordercreated";
	public static final String TASK_ORDER_REQUEST_SUCCESS = "taskorderrequestsuccess";
	public static final String TASK_ORDER_REQUEST_FAILURE = "taskorderrequestfailure";
	public static final String TASK_ORDER_CHAT_MESSAGE_SENT = "taskorderchatmessagesent";

	public static final String TASK_INITIALIZED = "taskinitialized";
	public static final String TASK_TERMINATED = "taskterminated";
	public static final String TASK_ABORTED = "taskaborted";
	public static final String TASK_ASSIGNED = "taskassigned";
	public static final String TASK_UNASSIGNED = "taskunassigned";
	public static final String TASK_FINISHING = "taskfinishing";
	public static final String TASK_FINISHED = "taskfinished";
	public static final String TASK_PLACE_ARRIVAL = "taskplacearrival";
	public static final String TASK_PLACE_TIMEOUT = "taskplacetimeout";
	public static final String TASK_PLACE_TOOK = "taskplacetook";
	public static final String TASK_ACTION_SOLVED = "taskactionsolved";
	public static final String TASK_SELECT_DELEGATION_ROLE = "taskselectdelegationrole";
	public static final String TASK_SETUP_DELEGATION = "tasksetupdelegation";
	public static final String TASK_SETUP_WAIT = "tasksetupwait";
	public static final String TASK_SETUP_TIMEOUT = "tasksetuptimeout";
	public static final String TASK_SETUP_DELEGATION_COMPLETE = "tasksetupdelegationcomplete";
	public static final String TASK_SELECT_JOB_ROLE = "taskselectjobrole";
	public static final String TASK_SELECT_JOB_ROLE_COMPLETE = "taskselectjobrolecomplete";
	public static final String TASK_SETUP_JOB = "tasksetupjob";
	public static final String TASK_SETUP_JOB_COMPLETE = "tasksetupjobcomplete";
	public static final String TASK_SETUP_EDITION = "tasksetupedition";
	public static final String TASK_VALIDATE_FORM = "taskvalidateform";
	public static final String TASK_CUSTOMER_INITIALIZED = "taskcustomerinitialized";
	public static final String TASK_CUSTOMER_EXPIRED = "taskcustomerexpired";
	public static final String TASK_CUSTOMER_ABORTED = "taskcustomeraborted";
	public static final String TASK_CUSTOMER_REQUEST_IMPORT = "taskcustomerrequestimport";
	public static final String TASK_CUSTOMER_RESPONSE_CONSTRUCTOR = "taskcustomerresponseconstructor";
	public static final String TASK_CONTESTANT_INITIALIZED = "taskcontestantinitialized";
	public static final String TASK_CONTESTANT_RESPONSE_CONSTRUCTOR = "taskcontestantresponseconstructor";
	public static final String TASK_CONTESTANT_REQUEST_IMPORT = "taskcontestantrequestimport";
	public static final String TASK_CONTEST_INITIALIZED = "taskcontestinitialized";
	public static final String TASK_CONTEST_TERMINATED = "taskcontestterminated";
	public static final String TASK_CONTEST_REQUEST_CONSTRUCTOR = "taskcontestrequestconstructor";
	public static final String TASK_CONTEST_RESPONSE_IMPORT = "taskcontestresponseimport";
	public static final String TASK_TRANSFERENCE_CALCULATE_CLASSIFICATOR = "tasktransferencecalculateclassificator";
	public static final String TASK_PROVIDER_INITIALIZED = "taskproviderinitialized";
	public static final String TASK_PROVIDER_TERMINATED = "taskproviderterminated";
	public static final String TASK_PROVIDER_REJECTED = "taskproviderrejected";
	public static final String TASK_PROVIDER_EXPIRED = "taskproviderexpired";
	public static final String TASK_PROVIDER_REQUEST_CONSTRUCTOR = "taskproviderrequestconstructor";
	public static final String TASK_PROVIDER_RESPONSE_IMPORT = "taskproviderresponseimport";
	public static final String TASK_ORDER_CHAT_MESSAGE_RECEIVED = "taskproviderchatmessagereceived";
	public static final String TASK_CREATE_JOB_MESSAGE = "taskcreatejobmessage";
	public static final String TASK_CREATED_JOB = "taskcreatedjob";
	public static final String TASK_FINISHED_JOB_MESSAGE = "taskfinishedjobmessage";
	public static final String TASK_SENSOR_FINISHED = "tasksensorfinished";
	public static final String TASK_IS_BACK_ENABLE = "taskisbackenable";
	public static final String TASK_BACK = "taskback";
	public static final String TASK_NEW_MESSAGES_RECEIVED = "tasknewmessagesreceived";
	public static final String TASK_NEW_MESSAGES_SENT = "tasknewmessagessent";
	public static final String TASK_JOB_NEW = "taskjobnew";
	public static final String TASK_JOB_UNASSIGN = "taskjobunassign";

	public static final String NOTIFICATION_CREATED = "notificationcreated";
	public static final String NOTIFICATION_PRIORIZED = "notificationpriorized";

	public static final String POST_CREATED = "postcreated";
	public static final String POST_COMMENT_CREATED = "postcommentcreated";

	public static final String MODEL_UPDATED = "modelupdated";
	public static final String SOURCE_POPULATED = "sourcepopulated";
	public static final String SOURCE_SYNCHRONIZED = "sourcesynchronized";
	public static final String SOURCE_TERM_ADDED = "sourcetermadded";
	public static final String SOURCE_TERM_MODIFIED = "sourcetermmodified";
	public static final String DATASTORE_MOUNTED = "datastoreMounted";
	public static final String KERNEL_RESET = "kernelreset";
	public static final String SESSION_CREATED = "sessioncreated";
	public static final String SESSION_DESTROYED = "sessiondestroyed";

	public static final String MODEL_EVENT_FIRED = "modeleventfired";

	public static final String PARAMETER_PLACE = "place";
	public static final String PARAMETER_CONTEST = "contest";
	public static final String PARAMETER_ACTION = "action";
	public static final String PARAMETER_ROLE = "role";
	public static final String PARAMETER_ROUTE = "route";
	public static final String PARAMETER_FORM = "form";
	public static final String PARAMETER_ROLE_CHOOSER = "rolechooser";
	public static final String PARAMETER_DELEGATION_SETUP = "delegationsetup";
	public static final String PARAMETER_TIMEOUT_SETUP = "timeoutsetup";
	public static final String PARAMETER_WAIT_SETUP = "waitsetup";
	public static final String PARAMETER_JOB_SETUP = "jobsetup";
	public static final String PARAMETER_JOB = "job";
	public static final String PARAMETER_VALIDATION_RESULT = "validationresult";
	public static final String PARAMETER_REFERENCE = "reference";
	public static final String PARAMETER_COMMAND = "command";
	public static final String PARAMETER_COMMAND_CONFIRMATION_REQUIRED = "result";
	public static final String PARAMETER_FIELD = "field";
	public static final String PARAMETER_COLLECTION = "collection";
	public static final String PARAMETER_SCOPE = "scope";
	public static final String PARAMETER_STREAM = "stream";
	public static final String PARAMETER_STREAM_SIZE = "streamsize";
	public static final String PARAMETER_CALLBACK = "callback";
	public static final String PARAMETER_POST = "post";
	public static final String PARAMETER_PROVIDER = "provider";
	public static final String PARAMETER_CODE = "code";
	public static final String PARAMETER_MESSAGE = "message";
	public static final String PARAMETER_RESULT = "result";
	public static final String PARAMETER_MAILBOX = "mailbox";
	public static final String PARAMETER_USER_ID = "userid";
	public static final String PARAMETER_USER = "user";
	public static final String PARAMETER_REASON = "reason";
	public static final String PARAMETER_ORDER_ID = "orderid";
	public static final String PARAMETER_TITLE = "title";
	public static final String PARAMETER_BODY = "body";
	public static final String PARAMETER_CHAT_ITEM_ID = "chatitemid";
	public static final String PARAMETER_SUGGESTED_START_DATE = "suggestedstartdate";
	public static final String PARAMETER_SUGGESTED_END_DATE = "suggestedenddate";
	public static final String PARAMETER_OBSERVATIONS = "observations";
	public static final String PARAMETER_URGENT = "urgent";
	public static final String PARAMETER_TERM = "term";
	public static final String PARAMETER_SIGN_USER = "user";
	public static final String PARAMETER_ACCOUNT = "account";
	public static final String PARAMETER_ADD_NOTIFICATION = "addnotification";

	public MonetEvent(String code, String property, Object sender, Map<String, Object> parameters) {
		this.code = code;
		this.property = property;
		this.sender = sender;
		this.parameters = parameters;
	}

	public MonetEvent(String code, String property, Object sender) {
		this(code, property, sender, new HashMap<String, Object>());
	}

	public String getProperty() {
		return this.property;
	}

	public Object getSender() {
		return this.sender;
	}

	public <T extends Object> T getParameter(String name) {
		return (T) this.parameters.get(name);
	}

	public void addParameter(String name, Object value) {
		this.parameters.put(name, value);
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}

}