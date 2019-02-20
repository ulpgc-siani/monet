package org.monet.space.kernel.bpi;

import org.monet.space.kernel.machines.ttm.model.BooleanResult;
import org.monet.space.kernel.machines.ttm.model.ClassificatorResult;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.ValidationResult;
import org.monet.space.kernel.model.*;

import java.io.Reader;

public interface BPIEventManager {

	void onNodeCreated(Node node);

	void onNodeOpened(Node node);

	void onNodeSave(Node node);

	void onNodeSaved(Node node);

	void onNodeSetContext(Node node);

	void onNodeCalculateState(Node node);

	void onNodeRemoved(Node node);

	void onNodeFieldChanged(Node node, String fieldName);

	void onNodeItemAdded(Node collection, Node newNode);

	void onNodeItemRemoved(Node collection, Node removedNode);

	void onNodeSign(Node node, User user);

	void onNodeSignsComplete(Node node);

	void onNodeExecuteCommand(Node node, String command);

	void onNodeExecuteCommandConfirmationWhen(Node node, String command, OperationConfirmation confirmation);

	void onNodeExecuteCommandConfirmationOnCancel(Node node, String command);

	void onNodeRefreshMapping(Reference reference, Node form);

	void onNodeRefreshProperties(Reference reference, Node form);

	void onCreateTask(Task task);

	void onInitializeTask(String idTask);

	void onTerminateTask(String idTask);

	void onAbortTask(String idTask);

	void onAssignTask(String idTask, User user);

	void onUnAssignTask(String idTask);

	void onArriveTaskPlace(String idTask, String placeCode);

	void onTimeoutTaskPlace(String idTask, String placeCode, String actionCode);

	void onTakeTaskPlace(String idTask, String placeCode, String actionCode, String routeCode);

	void onSolveTaskAction(String idTask, String placeCode, String actionCode, Node form);

	void onSelectDelegationRole(String idTask, String placeCode, String actionCode, RoleChooser roleChooser);

	void onSetupDelegation(String idTask, String placeCode, String actionCode, DelegationSetup delegationSetup);

    void onSetupWait(String idTask, String placeCode, String actionCode, WaitSetup waitSetup);

    void onSetupTimeout(String idTask, String placeCode, String actionCode, TimeoutSetup timeoutSetup);

	void onSetupDelegationComplete(String idTask, String placeCode, String actionCode, final String provider, final java.util.Date suggestedStartDate, final java.util.Date suggestedEndDate, final String observations, final boolean urgent);

	void onSetupEdition(String idTask, String placeCode, String actionCode, Node form);

	void onSelectJobRole(String idTask, String placeCode, String actionCode, RoleChooser roleChooser);

	void onSelectJobRoleComplete(String idTask, String placeCode, String actionCode, Role role);

	void onSetupJob(String idTask, String placeCode, String actionCode, JobSetup jobSetup);

	void onSetupJobComplete(String idTask, String placeCode, String actionCode, final String provider, final java.util.Date suggestedStartDate, final java.util.Date suggestedEndDate, final String observations, final boolean urgent);

	void onValidateForm(String idTask, String placeCode, String actionCode, Node form, ValidationResult validationResult);

	void onTaskTransferenceCalculateClassificator(String idTask, String placeCode, String actionCode, ClassificatorResult result);

	void onTaskCustomerInit(String idTask);

	void onTaskCustomerExpiration(String idTask);

	void onTaskCustomerAborted(String idTask);

	void onTaskCustomerRequestImport(String idTask, String code, Message message);

	void onTaskCustomerResponseConstructor(String idTask, String code, Message message);

	void onTaskCustomerChatMessageReceived(String idTask, Message message);

	void onTaskContestantInit(String idTask);

	void onTaskContestantRequestImport(String idTask, String code, Message message);

	void onTaskContestantResponseConstructor(String idTask, String code, Message message);

	void onTaskContestInit(String idTask, String collaboratorName);

	void onTaskContestTerminate(String idTask, String collaboratorName);

	void onTaskContestRequestConstructor(String idTask, String collaboratorName, String code, Message message);

	void onTaskContestResponseImport(String idTask, String collaboratorName, String code, Message message);

	void onTaskProviderInit(String idTask, String providerKey);

	void onTaskProviderTerminate(String idTask, String providerKey);

	void onTaskProviderRejected(String idTask, String providerKey);

	void onTaskProviderExpiration(String idTask, String providerKey);

	void onTaskProviderRequestConstructor(String idTask, String providerKey, String code, Message message);

	void onTaskProviderResponseImport(String idTask, String providerKey, String code, Message message);

	void onTaskProviderChatMessageReceived(String idTask, String providerKey, Message message);

	void onTaskCreateJobAction(String idTask, String place, String action, Message message);

	void onTaskCreatedJobAction(String idTask, String idJob, String place, String action);

	void onTaskFinishedJobAction(String idTask, String jobCode, String place, String action, Message message);

	void onTaskSensorFinished(String idTask, Message message);

	void onImportExecute(String importerKey, Node scope, Reader sourceReader, long sourceSize, ProgressCallback progressCallback);

	void onTaskIsBackEnable(String idTask, String placeCode, String actionCode, BooleanResult result);

	void onTaskBack(String idTask, String placeCode, String actionCode);

	void onSourcePopulated(Source source);

	void onSourceSynchronized(Source source);

	void onSourceTermAdded(Source source, Term term);

	void onSourceTermModified(Source source, Term term);

	void onModelEventFired(Event event);

}