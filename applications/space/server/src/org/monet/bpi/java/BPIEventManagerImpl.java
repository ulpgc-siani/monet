package org.monet.bpi.java;

import net.minidev.json.JSONObject;
import org.monet.bpi.*;
import org.monet.bpi.Field;
import org.monet.bpi.types.Date;
import org.monet.metamodel.*;
import org.monet.metamodel.interfaces.HasMappings;
import org.monet.metamodel.interfaces.HasProperties;
import org.monet.metamodel.interfaces.HasSchema;
import org.monet.mobile.model.TaskMetadata;
import org.monet.space.kernel.agents.AgentPushService;
import org.monet.space.kernel.bpi.BPIEventManager;
import org.monet.space.kernel.bpi.java.importer.ImportIterable;
import org.monet.space.kernel.bpi.java.importer.ImportIterator;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.TaskLayer;
import org.monet.space.kernel.library.LibraryString;
import org.monet.space.kernel.listeners.ListenerPushService;
import org.monet.space.kernel.machines.ttm.model.BooleanResult;
import org.monet.space.kernel.machines.ttm.model.ClassificatorResult;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.ValidationResult;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.DelegationSetup;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.JobSetup;
import org.monet.space.kernel.model.Node;
import org.monet.space.kernel.model.Role;
import org.monet.space.kernel.model.RoleChooser;
import org.monet.space.kernel.model.Source;
import org.monet.space.kernel.model.Task;
import org.monet.space.kernel.model.User;
import org.monet.space.kernel.model.WaitSetup;
import org.simpleframework.xml.core.Persister;

import java.io.Reader;
import java.util.LinkedHashSet;
import java.util.List;

import org.monet.space.kernel.machines.ttm.model.ValidationResult;

public class BPIEventManagerImpl implements BPIEventManager {

	public static final String CUSTOMER_BEHAVIOR_NAME = "customer";
	public static final String CONTESTANTS_BEHAVIOR_NAME = "contestants";

	Persister persister = new Persister();
	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	static {
		ConsoleServiceImpl.init();
		ImporterServiceImpl.init();
		ExporterServiceImpl.init();
		TaskServiceImpl.init();
		JobServiceImpl.init();
		NodeServiceImpl.init();
		NewsServiceImpl.init();
		SourceServiceImpl.init();
		ClientServiceImpl.init();
		BusinessUnitImpl.init();
		SetupImpl.init();
		MailServiceImpl.init();
		DatastoreServiceImpl.init();
		DelivererServiceImpl.init();
		TranslationServiceImpl.init();
		FileServiceImpl.init();
		RoleServiceImpl.init();
		EventServiceImpl.init();
	}

  /* NODE EVENTS */

	@Override
	public void onNodeCreated(Node node) {
		Definition definition = node.getDefinition();

		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(definition);
		if (behaviour == null) return;

		behaviour.injectNode(node);

		behaviour.isCreating = true;
		((BehaviorNode) behaviour).constructor();
		behaviour.isCreating = false;

		this.buildNodeAsDimensionComponent(behaviour);
	}

	@Override
	public void onNodeOpened(Node node) {
		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(node.getDefinition());
		if (behaviour == null) return;
		behaviour.injectNode(node);
		((BehaviorNode) behaviour).onOpened();
	}

	@Override
	public void onNodeSave(Node node) {
		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(node.getDefinition());
		if (behaviour == null) return;
		behaviour.injectNode(node);
		behaviour.isSaving = true;
		((BehaviorNode) behaviour).onSave();
		behaviour.isSaving = false;
	}

	@Override
	public void onNodeSaved(Node node) {
		Definition definition = node.getDefinition();

		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(definition);
		if (behaviour == null) return;
		behaviour.injectNode(node);

		behaviour.isSaving = true;
		((BehaviorNode) behaviour).onSaved();
		behaviour.isSaving = false;

		this.buildNodeAsDimensionComponent(behaviour);
	}

