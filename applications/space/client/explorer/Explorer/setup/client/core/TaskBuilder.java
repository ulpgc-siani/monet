package client.core;

import client.core.model.definition.Ref;
import client.core.model.definition.entity.PlaceDefinition;
import client.core.model.*;
import client.core.model.workmap.WorkMap;
import client.core.model.workmap.DelegationAction;
import client.core.model.workmap.EditionAction;
import client.core.model.workmap.LineAction;
import client.core.model.workmap.WaitAction;
import client.core.system.workmap.*;
import cosmos.types.Date;

import static client.core.model.workmap.DelegationAction.Step;

public class TaskBuilder {

	public static Task buildTask(String id) {
		client.core.system.Task task = new client.core.system.Task(id, "") {
			@Override
			protected ViewList<TaskView> loadViews() {
				return null;
			}

			@Override
			public ClassName getClassName() {
				return null;
			}
		};
		fillTask(task, Task.State.NEW, new client.core.system.workmap.WorkMap(new client.core.system.workmap.Place(new Action(null) {
			@Override
			public ClassName getClassName() {
				return null;
			}
		})), ListBuilder.EmptyTaskViewList);
		return task;
	}

	public static Activity buildActivity(String id, String label, Task.State state, WorkMap workMap, ViewList<TaskView> viewList) {
		client.core.system.Activity activity = new client.core.system.Activity<>(id, label);
		fillTask(activity, state, workMap, viewList);
		return activity;
	}

	public static Activity buildActivity(String id, String label) {
		return buildActivity(id, label, Task.State.NEW, null, ListBuilder.buildTaskViewList(new TaskView[0]));
	}

	public static <T extends client.core.model.workmap.Action> WorkMap buildWorkMap(T action) {
		return new client.core.system.workmap.WorkMap(new client.core.system.workmap.Place(action));
	}

	public static DelegationAction buildDelegationActionInRoleStep(final String label) {
		return new client.core.system.workmap.DelegationAction(Step.SETUP_ROLE, new Date(), null, null, new PlaceDefinition.DelegationActionDefinition() {
			@Override
			public Ref getProvider() {
				return new client.core.system.definition.Ref("provider", "Proveedor");
			}

			@Override
			public String getCode() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public Instance.ClassName getClassName() {
				return PlaceDefinition.DelegationActionDefinition.CLASS_NAME;
			}

		});
	}

	public static DelegationAction buildDelegationActionInOrderStep(final String label, final Role role, Node orderNode) {
		return new client.core.system.workmap.DelegationAction(Step.SETUP_ORDER, null, role, orderNode, new PlaceDefinition.DelegationActionDefinition() {
			@Override
			public Ref getProvider() {
				return new client.core.system.definition.Ref("provider", "Proveedor");
			}

			@Override
			public String getCode() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public Instance.ClassName getClassName() {
				return PlaceDefinition.DelegationActionDefinition.CLASS_NAME;
			}


		});
	}

	public static DelegationAction buildDelegationActionInSendingStep(final String label, final Role role) {
		return new client.core.system.workmap.DelegationAction(Step.SENDING, null, role, null, new PlaceDefinition.DelegationActionDefinition() {
			@Override
			public Ref getProvider() {
				return new client.core.system.definition.Ref("provider", "Proveedor");
			}

			@Override
			public String getCode() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public Instance.ClassName getClassName() {
				return PlaceDefinition.DelegationActionDefinition.CLASS_NAME;
			}

		});
	}

	public static DelegationAction buildDelegationAction(final String label, Step step, final Role role, Node orderNode) {
		return new client.core.system.workmap.DelegationAction(step, null, role, orderNode, new PlaceDefinition.DelegationActionDefinition() {
			@Override
			public Ref getProvider() {
				return new client.core.system.definition.Ref("provider", "Proveedor");
			}

			@Override
			public String getCode() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public Instance.ClassName getClassName() {
				return PlaceDefinition.DelegationActionDefinition.CLASS_NAME;
			}
		});
	}

	public static LineAction buildLineAction(final String label, Date dueDate, final client.core.model.workmap.LineAction.Stop[] stops) {
		return new client.core.system.workmap.LineAction(dueDate, new PlaceDefinition.LineActionDefinition() {
			@Override
			public TimeoutDefinition getTimeout() {
				return null;
			}

			@Override
			public LineStopDefinition[] getStop() {
				LineStopDefinition[] definitions = new LineStopDefinition[stops.length];

				for (int i=0; i<stops.length; i++) {
					final LineAction.Stop stop = stops[i];
					definitions[i] = new LineStopDefinition() {
						@Override
						public String getCode() {
							return stop.getCode();
						}

						@Override
						public String getLabel() {
							return stop.getLabel();
						}
					};
				}

				return definitions;
			}

			@Override
			public String getCode() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public Instance.ClassName getClassName() {
				return PlaceDefinition.LineActionDefinition.CLASS_NAME;
			}
		});
	}

	public static LineAction.Stop buildLineActionStop(final String code, final String label) {
		return new LineAction.Stop() {
			@Override
			public String getCode() {
				return code;
			}

			@Override
			public String getLabel() {
				return label;
			}
		};
	}

	public static EditionAction buildEditionAction(final String label, Form form) {
		return new client.core.system.workmap.EditionAction(form, new PlaceDefinition.EditionActionDefinition() {
			@Override
			public String getCode() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public Instance.ClassName getClassName() {
				return PlaceDefinition.EditionActionDefinition.CLASS_NAME;
			}
		});
	}

	public static WaitAction buildWaitAction(final String label, Date dueDate) {
		return new client.core.system.workmap.WaitAction(dueDate, new PlaceDefinition.WaitActionDefinition() {
			@Override
			public String getCode() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return null;
			}

			@Override
			public Instance.ClassName getClassName() {
				return PlaceDefinition.WaitActionDefinition.CLASS_NAME;
			}
		});
	}

	private static void fillTask(client.core.system.Task task, Task.State state, WorkMap workMap, ViewList<TaskView> views) {
		task.setState(state);
		task.setWorkMap(workMap);
		task.setViews(views);
	}

}
