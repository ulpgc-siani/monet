package client.widgets.view.workmap;

import client.core.model.List;
import client.core.model.Role;
import client.presenters.displays.entity.workmap.TaskStateDelegationActionDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.services.TranslatorService;
import client.widgets.toolbox.HTMLListWidget;
import client.widgets.view.EntityViewWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.model.HoldAble;
import cosmos.gwt.presenters.PresenterHolder;
import cosmos.gwt.utils.StyleUtils;
import cosmos.presenters.Presenter;

import static client.core.model.workmap.DelegationAction.Step;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElementAndKeepContent;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.*;

public class TaskStateDelegationActionWidget extends HTMLPanel implements HoldAble {
	private final TaskStateDelegationActionDisplay display;
	private final TranslatorService translator;
	private HTMLListWidget roleListComponent;
	private HTMLPanel entityComponent;
	private List<Role> roleList;
	private ViewDisplay orderDisplay;
	private boolean rolesDisabled = false;

	public TaskStateDelegationActionWidget(TaskStateDelegationActionDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
		this.translator = translator;
		this.createComponents();
		this.refresh();
		this.hook();
	}

	@Override
	public void onHold(PresenterHolder holder) {
		display.load();
	}

	private void createComponents() {
		$(this).addClass(Style.WIDGET, Style.DELEGATION);

		createSteps();
		this.bind();
	}

	private void createSteps() {
		createSetupRoleStep();
		createSetupOrderStep();
	}

	private void createSetupRoleStep() {
		roleListComponent = new HTMLListWidget<>(new RoleItem.Builder(), translator);
		roleListComponent.addClickHandler(new HTMLListWidget.ClickHandler<Role>() {
			@Override
			public void onClick(int index, Role role) {
				selectRole(role);
			}
		});
	}

	private void selectRole(Role role) {
		if (rolesDisabled)
			return;

		rolesDisabled = true;
		refresh();

		RoleItem item = (RoleItem)roleListComponent.getItem(role);
		item.activate();

		display.selectRole(role);
	}

	private void createSetupOrderStep() {
		entityComponent = new HTMLPanel("");
	}

	private void refresh() {
		refreshMessage();
		refreshSteps();
	}

	private void refreshMessage() {
		String message = display.getMessage();

		if (message != null)
			$(this).find(toRule(Style.MESSAGE)).get(0).setInnerHTML(message);
	}

	private void refreshSteps() {
		Step step = display.getStep();

		$(this).find(toRule(Style.STEP)).removeClass(Style.ACTIVE);
		$(this).find(toCombinedRule(Style.STEP, step.toString())).addClass(Style.ACTIVE);

		refreshSetupRoleStep(step);
		refreshSetupOrderStep(step);
	}

	private void refreshSetupRoleStep(Step step) {
		if (step != Step.SETUP_ROLE || roleList == null)
			return;

		if (rolesDisabled) {
			$(roleListComponent).find(toRuleCheckingTags(StyleUtils.LI)).addClass(Style.DISABLED);
			return;
		}

		$(roleListComponent).find(toRuleCheckingTags(StyleUtils.LI)).removeClass(Style.DISABLED);
		roleListComponent.clear();
		for (Role role : roleList)
			roleListComponent.addItem(role);
	}

	private void refreshSetupOrderStep(Step step) {
		if (step != Step.SETUP_ORDER || orderDisplay == null)
			return;

		EntityViewWidget.Builder entityViewBuilder = Builder.getViewBuilder();

		entityComponent.clear();
		entityComponent.add(entityViewBuilder.build(orderDisplay, null, null));
	}

	private void hook() {
		this.display.addHook(new TaskStateDelegationActionDisplay.Hook() {
			@Override
			public void step(Step step) {
				refresh();
			}

			@Override
			public void roles(List<Role> roleList) {
				updateRoleList(roleList);
			}

			@Override
			public void rolesFailure(String error) {
			}

			@Override
			public void role(Role role) {
				refresh();
			}

			@Override
			public void roleFailure(String error) {
			}

			@Override
			public void order(ViewDisplay display) {
				updateOrder(display);
			}

			@Override
			public void orderFailure(String error) {
			}

		});
	}

	private void updateRoleList(List<Role> roleList) {
		this.roleList = roleList;
		refresh();
	}

	private void updateOrder(ViewDisplay display) {
		this.orderDisplay = display;
		refresh();
	}

	private void bind() {
		bindWidgetToElement(this, roleListComponent, $(getElement()).find(toRuleCheckingTags(Style.STEP_SETUP_ROLE, Style.STEP_SETUP_ROLE_LIST)).get(0));
		bindWidgetToElementAndKeepContent(this, entityComponent, $(getElement()).find(toRule(Style.STEP_SETUP_ORDER, Style.STEP_SETUP_ORDER_ENTITY)).get(0));

		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public static class Builder extends TaskStateActionWidget.Builder {
		private static EntityViewWidget.Builder viewBuilder;

		public static void register() {
			registerBuilder(TaskStateDelegationActionDisplay.TYPE.toString(), new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			createBuilders();
			return new TaskStateDelegationActionWidget((TaskStateDelegationActionDisplay) presenter, theme.getLayout("task-state-delegation-action"), translator);
		}

		public static EntityViewWidget.Builder getViewBuilder() {
			return viewBuilder;
		}

		private void createBuilders() {
			viewBuilder = new EntityViewWidget.Builder();
			viewBuilder.inject(theme);
			viewBuilder.inject(translator);
		}
	}

	private static class RoleItem extends HTMLListWidget.ListItem<Role> {
		private Anchor component;

		public RoleItem(Role value) {
			super(value);
			init();
		}

		@Override
		protected Widget[] createControlOperations() {
			return new Widget[0];
		}

		@Override
		protected Widget createComponent() {
			component = new Anchor(value.getLabel());
			component.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					notifyClick();
				}
			});
			return component;
		}

		public void activate() {
			component.addStyleName(TaskStateDelegationActionWidget.Style.ACTIVE);
		}

		@Override
		protected Widget[] createOperations() {
			return new Widget[0];
		}

		public static class Builder extends HTMLListWidget.ListItem.Builder<Role> {
			@Override
			public HTMLListWidget.ListItem build(Role value, HTMLListWidget.Mode mode) {
				return new RoleItem(value);
			}
		}
	}

	public static class Style {
		public static final String WIDGET = "task-state-delegation-widget";
		public static final String DELEGATION = "delegation";
		public static final String STEP = "step";
		public static final String STEP_SETUP_ROLE = "setup-role";
		public static final String STEP_SETUP_ROLE_LIST = "ul";
		public static final String STEP_SETUP_ORDER = "setup-order";
		public static final String STEP_SETUP_ORDER_ENTITY = "entity";
		public static final String MESSAGE = "message";
		public static final String ACTIVE = "active";
		public static final String DISABLED = "disabled";
	}
}
