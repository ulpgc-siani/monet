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

package org.monet.space.kernel.listeners;

import org.monet.space.kernel.model.MonetEvent;

public interface IListenerMonetEvent {

	void accountModified(MonetEvent event);

	void nodeCreated(MonetEvent event);

	void nodeRemoved(MonetEvent event);

	void nodeVisited(MonetEvent event);

	void beforeModifyNode(MonetEvent event);

	void nodeModified(MonetEvent event);

	void nodeSaved(MonetEvent event);

	void nodeExecuteCommand(MonetEvent event);

	void nodeExecuteCommandConfirmationWhen(MonetEvent event);

	void nodeExecuteCommandConfirmationOnCancel(MonetEvent event);

	void nodeFieldChanged(MonetEvent event);

	void nodeMovedToTrash(MonetEvent event);

	void nodeRecoveredFromTrash(MonetEvent event);

	void nodeAddedToCollection(MonetEvent event);

	void nodeRemovedFromCollection(MonetEvent event);

	void nodeSigned(MonetEvent event);

	void nodeSignsCompleted(MonetEvent event);

	void nodeRefreshMapping(MonetEvent event);

	void nodeRefreshProperties(MonetEvent event);

	void taskCreated(MonetEvent event);

	void taskStateUpdated(MonetEvent event);

	void taskRemoved(MonetEvent event);

	void taskMovedToTrash(MonetEvent event);

	void taskRecoveredFromTrash(MonetEvent event);

	void taskInitialized(MonetEvent event);

	void taskTerminated(MonetEvent event);

	void taskAborted(MonetEvent event);

	void taskAssigned(MonetEvent event);

	void taskUnAssigned(MonetEvent event);

	void taskLockSolved(MonetEvent event);

	void taskPlaceArrival(MonetEvent event);

	void taskFinishing(MonetEvent event);

	void taskFinished(MonetEvent event);

	void taskPlaceTimeout(MonetEvent event);

	void taskPlaceTook(MonetEvent event);

	void taskActionSolved(MonetEvent event);

	void taskSelectDelegationRole(MonetEvent event);

	void taskSetupDelegation(MonetEvent event);

    void taskSetupWait(MonetEvent event);

    void taskSetupTimeout(MonetEvent event);

	void taskSetupDelegationComplete(MonetEvent event);

	void taskSetupJob(MonetEvent event);

	void taskSetupJobComplete(MonetEvent event);

	void taskSetupEdition(MonetEvent event);

	void taskSelectJobRole(MonetEvent event);

	void taskSelectJobRoleComplete(MonetEvent event);

	void taskValidateForm(MonetEvent event);

	void taskCustomerInitialized(MonetEvent event);

	void taskCustomerExpired(MonetEvent event);

	void taskCustomerAborted(MonetEvent event);

	void taskCustomerRequestImport(MonetEvent event);

	void taskCustomerResponseConstructor(MonetEvent event);

	void taskContestantInitialized(MonetEvent event);

	void taskContestantRequestImport(MonetEvent event);

	void taskContestantResponseConstructor(MonetEvent event);

	void taskContestInitialized(MonetEvent event);

	void taskContestTerminated(MonetEvent event);

	void taskContestRequestConstructor(MonetEvent event);

	void taskContestResponseImport(MonetEvent event);

	void taskTransferenceCalculateClassificator(MonetEvent event);

	void taskProviderInitialized(MonetEvent event);

	void taskProviderTerminated(MonetEvent event);

	void taskProviderRejected(MonetEvent event);

	void taskProviderExpired(MonetEvent event);

	void taskProviderRequestConstructor(MonetEvent event);

	void taskProviderResponseImport(MonetEvent event);

	void taskOrderCreated(MonetEvent event);

	void taskOrderRequestSuccess(MonetEvent event);

	void taskOrderRequestFailure(MonetEvent event);

	void taskOrderChatMessageReceived(MonetEvent event);

	void taskOrderChatMessageSent(MonetEvent event);

	void taskCreateJobMessage(MonetEvent event);

	void taskCreatedJob(MonetEvent event);

	void taskFinishedJobMessage(MonetEvent event);

	void taskSensorFinished(MonetEvent event);

	void taskBack(MonetEvent event);

	void taskIsBackEnable(MonetEvent event);

	void taskNewMessagesReceived(MonetEvent event);

	void taskNewMessagesSent(MonetEvent event);

	void taskJobUnassigned(MonetEvent event);

	void taskJobCreated(MonetEvent event);

	void notificationCreated(MonetEvent event);

	void notificationPriorized(MonetEvent event);

	void nodeImport(MonetEvent event);

	void serviceStarted(MonetEvent event);

	void serviceFinished(MonetEvent event);

	void nodeCalculateState(MonetEvent event);

	void postCreated(MonetEvent event);

	void postCommentCreated(MonetEvent event);

	void modelUpdated(MonetEvent event);

	void sourcePopulated(MonetEvent event);

	void sourceSynchronized(MonetEvent event);

	void sourceTermAdded(MonetEvent event);

	void sourceTermModified(MonetEvent event);

	void kernelReset(MonetEvent event);

	void datastoreMounted(MonetEvent event);

    void sessionCreated(MonetEvent event);

    void sessionDestroyed(MonetEvent event);

    void modelEventFired(MonetEvent event);
}