	@Override
	public void onNodeSetContext(Node node) {
		Definition definition = node.getDefinition();

		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(definition);
		if (behaviour == null) return;
		behaviour.injectNode(node);

		((BehaviorNode) behaviour).onSetContext();
	}

	@Override
	public void onNodeCalculateState(Node node) {
		Definition definition = node.getDefinition();

		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(definition);
		if (behaviour == null) return;
		behaviour.injectNode(node);
		NodeState nodeState = behaviour.calculateNodeState();

		JSONObject message = new JSONObject();
		message.put("node", node.getId());
		message.put("state", nodeState.toJson());

		AgentPushService.getInstance().pushToViewers(null, PushClient.generateViewId(Node.class, node.getId()), ListenerPushService.PushClientMessages.UPDATE_NODE_STATE, message);
	}

	@Override
	public void onNodeRemoved(Node node) {
		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(node.getDefinition());
		if (behaviour == null) return;
		behaviour.injectNode(node);
		((BehaviorNode) behaviour).onRemoved();
	}

	@Override
	public void onNodeExecuteCommand(Node node, String command) {
		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(node.getDefinition());
		if (behaviour == null) return;
		behaviour.injectNode(node);
		String commandEnumItem = LibraryString.toJavaIdentifier(command);
		((BehaviorNode) behaviour).executeCommand(commandEnumItem);
	}

	@Override
	public void onNodeExecuteCommandConfirmationWhen(Node node, String command, OperationConfirmation confirmation) {
		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(node.getDefinition());
		if (behaviour == null) return;
		behaviour.injectNode(node);
		String commandEnumItem = LibraryString.toJavaIdentifier(command);
		confirmation.setRequired(behaviour.executeCommandConfirmationWhen(commandEnumItem));
	}

	@Override
	public void onNodeExecuteCommandConfirmationOnCancel(Node node, String command) {
		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(node.getDefinition());
		if (behaviour == null) return;
		behaviour.injectNode(node);
		String commandEnumItem = LibraryString.toJavaIdentifier(command);
		behaviour.executeCommandConfirmationOnCancel(commandEnumItem);
	}

  /* COLLECTION EVENTS */

	@Override
	public void onNodeItemAdded(Node collection, Node newNode) {
		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(collection.getDefinition());
		if (behaviour == null) return;
		behaviour.injectNode(collection);
		NodeImpl bpiNewNode = this.bpiClassLocator.instantiateBehaviour(newNode.getDefinition());
		if (bpiNewNode == null) return;
		bpiNewNode.injectNode(newNode);
		((BehaviorNodeCollection) behaviour).onItemAdded(bpiNewNode);
	}

	@Override
	public void onNodeItemRemoved(Node collection, Node removedNode) {
		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(collection.getDefinition());
		if (behaviour == null) return;
		behaviour.injectNode(collection);
		NodeImpl bpiNewNode = this.bpiClassLocator.instantiateBehaviour(removedNode.getDefinition());
		if (bpiNewNode == null) return;
		bpiNewNode.injectNode(removedNode);
		((BehaviorNodeCollection) behaviour).onItemRemoved(bpiNewNode);
	}

	@Override
	public void onNodeSign(Node node, User user) {
		NodeImpl bpiDocument = this.bpiClassLocator.instantiateBehaviour(node.getDefinition());
		if (bpiDocument == null) return;
		bpiDocument.injectNode(node);
		UserImpl userImpl = new UserImpl();
		userImpl.injectUser(user);
		((BehaviorNodeDocument) bpiDocument).onSign(userImpl);
	}

	@Override
	public void onNodeSignsComplete(Node node) {
		NodeImpl bpiDocument = this.bpiClassLocator.instantiateBehaviour(node.getDefinition());
		if (bpiDocument == null) return;
		bpiDocument.injectNode(node);
		((BehaviorNodeDocument) bpiDocument).onSignsComplete();
	}

  /* FORM EVENTS */

