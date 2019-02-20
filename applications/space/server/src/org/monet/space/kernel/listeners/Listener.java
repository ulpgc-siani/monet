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

public abstract class Listener implements IListenerMonetEvent {

	public Listener() {
	}

	@Override
	public void accountModified(MonetEvent event) {
	}

	@Override
	public void nodeVisited(MonetEvent event) {
	}

	@Override
	public void nodeCreated(MonetEvent event) {
	}

	@Override
	public void beforeModifyNode(MonetEvent event) {
	}

	@Override
	public void nodeModified(MonetEvent event) {
	}

	@Override
	public void nodeSaved(MonetEvent event) {
	}

	@Override
	public void nodeRemoved(MonetEvent event) {
	}

	@Override
	public void nodeExecuteCommand(MonetEvent event) {
	}

	@Override
	public void nodeExecuteCommandConfirmationWhen(MonetEvent event) {
	}

	@Override
	public void nodeExecuteCommandConfirmationOnCancel(MonetEvent event) {
	}

	@Override
	public void nodeFieldChanged(MonetEvent event) {
	}

	@Override
	public void nodeMovedToTrash(MonetEvent event) {
	}

	@Override
	public void nodeRecoveredFromTrash(MonetEvent event) {
	}

	@Override
	public void nodeAddedToCollection(MonetEvent event) {
	}

	@Override
	public void nodeRemovedFromCollection(MonetEvent event) {
	}

	@Override
	public void nodeSigned(MonetEvent event) {
	}

	@Override
	public void nodeSignsCompleted(MonetEvent event) {
	}

	@Override
	public void nodeRefreshMapping(MonetEvent event) {
	}

	@Override
	public void nodeRefreshProperties(MonetEvent event) {
	}

	@Override
	public void taskCreated(MonetEvent event) {
	}

	@Override
	public void taskStateUpdated(MonetEvent event) {
	}

	@Override
	public void taskRemoved(MonetEvent event) {
	}

	@Override
	public void taskMovedToTrash(MonetEvent event) {
	}

	@Override
	public void taskRecoveredFromTrash(MonetEvent event) {
	}

	@Override
	public void taskInitialized(MonetEvent event) {
	}

	@Override
	public void taskTerminated(MonetEvent event) {
	}

	@Override
	public void taskAborted(MonetEvent event) {
	}

	@Override
	public void taskAssigned(MonetEvent event) {
	}

	@Override
	public void taskUnAssigned(MonetEvent event) {
	}

	@Override
	public void taskLockSolved(MonetEvent event) {
	}

	@Override
	public void taskPlaceArrival(MonetEvent event) {
	}

	@Override
	public void taskFinishing(MonetEvent event) {
	}

	@Override
	public void taskFinished(MonetEvent event) {
	}

	@Override
	public void notificationCreated(MonetEvent event) {
	}

	@Override
	public void notificationPriorized(MonetEvent event) {
	}

	@Override
	public void taskPlaceTimeout(MonetEvent event) {
	}

	@Override
	public void taskPlaceTook(MonetEvent event) {
	}

	@Override
	public void taskActionSolved(MonetEvent event) {
	}

	@Override
	public void taskSelectDelegationRole(MonetEvent event) {
	}

	@Override
	public void taskSetupDelegation(MonetEvent event) {
	}

    @Override
    public void taskSetupWait(MonetEvent event) {
    }

	@Override
	public void taskSetupTimeout(MonetEvent event) {
	}

	@Override
	public void taskSetupDelegationComplete(MonetEvent event) {
	}

	@Override
	public void taskSelectJobRole(MonetEvent event) {
	}

	@Override
	public void taskSelectJobRoleComplete(MonetEvent event) {
	}

	@Override
	public void taskSetupJob(MonetEvent event) {
	}

	@Override
	public void taskSetupJobComplete(MonetEvent event) {
	}

	@Override
	public void taskSetupEdition(MonetEvent event) {
	}

