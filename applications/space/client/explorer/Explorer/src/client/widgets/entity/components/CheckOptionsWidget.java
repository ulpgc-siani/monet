package client.widgets.entity.components;

import client.core.model.List;
import client.core.model.types.*;
import client.services.TranslatorService;
import client.widgets.entity.components.composite.CheckCategoryPanelWidget;
import client.widgets.toolbox.CheckListWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import cosmos.gwt.model.HasValue;

import java.util.HashMap;
import java.util.Map;

public class CheckOptionsWidget extends VerticalPanel {

	private final TranslatorService translator;
	private final Map<Check, CheckListWidget.CheckListItem> checkToListItem;
	private final Map<Check, CompositeCheck> childrenToParent;
	private CheckToggleHandler checkToggleHandler;
	private CheckListWidget<ValueCheck> currentPanel;

	public CheckOptionsWidget(TranslatorService translator) {
		this.translator = translator;
		this.checkToListItem = new HashMap<>();
		this.childrenToParent = new HashMap<>();
	}

	public void setCheckToggleHandler(CheckToggleHandler checkToggleHandler) {
		this.checkToggleHandler = checkToggleHandler;
	}

	public void setChecks(CheckList list) {
		clear();
		checkToListItem.clear();
		childrenToParent.clear();
		addChecks(list);
	}

	public void select(Check check) {
		if (checkToListItem.containsKey(check) && check.isChecked()) checkToListItem.get(check).check();
		if (check.isLeaf()) return;
		for (Check children : ((CompositeCheck) check).getChecks())
			if (children.isChecked()) select(children);
	}

	public void unSelectAll() {
		for (CheckListWidget.CheckListItem item : checkToListItem.values())
			item.unCheck();
	}

	private void addChecks(CheckList list) {
		for (Check check : list) {
			if (check.isLeaf())
				addCheck(check);
			else if (((CompositeCheck) check).isSelectable())
				addSuperCheck((SuperCheck) check);
			else
				addCategory((CheckCategory) check);
		}
	}

	private void addCheck(Check check) {
		if (currentPanel == null) addNewPanel();
		checkToListItem.put(check, (CheckListWidget.CheckListItem) currentPanel.addItem(new ValueCheck(check)));
	}

	private void addSuperCheck(SuperCheck check) {
		if (currentPanel == null) addNewPanel();
		checkToListItem.put(check, (CheckListWidget.CheckListItem) currentPanel.addItem(new ValueCheck(check)));
		for (Check child : check.getChecks()) {
			childrenToParent.put(child, check);
			checkToListItem.put(child, (CheckListWidget.CheckListItem) currentPanel.addItemWithLevel(new ValueCheck(child)));
		}
	}

	private void addCategory(final CheckCategory category) {
		createPanel();
		addChecks(category.getChecks());
		final CheckCategoryPanelWidget categoryPanel = new CheckCategoryPanelWidget(translator, category, currentPanel);
		categoryPanel.setSelectionChangeHandler(new CheckCategoryPanelWidget.SelectionChangeHandler() {
			@Override
			public void all() {
				selectChildren(category);
				checkToggleHandler.onSelectAll(category);
			}

			@Override
			public void none() {
				unSelectChildren(category);
				checkToggleHandler.onSelectNone(category);
			}
		});
		add(categoryPanel);
		currentPanel = null;
	}

	private void addNewPanel() {
		createPanel();
		add(currentPanel);
	}

	private void createPanel() {
		currentPanel = new CheckListWidget<>(translator);
		currentPanel.addToggleHandler(new CheckListWidget.ToggleHandler<ValueCheck>() {
			@Override
			public void onToggle(ValueCheck value, boolean checked) {
				if (childrenToParent.containsKey(value.check) && !checked)
					checkToListItem.get(childrenToParent.get(value.check)).unCheckFiringEvents();
				if (!value.check.isLeaf() && checked)
					selectChildren((CompositeCheck) value.check);
				checkToggleHandler.onToggle(value.check);
			}

			@Override
			public void onChange(List selection) {
			}
		}, 0);
	}

	private void selectChildren(CompositeCheck value) {
		for (Check check : value.getChecks())
			select(check);
	}

	private void unSelectChildren(CompositeCheck value) {
		for (Check check : value.getChecks())
			unSelect(check);
	}

	private void unSelect(Check check) {
		if (!checkToListItem.containsKey(check)) return;
		checkToListItem.get(check).unCheck();
		if (check.isLeaf()) return;
		for (Check children : ((CompositeCheck) check).getChecks())
			unSelect(children);
	}

	public class ValueCheck implements HasValue {
		private final Check check;

		public ValueCheck(Check check) {
			this.check = check;
		}

		@Override
		public String getValue() {
			return check.getValue();
		}

		@Override
		public String getLabel() {
			return check.getLabel();
		}
	}

	public interface CheckToggleHandler {
		void onSelectAll(CompositeCheck check);

		void onSelectNone(CompositeCheck check);

		void onToggle(Check value);
	}
}