	@Override
	public void onNodeFieldChanged(Node node, String name) {
		Attribute attribute;

		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(node.getDefinition());
		if (behaviour == null) return;
		behaviour.injectNode(node);

		FormDefinition formDefinition = (FormDefinition) node.getDefinition();
		FieldProperty fieldDefinition = formDefinition.getField(name);

		attribute = this.getNodeAttribute(node, fieldDefinition.getCode());
		if (attribute == null)
			attribute = this.getNodeAttribute(node, name);
		if (attribute == null)
			attribute = new Attribute();

		Field<?> field = FieldFactory.getInstance().get(formDefinition.getName(), fieldDefinition, attribute, node);

		((BehaviorNodeForm) behaviour).onFieldChanged(field);
	}

	@Override
	public void onNodeRefreshMapping(Reference reference, Node node) {
		NodeDefinition definition = node.getDefinition();
		NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(definition);
		if (behaviour == null) return;
		behaviour.injectNode(node);

		IndexEntryImpl indexEntryBehavior = this.bpiClassLocator.instantiateBehaviour(reference.getDefinition());
		if (indexEntryBehavior == null) return;
		indexEntryBehavior.injectIndexEntry(reference);
		indexEntryBehavior.injectNode(node);

		MappingImpl mapping = this.bpiClassLocator.instantiateBehaviour(((HasMappings) definition).getMappingClass(reference.getCode()));
		mapping.injectIndexEntry(indexEntryBehavior);
		mapping.injectNode(behaviour);
		mapping.calculateMapping();
	}

	@Override
	public void onNodeRefreshProperties(Reference reference, Node node) {
		NodeDefinition definition = node.getDefinition();
		if (definition instanceof HasProperties) {
			NodeImpl behaviour = this.bpiClassLocator.instantiateBehaviour(definition);
			if (behaviour == null) return;
			behaviour.injectNode(node);

			IndexEntryImpl indexEntryBehavior = this.bpiClassLocator.instantiateBehaviour(reference.getDefinition());
			if (indexEntryBehavior == null) return;
			indexEntryBehavior.injectIndexEntry(reference);
			indexEntryBehavior.injectNode(node);

			MappingImpl mapping = this.bpiClassLocator.instantiateBehaviour(((HasProperties) definition).getPropertiesClass());
			mapping.injectIndexEntry(indexEntryBehavior);
			mapping.injectNode(behaviour);
			mapping.calculateMapping();
		}
	}

