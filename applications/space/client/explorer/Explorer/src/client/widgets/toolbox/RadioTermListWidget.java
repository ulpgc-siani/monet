package client.widgets.toolbox;

import client.core.model.types.Term;
import client.services.TranslatorService;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.utils.UUIDUtils;

import java.util.ArrayList;
import java.util.List;

public class RadioTermListWidget extends HTMLListWidget<Term> {

	private static final int INDENTATION = 25;
	private List<SelectHandler> selectHandlers = new ArrayList<>();

    public RadioTermListWidget(TranslatorService translator) {
        super(new TermListItem.Builder(UUIDUtils.uuid()), translator);
        addStyleName("radio");
    }

	public ListItem<Term> addItemWithLevel(Term item) {
		final ListItem<Term> listItem = super.addItem(item);
		listItem.getElement().getStyle().setPaddingLeft(INDENTATION, Style.Unit.PX);
		return listItem;
	}

	public void select(Term item) {
		TermListItem listItem = findItem(item);
        if (listItem != null)
			listItem.check();
	}

	public void selectFirst() {
		TermListItem listItem = findFirst();
		if (listItem != null)
			listItem.check();
	}

	public void addSelectHandler(SelectHandler handler) {
        selectHandlers.add(handler);
    }

	public static class TermListItem extends ListItem<Term> {
	    private final String groupName;
		private RadioButton component;

		public TermListItem(String groupName, Term value) {
            super(value);
	        this.groupName = groupName;
			init();
        }

		@Override
		protected Widget[] createControlOperations() {
			return new Widget[0];
		}

		@Override
		protected Widget createComponent() {
			Term value = getValue();

			component = new RadioButton(groupName, value.getLabel());
			component.setTitle(value.getLabel());
			component.addStyleName("radio");

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

		public static class Builder extends ListItem.Builder<Term> {
		    private final String groupName;

		    public Builder(String groupName) {
                super();
	            this.groupName = groupName;
            }

            @Override
            public ListItem<Term> build(Term value, Mode mode) {
                return new TermListItem(groupName, value);
            }
        }

    }

    public interface SelectHandler {
        void onSelect(Term value);
    }

    @Override
    protected void addListItemBehaviors(final ListItem<Term> listItem, int index) {
        ((TermListItem)listItem).addBooleanChangeHandler(new ListItem.ValueChangeHandler<Boolean>() {
	        @Override
	        public void onValueChange(ValueChangeEvent<Boolean> event) {
		        if (!event.getValue()) return;
		        notifySelect((TermListItem)listItem);
	        }

            @Override
            public void onChange(ListItem<Boolean> item) {
            }
        });
    }

    private void notifySelect(TermListItem item) {
        for (SelectHandler selectHandler : selectHandlers)
            selectHandler.onSelect(item.getValue());
    }

	private void check(TermListItem listItem, boolean checked) {
		if (checked)
			listItem.check();
	}

	private TermListItem findItem(Term item) {

		for (Widget listItem : getItems()) {
			if (!(listItem instanceof TermListItem)) continue;

			if (((TermListItem)listItem).getValue().equals(item))
				return (TermListItem) listItem;
		}

		return null;
	}

	private TermListItem findFirst() {

		for (Widget listItem : getItems()) {
			if (listItem instanceof TermListItem)
				return (TermListItem) listItem;
		}

		return null;
	}
}