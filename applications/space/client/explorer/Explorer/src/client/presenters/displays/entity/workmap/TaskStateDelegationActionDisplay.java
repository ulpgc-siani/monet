package client.presenters.displays.entity.workmap;

import client.core.model.List;
import client.core.model.Node;
import client.core.model.Role;
import client.core.model.Task;
import client.core.model.definition.entity.ActivityDefinition;
import client.core.model.definition.entity.ProviderDefinition;
import client.core.model.workmap.DelegationAction;
import client.core.model.workmap.DelegationAction.Message;
import client.core.model.workmap.DelegationAction.Step;
import client.core.model.workmap.Place;
import client.core.model.workmap.WorkMap;
import client.presenters.displays.view.ViewDisplay;
import client.presenters.operations.SelectTaskDelegationRoleOperation;
import client.presenters.operations.SetupTaskDelegationOrderOperation;
import client.services.NodeService;
import client.services.TranslatorService;
import client.services.callback.NodeCallback;
import client.services.callback.RoleListCallback;
import cosmos.presenters.Operation;
import cosmos.presenters.Presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class TaskStateDelegationActionDisplay extends TaskStateActionDisplay<DelegationAction> {
	private SetupTaskDelegationOrderOperation setupOrderOperation;
	private List<Role> roleList = null;
	private Node orderNode = null;

	public static final Type TYPE = new Type("TaskStateDelegationActionDisplay", TaskStateActionDisplay.TYPE);

	public TaskStateDelegationActionDisplay(Task task, DelegationAction action) {
        super(task, action);
	}

	@Override
	protected void onInjectServices() {
		addSetupOperation();
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void load() {
		Step step = getStep();

		setupOrderOperation.setVisible(step == Step.SETUP_ORDER);

		if (step == Step.SETUP_ROLE)
			loadRoleList();
		else if (step == Step.SETUP_ORDER)
			loadOrderNode();
		else
			notifyStep(step);
	}

	public Step getStep() {
		return getAction().getStep();
	}

	public String getMessage() {
		TranslatorService translator = services.getTranslatorService();
		Place<DelegationAction> place = getDelegationPlace();
		DelegationAction action = place.getAction();
		ActivityDefinition taskDefinition = (ActivityDefinition) getTask().getDefinition();
		ProviderDefinition providerDefinition = taskDefinition.getProvider(action.getDefinition().getProvider().getValue());
		String roleTypeLabel = "";

		if (providerDefinition != null && providerDefinition.getRole() != null)
			roleTypeLabel = services.getSpaceService().load().getDictionary().getDefinitionLabel(providerDefinition.getRole().getValue());

		return translator.getTaskDelegationMessage(calculateMessage(), action.getFailureDate(), roleTypeLabel);
	}

	public Message calculateMessage() {
		Task task = getTask();
		Place<DelegationAction> place = task.getWorkMap().getPlace();
		DelegationAction action = place.getAction();

		if (task.isPending()) {
			Step step = action.getStep();

			if (step == Step.SETUP_ROLE)
				return roleList != null && roleList.size() > 0 ? Message.SETUP_ROLE : Message.NO_ROLES;
			if (step == Step.SETUP_ORDER)
				return orderNode!=null?Message.SETUP_ORDER:Message.SENDING;
		}
		else if (task.isWaiting())
			return Message.SENDING;
		else if (task.isFailure()) {
			Role role = action.getRole();

			if (role == null)
				return Message.SENDING_FAILURE;

			if (role.isForUser())
				return Message.FAILURE_INTERNAL;
			else if (role.isForService())
				return Message.FAILURE_EXTERNAL;
		}

		return null;
	}

	public void loadRoleList() {
		services.getTaskService().loadDelegationRoles(getTask(), new RoleListCallback() {

			@Override
			public void success(List<Role> roleList) {
				setRoleList(roleList);
			}

			@Override
			public void failure(String error) {
				notifyRolesFailure(error);
			}
		});
	}

	public void selectRole(final Role role) {
		SelectTaskDelegationRoleOperation operation = new SelectTaskDelegationRoleOperation(new Operation.Context() {
			@Override
			public Presenter getCanvas() {
				return TaskStateDelegationActionDisplay.this.getOwner();
			}

			@Override
			public Operation getReferral() {
				return getVisitingDisplayOperation();
			}
		}, getTask(), role);

		operation.inject(services);
		operation.execute();
	}

	public void loadOrderNode() {
		DelegationAction delegationAction = getAction();
		NodeService service = services.getNodeService();

		service.open(delegationAction.getOrderNode().getId(), new NodeCallback() {
			@Override
			public void success(Node orderNode) {
				setOrderNode(orderNode);
			}

			@Override
			public void failure(String error) {
				notifyOrderFailure(error);
			}
		});
	}

	private void addSetupOperation() {
		setupOrderOperation = new SetupTaskDelegationOrderOperation(new Operation.Context() {
			@Override
			public Presenter getCanvas() {
				return TaskStateDelegationActionDisplay.this.getOwner();
			}

			@Override
			public Operation getReferral() {
				return getVisitingDisplayOperation();
			}
		}, getTask());
		setupOrderOperation.setVisible(getStep() == Step.SETUP_ORDER);
		setupOrderOperation.inject(services);
		addChild(setupOrderOperation);
	}

	private void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
		notifyRoles(roleList);
	}

	private void setOrderNode(Node orderNode) {
		this.orderNode = orderNode;
		ViewDisplay display = new ViewDisplay.Builder().build(orderNode, orderNode.getViews().getDefaultView());
		notifyOrder(display);
	}

	private Place getDelegationPlace() {
		WorkMap workMap = getTask().getWorkMap();
		return workMap.getPlace();
	}

	private void notifyStep(final Step step) {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.step(step);
			}
		});
	}

	private void notifyRoles(final List<Role> roleList) {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.roles(roleList);
			}
		});
	}

	private void notifyRolesFailure(final String error) {
		Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not load task delegation role list " + getTask().getId());
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.rolesFailure(error);
			}
		});
	}

	private void notifyRole(final Role role) {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.role(role);
			}
		});
	}

	private void notifyRoleFailure(final String error) {
		Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not set delegation role " + getTask().getId());
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.roleFailure(error);
			}
		});
	}

	private void notifyOrder(final ViewDisplay display) {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.order(display);
			}
		});
	}

	private void notifyOrderFailure(final String error) {
		Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not load task delegation order node " + getTask().getId());
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.orderFailure(error);
			}
		});
	}

	public interface Hook extends TaskStateActionDisplay.Hook {
		void step(Step step);
		void roles(List<Role> roleList);
		void rolesFailure(String error);
		void role(Role role);
		void roleFailure(String error);
		void order(ViewDisplay display);
		void orderFailure(String error);
	}

	public static class Builder extends TaskStateActionDisplay.Builder<DelegationAction> {

		protected static void register() {
			registerBuilder(DelegationAction.CLASS_NAME.toString(), new Builder());
		}

		@Override
		public TaskStateActionDisplay build(Task entity, DelegationAction action) {
			return new TaskStateDelegationActionDisplay(entity, action);
		}
	}

}
