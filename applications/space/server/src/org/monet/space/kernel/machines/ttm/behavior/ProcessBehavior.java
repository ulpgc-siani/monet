package org.monet.space.kernel.machines.ttm.behavior;

import com.google.inject.Inject;
import org.monet.bpi.java.FieldBooleanImpl;
import org.monet.bpi.java.FieldDateImpl;
import org.monet.bpi.java.FieldMemoImpl;
import org.monet.metamodel.*;
import org.monet.metamodel.DoorActionPropertyBase.ExitProperty;
import org.monet.metamodel.DoorActionPropertyBase.TimeoutProperty;
import org.monet.metamodel.FormDefinitionBase.FormViewProperty;
import org.monet.metamodel.LineActionPropertyBase.LineStopProperty;
import org.monet.metamodel.ServiceDefinition.CustomerProperty;
import org.monet.metamodel.ServiceDefinitionBase.CustomerPropertyBase.AbortedProperty;
import org.monet.metamodel.ServiceDefinitionBase.CustomerPropertyBase.CustomerRequestProperty;
import org.monet.metamodel.internal.Lock;
import org.monet.metamodel.internal.Ref;
import org.monet.metamodel.internal.TaskOrderDefinition;
import org.monet.metamodel.internal.Time;
import org.monet.space.kernel.agents.AgentNotifier;
import org.monet.space.kernel.constants.TaskState;
import org.monet.space.kernel.machines.ttm.CourierService;
import org.monet.space.kernel.machines.ttm.TimerService;
import org.monet.space.kernel.machines.ttm.model.*;
import org.monet.space.kernel.machines.ttm.model.Process;
import org.monet.space.kernel.machines.ttm.model.ValidationResult;
import org.monet.space.kernel.machines.ttm.persistence.HistoryLog;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceHandler;
import org.monet.space.kernel.machines.ttm.persistence.PersistenceService;
import org.monet.space.kernel.machines.ttm.persistence.SnapshotStack;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.threads.MonetSystemThread;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class ProcessBehavior extends Behavior implements PersistenceHandler {

	@Inject
	private com.google.inject.Provider<CustomerBehavior> customerFactory;
	@Inject
	private com.google.inject.Provider<ContestantsBehavior> contestantsFactory;
	@Inject
	private com.google.inject.Provider<ProviderBehavior> providerFactory;
	@Inject
	private com.google.inject.Provider<ContestBehavior> contestFactory;
	@Inject
	private PersistenceService persistenceService;
	@Inject
	private TimerService timerService;
	@Inject
	private AgentNotifier agentNotifier;

	@Inject
	private SnapshotStack snapshots;
	@Inject
	private HistoryLog history;
	@Inject
	private CourierService courierService;

	private Process model;

	private CustomerBehavior customer;
	private ContestantsBehavior contestants;
	private HashMap<String, ProviderBehavior> providers = new HashMap<String, ProviderBehavior>();
	private HashMap<String, ContestBehavior> contests = new HashMap<String, ContestBehavior>();

	public void injectModel(Process model) {
		this.model = model;

		String taskId = this.model.getId();
		ProcessDefinition definition = this.model.getDefinition();

		if (definition instanceof ServiceDefinition) {
			ServiceDefinition serviceDefinition = (ServiceDefinition) definition;
			this.customer = this.customerFactory.get();
			this.customer.inject(taskId, serviceDefinition.getCustomer(), this.model.getCustomer(), this, this.persistenceService);
		} else if (definition instanceof ActivityDefinition) {
			ActivityDefinition activityDefinition = (ActivityDefinition) definition;
// CONTESTANTS
//			if (activityDefinition.getContestants() != null) {
//				this.contestants = this.contestantsFactory.get();
//				this.contestants.inject(taskId, activityDefinition.getContestants(), this.model.getContestants(), this, this.persistenceService);
//			}
		}

		Map<String, TaskProviderProperty> providerDeclarationMap = definition.getTaskProviderPropertyMap();
		for (Entry<String, Provider> entry : this.model.getProviders().entrySet()) {
			ProviderBehavior behavior = this.providerFactory.get();
			behavior.inject(taskId, providerDeclarationMap.get(entry.getKey()), entry.getValue(), this, this.persistenceService);
			this.providers.put(entry.getKey(), behavior);
		}

		Map<String, TaskContestProperty> contestDeclarationMap = definition.getTaskContestPropertyMap();
		for (Entry<String, Contest> entry : this.model.getContests().entrySet()) {
			ContestBehavior behavior = this.contestFactory.get();
			behavior.inject(taskId, contestDeclarationMap.get(entry.getKey()), entry.getValue(), this, this.persistenceService);
			this.contests.put(entry.getKey(), behavior);
		}

		this.snapshots.init(model.getId());
		this.history.init(model.getId());
	}

	public synchronized void deliver(MailBox mailBox, Message message) {
		if (this.isFinished())
			throw new RuntimeException("Invalid operation: Task is finished.");

		Ref _goto = null;
		switch (mailBox.getType()) {
			case CUSTOMER:
				customer.process(message);
				if (!message.isChat()) {
					CustomerProperty customerProperty = ((ServiceDefinition) this.model.getDefinition()).getCustomer();
					CustomerRequestProperty requestProperty = customerProperty.getRequestByCode(message.getSubject());
					if (requestProperty != null)
						_goto = requestProperty.getGoto();
				}
				break;
			case EXTERNAL_PROVIDER:
			case INTERNAL_PROVIDER:
				ProviderBehavior provider = this.providers.get(mailBox.getCode());
				if (provider != null)
					provider.process(message);
				else
					throw new RuntimeException(String.format("No provider with code %s found in task with id %s", mailBox.getCode(), this.model.getId()));
				break;
			case CONTEST:
				ContestBehavior collaborator = this.contests.get(mailBox.getCode());
				if (collaborator != null)
					collaborator.process(message);
				else
					throw new RuntimeException(String.format("No collaborator with code %s found in task with id %s", mailBox.getCode(), this.model.getId()));
				break;
			case CONTESTANT:
//				contestants.process(message);
//				if (!message.isChat()) {
//					ContestantsProperty contestantsProperty = ((ActivityDefinition) this.model.getDefinition()).getContestants();
//					ContestantRequestProperty requestProperty = contestantsProperty.getRequestByCode(message.getSubject());
//					if (requestProperty != null)
//						_goto = requestProperty.getGoto();
//				}
				break;
		}

		if (_goto != null)
			this.gotoPlace(_goto.getValue(), null);

		this.resumeAsync();
	}

	public synchronized void signaling(MailBox mailBox, Signaling signal) {
		if (this.isFinished() && signal != Signaling.TERMINATE)
			throw new RuntimeException("Invalid operation: Task is finished.");

		switch (mailBox.getType()) {
			case CUSTOMER:
				switch (signal) {
					case ABORT:
						String currentPlace = this.model.getPlace();
						this.customer.abort();
						if (currentPlace.equals(this.model.getPlace())) {
							AbortedProperty abortedProperty = ((ServiceDefinition) this.model.getDefinition()).getCustomer().getAborted();
							if (abortedProperty.getGoto() != null)
								this.gotoPlace(abortedProperty.getGoto().getValue(), null);
						}
						break;
					default:
						return;
				}
				break;
			case EXTERNAL_PROVIDER:
			case INTERNAL_PROVIDER:
				ProviderBehavior provider = this.providers.get(mailBox.getCode());
				if (provider != null)
					switch (signal) {
						case TERMINATE:
							provider.terminate();
							break;
						case REJECT:
							provider.reject();
							break;
						default:
							return;
					}
				else
					throw new RuntimeException(String.format("No provider with code %s found in task with id %s", mailBox.getCode(), this.model.getId()));
				break;
			case CONTEST:
				ContestBehavior collaborator = this.contests.get(mailBox.getCode());
				if (collaborator != null)
					switch (signal) {
						case TERMINATE:
							collaborator.terminate();
							break;
						default:
							return;
					}
				else
					throw new RuntimeException(String.format("No collaborator with code %s found in task with id %s", mailBox.getCode(), this.model.getId()));
				break;
			case CONTESTANT:
				break;
		}

		this.resumeAsync();
	}

	public synchronized void init() {
		this.model.setPlace(this.model.getDefinition().getInitialPlace().getCode());

		this.save();
	}

	public synchronized void resumeAsync() {
		MonetSystemThread thread = new MonetSystemThread(new Runnable() {
			public void run() {
				resume();
			};
		});
		thread.start();
	}

	public synchronized void resume() {
		if (!this.model.isInitialized()) {
			this.saveSnapshot();

			this.agentNotifier.notify(new MonetEvent(MonetEvent.TASK_INITIALIZED, null, this.model.getId()));

			this.model.setInitialized(true);
			this.save();
		}

		while (!this.isLocked() && !this.isFinished()) {
			boolean stop = step();

			PlaceProperty placeProperty = this.model.getPlaceProperty();
			if (placeProperty.isOust() || placeProperty.isFinal()) {
				this.model.setFinished(true);
				this.agentNotifier.notify(new MonetEvent(placeProperty.isOust() ? MonetEvent.TASK_ABORTED : MonetEvent.TASK_TERMINATED, null, this.model.getId()));

				if (this.customer != null) {
                    if (this.customer.getOrderId() == null)
                        throw new RuntimeException("Customer not defined correctly or perhaps you declared a service instead of activity.");
					this.courierService.signaling(this.customer.getOrderId(), this.customer.getClientMailBox(), Signaling.TERMINATE);
                }
			}

			if (stop)
				break;
		}

		this.persistenceService.save(this.model);
		this.updateTaskState();
	}

	private void updateTaskState() {
		String newState = TaskState.PENDING;

		PlaceProperty placeProperty = this.model.getPlaceProperty();
		if (placeProperty.getDelegationActionProperty() != null) {
			String providerName = placeProperty.getDelegationActionProperty().getProvider().getValue();
			TaskProviderProperty providerDefinition = this.model.getDefinition().getTaskProviderPropertyMap().get(providerName);
			Provider provider = this.model.getProviders().get(providerDefinition.getCode());

			if (provider.hasFailures())
				newState = TaskState.FAILURE;
			else {
				TaskOrder order = provider.getOrderId() != null ? this.persistenceService.loadTaskOrder(provider.getOrderId()) : null;

				if (order != null && order.getSetupNodeId() == null)
					newState = TaskState.WAITING;
                else if (order != null && order.getSetupNodeId() != null)
                    newState = TaskState.PENDING;
			}
		} else if (placeProperty.getSendRequestActionProperty() != null) {
			SendRequestActionProperty actionProperty = placeProperty.getSendRequestActionProperty();

			if (actionProperty.getCollaborator() != null) {
				TaskContestProperty taskContestDefinition = this.model.getDefinition().getTaskContestPropertyMap().get(actionProperty.getCollaborator().getValue());
				Contest contest = this.model.getContests().get(taskContestDefinition.getCode());
				if (contest.hasFailures())
					newState = TaskState.FAILURE;
			} else if (actionProperty.getProvider() != null) {
				TaskProviderProperty taskProviderDefinition = this.model.getDefinition().getTaskProviderPropertyMap().get(actionProperty.getProvider().getValue());
				Provider provider = this.model.getProviders().get(taskProviderDefinition.getCode());
				if (provider.hasFailures())
					newState = TaskState.FAILURE;
			}
		} else if (placeProperty.getSendResponseActionProperty() != null) {
			if (this.model.getCustomer().hasFailures())
				newState = TaskState.FAILURE;
		} else if (this.isLocked()) {
			if ((placeProperty.getWaitActionProperty() != null && this.isTimerActive(placeProperty.getCode())) || (placeProperty.getDoorActionProperty() != null))
				newState = TaskState.WAITING;
		} else if (isFinished()) {
			if (placeProperty.isOust())
				newState = TaskState.ABORTED;
			else
				newState = TaskState.FINISHED;
		}

		this.persistenceService.updateTaskState(this.model, newState);
	}

	private boolean isFinished() {
		return this.model.isFinished();
	}

	private boolean step() {
		PlaceProperty placeProperty = this.model.getPlaceProperty();

		notifyPlaceArrival(placeProperty);

		if (placeProperty.getDelegationActionProperty() != null) {
			doDelegationAction(placeProperty, placeProperty.getDelegationActionProperty());
		} else if (placeProperty.getDoorActionProperty() != null) {
			doDoorAction(placeProperty, placeProperty.getDoorActionProperty());
		} else if (placeProperty.getEditionActionProperty() != null) {
			doEditionAction(placeProperty, placeProperty.getEditionActionProperty());
		} else if (placeProperty.getLineActionProperty() != null) {
			doLineAction(placeProperty, placeProperty.getLineActionProperty());
		} else if (placeProperty.getSendRequestActionProperty() != null) {
			doSendRequestAction(placeProperty.getSendRequestActionProperty());
		} else if (placeProperty.getSendResponseActionProperty() != null) {
			doSendResponseAction(placeProperty.getSendResponseActionProperty());
		} else if (placeProperty.getEnrollActionProperty() != null) {
			doEnrollAction(placeProperty.getEnrollActionProperty());
		} else if (placeProperty.getWaitActionProperty() != null) {
			doWaitAction(placeProperty, placeProperty.getWaitActionProperty());
		} else if (placeProperty.getSendJobActionProperty() != null) {
			doSendJobAction(placeProperty, placeProperty.getSendJobActionProperty());
		} else {
			return this.model.getPlace().equals(placeProperty.getCode());
		}

		return false;
	}

	private void doDelegationAction(PlaceProperty placeProperty, DelegationActionProperty actionProperty) {
		TaskProviderProperty providerDefinition = this.model.getDefinition().getTaskProviderPropertyMap().get(actionProperty.getProvider().getValue());
		Role.Nature nature;

		if (providerDefinition.getExternal() != null && providerDefinition.getInternal() != null)
			nature = Role.Nature.Both;
		else if (providerDefinition.getExternal() != null)
			nature = Role.Nature.External;
		else
			nature = Role.Nature.Internal;

		Role choosedRole = null;
		RoleList roleList = this.persistenceService.loadNonExpiredRoleList(providerDefinition.getRole().getValue(), nature);
		if (roleList.getTotalCount() == 1) {
			choosedRole = roleList.iterator().next();
		} else {
			RoleChooser chooser = new RoleChooser(roleList);

			MonetEvent event = new MonetEvent(MonetEvent.TASK_SELECT_DELEGATION_ROLE, null, this.model.getId());
			event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
			event.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
			event.addParameter(MonetEvent.PARAMETER_ROLE_CHOOSER, chooser);
			this.agentNotifier.notify(event);

			choosedRole = chooser.getChoosedRole();
		}

		this.lock(new Lock(placeProperty.getCode(), placeProperty.getCode()));

		this.saveSnapshot();

		if (choosedRole != null)
			this.selectDelegationActionRole(choosedRole);
	}

	private void doDoorAction(PlaceProperty placeProperty, DoorActionProperty actionProperty) {
		TimeoutProperty timeoutProperty = actionProperty.getTimeout();
		if (timeoutProperty != null)
			this.setupTimer(placeProperty.getCode(), new Date(), timeout(placeProperty, actionProperty));

		ArrayList<Lock> locks = new ArrayList<Lock>();
		for (ExitProperty exit : actionProperty.getExitList())
			locks.add(new Lock(placeProperty.getCode(), exit.getCode()));
		doMultiLock(locks.toArray(new Lock[]{}));

		this.saveSnapshot();
	}

	private Time timeout(PlaceProperty placeProperty, DoorActionProperty actionProperty) {
		return timeout(placeProperty.getCode(), actionProperty.getCode(), actionProperty.getTimeout().getAfter());
	}

	private Time timeout(PlaceProperty placeProperty, EditionActionProperty actionProperty) {
		return timeout(placeProperty.getCode(), actionProperty.getCode(), actionProperty.getTimeout().getAfter());
	}

	private Time timeout(PlaceProperty placeProperty, LineActionPropertyBase actionProperty) {
		return timeout(placeProperty.getCode(), actionProperty.getCode(), actionProperty.getTimeout().getAfter());
	}

	private Time timeout(String placeCode, String actionCode, Time after) {
		TimeoutSetup setup = new TimeoutSetup(after);

		MonetEvent event = new MonetEvent(MonetEvent.TASK_SETUP_TIMEOUT, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeCode);
		event.addParameter(MonetEvent.PARAMETER_ACTION, actionCode);
		event.addParameter(MonetEvent.PARAMETER_TIMEOUT_SETUP, setup);
		this.agentNotifier.notify(event);

		return setup.getTimeAfter();
	}


	private void doEditionAction(PlaceProperty placeProperty, EditionActionProperty actionProperty) {
		EditionActionProperty.TimeoutProperty timeoutProperty = actionProperty.getTimeout();
		if (timeoutProperty != null)
			this.setupTimer(placeProperty.getCode(), new Date(), timeout(placeProperty, actionProperty));

		this.saveSnapshot();

		Node form = this.persistenceService.createForm(this.model.getId(), actionProperty.getUse().getForm().getValue());
		this.model.setEditionFormId(form.getId());

		MonetEvent event = new MonetEvent(MonetEvent.TASK_SETUP_EDITION, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_FORM, form);
		this.agentNotifier.notify(event);

		this.persistenceService.saveForm(form);

		this.lock(new Lock(placeProperty.getCode(), placeProperty.getCode()));
	}

	private void doLineAction(PlaceProperty placeProperty, LineActionProperty actionProperty) {
		org.monet.metamodel.LineActionProperty.TimeoutProperty timeoutProperty = actionProperty.getTimeout();
		if (timeoutProperty != null)
			this.setupTimer(placeProperty.getCode(), new Date(), timeout(placeProperty, actionProperty));

		ArrayList<Lock> locks = new ArrayList<Lock>();
		for (LineStopProperty line : actionProperty.getStopList())
			locks.add(new Lock(placeProperty.getCode(), line.getCode()));
		doMultiLock(locks.toArray(new Lock[]{}));

		this.saveSnapshot();
	}

	private void doSendRequestAction(SendRequestActionProperty actionProperty) {

		try {
			if (actionProperty.getCollaborator() != null) {
				TaskContestProperty taskContestDefinition = this.model.getDefinition().getTaskContestPropertyMap().get(actionProperty.getCollaborator().getValue());
				ContestBehavior collaborator = this.contests.get(taskContestDefinition.getCode());
				collaborator.send(actionProperty.getRequest().getValue());
			} else if (actionProperty.getProvider() != null) {
				TaskProviderProperty taskProviderDefinition = this.model.getDefinition().getTaskProviderPropertyMap().get(actionProperty.getProvider().getValue());
				ProviderBehavior provider = this.providers.get(taskProviderDefinition.getCode());
				provider.send(actionProperty.getRequest().getValue());
			} else {
				throw new RuntimeException("No collaborator or provider defined");
			}
		}
		catch (Throwable exception) {
			this.updateTaskState();
			throw new RuntimeException("Could not prepare request. Check both your send request bpi code is correct and monet services are ok!", exception);
		}

		this.gotoPlace(actionProperty.getGoto().getValue(), actionProperty.getHistory());
		notifyPlaceArrival(actionProperty.getGoto().getValue());
	}

	private void doSendResponseAction(SendResponseActionProperty actionProperty) {
        this.customer.send(actionProperty.getResponse().getValue());

		this.gotoPlace(actionProperty.getGoto().getValue(), actionProperty.getHistory());
		notifyPlaceArrival(actionProperty.getGoto().getValue());
	}

	private void notifyPlaceArrival(PlaceProperty place) {
		notifyPlaceArrival(place.getCode());
	}

	private void notifyPlaceArrival(String key) {
		PlaceProperty placeDefinition = this.model.getDefinition().getPlace(key);
		MonetEvent event = new MonetEvent(MonetEvent.TASK_PLACE_ARRIVAL, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeDefinition.getCode());
		this.agentNotifier.notify(event);
	}

	private void doEnrollAction(EnrollActionProperty actionProperty) {
		// CONTESTANTS
//		ContestBehavior contest = this.contests.get(actionProperty.getCode());
//		if (contest == null) {
//			contest = this.contestFactory.get();
//			this.contests.put(actionProperty.getCode(), contest);
//		}
//		contest.initialize(this.model.getId(), this.model.getDefinition().getTaskContestPropertyMap().get(actionProperty.getContest().getValue()), null);
//
//		this.gotoPlace(actionProperty.getGoto().getValue(), actionProperty.getHistory());
//
//		this.saveSnapshot();
	}

	private void doWaitAction(PlaceProperty placeProperty, WaitActionProperty actionProperty) {
		this.lock(new Lock(placeProperty.getCode(), placeProperty.getCode()));

        WaitSetup waitSetup = new WaitSetup(this.getTimerDue(placeProperty.getCode()));

        MonetEvent event = new MonetEvent(MonetEvent.TASK_SETUP_WAIT, null, this.model.getId());
        event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
        event.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
        event.addParameter(MonetEvent.PARAMETER_WAIT_SETUP, waitSetup);
        this.agentNotifier.notify(event);

		if (waitSetup.isCanceled())
			this.setupWaitAction(new Date(waitSetup.getTimerDue()), new Time(0));
		else {
	        Time timeAfter = waitSetup.getTimeAfter();
	        if (timeAfter != null)
	            this.setupWaitAction(new Date(waitSetup.getTimerDue()), timeAfter);
		}

		this.saveSnapshot();
	}

	private void doSendJobAction(PlaceProperty placeProperty, SendJobActionProperty actionProperty) {
		Role choosedRole = null;
		RoleList roleList = this.persistenceService.loadNonExpiredRoleList(actionProperty.getRole().getValue(), Role.Nature.Internal);
		Boolean isNoneChoosed = false;

		if (roleList.getTotalCount() == 1) {
			choosedRole = roleList.iterator().next();
		} else {
			RoleChooser chooser = new RoleChooser(roleList);

			MonetEvent event = new MonetEvent(MonetEvent.TASK_SELECT_JOB_ROLE, null, this.model.getId());
			event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
			event.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
			event.addParameter(MonetEvent.PARAMETER_ROLE_CHOOSER, chooser);
			this.agentNotifier.notify(event);

			choosedRole = chooser.getChoosedRole();

			event = new MonetEvent(MonetEvent.TASK_SELECT_JOB_ROLE_COMPLETE, null, this.model.getId());
			event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
			event.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
			event.addParameter(MonetEvent.PARAMETER_ROLE, choosedRole);
			this.agentNotifier.notify(event);

			if (choosedRole == null)
				isNoneChoosed = chooser.isNoneChoosed();
		}

		this.lock(new Lock(placeProperty.getCode(), placeProperty.getCode()));

		this.saveSnapshot();

		if (choosedRole != null)
			this.selectSendJobActionRole(choosedRole);
		else if (isNoneChoosed)
			this.selectSendJobActionRole(null);
	}

	private void saveSnapshot() {
		this.snapshots.push(Snapshot.from(this.model));
	}

	private void gotoPlace(String newPlace, Object historyFact) {
		PlaceProperty placeDefinition = this.model.getDefinition().getPlace(newPlace);

		this.timerService.cancel(this.model.getId(), this.model.getPlace());
		this.model.setPlace(placeDefinition.getCode());

		if (historyFact != null)
			this.history.add(Language.getInstance().getModelResource(historyFact));
	}

	private void doUnlock(Lock lock) {
		if (this.model.getPlace().equals(lock.getPlace())) {
			PlaceProperty placeProperty = this.model.getPlaceProperty();
			if (placeProperty.getDoorActionProperty() != null) {
				ExitProperty unlockedExit = placeProperty.getDoorActionProperty().getExitMap().get(lock.getId());
				if (unlockedExit != null) {
					this.taskActionPlaceTook(placeProperty, placeProperty.getDoorActionProperty(), unlockedExit.getCode());
					this.taskActionSolved(placeProperty, placeProperty.getDoorActionProperty());
					this.gotoPlace(unlockedExit.getGoto().getValue(), unlockedExit.getHistory());
				}
				ArrayList<Lock> locks = new ArrayList<Lock>();
				for (ExitProperty exit : placeProperty.getDoorActionProperty().getExitList())
					if (!exit.getCode().equals(lock.getId()))
						locks.add(new Lock(placeProperty.getCode(), exit.getCode()));
				doResetLocks(locks.toArray(new Lock[]{}));
			} else if (placeProperty.getEditionActionProperty() != null) {
				EditionActionProperty editionPlaceAction = placeProperty.getEditionActionProperty();
				this.gotoPlace(editionPlaceAction.getGoto().getValue(), null);
			} else if (placeProperty.getLineActionProperty() != null) {
				LineStopProperty unlockedStop = placeProperty.getLineActionProperty().getStopMap().get(lock.getId());
				if (unlockedStop != null) {
					this.taskActionPlaceTook(placeProperty, placeProperty.getLineActionProperty(), unlockedStop.getCode());
					this.taskActionSolved(placeProperty, placeProperty.getLineActionProperty());
					this.gotoPlace(unlockedStop.getGoto().getValue(), unlockedStop.getHistory());
				}
				ArrayList<Lock> locks = new ArrayList<Lock>();
				for (LineStopProperty stop : placeProperty.getLineActionProperty().getStopList())
					if (!stop.getCode().equals(lock.getId()))
						locks.add(new Lock(placeProperty.getCode(), stop.getCode()));
				doResetLocks(locks.toArray(new Lock[]{}));
			} else if (placeProperty.getWaitActionProperty() != null) {
				WaitActionProperty waitPlaceAction = placeProperty.getWaitActionProperty();
				this.taskActionSolved(placeProperty, placeProperty.getWaitActionProperty());
				this.gotoPlace(waitPlaceAction.getGoto().getValue(), waitPlaceAction.getHistory());
			}
		}
	}

	private void doResetLocks(Lock[] locks) {
		HashMap<String, Integer> lockStates = this.model.getLockStates();
		for (Lock lock : locks) {
			lockStates.put(lock.getPlace() + "$" + lock.getId(), 0);
		}
	}

	private void doMultiLock(Lock[] locks) {
		Lock unlockedLock = null;
		HashMap<String, Integer> lockStates = this.model.getLockStates();
		for (Lock lock : locks) {
			String lockName = lock.getPlace() + "$" + lock.getId();
			Integer state = lockStates.get(lockName);
			if (state == null)
				state = 0;
			lockStates.put(lockName, ++state);

			if (state < 1 && unlockedLock == null)
				unlockedLock = lock;
		}

		if (unlockedLock != null)
			this.doUnlock(unlockedLock);
	}

	private void doLock(Lock lock) {
		HashMap<String, Integer> lockStates = this.model.getLockStates();
		String lockName = lock.getPlace() + "$" + lock.getId();
		Integer state = lockStates.get(lockName);
		if (state == null)
			state = 0;
		lockStates.put(lockName, ++state);

		if (state < 1)
			this.doUnlock(lock);
	}

	private void setupTimer(String tag, Date dueDate, Time timeAfter) {
		this.timerService.schedule(this.model.getId(), tag, dueDate, timeAfter);
	}

	private boolean isTimerActive(String tag) {
		return this.timerService.isActive(this.model.getId(), tag);
	}

	public long getTimerDue(String name) {
		return this.timerService.getTimerDue(this.model.getId(), name);
	}

	private void taskActionSolved(PlaceProperty placeProperty, PlaceActionProperty actionProperty) {
		MonetEvent event = new MonetEvent(MonetEvent.TASK_ACTION_SOLVED, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
		this.agentNotifier.notify(event);
	}

	private void taskActionPlaceTook(PlaceProperty placeProperty, PlaceActionProperty actionProperty, String routeCode) {
		MonetEvent event = new MonetEvent(MonetEvent.TASK_PLACE_TOOK, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_ROUTE, routeCode);
		this.agentNotifier.notify(event);
	}

	public synchronized boolean isLocked() {
		PlaceProperty placeProperty = this.model.getPlaceProperty();
		if (placeProperty.getDoorActionProperty() != null) {
			for (ExitProperty exit : placeProperty.getDoorActionProperty().getExitList())
				if (isUnlocked(placeProperty.getCode() + "$" + exit.getCode()))
					return false;
		} else if (placeProperty.getLineActionProperty() != null) {
			for (LineStopProperty stop : placeProperty.getLineActionProperty().getStopList())
				if (isUnlocked(placeProperty.getCode() + "$" + stop.getCode()))
					return false;
		} else {
			String lockName = placeProperty.getCode() + "$" + placeProperty.getCode();
			return !isUnlocked(lockName);
		}

		return true;
	}

	private boolean isUnlocked(String lockName) {
		Integer state = this.model.getLockStates().get(lockName);
		if (state != null && state > 0)
			return false;
		return true;
	}

	public synchronized void lock(Lock lock) {
		if (this.isFinished())
			throw new RuntimeException("Invalid operation: Task is finished.");

		this.doLock(lock);
	}

	public synchronized void unlock(Lock lock) {
		if (this.isFinished())
			throw new RuntimeException("Invalid operation: Task is finished.");

		Map<String, Integer> lockStates = model.getLockStates();
		String lockName = lock.getPlace() + "$" + lock.getId();
		Integer state = lockStates.get(lockName);
		if (state == null)
			state = 0;
		state--; // Unlock
		if (state != 0)
			lockStates.put(lockName, state);
		else
			lockStates.remove(lockName);

		if (state < 1)
			doUnlock(lock);
	}

	public synchronized void removeLock(Lock lock) {
		if (this.isFinished())
			throw new RuntimeException("Invalid operation: Task is finished.");

		model.getLockStates().remove(lock.getPlace() + "$" + lock.getId());
	}

	public synchronized ValidationResult solveEditionAction() {
		if (this.isFinished())
			throw new RuntimeException("Invalid operation: Task is finished.");

		PlaceProperty placeProperty = this.model.getPlaceProperty();
		EditionActionProperty actionProperty = placeProperty.getEditionActionProperty();
		if (actionProperty == null) {
			throw new RuntimeException("Invalid state, this task isn't on a edition action");
		}

		Node form = this.persistenceService.loadForm(this.model.getEditionFormId());

		ValidationResult validationResult = new ValidationResult();

		MonetEvent event = new MonetEvent(MonetEvent.TASK_VALIDATE_FORM, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_FORM, form);
		event.addParameter(MonetEvent.PARAMETER_VALIDATION_RESULT, validationResult);
		this.agentNotifier.notify(event);

		if (!validationResult.isValid())
			return validationResult;

		event = new MonetEvent(MonetEvent.TASK_ACTION_SOLVED, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_FORM, form);
		this.agentNotifier.notify(event);

		this.addFormFactToHistory(actionProperty, form);

		this.model.setEditionFormId(null);
		this.persistenceService.deleteNodeAndRemoveFromTrash(form.getId());

		this.unlock(new Lock(placeProperty.getCode(), placeProperty.getCode()));

		this.gotoPlace(actionProperty.getGoto().getValue(), null);

		this.resume();

		return validationResult;
	}

	private void addFormFactToHistory(EditionActionProperty actionProperty, Node form) {
		StringBuilder builder = new StringBuilder();
		HashMap<String, String> formContent = AttributeList.extractIndicators("value", form.getAttributeList().serializeToXML().toString());
		Language language = Language.getInstance();
		FormDefinition nodeDefinition = (FormDefinition) form.getDefinition();
		for (Ref show : ((FormViewProperty) nodeDefinition.getNodeView(actionProperty.getUse().getWithView().getValue())).getShow().getField()) {
			FieldProperty field = nodeDefinition.getField(show.getValue());
			String code = field.getCode();
			if (formContent.containsKey(code)) {
				builder.append(language.getModelResource(field.getLabel()));
				builder.append(": ");
				builder.append(formContent.get(code));
				builder.append(", ");
			}
		}
		if (builder.length() > 0)
			builder.delete(builder.length() - 2, builder.length());

		TaskFact formFact = new TaskFact();
		formFact.setTitle(language.getModelResource(actionProperty.getHistory()));
		formFact.setSubTitle(builder.toString());
		formFact.setCreateDate(new Date());
		this.history.add(formFact);
	}

	public synchronized void selectDelegationActionRole(Role role) {
		if (this.isFinished())
			throw new RuntimeException("Invalid operation: Task is finished.");

		PlaceProperty placeProperty = this.model.getPlaceProperty();
		DelegationActionProperty actionProperty = placeProperty.getDelegationActionProperty();
		if (actionProperty == null)
			throw new RuntimeException("Invalid state, this task isn't on a delegation action");

		TaskProviderProperty declaration = this.model.getDefinition().getTaskProviderPropertyMap().get(actionProperty.getProvider().getValue());
		ProviderBehavior provider = this.providers.get(declaration.getCode());
		boolean isTaskUrgent = this.persistenceService.loadTask(this.model.getId()).isUrgent();
		TaskOrder taskOrder = provider.initialize(this.model.getId(), declaration, role, isTaskUrgent);

		if (taskOrder.getSetupNodeId() != null) {
			this.setupDelegationAction();
			return;
		}

		DelegationSetup delegationSetup = new DelegationSetup(isTaskUrgent);

		MonetEvent event = new MonetEvent(MonetEvent.TASK_SETUP_DELEGATION, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_DELEGATION_SETUP, delegationSetup);
		this.agentNotifier.notify(event);

		if (delegationSetup.isCanceled())
			this.setupDelegationAction();
		else if (delegationSetup.isWithValues()) {
			// Rellenamos el taskOrder con los datos
			taskOrder.setSuggestedStartDate(delegationSetup.getSuggestedStartDate());
			taskOrder.setSuggestedEndDate(delegationSetup.getSuggestedEndDate());
			taskOrder.setUrgent(delegationSetup.isUrgent());
			taskOrder.setComments(delegationSetup.getComments());

			this.persistenceService.saveTaskOrder(taskOrder);
			// Hacemos un SetupDelegation
			this.setupDelegationAction();
		} else { // delegationSetup.isWithDefaultValues()
			Node setupNode = this.persistenceService.createForm(this.model.getId(), TaskOrderDefinition.CODE);

			FieldDateImpl.set(setupNode.getAttribute(TaskOrderDefinition.SuggestedStartDateProperty.CODE), TaskOrderDefinition.SuggestedStartDate, delegationSetup.getSuggestedStartDate());
			FieldDateImpl.set(setupNode.getAttribute(TaskOrderDefinition.SuggestedEndDateProperty.CODE), TaskOrderDefinition.SuggestedEndDate, delegationSetup.getSuggestedEndDate());
			FieldMemoImpl.set(setupNode.getAttribute(TaskOrderDefinition.CommentsProperty.CODE), TaskOrderDefinition.Comments, delegationSetup.getComments());
			FieldBooleanImpl.set(setupNode.getAttribute(TaskOrderDefinition.UrgentProperty.CODE), TaskOrderDefinition.Urgent, delegationSetup.isUrgent());

			taskOrder.setSetupNodeId(setupNode.getId());

			this.persistenceService.saveForm(setupNode);
			this.persistenceService.saveTaskOrder(taskOrder);
		}

	}

	public synchronized void setupDelegationAction() {
		if (this.isFinished())
			throw new RuntimeException("Invalid operation: Task is finished.");

		PlaceProperty placeProperty = this.model.getPlaceProperty();
		DelegationActionProperty actionProperty = placeProperty.getDelegationActionProperty();
		if (actionProperty == null)
			throw new RuntimeException("Invalid state, this task isn't on a delegation action");

		TaskProviderProperty declaration = this.model.getDefinition().getTaskProviderPropertyMap().get(actionProperty.getProvider().getValue());
		ProviderBehavior provider = this.providers.get(declaration.getCode());
		provider.start();

		TaskOrder order = this.persistenceService.loadTaskOrder(provider.getModel().getOrderId());
		order.setSetupNodeId(null);
		this.persistenceService.saveTaskOrder(order);

		MonetEvent event = new MonetEvent(MonetEvent.TASK_SETUP_DELEGATION_COMPLETE, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_SUGGESTED_START_DATE, order.getInternalSuggestedStartDate());
		event.addParameter(MonetEvent.PARAMETER_SUGGESTED_END_DATE, order.getInternalSuggestedEndDate());
		event.addParameter(MonetEvent.PARAMETER_OBSERVATIONS, order.getComments());
		event.addParameter(MonetEvent.PARAMETER_URGENT, order.isUrgent());
		event.addParameter(MonetEvent.PARAMETER_PROVIDER, order.getRole().getLabel());
		this.agentNotifier.notify(event);

		this.updateTaskState();
	}

	public synchronized void completeDelegationAction(MailBoxUri providerMailbox, String providerUserId) {
		PlaceProperty placeProperty = this.model.getPlaceProperty();
		DelegationActionProperty actionProperty = placeProperty.getDelegationActionProperty();
		if (actionProperty == null)
			throw new RuntimeException("Invalid state, this task isn't on a delegation action");

		TaskProviderProperty declaration = this.model.getDefinition().getTaskProviderPropertyMap().get(actionProperty.getProvider().getValue());
		ProviderBehavior provider = this.providers.get(declaration.getCode());

		provider.complete(providerMailbox);
		this.persistenceService.addMailBoxPermission(provider.getModel().getLocalMailBox().getId(), providerUserId);

		this.unlock(new Lock(placeProperty.getCode(), placeProperty.getCode()));

		if (this.model.getPlace().equals(placeProperty.getCode())) {
			if (actionProperty.getGoto() != null)
				this.gotoPlace(actionProperty.getGoto().getValue(), actionProperty.getHistory());
		}

		this.resumeAsync();
	}

	public void failureDelegationAction() {
		PlaceProperty placeProperty = this.model.getPlaceProperty();
		DelegationActionProperty actionProperty = placeProperty.getDelegationActionProperty();
		if (actionProperty == null)
			throw new RuntimeException("Invalid state, this task isn't on a delegation action");

		TaskProviderProperty declaration = this.model.getDefinition().getTaskProviderPropertyMap().get(actionProperty.getProvider().getValue());
		ProviderBehavior provider = this.providers.get(declaration.getCode());
		provider.getModel().setFailureDate(new Date());

		this.updateTaskState();
	}

	public synchronized void selectSendJobActionRole(Role role) {
		if (this.isFinished())
			throw new RuntimeException("Invalid operation: Task is finished.");

		PlaceProperty placeProperty = this.model.getPlaceProperty();
		SendJobActionProperty actionProperty = placeProperty.getSendJobActionProperty();
		if (actionProperty == null)
			throw new RuntimeException("Invalid state, this task isn't on a send job action");

		TaskOrder taskOrder = null;
		if (this.model.getCurrentJobOrderId() != null) {
			taskOrder = this.persistenceService.loadTaskOrder(this.model.getCurrentJobOrderId());
		} else {
			boolean isTaskUrgent = this.persistenceService.loadTask(this.model.getId()).isUrgent();
			taskOrder = this.persistenceService.createTaskOrder(this.model.getId(), actionProperty.getCode(), role != null ? role.getId() : null, TaskOrder.Type.job, isTaskUrgent);
			this.model.setCurrentJobOrderId(taskOrder.getId());
		}

		if (taskOrder.getSetupNodeId() != null) {
			this.setupSendJobAction();
			return;
		}

		JobSetup jobSetup = new JobSetup();

		MonetEvent event = new MonetEvent(MonetEvent.TASK_SETUP_JOB, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_JOB_SETUP, jobSetup);
		this.agentNotifier.notify(event);

		if (jobSetup.isCanceled()) {
			this.setupSendJobAction();
		} else if (jobSetup.isWithValues()) {
			// Rellenamos el taskOrder con los datos
			taskOrder.setSuggestedStartDate(jobSetup.getSuggestedStartDate());
			taskOrder.setSuggestedEndDate(jobSetup.getSuggestedEndDate());
			taskOrder.setUrgent(jobSetup.isUrgent());
			taskOrder.setComments(jobSetup.getComments());

			this.persistenceService.saveTaskOrder(taskOrder);
			// Hacemos un SetupDelegation
			this.setupSendJobAction();
		} else { // delegationSetup.isWithDefaultValues()
			Node setupNode = this.persistenceService.createForm(this.model.getId(), TaskOrderDefinition.CODE);

			FieldDateImpl.set(setupNode.getAttribute(TaskOrderDefinition.SuggestedStartDateProperty.CODE), TaskOrderDefinition.SuggestedStartDate, jobSetup.getSuggestedStartDate());
			FieldDateImpl.set(setupNode.getAttribute(TaskOrderDefinition.SuggestedEndDateProperty.CODE), TaskOrderDefinition.SuggestedEndDate, jobSetup.getSuggestedEndDate());
			FieldMemoImpl.set(setupNode.getAttribute(TaskOrderDefinition.CommentsProperty.CODE), TaskOrderDefinition.Comments, jobSetup.getComments());
			FieldBooleanImpl.set(setupNode.getAttribute(TaskOrderDefinition.UrgentProperty.CODE), TaskOrderDefinition.Urgent, jobSetup.isUrgent());

			taskOrder.setSetupNodeId(setupNode.getId());

			this.persistenceService.saveForm(setupNode);
			this.persistenceService.saveTaskOrder(taskOrder);
		}
	}

	public void setupSendJobAction() {
		if (this.isFinished())
			throw new RuntimeException("Invalid operation: Task is finished.");

		PlaceProperty placeProperty = this.model.getPlaceProperty();
		SendJobActionProperty actionProperty = placeProperty.getSendJobActionProperty();
		if (actionProperty == null)
			throw new RuntimeException("Invalid state, this task isn't on a send job action");

		TaskOrder taskOrder = this.persistenceService.loadTaskOrder(this.model.getCurrentJobOrderId());

		MonetEvent eventComplete = new MonetEvent(MonetEvent.TASK_SETUP_JOB_COMPLETE, null, this.model.getId());
		eventComplete.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		eventComplete.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
		eventComplete.addParameter(MonetEvent.PARAMETER_SUGGESTED_START_DATE, taskOrder.getInternalSuggestedStartDate());
		eventComplete.addParameter(MonetEvent.PARAMETER_SUGGESTED_END_DATE, taskOrder.getInternalSuggestedEndDate());
		eventComplete.addParameter(MonetEvent.PARAMETER_OBSERVATIONS, taskOrder.getComments());
		eventComplete.addParameter(MonetEvent.PARAMETER_URGENT, taskOrder.isUrgent());
		eventComplete.addParameter(MonetEvent.PARAMETER_PROVIDER, taskOrder.getRole()!=null?taskOrder.getRole().getLabel():null);
		this.agentNotifier.notify(eventComplete);

		Task job = this.persistenceService.createTask(actionProperty.getJob().getValue());
		String role = Dictionary.getInstance().getRoleDefinition(actionProperty.getRole().getValue()).getCode();
		job.setPartnerContext(this.persistenceService.loadTask(this.model.getId()).getPartnerContext());
		job.setRole(role);
		job.setStartSuggestedDate(taskOrder.getInternalSuggestedStartDate());
		job.setEndSuggestedDate(taskOrder.getInternalSuggestedEndDate());
		job.setUrgent(taskOrder.isUrgent());
		job.setComments(taskOrder.getComments());

		UserRole roleInstance = (UserRole) taskOrder.getRole();
		if (roleInstance != null)
			job.setOwner(roleInstance.getUser());

		Message message = new Message();
		message.setType(Message.Type.JOB);

		MonetEvent event = new MonetEvent(MonetEvent.TASK_CREATE_JOB_MESSAGE, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_MESSAGE, message);
		this.agentNotifier.notify(event);

		if (message.getSubject() != null)
			job.setLabel(message.getSubject());
		if (message.getContent() != null)
			job.setDescription(message.getContent());
		job.setLocation(message.getLocation());
		job.setState(TaskState.WAITING);

		this.persistenceService.save(job);

		this.persistenceService.resumeJob(job, message, this.model.getId(), placeProperty.getCode(), taskOrder.getId());

		this.unlock(new Lock(placeProperty.getCode(), placeProperty.getCode()));

		this.gotoPlace(actionProperty.getGoto().getValue(), actionProperty.getHistory());

		this.saveSnapshot();

		MonetEvent eventCreated = new MonetEvent(MonetEvent.TASK_CREATED_JOB, null, this.model.getId());
		eventCreated.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		eventCreated.addParameter(MonetEvent.PARAMETER_ACTION, actionProperty.getCode());
		eventCreated.addParameter(MonetEvent.PARAMETER_JOB, job.getId());
		this.agentNotifier.notify(eventCreated);

		this.model.setCurrentJobOrderId(null);
		this.save();
		this.resume();
	}

	public String getCurrentJobOrderId() {
		return this.model.getCurrentJobOrderId();
	}

	public synchronized void setupEnrollAction(String contestTaskId) {
		// CONTESTANTS
//		if (this.isFinished())
//			throw new RuntimeException("Invalid operation: Task is finished.");
//
//		PlaceProperty placeProperty = this.model.getPlaceProperty();
//		EnrollActionProperty actionProperty = placeProperty.getEnrollActionProperty();
//
//		if (actionProperty == null) {
//			throw new RuntimeException("Invalid state, this task isn't on a delegation action");
//		}
//
//		ContestBehavior contest = this.contests.get(actionProperty.getCode());
//		if (contestTaskId != null) {
//			contest.initialize(this.model.getId(), this.model.getDefinition().getTaskContestPropertyMap().get(actionProperty.getContest().getValue()), contestTaskId);
//			this.unlock(new Lock(placeProperty.getCode(), placeProperty.getCode()));
//			this.gotoPlace(actionProperty.getGoto().getValue(), actionProperty.getHistory());
//			this.saveSnapshot();
//		}
//
//		this.resume();
	}

	public synchronized void setupWaitAction(Date dueDate, Time timeAfter) {
		if (this.isFinished())
			throw new RuntimeException("Invalid operation: Task is finished.");

		PlaceProperty placeProperty = this.model.getPlaceProperty();
		WaitActionProperty actionProperty = placeProperty.getWaitActionProperty();
		if (actionProperty == null) {
			throw new RuntimeException("Invalid state, this task isn't on a wait action");
		}

		this.setupTimer(placeProperty.getCode(), dueDate, timeAfter);
		this.resume();
	}

	public synchronized void onTimeout(String tag) {
		if (this.isFinished())
			throw new RuntimeException("Invalid operation: Task is finished.");

		PlaceProperty placeProperty = this.model.getPlaceProperty();
		WaitActionProperty actionProperty = placeProperty.getWaitActionProperty();
		if (actionProperty != null) {
			this.taskActionSolved(placeProperty, actionProperty);

			if (actionProperty.getGoto() != null && placeProperty.getCode().equals(this.model.getPlace()))
				this.gotoPlace(actionProperty.getGoto().getValue(), actionProperty.getHistory());

			this.removeLock(new Lock(placeProperty.getCode(), placeProperty.getCode()));
		} else {
			MonetEvent event = new MonetEvent(MonetEvent.TASK_PLACE_TIMEOUT, null, this.model.getId());
			event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode() + "");
			event.addParameter(MonetEvent.PARAMETER_ACTION, placeProperty.getPlaceActionProperty().getCode());
			this.agentNotifier.notify(event);

			if (placeProperty.getDoorActionProperty() != null) {
				DoorActionProperty doorActionProperty = placeProperty.getDoorActionProperty();
				Ref take = doorActionProperty.getTimeout().getTake();
				if (take != null && placeProperty.getCode().equals(this.model.getPlace())) {
					ExitProperty exitProperty = doorActionProperty.getExitMap().get(take.getValue());
					this.unlock(new Lock(placeProperty.getCode(), exitProperty.getCode()));
				}
			} else if (placeProperty.getLineActionProperty() != null) {
				LineActionProperty lineActionProperty = placeProperty.getLineActionProperty();
				Ref take = lineActionProperty.getTimeout().getTake();
				if (take != null && placeProperty.getCode().equals(this.model.getPlace())) {
					LineStopProperty lineStopProperty = lineActionProperty.getStopMap().get(take.getValue());
					this.unlock(new Lock(placeProperty.getCode(), lineStopProperty.getCode()));
				}
			} else if (placeProperty.getEditionActionProperty() != null) {
				EditionActionProperty editionActionProperty = placeProperty.getEditionActionProperty();
				Ref take = editionActionProperty.getTimeout().getTake();
				if (take != null && placeProperty.getCode().equals(this.model.getPlace())) {
					String placeCode = this.model.getDefinition().getPlace(take.getValue()).getCode();
					this.unlock(new Lock(placeProperty.getCode(), placeCode));
				}
			}
		}

		this.resume();
	}

	public synchronized boolean isBackEnable() {
		BooleanResult result = new BooleanResult();
		PlaceProperty placeProperty = this.model.getPlaceProperty();

		MonetEvent event = new MonetEvent(MonetEvent.TASK_IS_BACK_ENABLE, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_ACTION, placeProperty.getPlaceActionProperty().getCode());
		event.addParameter(MonetEvent.PARAMETER_RESULT, result);
		this.agentNotifier.notify(event);

		return result.getValue();
	}

	public synchronized void back() {
		if (this.isFinished())
			throw new RuntimeException("Invalid operation: Task is finished.");

		PlaceProperty placeProperty = this.model.getPlaceProperty();

		Snapshot snapshot = this.snapshots.pop();
		if (snapshot != null) {
			snapshot.restoreTo(this.model);
			if (this.model.getEditionFormId() != null)
				this.persistenceService.deleteNodeAndRemoveFromTrash(this.model.getEditionFormId());

			// Abort any provider or collaboration open, that isn't open in snapshot
			for (Entry<String, ProviderBehavior> entry : this.providers.entrySet()) {
				ProviderBehavior behavior = entry.getValue();
				Provider provider = behavior.getModel();
				if (provider.isOpen()) {
					String mailBox = snapshot.getOpenProviders().get(entry.getKey());
					if (mailBox == null || (mailBox != null && !mailBox.equals(provider.getLocalMailBox()))) {
						behavior.abort();
					}
				}
			}

			for (Entry<String, ContestBehavior> entry : this.contests.entrySet()) {
				ContestBehavior behavior = entry.getValue();
				Contest collaborator = behavior.getModel();
				if (collaborator.isOpen()) {
					String mailBox = snapshot.getOpenProviders().get(entry.getKey());
					if (mailBox == null || (mailBox != null && !mailBox.equals(collaborator.getLocalMailBox()))) {
						behavior.abort();
					}
				}
			}
		}

		this.history.add("Restoring previous snapshot"); // TODO: Poner mensaje
		// localizado

		MonetEvent event = new MonetEvent(MonetEvent.TASK_BACK, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_ACTION, placeProperty.getPlaceActionProperty().getCode());
		this.agentNotifier.notify(event);

		this.resume();
	}

	public synchronized void doGoto(String placeName, String historyText) {
		if (this.isFinished())
			throw new RuntimeException("Invalid operation: Task is finished.");

		PlaceProperty placeProperty = this.model.getPlaceProperty();
		String lockName = placeProperty.getCode() + "$" + placeProperty.getCode();
		boolean resetEdition = false;
		if (placeProperty.getEditionActionProperty() != null) resetEdition = true;

		this.gotoPlace(placeName, historyText);

		if (resetEdition) {
			this.model.setEditionFormId(null);
			this.model.getLockStates().clear();
			this.save();
		}
	}

	public synchronized ProviderBehavior getCurrentProvider() {
		PlaceProperty placeProperty = this.model.getPlaceProperty();
		ProviderBehavior providerBehavior = null;

		if (placeProperty.getDelegationActionProperty() != null) {
			String providerName = placeProperty.getDelegationActionProperty().getProvider().getValue();
			TaskProviderProperty providerDefinition = this.model.getDefinition().getTaskProviderPropertyMap().get(providerName);
			return this.providers.get(providerDefinition.getCode());
		}

		return providerBehavior;
	}

	public synchronized ProviderBehavior getProvider(String code) {
		return this.providers.get(code);
	}

	public synchronized CustomerBehavior getCustomer() {
		return this.customer;
	}

	public PlaceProperty getCurrentPlace() {
		return this.model.getPlaceProperty();
	}

	public synchronized void addFact(Fact fact) {
		this.history.add(fact);
	}

	public void save() {
		this.persistenceService.save(this.model);
	}

	public String getClassificator() {
		ClassificatorResult result = new ClassificatorResult();
		PlaceProperty placeProperty = this.model.getPlaceProperty();

		MonetEvent event = new MonetEvent(MonetEvent.TASK_TRANSFERENCE_CALCULATE_CLASSIFICATOR, null, this.model.getId());
		event.addParameter(MonetEvent.PARAMETER_PLACE, placeProperty.getCode());
		event.addParameter(MonetEvent.PARAMETER_ACTION, placeProperty.getEnrollActionProperty().getCode());
		event.addParameter(MonetEvent.PARAMETER_RESULT, result);
		this.agentNotifier.notify(event);

		return result.value;
	}

	public String getEditionFormId() {
		return this.model.getEditionFormId();
	}

	public boolean abort() {
		MonetEvent event = new MonetEvent(MonetEvent.TASK_ABORTED, null, this.model.getId());
		this.agentNotifier.notify(event);
		return true;
	}

	public HashMap<String, String> getFlags() {
		return this.model.getFlags();
	}

	public String getFlag(String name) {
		return this.model.getFlag(name);
	}

	public boolean isFlagActive(String name) {
		return this.model.isFlagActive(name);
	}

	public void setFlag(String name, String value) {
		this.model.setFlag(name, value);
		this.save();
	}

	public void removeFlag(String name) {
		this.model.removeFlag(name);
		this.save();
	}

}
