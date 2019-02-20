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

import org.monet.space.kernel.bpi.BPIEventManager;
import org.monet.space.kernel.bpi.BPIEventManagerFactory;
import org.monet.space.kernel.machines.ttm.behavior.CustomerBehavior;
import org.monet.space.kernel.machines.ttm.model.BooleanResult;
import org.monet.space.kernel.machines.ttm.model.ClassificatorResult;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.ValidationResult;
import org.monet.space.kernel.model.*;

import java.io.Reader;

public class ListenerBPI extends Listener {
	private BPIEventManager bpi = null;

	public ListenerBPI() {
	}

	private BPIEventManager bpi() {
		if (this.bpi == null)
			this.bpi = BPIEventManagerFactory.getInstance().get();
		return this.bpi;
	}

	@Override
	public void nodeCreated(MonetEvent event) {
		Node node = (Node) event.getSender();
		bpi().onNodeCreated(node);
	}

	@Override
	public void nodeVisited(MonetEvent event) {
		Node node = (Node) event.getSender();
		bpi().onNodeOpened(node);
	}

	@Override
	public void beforeModifyNode(MonetEvent event) {
		Node node = (Node) event.getSender();
		bpi().onNodeSave(node);
	}

	@Override
	public void nodeModified(MonetEvent event) {
		Node node = (Node) event.getSender();
		if (event.getProperty().equals(org.monet.space.kernel.constants.ModelProperty.ATTRIBUTELIST))
			bpi().onNodeSaved(node);
		else if (event.getProperty().equals(org.monet.space.kernel.constants.ModelProperty.PARTNERCONTEXT))
			bpi().onNodeSetContext(node);
	}

	@Override
	public void nodeCalculateState(MonetEvent event) {
		Node node = (Node) event.getSender();
		bpi().onNodeCalculateState(node);
	}

	@Override
	public void nodeRemoved(MonetEvent event) {
		Node node = (Node) event.getSender();
		bpi().onNodeRemoved(node);
	}

	@Override
	public void nodeExecuteCommand(MonetEvent event) {
		Node node = (Node) event.getSender();
		bpi().onNodeExecuteCommand(node, (String) event.getParameter(MonetEvent.PARAMETER_COMMAND));
	}

	@Override
	public void nodeExecuteCommandConfirmationWhen(MonetEvent event) {
		Node node = (Node) event.getSender();
		bpi().onNodeExecuteCommandConfirmationWhen(node, (String) event.getParameter(MonetEvent.PARAMETER_COMMAND), (OperationConfirmation) event.getParameter(MonetEvent.PARAMETER_COMMAND_CONFIRMATION_REQUIRED));
	}

	@Override
	public void nodeExecuteCommandConfirmationOnCancel(MonetEvent event) {
		Node node = (Node) event.getSender();
		bpi().onNodeExecuteCommandConfirmationOnCancel(node, (String) event.getParameter(MonetEvent.PARAMETER_COMMAND));
	}

	@Override
	public void nodeFieldChanged(MonetEvent event) {
		Node node = (Node) event.getSender();
		bpi().onNodeFieldChanged(node, (String) event.getParameter(MonetEvent.PARAMETER_FIELD));
	}

	@Override
	public void nodeAddedToCollection(MonetEvent event) {
		Node node = (Node) event.getSender();
		Node parent = (Node) event.getParameter(MonetEvent.PARAMETER_COLLECTION);
		bpi().onNodeItemAdded(parent, node);
	}

	@Override
	public void nodeRemovedFromCollection(MonetEvent event) {
		Node node = (Node) event.getSender();
		Node parent = (Node) event.getParameter(MonetEvent.PARAMETER_COLLECTION);
		bpi().onNodeItemRemoved(parent, node);
	}

	@Override
	public void nodeSigned(MonetEvent event) {
		Node node = (Node) event.getSender();
		User user = (User) event.getParameter(MonetEvent.PARAMETER_SIGN_USER);
		bpi().onNodeSign(node, user);
	}

	@Override
	public void nodeSignsCompleted(MonetEvent event) {
		Node node = (Node) event.getSender();
		bpi().onNodeSignsComplete(node);
	}

	@Override
	public void nodeRefreshMapping(MonetEvent event) {
		Node form = (Node) event.getSender();
		Reference reference = (Reference) event.getParameter(MonetEvent.PARAMETER_REFERENCE);
		bpi().onNodeRefreshMapping(reference, form);
	}

