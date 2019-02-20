package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.Check;
import client.core.model.types.CheckList;
import client.core.model.types.CompositeCheck;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.CheckFieldDisplay;
import client.presenters.displays.entity.field.IsCheckFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.CheckOptionsWidget;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class CheckFieldWidget extends FieldWidget {

	private CheckOptionsWidget checkOptionsWidget;

	public CheckFieldWidget(IsCheckFieldDisplay display, String layout, TranslatorService translator) {
		super(display, layout, translator);
		checkFieldDisplay().loadOptions();
	}

	@Override
	protected void addStyleNames() {
		super.addStyleNames();
		addStyleName(StyleName.CHECK);
	}

	@Override
	protected void createComponent() {
		checkOptionsWidget = new CheckOptionsWidget(translator);
		checkOptionsWidget.setCheckToggleHandler(new CheckOptionsWidget.CheckToggleHandler() {
			@Override
			public void onSelectAll(CompositeCheck check) {
				checkFieldDisplay().selectAll(check);
			}

			@Override
			public void onSelectNone(CompositeCheck check) {
				checkFieldDisplay().selectNone(check);
			}

			@Override
			public void onToggle(Check value) {
				checkFieldDisplay().toggle(value);
			}
		});
	}

	@Override
	protected void bind() {
		bind(checkOptionsWidget, toRule(StyleName.TERM_LIST));
		super.bind();
	}

	@Override
	protected void refreshComponent() {
		checkOptionsWidget.unSelectAll();
		for (Check check : checkFieldDisplay().getValue())
			if (check.isChecked()) checkOptionsWidget.select(check);
	}

	@Override
	protected CheckFieldDisplay.Hook createHook() {
		return new CheckFieldDisplay.Hook() {
			@Override
			public void value() {
				refresh();
			}

			@Override
			public void error(String error) {
			}

			@Override
			public void options(CheckList options) {
				fillCheckList(options);
			}

			@Override
			public void optionsFailure() {
			}
		};
	}

	private void fillCheckList(CheckList checks) {
		checkOptionsWidget.setChecks(checks);
		refresh();
	}

	public static class Builder extends FieldWidget.Builder {

		public static void register() {
			registerBuilder(CheckFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
		}

		@Override
		public Widget build(Presenter presenter, String design, String layout) {
			return new CheckFieldWidget((IsCheckFieldDisplay) presenter, layout, translator);
		}

		@Override
		protected String getDialogClass(FieldDisplay display) {
			return "";
		}

		@Override
		protected String getComponentClass(FieldDisplay display) {
			return "component-check";
		}
	}

	private IsCheckFieldDisplay checkFieldDisplay() {
		return (IsCheckFieldDisplay) display;
	}

	public interface StyleName extends FieldWidget.StyleName {
		String CHECK = "check";
		String TERM_LIST = "terms";
	}
}