	/* TASK EVENTS */
	@Override
	public void onCreateTask(Task task) {
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);
		behaviour.onCreate();
	}

	@Override
	public void onInitializeTask(String idTask) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);
		behaviour.onInitialize();
	}

	@Override
	public void onTerminateTask(String idTask) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);
		behaviour.onTerminate();
	}

	@Override
	public void onAbortTask(String idTask) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);
		behaviour.onAbort();
	}

	@Override
	public void onAssignTask(String idTask, User user) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		UserImpl userImpl = new UserImpl();
		userImpl.injectUser(user);
		if (behaviour == null) return;
		behaviour.injectTask(task);
		behaviour.onAssign(userImpl);
	}

	@Override
	public void onUnAssignTask(String idTask) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);
		behaviour.onUnassign();
	}

	@Override
	public void onArriveTaskPlace(String idTask, String placeCode) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);
		behaviour.onArrivePlace(placeCode);
	}

	@Override
	public void onTimeoutTaskPlace(String idTask, String placeCode, String actionCode) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);
		behaviour.onTimeoutPlace(placeCode, actionCode);
	}

	@Override
	public void onTakeTaskPlace(String idTask, String placeCode, String actionCode, String routeCode) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);
		behaviour.onTakePlace(placeCode, actionCode, routeCode);
	}

	@Override
	public void onSolveTaskAction(String idTask, String placeCode, String actionCode, Node form) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		NodeFormImpl bpiForm = null;
		if (form != null) {
			bpiForm = this.bpiClassLocator.instantiateBehaviour(form.getDefinition());
			if (bpiForm == null) return;
			bpiForm.injectNode(form);
		}

		behaviour.onSolveAction(placeCode, actionCode, bpiForm);
	}

	@Override
	public void onSelectDelegationRole(String idTask, String placeCode, String actionCode, RoleChooser roleChooser) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		RoleChooserImpl bpiRoleChooser = new RoleChooserImpl();
		bpiRoleChooser.inject(roleChooser);

		behaviour.onSelectDelegationRole(placeCode, actionCode, bpiRoleChooser);
	}

	@Override
	public void onSetupDelegation(String idTask, String placeCode, String actionCode, DelegationSetup delegationSetup) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		DelegationSetupImpl bpiDelegationSetup = new DelegationSetupImpl();
		bpiDelegationSetup.inject(delegationSetup);

		behaviour.onSetupDelegation(placeCode, actionCode, bpiDelegationSetup);
	}

	@Override
	public void onSetupWait(String idTask, String placeCode, String actionCode, WaitSetup waitSetup) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		WaitSetupImpl bpiWaitSetup = new WaitSetupImpl();
		bpiWaitSetup.inject(waitSetup);

		behaviour.onSetupWait(placeCode, actionCode, bpiWaitSetup);
	}

	@Override
	public void onSetupTimeout(String idTask, String placeCode, String actionCode, org.monet.space.kernel.model.TimeoutSetup timeoutSetup) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		TimeoutSetupImpl bpiTimeoutSetup = new TimeoutSetupImpl();
		bpiTimeoutSetup.inject(timeoutSetup);

		behaviour.onSetupTimeout(placeCode, actionCode, bpiTimeoutSetup);
	}

	@Override
	public void onSetupDelegationComplete(String idTask, String placeCode, String actionCode, final String provider, final java.util.Date suggestedStartDate, final java.util.Date suggestedEndDate, final String observations, final boolean urgent) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		behaviour.onSetupDelegationComplete(placeCode, actionCode, provider, new Date(suggestedStartDate), new Date(suggestedEndDate), observations, urgent);
	}

	@Override
	public void onSetupEdition(String idTask, String placeCode, String actionCode, Node form) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());

		if (behaviour == null) return;
		behaviour.injectTask(task);

		NodeImpl bpiForm = this.bpiClassLocator.instantiateBehaviour(form.getDefinition());
		bpiForm.injectNode(form);

		behaviour.onSetupEdition(placeCode, actionCode, bpiForm);
	}

	@Override
	public void onSelectJobRole(String idTask, String placeCode, String actionCode, RoleChooser roleChooser) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		RoleChooserImpl bpiRoleChooser = new RoleChooserImpl();
		bpiRoleChooser.inject(roleChooser);

		behaviour.onSelectJobRole(placeCode, actionCode, bpiRoleChooser);
	}

	@Override
	public void onSelectJobRoleComplete(String idTask, String placeCode, String actionCode, Role role) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		RoleImpl bpiRole = new RoleImpl();
		bpiRole.injectRole(role);

		behaviour.onSelectJobRoleComplete(placeCode, actionCode, bpiRole);
	}

	@Override
	public void onSetupJob(String idTask, String placeCode, String actionCode, JobSetup jobSetup) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		JobSetupImpl bpiJobSetup = new JobSetupImpl();
		bpiJobSetup.inject(jobSetup);

		behaviour.onSetupJob(placeCode, actionCode, bpiJobSetup);
	}

	@Override
	public void onSetupJobComplete(String idTask, String placeCode, String actionCode, final String provider, final java.util.Date suggestedStartDate, final java.util.Date suggestedEndDate, final String observations, final boolean urgent) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		behaviour.onSetupJobComplete(placeCode, actionCode, provider, new Date(suggestedStartDate), new Date(suggestedEndDate), observations, urgent);
	}

	@Override
	public void onValidateForm(String idTask, String placeCode, String actionCode, Node form, ValidationResult validationResult) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		NodeFormImpl bpiForm = null;
		if (form != null) {
			bpiForm = this.bpiClassLocator.instantiateBehaviour(form.getDefinition());
			if (bpiForm == null) return;
			bpiForm.injectNode(form);
		}

		ValidationResultImpl bpiValidationResult = new ValidationResultImpl();
		bpiValidationResult.inject(validationResult);

		behaviour.onValidateForm(placeCode, actionCode, bpiForm, bpiValidationResult);
	}

	@Override
	public void onTaskIsBackEnable(String idTask, String placeCode, String actionCode, BooleanResult result) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);
		result.setValue(behaviour.onIsBackEnable(placeCode, actionCode));
	}

	@Override
	public void onTaskBack(String idTask, String placeCode, String actionCode) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);
		behaviour.onBack(placeCode, actionCode);
	}

	@Override
	public void onTaskTransferenceCalculateClassificator(String idTask, String placeCode, String actionCode, ClassificatorResult result) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);
		result.value = behaviour.onCalculateClassificatorPlace(placeCode, actionCode);
	}

	@Override
	public void onTaskCustomerInit(String idTask) {
		BehaviorTaskCustomerImpl subBehavior = instanceSubBehaviour(idTask, CUSTOMER_BEHAVIOR_NAME);
		subBehavior.onInit();
	}

	@Override
	public void onTaskCustomerExpiration(String idTask) {
		BehaviorTaskCustomerImpl subBehavior = instanceSubBehaviour(idTask, CUSTOMER_BEHAVIOR_NAME);
		subBehavior.onExpiration();
	}

	@Override
	public void onTaskCustomerAborted(String idTask) {
		BehaviorTaskCustomerImpl subBehavior = instanceSubBehaviour(idTask, CUSTOMER_BEHAVIOR_NAME);
		subBehavior.onAborted();
	}

	@Override
	public void onTaskCustomerRequestImport(String idTask, String code, Message message) {
		BehaviorTaskCustomerImpl subBehavior = instanceSubBehaviour(idTask, CUSTOMER_BEHAVIOR_NAME);
		CustomerRequestImpl request = new CustomerRequestImpl(message);
		subBehavior.onRequest(request);
		subBehavior.onImportRequest(code, request);
	}

	@Override
	public void onTaskCustomerResponseConstructor(String idTask, String code, Message message) {
		BehaviorTaskCustomerImpl subBehavior = instanceSubBehaviour(idTask, CUSTOMER_BEHAVIOR_NAME);
		CustomerResponseImpl response = new CustomerResponseImpl(message);
		subBehavior.onConstructResponse(code, response);
	}

	@Override
	public void onTaskCustomerChatMessageReceived(String idTask, Message message) {
		BehaviorTaskCustomerImpl subBehavior = instanceSubBehaviour(idTask, CUSTOMER_BEHAVIOR_NAME);
		CustomerResponseImpl response = new CustomerResponseImpl(message);
		subBehavior.onChatMessageReceived(response);
	}

	@Override
	public void onTaskContestantInit(String idTask) {
		BehaviorTaskContestantImpl subBehavior = instanceSubBehaviour(idTask, CONTESTANTS_BEHAVIOR_NAME);
		subBehavior.onInit();
	}

	@Override
	public void onTaskContestantRequestImport(String idTask, String code, Message message) {
		BehaviorTaskContestantImpl subBehavior = instanceSubBehaviour(idTask, CONTESTANTS_BEHAVIOR_NAME);
		ContestantRequestImpl request = new ContestantRequestImpl(message);
		subBehavior.onRequest(request);
		subBehavior.onImportRequest(code, request);
	}

	@Override
	public void onTaskContestantResponseConstructor(String idTask, String code, Message message) {
		BehaviorTaskContestantImpl subBehavior = instanceSubBehaviour(idTask, CONTESTANTS_BEHAVIOR_NAME);
		ContestantResponseImpl response = new ContestantResponseImpl(message);
		subBehavior.onConstructResponse(code, response);
	}

	@Override
	public void onTaskContestInit(String idTask, String collaboratorName) {
		BehaviorTaskContestImpl subBehavior = instanceSubBehaviour(idTask, collaboratorName);
		subBehavior.onInit();
	}

	@Override
	public void onTaskContestTerminate(String idTask, String collaboratorName) {
		BehaviorTaskContestImpl subBehavior = instanceSubBehaviour(idTask, collaboratorName);
		subBehavior.onTerminate();
	}

	@Override
	public void onTaskContestRequestConstructor(String idTask, String collaboratorName, String code, Message message) {
		BehaviorTaskContestImpl subBehavior = instanceSubBehaviour(idTask, collaboratorName);
		TransferRequestImpl request = new TransferRequestImpl(message);
		subBehavior.onConstructRequest(code, request);
	}

	@Override
	public void onTaskContestResponseImport(String idTask, String collaboratorName, String code, Message message) {
		BehaviorTaskContestImpl subBehavior = instanceSubBehaviour(idTask, collaboratorName);
		TransferResponseImpl response = new TransferResponseImpl(message);
		subBehavior.onResponse(response);
		subBehavior.onImportResponse(code, response);
	}

	@Override
	public void onTaskProviderInit(String idTask, String providerKey) {
		BehaviorTaskProviderImpl subBehavior = instanceSubBehaviour(idTask, providerKey);
		subBehavior.onInit();
	}

	@Override
	public void onTaskProviderTerminate(String idTask, String providerKey) {
		BehaviorTaskProviderImpl subBehavior = instanceSubBehaviour(idTask, providerKey);
		subBehavior.onTerminate();
	}

	@Override
	public void onTaskProviderRejected(String idTask, String providerKey) {
		BehaviorTaskProviderImpl subBehavior = instanceSubBehaviour(idTask, providerKey);
		subBehavior.onRejected();
	}

	@Override
	public void onTaskProviderExpiration(String idTask, String providerKey) {
		BehaviorTaskProviderImpl subBehavior = instanceSubBehaviour(idTask, providerKey);
		subBehavior.onExpiration();
	}

	@Override
	public void onTaskProviderRequestConstructor(String idTask, String providerKey, String code, Message message) {
		BehaviorTaskBaseComponent subBehavior = instanceSubBehaviour(idTask, providerKey);
		if (subBehavior instanceof BehaviorTaskProviderInternalImpl) {
			BehaviorTaskProviderInternalImpl internalBehavior = (BehaviorTaskProviderInternalImpl) subBehavior;
			InsourcingRequestImpl request = new InsourcingRequestImpl(message);
			internalBehavior.onConstructRequest(code, request);
		} else if (subBehavior instanceof BehaviorTaskProviderExternalImpl) {
			BehaviorTaskProviderExternalImpl externalBehavior = (BehaviorTaskProviderExternalImpl) subBehavior;
			ProviderRequestImpl request = new ProviderRequestImpl(message);
			externalBehavior.onConstructRequest(code, request);
		}
	}

	@Override
	public void onTaskProviderResponseImport(String idTask, String providerKey, String code, Message message) {
		BehaviorTaskBaseComponent subBehavior = instanceSubBehaviour(idTask, providerKey);
		if (subBehavior instanceof BehaviorTaskProviderInternalImpl) {
			BehaviorTaskProviderInternalImpl internalBehavior = (BehaviorTaskProviderInternalImpl) subBehavior;
			InsourcingResponseImpl response = new InsourcingResponseImpl(message);
			internalBehavior.onResponse(response);
			internalBehavior.onImportResponse(code, response);
		} else if (subBehavior instanceof BehaviorTaskProviderExternalImpl) {
			BehaviorTaskProviderExternalImpl externalBehavior = (BehaviorTaskProviderExternalImpl) subBehavior;
			ProviderResponseImpl response = new ProviderResponseImpl(message);
			externalBehavior.onResponse(response);
			externalBehavior.onImportResponse(code, response);
		}
	}

	@Override
	public void onTaskProviderChatMessageReceived(String idTask, String providerKey, Message message) {
		BehaviorTaskBaseComponent subBehavior = instanceSubBehaviour(idTask, providerKey);

		if (subBehavior instanceof BehaviorTaskProviderInternalImpl) {
			BehaviorTaskProviderInternalImpl internalBehavior = (BehaviorTaskProviderInternalImpl) subBehavior;
			InsourcingResponseImpl response = new InsourcingResponseImpl(message);
			internalBehavior.onChatMessageReceived(response);
		} else if (subBehavior instanceof BehaviorTaskProviderExternalImpl) {
			BehaviorTaskProviderExternalImpl externalBehavior = (BehaviorTaskProviderExternalImpl) subBehavior;
			ProviderResponseImpl response = new ProviderResponseImpl(message);
			externalBehavior.onChatMessageReceived(response);
		}
	}

	@Override
	public void onTaskCreateJobAction(String idTask, String placeCode, String actionCode, Message message) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		JobRequestImpl request = new JobRequestImpl(message);
		behaviour.onCreateJobAction(placeCode, actionCode, request);
	}

	@Override
	public void onTaskCreatedJobAction(String idTask, String idJob, String placeCode, String actionCode) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		Task job = taskLayer.loadTask(idJob);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		JobImpl bpiJob = new JobImpl();
		bpiJob.injectTask(job);

		behaviour.onCreatedJobAction(placeCode, actionCode, bpiJob);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onTaskFinishedJobAction(String idTask, String jobCode, String placeCode, String actionCode, Message message) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		TaskImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		TaskMetadata metadata = message.getMetadata();
		Date startDate = new Date(metadata.getStartDate());
		Date endDate = new Date(metadata.getEndDate());

		org.monet.metamodel.JobDefinition jobDefinition = (org.monet.metamodel.JobDefinition) Dictionary.getInstance().getTaskDefinition(jobCode);
		Class<? extends Schema> schemaClass = (Class<? extends Schema>) ((HasSchema) jobDefinition).getSchemaClass();
		JobResponseImpl request = new JobResponseImpl(message, schemaClass, startDate, endDate);
		behaviour.onFinishedJobAction(placeCode, actionCode, request);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onTaskSensorFinished(String idTask, Message message) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);
		SensorImpl behaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		if (behaviour == null) return;
		behaviour.injectTask(task);

		Date startDate = new Date(task.getInternalStartDate());
		Date endDate = new Date(task.getInternalEndDate());

		Class<? extends Schema> schemaClass = (Class<? extends Schema>) ((HasSchema) task.getDefinition()).getSchemaClass();
		JobResponseImpl request = new JobResponseImpl(message, schemaClass, startDate, endDate);
		behaviour.onFinished(request);
	}

	/* SOURCE EVENTS */
	@Override
	public void onSourcePopulated(Source source) {
		SourceImpl behaviour = this.bpiClassLocator.instantiateBehaviour(source.getDefinition());
		if (behaviour == null) return;
		behaviour.injectSource(source);
		behaviour.onPopulated();
	}

	@Override
	public void onSourceSynchronized(Source source) {
		SourceImpl behaviour = this.bpiClassLocator.instantiateBehaviour(source.getDefinition());
		if (behaviour == null) return;
		behaviour.injectSource(source);
		behaviour.onSynchronized();
	}

	@Override
	public void onSourceTermAdded(Source source, Term term) {
		SourceImpl behaviour = this.bpiClassLocator.instantiateBehaviour(source.getDefinition());
		if (behaviour == null) return;

		behaviour.injectSource(source);

		org.monet.bpi.types.Term bpiTerm = new org.monet.bpi.types.Term();
		bpiTerm.setKey(term.getCode());
		bpiTerm.setLabel(term.getLabel());

		behaviour.onTermAdded(bpiTerm);
	}

	@Override
	public void onSourceTermModified(Source source, Term term) {
		SourceImpl behaviour = this.bpiClassLocator.instantiateBehaviour(source.getDefinition());
		if (behaviour == null) return;

		behaviour.injectSource(source);

		org.monet.bpi.types.Term bpiTerm = new org.monet.bpi.types.Term();
		bpiTerm.setKey(term.getCode());
		bpiTerm.setLabel(term.getLabel());

		behaviour.onTermModified(bpiTerm);
	}

	@Override
	public void onModelEventFired(Event event) {
		Project behaviour = BusinessUnit.getInstance().getBusinessModel().getProject();
		if (behaviour == null) return;

		behaviour.onReceiveEvent(new org.monet.bpi.types.Event(event.getName(), new Date(event.getDueDate()), event.getProperties()));
	}

	public void onImportExecute(String importerKey, Node scope, Reader sourceReader, long sourceSize, ProgressCallback progressCallback) {
		Dictionary dictionary = Dictionary.getInstance();
		ImporterDefinition importerDefinition = dictionary.getImporterDefinition(importerKey);
		ImporterImpl behaviour = this.bpiClassLocator.instantiateBehaviour(importerDefinition);
		String targetDefinitionKey = importerDefinition.getTarget().getValue();
		Class<?> schemaClass = (Class) this.bpiClassLocator.getSchemaClass(dictionary.getDefinition(targetDefinitionKey));

		if (schemaClass == null)
			throw new RuntimeException("BPIEventManagerImpl.onImportExecute: Schema class not found");

		ImportIterator<Schema> iterator = new ImportIterator<Schema>(schemaClass, sourceReader, sourceSize, "schema", progressCallback);

		if (scope != null) {
			NodeImpl bpiScope = this.bpiClassLocator.instantiateBehaviour(scope.getDefinition());
			bpiScope.injectNode(scope);
			behaviour.injectScope(bpiScope);
		}

		behaviour.onInitialize();
		for (Schema schema : new ImportIterable<Schema>(iterator)) {
			try {
				behaviour.onImportItem(schema);
			} catch (Throwable ex) {
				throw new RuntimeException("Error on importing items: " + ex.getMessage(), ex);
			}
			finally {
				iterator.onItemImported();
			}
		}
		iterator.onComplete();
	}

	private Attribute getNodeAttribute(Node node, String code) {
		LinkedHashSet<Attribute> result = node.getAttributeList().searchByCode(code);
		return (result.size() > 0) ? result.iterator().next() : null;
	}

	@SuppressWarnings("unchecked")
	private <T extends BehaviorTaskBaseComponent> T instanceSubBehaviour(String idTask, String providerKey) {
		TaskLayer taskLayer = ComponentPersistence.getInstance().getTaskLayer();
		Task task = taskLayer.loadTask(idTask);

		TaskImpl taskBehaviour = this.bpiClassLocator.instantiateBehaviour(task.getDefinition());
		taskBehaviour.injectTask(task);

		BehaviorTaskBaseComponent subBehavior = this.bpiClassLocator.instantiateBehaviour(((ProcessDefinition) task.getDefinition()).getSubBehaviorClass(providerKey));
		subBehavior.injectTask(taskBehaviour);
		return (T) subBehavior;
	}

	private void buildNodeAsDimensionComponent(NodeImpl nodeImpl) {

		if (nodeImpl.node != null && nodeImpl.node.isPrototype())
			return;

		List<DatastoreBuilderDefinition> builderDefinitions = Dictionary.getInstance().getDatastoreBuildersForNode(nodeImpl.node.getDefinition().getName());
		for (DatastoreBuilderDefinition builder : builderDefinitions) {
			DatastoreBuilderImpl bpiBuilder = this.bpiClassLocator.instantiateBehaviour(builder);
			bpiBuilder.onBuild(nodeImpl);
		}
	}

}
