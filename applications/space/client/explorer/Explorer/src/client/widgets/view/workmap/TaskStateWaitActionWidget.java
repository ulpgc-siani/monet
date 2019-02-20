package client.widgets.view.workmap;

import client.presenters.displays.entity.workmap.TaskStateWaitActionDisplay;
import client.services.TranslatorService;
import com.google.gwt.dom.client.Element;
import com.google.gwt.query.client.Function;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.model.HoldAble;
import cosmos.gwt.presenters.PresenterHolder;
import cosmos.gwt.utils.StyleUtils;
import cosmos.presenters.Presenter;

import static client.core.model.workmap.WaitAction.Scale;
import static client.core.model.workmap.WaitAction.Step;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.*;

public class TaskStateWaitActionWidget extends HTMLPanel implements HoldAble {
	private final TaskStateWaitActionDisplay display;
    private boolean disabled = false;

	public TaskStateWaitActionWidget(TaskStateWaitActionDisplay display, String layout, TranslatorService translator) {
		super(getHtml(layout, translator));
		this.display = display;
        this.createComponents();
		this.refresh();
		this.hook();
	}

	@Override
	public void onHold(PresenterHolder holder) {
		display.load();
	}

	private void createComponents() {
		$(this).addClass(Style.WIDGET, Style.WAIT);

		$(this).find(toRule(Style.COMMAND)).click(new Function() {
			@Override
			public void f(Element element) {
				setup(getConfiguration(element));
			}
		});
		this.bind();
	}

	private void setup(Configuration configuration) {
		if (disabled)
			return;

		$(this).find(toRuleCheckingTags(StyleUtils.LI, StyleUtils.ANCHOR)).removeClass(Style.ACTIVE);
		configuration.activateElement();

		disabled = true;
		refresh();

		if (configuration.plus())
			display.increment(configuration.scale(), 1);
		else
			display.decrement(configuration.scale(), 1);
	}

	private Configuration getConfiguration(final Element element) {
		return new Configuration() {
			private String[] className;

			private void parse() {
				String fullClassName = element.getClassName();
				fullClassName = fullClassName.replace(Style.ACTIVE, "").replace("  ", "").trim();
				className = fullClassName.split(" ");
			}

			@Override
			public void activateElement() {
				element.addClassName(Style.ACTIVE);
			}

			@Override
			public Scale scale() {
				parse();
				return Scale.valueOf(className[1]);
			}

			@Override
			public boolean plus() {
				parse();
				return className[2].toLowerCase().equals(Style.PLUS);
			}

		};
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

		if (disabled)
			$(this).find(toRuleCheckingTags(StyleUtils.LI)).addClass(Style.DISABLED);
		else
			$(this).find(toRuleCheckingTags(StyleUtils.LI)).removeClass(Style.DISABLED);
	}

	private void hook() {
		this.display.addHook(new TaskStateWaitActionDisplay.Hook() {
			@Override
			public void step() {
				refresh();
			}
		});
	}

	private void bind() {
		this.onAttach();
		RootPanel.detachOnWindowClose(this);
	}

	private static String getHtml(String layout, TranslatorService service) {
		return service.translateHTML(layout);
	}

	private interface Configuration {
		void activateElement();
		Scale scale();
		boolean plus();
	}

	public static class Builder extends TaskStateActionWidget.Builder {

		public static void register() {
			registerBuilder(TaskStateWaitActionDisplay.TYPE.toString(), new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new TaskStateWaitActionWidget((TaskStateWaitActionDisplay) presenter, theme.getLayout("task-state-wait-action"), translator);
		}
	}

	public static class Style {
		public static final String WIDGET = "task-state-delegation-widget";
		public static final String WAIT = "wait";
		public static final String MESSAGE = "message";
		public static final String STEP = "step";
		public static final String ACTIVE = "active";
		public static final String DISABLED = "disabled";
		public static final String COMMAND = "command";
		public static final String PLUS = "plus";
	}
}
