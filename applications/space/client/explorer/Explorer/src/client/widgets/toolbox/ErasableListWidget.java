package client.widgets.toolbox;

import client.core.model.types.Term;
import client.services.TranslatorService;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;

import static client.services.TranslatorService.ListLabel;
import static com.google.gwt.query.client.GQuery.$;

public class ErasableListWidget<T> extends HTMLListWidget<T> {
    private List<DeleteHandler<T>> deleteHandlers = new ArrayList<>();

	public ErasableListWidget(ListItem.Builder builder, TranslatorService translator) {
		super(builder, translator);
		addStyleName(StyleName.ERASABLE);
	}

	public void addDeleteHandler(DeleteHandler<T> handler) {
        deleteHandlers.add(handler);
    }

    public static class ListItem<T> extends HTMLListWidget.ListItem<T> {
	    protected TranslatorService translator;
	    private Anchor erasableOperation;

	    public ListItem(T value, TranslatorService translator) {
		    this(value, translator, true);
	    }

	    public ListItem(T value, TranslatorService translator, boolean init) {
			super(value);
		    this.translator = translator;
		    if (init)
		        init();
		}

	    @Override
	    protected Widget[] createControlOperations() {
		    return new Widget[0];
	    }

	    @Override
	    protected Widget createComponent() {
		    return new InlineLabel(getLabel());
	    }

	    @Override
	    protected Widget[] createOperations() {
		    return new Widget[] { addErasableOperation() };
	    }

        public HandlerRegistration addDeleteHandler(com.google.gwt.event.dom.client.ClickHandler handler) {
            return erasableOperation.addClickHandler(handler);
        }

        public static class Builder<T> extends HTMLListWidget.ListItem.Builder<T> {
	        protected final TranslatorService translator;

	        public Builder(TranslatorService translator) {
				super();
				this.translator = translator;
			}

			@Override
			public HTMLListWidget.ListItem<T> build(T value, Mode mode) {
				return new ListItem<>(value, translator);
			}
        }

	    protected String getLabel() {
		    return value.toString();
	    }

	    protected Widget addErasableOperation() {
		    erasableOperation = new Anchor();
		    erasableOperation.setTitle(translator.translate(ListLabel.DELETE));
		    $(erasableOperation).addClass(StyleName.DELETE, StyleName.OPERATION);
		    return erasableOperation;
        }
    }

	public static class TermListItem extends ListItem<Term> {

		public TermListItem(Term value, TranslatorService translator) {
			super(value, translator);
		}

		@Override
		protected String getLabel() {
			return value.getLabel();
		}

		public static class Builder extends ListItem.Builder<Term> {

			public Builder(TranslatorService translator) {
				super(translator);
			}

			@Override
			public HTMLListWidget.ListItem build(Term value, Mode mode) {
				return new TermListItem(value, translator);
			}

		}
	}

	public static class EditableListItem<T> extends ListItem<T> {
		public EditableListItem(T value, TranslatorService translator) {
			super(value, translator);
		}

		@Override
		protected Widget createComponent() {
			final TextBox component = new TextBox();
			component.setValue(getLabel(), false);
			component.addChangeHandler(new com.google.gwt.event.dom.client.ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					notifyChange((T) component.getValue());
				}
			});
			component.addFocusHandler(new FocusHandler() {
				@Override
				public void onFocus(FocusEvent event) {
					component.selectAll();
				}
			});
			return component;
		}

		public static class Builder<T> extends ListItem.Builder<T> {

			public Builder(TranslatorService translator) {
				super(translator);
			}

			@Override
			public HTMLListWidget.ListItem<T> build(T value, Mode mode) {
				ListItem<T> widget = new EditableListItem<>(value, translator);
                $(widget.getElement()).find(StyleName.INPUT).addClass(StyleName.FOCUSABLE);
				return widget;
			}
		}
	}

	public static class WidgetListItem<T extends Widget> extends ListItem<T> {
		public WidgetListItem(T value, TranslatorService translator) {
			super(value, translator);
		}

		@Override
		protected Widget createComponent() {
			return value;
		}

		@Override
		protected Widget[] createOperations() {
			return new Widget[] { addErasableOperation() };
		}

		public static class Builder<T extends Widget> extends ListItem.Builder<T> {

			public Builder(TranslatorService translator) {
				super(translator);
			}

			@Override
			public HTMLListWidget.ListItem<T> build(T value, Mode mode) {
				return new WidgetListItem<>(value, translator);
			}
		}
	}

	public static class SelectableListItem<T> extends ListItem<T> {

		private final CheckBox checkBox;

		public SelectableListItem(T value, TranslatorService translator) {
			super(value, translator);
			this.checkBox = new CheckBox();
			addControlOperation(checkBox);
		}

		@Override
		public HandlerRegistration addValueChangeHandler(com.google.gwt.event.logical.shared.ValueChangeHandler<T> handler) {
			return checkBox.addValueChangeHandler((com.google.gwt.event.logical.shared.ValueChangeHandler<Boolean>) handler);
		}

		public boolean isSelected() {
			return checkBox.getValue();
		}

		public void select(boolean selected) {
			checkBox.setValue(selected, true);
		}

		public static class Builder<T> extends ListItem.Builder<T> {

			public Builder(TranslatorService translator) {
				super(translator);
			}

			@Override
			public HTMLListWidget.ListItem<T> build(T value, Mode mode) {
				return new SelectableListItem<>(value, translator);
			}
		}
	}

	public interface DeleteHandler<T> {
		void onDelete(T value);
	}

    @Override
    protected void addListItemBehaviors(final HTMLListWidget.ListItem<T> listItem, int index) {
        super.addListItemBehaviors(listItem, index);

        ((ListItem)listItem).addDeleteHandler(new com.google.gwt.event.dom.client.ClickHandler() {
	        @Override
	        public void onClick(ClickEvent event) {
		        notifyDelete(listItem.getValue());
		        event.stopPropagation();
	        }
        });
    }

	private void notifyDelete(T value) {
		for (DeleteHandler<T> handler : deleteHandlers)
            handler.onDelete(value);
	}

	public interface StyleName extends HTMLListWidget.StyleName {
		String ERASABLE = "erasable";
	}
}