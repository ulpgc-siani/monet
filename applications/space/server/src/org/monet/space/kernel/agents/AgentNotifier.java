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

package org.monet.space.kernel.agents;

import org.monet.space.kernel.listeners.IListenerMonetEvent;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.MonetEvent;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class AgentNotifier {

	private static AgentNotifier instance;
	Map<String, IListenerMonetEvent> listeners;
	Context context;
	Set<Long> disabledThreads;

	protected AgentNotifier() {
		listeners = new ConcurrentHashMap<>();
		context = Context.getInstance();
		disabledThreads = new HashSet<>();
	}

	public synchronized static AgentNotifier getInstance() {
		if (instance == null)
			instance = new AgentNotifier();
		return instance;
	}

	public Boolean register(String code, Class<?> listenerClass) {
        try {
            listeners.put(code, (IListenerMonetEvent) listenerClass.getConstructor().newInstance());
		} catch (Exception ex) {
			AgentLogger.getInstance().error(ex);
			return false;
		}
		return true;
	}

	public IListenerMonetEvent get(String code) {
		return this.listeners.get(code);
	}

	public Boolean register(String code, IListenerMonetEvent listener) {
		this.listeners.put(code, listener);
		return true;
	}

	public void enable(Long idThread) {
		this.disabledThreads.remove(idThread);
	}

	public void disable(Long idThread) {
		this.disabledThreads.add(idThread);
	}

	public Boolean notify(MonetEvent event) {
		String code = event.getCode();

		if (this.disabledThreads.contains(Thread.currentThread().getId())) return true;

		try {
			Iterator<IListenerMonetEvent> iterator = listeners.values().iterator();

			while (iterator.hasNext()) {
				IListenerMonetEvent listener = iterator.next();

				if (code.equals(MonetEvent.NODE_VISITED)) listener.nodeVisited(event);
				else if (code.equals(MonetEvent.BEFORE_MODIFY_NODE)) listener.beforeModifyNode(event);
				else if (code.equals(MonetEvent.NODE_CREATED)) listener.nodeCreated(event);
				else if (code.equals(MonetEvent.NODE_MODIFIED)) listener.nodeModified(event);
				else if (code.equals(MonetEvent.NODE_SAVED)) listener.nodeSaved(event);
				else if (code.equals(MonetEvent.NODE_REFRESH_STATE)) listener.nodeCalculateState(event);
				else if (code.equals(MonetEvent.NODE_REMOVED)) listener.nodeRemoved(event);
				else if (code.equals(MonetEvent.NODE_EXECUTE_COMMAND)) listener.nodeExecuteCommand(event);
				else if (code.equals(MonetEvent.NODE_EXECUTE_COMMAND_CONFIRMATION_WHEN)) listener.nodeExecuteCommandConfirmationWhen(event);
				else if (code.equals(MonetEvent.NODE_EXECUTE_COMMAND_CONFIRMATION_ON_CANCEL)) listener.nodeExecuteCommandConfirmationOnCancel(event);
				else if (code.equals(MonetEvent.NODE_FIELD_CHANGED)) listener.nodeFieldChanged(event);
				else if (code.equals(MonetEvent.NODE_MOVED_TO_TRASH)) listener.nodeMovedToTrash(event);
				else if (code.equals(MonetEvent.NODE_RECOVERED_FROM_TRASH)) listener.nodeRecoveredFromTrash(event);
				else if (code.equals(MonetEvent.NODE_ADDED_TO_COLLECTION)) listener.nodeAddedToCollection(event);
				else if (code.equals(MonetEvent.NODE_REMOVED_FROM_COLLECTION)) listener.nodeRemovedFromCollection(event);
				else if (code.equals(MonetEvent.NODE_SIGNED)) listener.nodeSigned(event);
				else if (code.equals(MonetEvent.NODE_SIGNS_COMPLETED)) listener.nodeSignsCompleted(event);
				else if (code.equals(MonetEvent.NODE_IMPORT)) listener.nodeImport(event);
				else if (code.equals(MonetEvent.NODE_REFRESH_MAPPING)) listener.nodeRefreshMapping(event);
				else if (code.equals(MonetEvent.NODE_REFRESH_PROPERTIES)) listener.nodeRefreshProperties(event);
				else if (code.equals(MonetEvent.TASK_CREATED)) listener.taskCreated(event);
				else if (code.equals(MonetEvent.TASK_REMOVED)) listener.taskRemoved(event);
				else if (code.equals(MonetEvent.TASK_MOVED_TO_TRASH)) listener.taskMovedToTrash(event);
				else if (code.equals(MonetEvent.TASK_RECOVERED_FROM_TRASH)) listener.taskRecoveredFromTrash(event);
				else if (code.equals(MonetEvent.TASK_INITIALIZED)) listener.taskInitialized(event);
				else if (code.equals(MonetEvent.TASK_TERMINATED)) listener.taskTerminated(event);
				else if (code.equals(MonetEvent.TASK_ABORTED)) listener.taskAborted(event);
				else if (code.equals(MonetEvent.TASK_STATE_UPDATED)) listener.taskStateUpdated(event);
				else if (code.equals(MonetEvent.TASK_ASSIGNED)) listener.taskAssigned(event);
				else if (code.equals(MonetEvent.TASK_UNASSIGNED)) listener.taskUnAssigned(event);
				else if (code.equals(MonetEvent.TASK_PLACE_ARRIVAL)) listener.taskPlaceArrival(event);
				else if (code.equals(MonetEvent.TASK_FINISHING)) listener.taskFinishing(event);
				else if (code.equals(MonetEvent.TASK_FINISHED)) listener.taskFinished(event);
				else if (code.equals(MonetEvent.TASK_PLACE_TIMEOUT)) listener.taskPlaceTimeout(event);
				else if (code.equals(MonetEvent.TASK_PLACE_TOOK)) listener.taskPlaceTook(event);
				else if (code.equals(MonetEvent.TASK_ACTION_SOLVED)) listener.taskActionSolved(event);
				else if (code.equals(MonetEvent.TASK_SETUP_DELEGATION)) listener.taskSetupDelegation(event);
				else if (code.equals(MonetEvent.TASK_SETUP_WAIT)) listener.taskSetupWait(event);
				else if (code.equals(MonetEvent.TASK_SETUP_TIMEOUT)) listener.taskSetupTimeout(event);
				else if (code.equals(MonetEvent.TASK_SETUP_DELEGATION_COMPLETE))
					listener.taskSetupDelegationComplete(event);
				else if (code.equals(MonetEvent.TASK_SELECT_DELEGATION_ROLE)) listener.taskSelectDelegationRole(event);
				else if (code.equals(MonetEvent.TASK_SETUP_JOB)) listener.taskSetupJob(event);
				else if (code.equals(MonetEvent.TASK_SETUP_JOB_COMPLETE)) listener.taskSetupJobComplete(event);
				else if (code.equals(MonetEvent.TASK_SETUP_EDITION)) listener.taskSetupEdition(event);
				else if (code.equals(MonetEvent.TASK_SELECT_JOB_ROLE)) listener.taskSelectJobRole(event);
				else if (code.equals(MonetEvent.TASK_SELECT_JOB_ROLE_COMPLETE))
					listener.taskSelectJobRoleComplete(event);
				else if (code.equals(MonetEvent.TASK_VALIDATE_FORM)) listener.taskValidateForm(event);
				else if (code.equals(MonetEvent.TASK_CUSTOMER_INITIALIZED)) listener.taskCustomerInitialized(event);
				else if (code.equals(MonetEvent.TASK_CUSTOMER_EXPIRED)) listener.taskCustomerExpired(event);
				else if (code.equals(MonetEvent.TASK_CUSTOMER_ABORTED)) listener.taskCustomerAborted(event);
				else if (code.equals(MonetEvent.TASK_CUSTOMER_REQUEST_IMPORT))
					listener.taskCustomerRequestImport(event);
				else if (code.equals(MonetEvent.TASK_CUSTOMER_RESPONSE_CONSTRUCTOR))
					listener.taskCustomerResponseConstructor(event);
				else if (code.equals(MonetEvent.TASK_CONTESTANT_INITIALIZED)) listener.taskContestantInitialized(event);
				else if (code.equals(MonetEvent.TASK_CONTESTANT_REQUEST_IMPORT))
					listener.taskContestantRequestImport(event);
				else if (code.equals(MonetEvent.TASK_CONTESTANT_RESPONSE_CONSTRUCTOR))
					listener.taskContestantResponseConstructor(event);
				else if (code.equals(MonetEvent.TASK_CONTEST_INITIALIZED)) listener.taskContestInitialized(event);
				else if (code.equals(MonetEvent.TASK_CONTEST_TERMINATED)) listener.taskContestTerminated(event);
				else if (code.equals(MonetEvent.TASK_CONTEST_REQUEST_CONSTRUCTOR))
					listener.taskContestRequestConstructor(event);
				else if (code.equals(MonetEvent.TASK_CONTEST_RESPONSE_IMPORT))
					listener.taskContestResponseImport(event);
				else if (code.equals(MonetEvent.TASK_TRANSFERENCE_CALCULATE_CLASSIFICATOR))
					listener.taskTransferenceCalculateClassificator(event);
				else if (code.equals(MonetEvent.TASK_PROVIDER_INITIALIZED)) listener.taskProviderInitialized(event);
				else if (code.equals(MonetEvent.TASK_PROVIDER_TERMINATED)) listener.taskProviderTerminated(event);
				else if (code.equals(MonetEvent.TASK_PROVIDER_REJECTED)) listener.taskProviderRejected(event);
				else if (code.equals(MonetEvent.TASK_PROVIDER_EXPIRED)) listener.taskProviderExpired(event);
				else if (code.equals(MonetEvent.TASK_PROVIDER_REQUEST_CONSTRUCTOR))
					listener.taskProviderRequestConstructor(event);
				else if (code.equals(MonetEvent.TASK_PROVIDER_RESPONSE_IMPORT))
					listener.taskProviderResponseImport(event);
				else if (code.equals(MonetEvent.TASK_ORDER_CHAT_MESSAGE_RECEIVED))
					listener.taskOrderChatMessageReceived(event);
				else if (code.equals(MonetEvent.TASK_ORDER_CHAT_MESSAGE_SENT)) listener.taskOrderChatMessageSent(event);
				else if (code.equals(MonetEvent.TASK_ORDER_CREATED)) listener.taskOrderCreated(event);
				else if (code.equals(MonetEvent.TASK_ORDER_REQUEST_SUCCESS)) listener.taskOrderRequestSuccess(event);
				else if (code.equals(MonetEvent.TASK_ORDER_REQUEST_FAILURE)) listener.taskOrderRequestFailure(event);
				else if (code.equals(MonetEvent.TASK_CREATE_JOB_MESSAGE)) listener.taskCreateJobMessage(event);
				else if (code.equals(MonetEvent.TASK_CREATED_JOB)) listener.taskCreatedJob(event);
				else if (code.equals(MonetEvent.TASK_FINISHED_JOB_MESSAGE)) listener.taskFinishedJobMessage(event);
				else if (code.equals(MonetEvent.TASK_SENSOR_FINISHED)) listener.taskSensorFinished(event);
				else if (code.equals(MonetEvent.TASK_IS_BACK_ENABLE)) listener.taskIsBackEnable(event);
				else if (code.equals(MonetEvent.TASK_BACK)) listener.taskBack(event);
				else if (code.equals(MonetEvent.TASK_NEW_MESSAGES_RECEIVED)) listener.taskNewMessagesReceived(event);
				else if (code.equals(MonetEvent.TASK_NEW_MESSAGES_SENT)) listener.taskNewMessagesSent(event);
				else if (code.equals(MonetEvent.TASK_JOB_NEW)) listener.taskJobCreated(event);
				else if (code.equals(MonetEvent.TASK_JOB_UNASSIGN)) listener.taskJobUnassigned(event);
				else if (code.equals(MonetEvent.NOTIFICATION_CREATED)) listener.notificationCreated(event);
				else if (code.equals(MonetEvent.NOTIFICATION_PRIORIZED)) listener.notificationPriorized(event);
				else if (code.equals(MonetEvent.SERVICE_STARTED)) listener.serviceStarted(event);
				else if (code.equals(MonetEvent.SERVICE_FINISHED)) listener.serviceFinished(event);
				else if (code.equals(MonetEvent.POST_CREATED)) listener.postCreated(event);
				else if (code.equals(MonetEvent.POST_COMMENT_CREATED)) listener.postCommentCreated(event);
				else if (code.equals(MonetEvent.MODEL_UPDATED)) listener.modelUpdated(event);
				else if (code.equals(MonetEvent.SOURCE_POPULATED)) listener.sourcePopulated(event);
				else if (code.equals(MonetEvent.SOURCE_SYNCHRONIZED)) listener.sourceSynchronized(event);
				else if (code.equals(MonetEvent.SOURCE_TERM_ADDED)) listener.sourceTermAdded(event);
				else if (code.equals(MonetEvent.SOURCE_TERM_MODIFIED)) listener.sourceTermModified(event);
				else if (code.equals(MonetEvent.KERNEL_RESET)) listener.kernelReset(event);
				else if (code.equals(MonetEvent.DATASTORE_MOUNTED)) listener.datastoreMounted(event);
				else if (code.equals(MonetEvent.SESSION_CREATED)) listener.sessionCreated(event);
				else if (code.equals(MonetEvent.SESSION_DESTROYED)) listener.sessionDestroyed(event);
				else if (code.equals(MonetEvent.ACCOUNT_MODIFIED)) listener.accountModified(event);
				else if (code.equals(MonetEvent.MODEL_EVENT_FIRED)) listener.modelEventFired(event);
			}

		}
		catch (Exception exception) {
			AgentLogger.getInstance().error(exception);
			throw new RuntimeException(exception);
		}

		return true;
	}
}