	@Override
	public void nodeRefreshProperties(MonetEvent event) {
		Node form = (Node) event.getSender();
		Reference reference = (Reference) event.getParameter(MonetEvent.PARAMETER_REFERENCE);
		bpi().onNodeRefreshProperties(reference, form);
	}

	@Override
	public void nodeImport(MonetEvent event) {
		String importerKey = (String) event.getSender();
		Node scope = (Node) event.getParameter(MonetEvent.PARAMETER_SCOPE);
		Reader inputReader = (Reader) event.getParameter(MonetEvent.PARAMETER_STREAM);
		Long size = (Long) event.getParameter(MonetEvent.PARAMETER_STREAM_SIZE);
		ProgressCallback progressCallback = (ProgressCallback) event.getParameter(MonetEvent.PARAMETER_CALLBACK);

		bpi().onImportExecute(importerKey, scope, inputReader, size, progressCallback);
	}

	@Override
	public void taskCreated(MonetEvent event) {
		Task task = (Task) event.getSender();
		bpi().onCreateTask(task);
	}

	@Override
	public void taskInitialized(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onInitializeTask(idTask);
	}

	@Override
	public void taskTerminated(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onTerminateTask(idTask);
	}

	@Override
	public void taskAborted(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onAbortTask(idTask);
	}

	@Override
	public void taskAssigned(MonetEvent event) {
		Task task = (Task) event.getSender();
		bpi().onAssignTask(task.getId(), (User) event.getParameter(MonetEvent.PARAMETER_USER));
	}

	@Override
	public void taskUnAssigned(MonetEvent event) {
		Task task = (Task) event.getSender();
		bpi().onUnAssignTask(task.getId());
	}

	@Override
	public void taskPlaceArrival(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onArriveTaskPlace(idTask, (String) event.getParameter(MonetEvent.PARAMETER_PLACE));
	}

	@Override
	public void taskPlaceTimeout(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onTimeoutTaskPlace(idTask, (String) event.getParameter(MonetEvent.PARAMETER_PLACE), (String) event.getParameter(MonetEvent.PARAMETER_ACTION));
	}

	@Override
	public void taskPlaceTook(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onTakeTaskPlace(idTask, (String) event.getParameter(MonetEvent.PARAMETER_PLACE), (String) event.getParameter(MonetEvent.PARAMETER_ACTION), (String) event.getParameter(MonetEvent.PARAMETER_ROUTE));
	}

	@Override
	public void taskActionSolved(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onSolveTaskAction(idTask, (String) event.getParameter(MonetEvent.PARAMETER_PLACE), (String) event.getParameter(MonetEvent.PARAMETER_ACTION), (Node) event.getParameter(MonetEvent.PARAMETER_FORM));
	}

	@Override
	public void taskSelectDelegationRole(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onSelectDelegationRole(idTask, (String) event.getParameter(MonetEvent.PARAMETER_PLACE), (String) event.getParameter(MonetEvent.PARAMETER_ACTION), (RoleChooser) event.getParameter(MonetEvent.PARAMETER_ROLE_CHOOSER));
	}

    @Override
    public void taskSetupDelegation(MonetEvent event) {
        String idTask = (String) event.getSender();
        bpi().onSetupDelegation(idTask, (String) event.getParameter(MonetEvent.PARAMETER_PLACE), (String) event.getParameter(MonetEvent.PARAMETER_ACTION), (DelegationSetup) event.getParameter(MonetEvent.PARAMETER_DELEGATION_SETUP));
    }

    @Override
    public void taskSetupWait(MonetEvent event) {
        String idTask = (String) event.getSender();
        bpi().onSetupWait(idTask, (String) event.getParameter(MonetEvent.PARAMETER_PLACE), (String) event.getParameter(MonetEvent.PARAMETER_ACTION), (WaitSetup) event.getParameter(MonetEvent.PARAMETER_WAIT_SETUP));
    }

	@Override
	public void taskSetupTimeout(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onSetupTimeout(idTask, (String) event.getParameter(MonetEvent.PARAMETER_PLACE), (String) event.getParameter(MonetEvent.PARAMETER_ACTION), (TimeoutSetup) event.getParameter(MonetEvent.PARAMETER_TIMEOUT_SETUP));
	}

