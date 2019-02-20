package client.widgets.toolbox;

import client.core.model.List;
import client.core.system.MonetList;
import client.services.TranslatorService;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.model.HasValue;

public class CheckListWidget<T extends HasValue> extends HTMLListWidget<T> {

    private static final int INDENTATION = 25;
	private List<T> selectedItems = new MonetList<>();
    private List<ToggleHandler<T>> toggleHandlers = new MonetList<>();
	private Timer changeTimer;
	private int notificationDelay;

	public CheckListWidget(TranslatorService translator) {
        super(new CheckListItem.Builder(), translator);
        getElement().addClassName(StyleName.CHECK);
		createChangeTimer();
    }

    public ListItem<T> addItemWithLevel(T item) {
        final ListItem<T> listItem = super.addItem(item);
        listItem.getElement().getStyle().setPaddingLeft(INDENTATION, Style.Unit.PX);
        return listItem;
    }

    public ListItem<T> add(int index, T item, boolean checked) {
        CheckListItem listItem = (CheckListItem)super.addItem(index, item);
        check(listItem, checked);
        return listItem;
    }

    public ListItem<T> add(T item, boolean checked) {
        CheckListItem listItem = (CheckListItem)super.addItem(item);
        check(listItem, checked);
        return listItem;
    }

    public void select(T item) {
        CheckListItem listItem = findItem(item);
        if (listItem == null) return;
        listItem.checkFiringEvents();
    }

    public int getCheckedCount() {
        int count = 0;
        for (Widget item : this.getItems()) {
            CheckListItem checkItem = (CheckListItem)item;
            if (checkItem.isChecked())
                count++;
        }
        return count;
    }

    public void selectNone() {
        for (Widget item : getItems()) {
            CheckListItem checkItem = (CheckListItem)item;
            if (checkItem.isChecked())
                checkItem.unCheckFiringEvents();
        }
    }

    public void addToggleHandler(ToggleHandler handler, int notificationDelay) {
        this.toggleHandlers.add(handler);
	    this.notificationDelay = notificationDelay;
    }

    public static class CheckListItem<T extends HasValue> extends ListItem<T> {
        public static final boolean FIRE_EVENTS = true;
        private CheckBox component;

        public CheckListItem(T value) {
            super(value);
            init();
        }

        @Override
        protected Widget[] createControlOperations() {
            return new Widget[0];
        }

        @Override
        protected Widget createComponent() {
            T value = this.getValue();

            component = new CheckBox(value.getLabel());
            component.addStyleName("check");

            return component;
        }

        @Override
        protected Widget[] createOperations() {
            return new Widget[0];
        }

        public HandlerRegistration addBooleanChangeHandler(ValueChangeHandler<Boolean> handler) {
            return component.addValueChangeHandler(handler);
        }

        public void check() {
            component.setValue(true);
        }

        public void unCheck() {
            component.setValue(false);
        }

        public void checkFiringEvents() {
            component.setValue(true, FIRE_EVENTS);
        }

        public void toggle() {
            component.setValue(!isChecked());
        }

        public void unCheckFiringEvents() {
            component.setValue(false, FIRE_EVENTS);
        }

	    public boolean isChecked() {
		    return component.getValue();
	    }

        public static class Builder<T extends HasValue> extends ListItem.Builder<T> {

            public Builder() {
                super();
            }

            @Override
            public ListItem<T> build(T value, Mode mode) {
                return new CheckListItem<>(value);
            }
        }

    }

    public interface ToggleHandler<T> {
        void onToggle(T value, boolean checked);
	    void onChange(List<T> selection);
    }

	@Override
    protected void addListItemBehaviors(final ListItem listItem, int index) {
        ((CheckListItem)listItem).addBooleanChangeHandler(new ListItem.ValueChangeHandler<T>() {
            @Override
            public void onValueChange(ValueChangeEvent<T> event) {
                CheckListItem item = (CheckListItem) listItem;

                if (item.isChecked())
                    selectedItems.add((T) item.getValue());
                else
                    selectedItems.remove(item.getValue());

                notifyToggle(item);
                notifyChangeDelayed();
            }

            @Override
            public void onChange(ListItem<T> item) {
            }
        });
    }

    private void notifyToggle(CheckListItem item) {
        for (ToggleHandler toggleHandler : toggleHandlers)
            toggleHandler.onToggle(item.getValue(), item.isChecked());
    }

	private void notifyChangeDelayed() {
		if (notificationDelay <= 0)
			notifyChange();

		changeTimer.cancel();
		changeTimer.schedule(notificationDelay);
	}

	private void notifyChange() {
		for (ToggleHandler toggleHandler : toggleHandlers)
			toggleHandler.onChange(selectedItems);
	}

    private void check(CheckListItem listItem, boolean checked) {
        if (checked)
            listItem.checkFiringEvents();
    }

    private CheckListItem findItem(T item) {

        for (Widget listItem : getItems()) {
            if (!(listItem instanceof CheckListItem)) continue;

            if (((CheckListItem)listItem).getValue().equals(item))
                return (CheckListItem) listItem;
        }

        return null;
    }

	private void createChangeTimer() {
		changeTimer = new Timer() {
			@Override
			public void run() {
				notifyChange();
			}
		};
	}

    public interface StyleName {
        String CHECK = "check";
    }

}
