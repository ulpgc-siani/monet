package client.presenters.displays.entity.field;

import client.core.constructors.CheckFieldCheckConstructor;
import client.core.model.Node;
import client.core.model.definition.entity.field.CheckFieldDefinition;
import client.core.model.fields.CheckField;
import client.core.model.types.Check;
import client.core.model.types.CheckList;
import client.core.model.types.CompositeCheck;
import client.core.model.types.TermList;
import client.presenters.displays.entity.FieldDisplay;
import client.services.callback.TermListCallback;

import java.util.logging.Level;
import java.util.logging.Logger;

import static client.services.SourceService.Mode;

public class CheckFieldDisplay extends FieldDisplay<CheckField, CheckFieldDefinition, CheckList> implements IsCheckFieldDisplay {

	public static final Type TYPE = new Type("CheckFieldDisplay", FieldDisplay.TYPE);

	public CheckFieldDisplay(Node node, CheckField field) {
		super(node, field);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public String getValueAsString() {
		return "";
	}

	@Override
	public CheckList getValue() {
		return super.getValue() == null ? new client.core.system.types.CheckList() : super.getValue();
	}

	@Override
	protected CheckList format(CheckList checks) {
		return checks;
	}

	@Override
	public void loadOptions() {
		if (getDefinition().getSource() != null) {
			TermListCallback callback = new TermListCallback() {
				@Override
				public void success(final TermList termList) {
					CheckList value = termList.toCheckList(getValue().toCheckedValues());
					notifyOptions(value);
					setValue(value);
				}

				@Override
				public void failure(String error) {
					notifyOptionsFailure(null);
				}
			};
			this.services.getSourceService().getTerms(getEntity().getSource(), Mode.TREE, 0, -1, getFlatten(), getDepth(), callback);
		} else if (getDefinition().getTerms() != null && !getDefinition().getTerms().isEmpty())
			notifyOptions(CheckFieldCheckConstructor.constructList(getDefinition().getTerms()));
		else
			notifyOptions(getValue());
	}

	private String getFlatten() {
		if (getDefinition().getSelect() == null)
			return CheckFieldDefinition.SelectDefinition.Flatten.ALL.toString();
		return getDefinition().getSelect().getFlatten().toString();
	}

	private String getDepth() {
		if (getDefinition().getSelect() == null)
			return "-1";
		return String.valueOf(getDefinition().getSelect().getDepth());
	}

	private void notifyOptions(final CheckList checkList) {
		updateHooks(new Notification<CheckFieldDisplay.Hook>() {
			@Override
			public void update(CheckFieldDisplay.Hook hook) {
				hook.options(checkList);
			}
		});
	}

	private void notifyOptionsFailure(String filter) {
		Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not load source " + getEntity().getSource().getId() + " options for condition " + filter);
		updateHooks(new Notification<CheckFieldDisplay.Hook>() {
			@Override
			public void update(CheckFieldDisplay.Hook hook) {
				hook.optionsFailure();
			}
		});
	}

	@Override
	public void selectAll(CompositeCheck check) {
		if (check.isSelectable())
			check.setChecked(true);
		selectChildren(check);
		updateValue();
	}

	@Override
	public void selectNone(CompositeCheck check) {
		if (check.isSelectable())
			check.setChecked(false);
		unSelectChildren(check);
		updateValue();
	}

	private void updateValue() {
		setValue(getValue());
	}

	@Override
	public void toggle(Check value) {
		value.setChecked(!value.isChecked());
		if (!value.isLeaf() && value.isChecked())
			selectChildren(((CompositeCheck) value).getChecks());
		else if (!value.isLeaf())
			unSelectChildren(((CompositeCheck) value).getChecks());
		updateValue();
	}

	@Override
	protected boolean shouldUpdateValue(CheckList oldValue, CheckList checks) {
		return true;
	}

	private void selectChildren(CheckList checkList) {
		for (Check check : checkList) {
			check.setChecked(true);
			if (!check.isLeaf())
				selectChildren(((CompositeCheck) check).getChecks());
		}
	}

	private void unSelectChildren(CheckList checkList) {
		for (Check check : checkList) {
			check.setChecked(false);
			if (!check.isLeaf())
				unSelectChildren(((CompositeCheck) check).getChecks());
		}
	}

	private void selectChildren(CompositeCheck check) {
		for (Check child : check.getChecks()) {
			child.setChecked(true);
			if (!child.isLeaf()) selectChildren((CompositeCheck) child);
		}
	}

	private void unSelectChildren(CompositeCheck check) {
		for (Check child : check.getChecks()) {
			child.setChecked(false);
			if (!child.isLeaf()) unSelectChildren((CompositeCheck) child);
		}
	}

	@Override
	public void addHook(Hook hook) {
		super.addHook(hook);
	}

	public interface Hook extends FieldDisplay.Hook {
		void value();

		void options(CheckList options);

		void optionsFailure();
	}
}