    @Override
	public void taskSetupDelegationComplete(MonetEvent event) {
		String idTask = (String) event.getSender();
		String placeCode = (String) event.getParameter(MonetEvent.PARAMETER_PLACE);
		String actionCode = (String) event.getParameter(MonetEvent.PARAMETER_ACTION);
		java.util.Date suggestedStartDate = (java.util.Date) event.getParameter(MonetEvent.PARAMETER_SUGGESTED_START_DATE);
		java.util.Date suggestedEndDate = (java.util.Date) event.getParameter(MonetEvent.PARAMETER_SUGGESTED_END_DATE);
		String observations = (String) event.getParameter(MonetEvent.PARAMETER_OBSERVATIONS);
		boolean urgent = (Boolean) event.getParameter(MonetEvent.PARAMETER_URGENT);
		String provider = (String) event.getParameter(MonetEvent.PARAMETER_PROVIDER);
		bpi().onSetupDelegationComplete(idTask, placeCode, actionCode, provider, suggestedStartDate, suggestedEndDate, observations, urgent);
	}

	@Override
	public void taskSetupJob(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onSetupJob(idTask, (String) event.getParameter(MonetEvent.PARAMETER_PLACE), (String) event.getParameter(MonetEvent.PARAMETER_ACTION), (JobSetup) event.getParameter(MonetEvent.PARAMETER_JOB_SETUP));
	}

	@Override
	public void taskSetupJobComplete(MonetEvent event) {
		String idTask = (String) event.getSender();
		String placeCode = (String) event.getParameter(MonetEvent.PARAMETER_PLACE);
		String actionCode = (String) event.getParameter(MonetEvent.PARAMETER_ACTION);
		java.util.Date suggestedStartDate = (java.util.Date) event.getParameter(MonetEvent.PARAMETER_SUGGESTED_START_DATE);
		java.util.Date suggestedEndDate = (java.util.Date) event.getParameter(MonetEvent.PARAMETER_SUGGESTED_END_DATE);
		String observations = (String) event.getParameter(MonetEvent.PARAMETER_OBSERVATIONS);
		boolean urgent = (Boolean) event.getParameter(MonetEvent.PARAMETER_URGENT);
		String provider = (String) event.getParameter(MonetEvent.PARAMETER_PROVIDER);
		bpi().onSetupJobComplete(idTask, placeCode, actionCode, provider, suggestedStartDate, suggestedEndDate, observations, urgent);
	}

	@Override
	public void taskSelectJobRole(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onSelectJobRole(idTask, (String) event.getParameter(MonetEvent.PARAMETER_PLACE), (String) event.getParameter(MonetEvent.PARAMETER_ACTION), (RoleChooser) event.getParameter(MonetEvent.PARAMETER_ROLE_CHOOSER));
	}

	@Override
	public void taskSelectJobRoleComplete(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onSelectJobRoleComplete(idTask, (String) event.getParameter(MonetEvent.PARAMETER_PLACE), (String) event.getParameter(MonetEvent.PARAMETER_ACTION), (Role) event.getParameter(MonetEvent.PARAMETER_ROLE));
	}

	@Override
	public void taskSetupEdition(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onSetupEdition(idTask, (String) event.getParameter(MonetEvent.PARAMETER_PLACE), (String) event.getParameter(MonetEvent.PARAMETER_ACTION), (Node) event.getParameter(MonetEvent.PARAMETER_FORM));
	}

	@Override
	public void taskValidateForm(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onValidateForm(idTask, (String) event.getParameter(MonetEvent.PARAMETER_PLACE), (String) event.getParameter(MonetEvent.PARAMETER_ACTION), (Node) event.getParameter(MonetEvent.PARAMETER_FORM), (ValidationResult) event.getParameter(MonetEvent.PARAMETER_VALIDATION_RESULT));
	}

	@Override
	public void taskTransferenceCalculateClassificator(MonetEvent event) {
		String idTask = (String) event.getSender();
		String placeCode = (String) event.getParameter(MonetEvent.PARAMETER_PLACE);
		String actionCode = (String) event.getParameter(MonetEvent.PARAMETER_ACTION);
		ClassificatorResult result = (ClassificatorResult) event.getParameter(MonetEvent.PARAMETER_RESULT);
		bpi().onTaskTransferenceCalculateClassificator(idTask, placeCode, actionCode, result);
	}

	@Override
	public void taskIsBackEnable(MonetEvent event) {
		String idTask = (String) event.getSender();
		String placeCode = (String) event.getParameter(MonetEvent.PARAMETER_PLACE);
		String actionCode = (String) event.getParameter(MonetEvent.PARAMETER_ACTION);
		BooleanResult result = (BooleanResult) event.getParameter(MonetEvent.PARAMETER_RESULT);
		bpi().onTaskIsBackEnable(idTask, placeCode, actionCode, result);
	}