	@Override
	public void taskValidateForm(MonetEvent event) {
	}

	@Override
	public void taskCustomerInitialized(MonetEvent event) {
	}

	@Override
	public void taskCustomerExpired(MonetEvent event) {
	}

	@Override
	public void taskCustomerAborted(MonetEvent event) {
	}

	@Override
	public void taskCustomerRequestImport(MonetEvent event) {
	}

	@Override
	public void taskCustomerResponseConstructor(MonetEvent event) {
	}

	@Override
	public void taskContestantInitialized(MonetEvent event) {
	}

	@Override
	public void taskContestantRequestImport(MonetEvent event) {
	}

	@Override
	public void taskContestantResponseConstructor(MonetEvent event) {
	}

	@Override
	public void taskContestInitialized(MonetEvent event) {
	}

	@Override
	public void taskContestTerminated(MonetEvent event) {
	}

	@Override
	public void taskContestRequestConstructor(MonetEvent event) {
	}

	@Override
	public void taskContestResponseImport(MonetEvent event) {
	}

	@Override
	public void taskTransferenceCalculateClassificator(MonetEvent event) {
	}

	@Override
	public void taskProviderInitialized(MonetEvent event) {
	}

	@Override
	public void taskProviderTerminated(MonetEvent event) {
	}

	@Override
	public void taskProviderRejected(MonetEvent event) {
	}

	@Override
	public void taskProviderExpired(MonetEvent event) {
	}

	@Override
	public void taskProviderRequestConstructor(MonetEvent event) {
	}

	@Override
	public void taskProviderResponseImport(MonetEvent event) {
	}

	@Override
	public void taskOrderCreated(MonetEvent event) {
	}

	@Override
	public void taskOrderRequestSuccess(MonetEvent event) {
	}

	@Override
	public void taskOrderRequestFailure(MonetEvent event) {
	}

	@Override
	public void taskOrderChatMessageReceived(MonetEvent event) {
	}

	@Override
	public void taskOrderChatMessageSent(MonetEvent event) {
	}

	@Override
	public void taskCreateJobMessage(MonetEvent event) {
	}

	@Override
	public void taskCreatedJob(MonetEvent event) {
	}

	@Override
	public void taskFinishedJobMessage(MonetEvent event) {
	}

	@Override
	public void taskSensorFinished(MonetEvent event) {
	}

	@Override
	public void taskNewMessagesReceived(MonetEvent event) {
	}

	@Override
	public void taskIsBackEnable(MonetEvent event) {
	}

	@Override
	public void taskBack(MonetEvent event) {
	}

	@Override
	public void nodeImport(MonetEvent event) {
	}

	@Override
	public void serviceStarted(MonetEvent event) {
	}

	@Override
	public void serviceFinished(MonetEvent event) {
	}

	@Override
	public void nodeCalculateState(MonetEvent event) {
	}

	@Override
	public void postCreated(MonetEvent event) {
	}

	@Override
	public void postCommentCreated(MonetEvent event) {
	}

	@Override
	public void taskNewMessagesSent(MonetEvent event) {
	}

	@Override
	public void taskJobUnassigned(MonetEvent event) {
	}

	@Override
	public void taskJobCreated(MonetEvent event) {
	}

	@Override
	public void modelUpdated(MonetEvent event) {
	}

	@Override
	public void sourcePopulated(MonetEvent event) {
	}

	@Override
	public void sourceSynchronized(MonetEvent event) {
	}

	@Override
	public void sourceTermAdded(MonetEvent event) {
	}

	@Override
	public void sourceTermModified(MonetEvent event) {
	}

	@Override
	public void kernelReset(MonetEvent event) {
	}

	@Override
	public void datastoreMounted(MonetEvent event) {
	}

    @Override
    public void sessionCreated(MonetEvent event) {
    }

    @Override
    public void sessionDestroyed(MonetEvent event) {
    }

	@Override
	public void modelEventFired(MonetEvent event) {
	}
}