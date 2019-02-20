package client.widgets.view.workmap;

import client.core.model.workmap.LineAction.Stop;
import client.presenters.displays.entity.workmap.TaskStateLineActionDisplay;
import client.services.TranslatorService;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.model.HoldAble;
import cosmos.gwt.presenters.PresenterHolder;
import cosmos.gwt.utils.StyleUtils;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.gwt.utils.StyleUtils.toRuleCheckingTags;

public class TaskStateLineActionWidget extends CosmosHtmlPanel implements HoldAble {
	private final TaskStateLineActionDisplay display;
	private final TranslatorService translator;
	private HTMLListWidget stopListComponent;
	private boolean stopsDisabled = false;

	public TaskStateLineActionWidget(TaskStateLineActionDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
		this.translator = translator;
		createComponents();
		refresh();
		hook();
	}

	@Override
	public void onHold(PresenterHolder holder) {
		display.load();
	}

	private void createComponents() {
        addStyleName(StyleName.WIDGET);
        addStyleName(StyleName.LINE);

		createStops();
		bind();
	}

	private void createStops() {
		stopListComponent = new HTMLListWidget<>(new StopItem.Builder(), translator);
		stopListComponent.addStyleName(StyleName.STOPS);
		stopListComponent.addClickHandler(new HTMLListWidget.ClickHandler<Stop>() {
			@Override
			public void onClick(int index, Stop stop) {
				selectStop(stop);
			}
		});
	}

	private void refresh() {
		refreshTimeout();
		refreshStops();
	}

	private void refreshTimeout() {
		String message = display.getDueMessage();

		if (message != null)
			$(this).find(toRule(StyleName.TIMEOUT)).get(0).setInnerHTML(message);
	}

	private void refreshStops() {
        if (stopsDisabled) {
			$(stopListComponent).find(toRuleCheckingTags(StyleUtils.LI)).addClass(StyleName.DISABLED);
			return;
		}

		$(stopListComponent).find(toRuleCheckingTags(StyleUtils.LI)).removeClass(StyleName.DISABLED);
		stopListComponent.clear();
		for (Stop stop : display.getStops())
			stopListComponent.addItem(stop);
	}

	private void selectStop(Stop stop) {
		if (stopsDisabled)
			return;

		stopsDisabled = true;
		refresh();

        ((StopItem)stopListComponent.getItem(stop)).activate();

		display.selectStop(stop);
	}

	private void hook() {
		this.display.addHook(new TaskStateLineActionDisplay.Hook() {
		});
	}

	private void bind() {
		bindKeepingStyles(stopListComponent, $(getElement()).find(toRuleCheckingTags(StyleName.STOPS)).get(0));

		onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	public static class Builder extends TaskStateActionWidget.Builder {

		public static void register() {
			registerBuilder(TaskStateLineActionDisplay.TYPE.toString(), new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new TaskStateLineActionWidget((TaskStateLineActionDisplay) presenter, theme.getLayout("task-state-line-action"), translator);
		}
	}

	private static class StopItem extends HTMLListWidget.ListItem<Stop> {
		private Anchor component;

		public StopItem(Stop value) {
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

		@Override
		protected Widget[] createOperations() {
			return new Widget[0];
		}

		public void activate() {
			component.addStyleName(TaskStateLineActionWidget.StyleName.ACTIVE);
		}

		public static class Builder extends HTMLListWidget.ListItem.Builder<Stop> {
			@Override
			public HTMLListWidget.ListItem build(Stop value, HTMLListWidget.Mode mode) {
				return new StopItem(value);
			}
		}

	}

	public interface StyleName {
		String WIDGET = "task-state-line-widget";
		String LINE = "line";
		String TIMEOUT = "timeout";
		String STOPS = "stops";
		String ACTIVE = "active";
		String DISABLED = "disabled";
	}
}