	@Override
	public void taskBack(MonetEvent event) {
		String idTask = (String) event.getSender();
		String placeCode = (String) event.getParameter(MonetEvent.PARAMETER_PLACE);
		String actionCode = (String) event.getParameter(MonetEvent.PARAMETER_ACTION);
		bpi().onTaskBack(idTask, placeCode, actionCode);
	}

	@Override
	public void taskCustomerInitialized(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onTaskCustomerInit(idTask);
	}

	@Override
	public void taskCustomerExpired(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onTaskCustomerExpiration(idTask);
	}

	@Override
	public void taskCustomerAborted(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onTaskCustomerAborted(idTask);
	}

	@Override
	public void taskCustomerRequestImport(MonetEvent event) {
		String idTask = (String) event.getSender();
		String code = (String) event.getParameter(MonetEvent.PARAMETER_CODE);
		Message message = (Message) event.getParameter(MonetEvent.PARAMETER_MESSAGE);
		bpi().onTaskCustomerRequestImport(idTask, code, message);
	}

	@Override
	public void taskCustomerResponseConstructor(MonetEvent event) {
		String idTask = (String) event.getSender();
		String code = (String) event.getParameter(MonetEvent.PARAMETER_CODE);
		Message message = (Message) event.getParameter(MonetEvent.PARAMETER_MESSAGE);
		bpi().onTaskCustomerResponseConstructor(idTask, code, message);
	}


	@Override
	public void taskContestantInitialized(MonetEvent event) {
		String idTask = (String) event.getSender();
		bpi().onTaskContestantInit(idTask);
	}

	@Override
	public void taskContestantRequestImport(MonetEvent event) {
		String idTask = (String) event.getSender();
		String code = (String) event.getParameter(MonetEvent.PARAMETER_CODE);
		Message message = (Message) event.getParameter(MonetEvent.PARAMETER_MESSAGE);
		bpi().onTaskContestantRequestImport(idTask, code, message);
	}

	@Override
	public void taskContestantResponseConstructor(MonetEvent event) {
		String idTask = (String) event.getSender();
		String code = (String) event.getParameter(MonetEvent.PARAMETER_CODE);
		Message message = (Message) event.getParameter(MonetEvent.PARAMETER_MESSAGE);
		bpi().onTaskContestantResponseConstructor(idTask, code, message);
	}

	@Override
	public void taskContestInitialized(MonetEvent event) {
		String idTask = (String) event.getSender();
		String contestName = (String) event.getParameter(MonetEvent.PARAMETER_CONTEST);
		bpi().onTaskContestInit(idTask, contestName);
	}

	@Override
	public void taskContestTerminated(MonetEvent event) {
		String idTask = (String) event.getSender();
		String contestName = (String) event.getParameter(MonetEvent.PARAMETER_CONTEST);
		bpi().onTaskContestTerminate(idTask, contestName);
	}

	@Override
	public void taskContestRequestConstructor(MonetEvent event) {
		String idTask = (String) event.getSender();
		String contestName = (String) event.getParameter(MonetEvent.PARAMETER_CONTEST);
		String code = (String) event.getParameter(MonetEvent.PARAMETER_CODE);
		Message message = (Message) event.getParameter(MonetEvent.PARAMETER_MESSAGE);
		bpi().onTaskContestRequestConstructor(idTask, contestName, code, message);
	}

	@Override
	public void taskContestResponseImport(MonetEvent event) {
		String idTask = (String) event.getSender();
		String contestName = (String) event.getParameter(MonetEvent.PARAMETER_CONTEST);
		String code = (String) event.getParameter(MonetEvent.PARAMETER_CODE);
		Message message = (Message) event.getParameter(MonetEvent.PARAMETER_MESSAGE);
		bpi().onTaskContestResponseImport(idTask, contestName, code, message);
	}

	@Override
	public void taskProviderInitialized(MonetEvent event) {
		String idTask = (String) event.getSender();
		String providerName = (String) event.getParameter(MonetEvent.PARAMETER_PROVIDER);
		bpi().onTaskProviderInit(idTask, providerName);
	}

	@Override
	public void taskProviderTerminated(MonetEvent event) {
		String idTask = (String) event.getSender();
		String providerName = (String) event.getParameter(MonetEvent.PARAMETER_PROVIDER);
		bpi().onTaskProviderTerminate(idTask, providerName);
	}

	@Override
	public void taskProviderRejected(MonetEvent event) {
		String idTask = (String) event.getSender();
		String providerName = (String) event.getParameter(MonetEvent.PARAMETER_PROVIDER);
		bpi().onTaskProviderRejected(idTask, providerName);
	}

	@Override
	public void taskProviderExpired(MonetEvent event) {
		String idTask = (String) event.getSender();
		String providerName = (String) event.getParameter(MonetEvent.PARAMETER_PROVIDER);
		bpi().onTaskProviderExpiration(idTask, providerName);
	}

	@Override
	public void taskProviderRequestConstructor(MonetEvent event) {
		String idTask = (String) event.getSender();
		String providerName = (String) event.getParameter(MonetEvent.PARAMETER_PROVIDER);
		String code = (String) event.getParameter(MonetEvent.PARAMETER_CODE);
		Message message = (Message) event.getParameter(MonetEvent.PARAMETER_MESSAGE);
		bpi().onTaskProviderRequestConstructor(idTask, providerName, code, message);
	}

	@Override
	public void taskProviderResponseImport(MonetEvent event) {
		String idTask = (String) event.getSender();
		String providerName = (String) event.getParameter(MonetEvent.PARAMETER_PROVIDER);
		String code = (String) event.getParameter(MonetEvent.PARAMETER_CODE);
		Message message = (Message) event.getParameter(MonetEvent.PARAMETER_MESSAGE);
		bpi().onTaskProviderResponseImport(idTask, providerName, code, message);
	}

	@Override
	public void taskCreateJobMessage(MonetEvent event) {
		String idTask = (String) event.getSender();
		String place = (String) event.getParameter(MonetEvent.PARAMETER_PLACE);
		String action = (String) event.getParameter(MonetEvent.PARAMETER_ACTION);
		Message message = (Message) event.getParameter(MonetEvent.PARAMETER_MESSAGE);
		bpi().onTaskCreateJobAction(idTask, place, action, message);
	}

	@Override
	public void taskCreatedJob(MonetEvent event) {
		String idTask = (String) event.getSender();
		String idJob = (String) event.getParameter(MonetEvent.PARAMETER_JOB);
		String place = (String) event.getParameter(MonetEvent.PARAMETER_PLACE);
		String action = (String) event.getParameter(MonetEvent.PARAMETER_ACTION);
		bpi().onTaskCreatedJobAction(idTask, idJob, place, action);
	}

	@Override
	public void taskFinishedJobMessage(MonetEvent event) {
		String idTask = (String) event.getSender();
		String jobCode = (String) event.getParameter(MonetEvent.PARAMETER_CODE);
		String place = (String) event.getParameter(MonetEvent.PARAMETER_PLACE);
		String action = (String) event.getParameter(MonetEvent.PARAMETER_ACTION);
		Message message = (Message) event.getParameter(MonetEvent.PARAMETER_MESSAGE);
		bpi().onTaskFinishedJobAction(idTask, jobCode, place, action, message);
	}

	@Override
	public void taskSensorFinished(MonetEvent event) {
		String idTask = (String) event.getSender();
		Message message = (Message) event.getParameter(MonetEvent.PARAMETER_MESSAGE);
		bpi().onTaskSensorFinished(idTask, message);
	}

	@Override
	public void taskOrderChatMessageReceived(MonetEvent event) {
		String idTask = (String) event.getSender();
		String providerCode = (String) event.getParameter(MonetEvent.PARAMETER_PROVIDER);
		Message message = (Message) event.getParameter(MonetEvent.PARAMETER_MESSAGE);

		if (providerCode == null)
			return;

		if (providerCode.equals(CustomerBehavior.NAME))
			bpi().onTaskCustomerChatMessageReceived(idTask, message);
		else
			bpi().onTaskProviderChatMessageReceived(idTask, providerCode, message);
	}

	@Override
	public void sourcePopulated(MonetEvent event) {
		Source source = (Source) event.getSender();
		bpi().onSourcePopulated(source);
	}

	@Override
	public void sourceSynchronized(MonetEvent event) {
		Source source = (Source) event.getSender();
		bpi().onSourceSynchronized(source);
	}

	@Override
	public void sourceTermAdded(MonetEvent event) {
		Source source = (Source) event.getSender();
		Term term = (Term)event.getParameter(MonetEvent.PARAMETER_TERM);
		bpi().onSourceTermAdded(source, term);
	}

	@Override
	public void sourceTermModified(MonetEvent event) {
		Source source = (Source) event.getSender();
		Term term = (Term)event.getParameter(MonetEvent.PARAMETER_TERM);
		bpi().onSourceTermModified(source, term);
	}

	@Override
	public void modelEventFired(MonetEvent event) {
		Event monetEvent = (Event) event.getSender();
		bpi().onModelEventFired(monetEvent);
	}

}