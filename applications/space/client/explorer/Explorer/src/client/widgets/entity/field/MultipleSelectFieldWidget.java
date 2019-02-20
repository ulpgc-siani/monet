package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsMultipleSelectFieldDisplay;
import client.presenters.displays.entity.field.MultipleSelectFieldDisplay;
import client.presenters.displays.entity.field.SelectFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.MultipleFieldSortableValueListWidget;
import client.widgets.toolbox.ErasableTermListWidget;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import java.util.HashMap;
import java.util.Map;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class MultipleSelectFieldWidget extends SelectFieldWidget {

	private Map<Term, Integer> indexesMap;
    private MultipleFieldSortableValueListWidget<Term> values;

    public MultipleSelectFieldWidget(IsMultipleSelectFieldDisplay display, String layout, TranslatorService translator) {
		super(display, layout, translator);
	}

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.MULTIPLE);
    }

	@Override
	protected void createField() {
		indexesMap = new HashMap<>();
		createValueList();
		super.createField();
	}

    @Override
    protected void update(Term value) {
        multipleSelectFieldDisplay().add(value);
        multipleSelectFieldDisplay().nextPage();
		if (popup != null) {
			popup.clearInput();
		}
    }

    @Override
    protected void bind() {
        bind(values, toRule(StyleName.VALUES));
        super.bind();
    }

    @Override
    protected SelectFieldDisplay.Hook createHook() {
        return new MultipleSelectFieldDisplay.Hook() {
            @Override
            public void historyOptions(SelectFieldDisplay.HistoryOptions historyOptions) {
	            popup.refreshHistoryOptions(historyOptions);
            }

            @Override
            public void optionsFailure() {
            }

            @Override
            public void page(TermList options) {
	            popup.addOptions(options);
            }

            @Override
            public void loading() {
                popup.showLoading();
            }

            @Override
            public void value() {
                refresh();
            }

            @Override
            public void error(String error) {
            }

            @Override
            public void values() {
                refresh();
            }
        };
    }

    @Override
	protected void refreshComponent() {
		values.clear();
		indexesMap.clear();
		for (Term term : multipleSelectFieldDisplay().getAllValues()) {
			values.addItem(term);
			indexesMap.put(term, indexesMap.size());
		}
        values.refresh();
		input.setText("");
	}

    @Override
    protected void showPopup() {
        super.showPopup();
        if (!multipleSelectFieldDisplay().isEmpty())
            multipleSelectFieldDisplay().nextPage();
    }

    private void createValueList() {
        values = new MultipleFieldSortableValueListWidget<>(new ErasableTermListWidget(translator, multipleSelectFieldDisplay().showCode()));
        values.setChangePositionHandler(new MultipleFieldSortableValueListWidget.ChangePositionHandler<Term>() {
            @Override
            public void onPositionChange(Term item, int newPosition) {
                multipleSelectFieldDisplay().changeOrder(item, newPosition);
            }
        });
        values.setDeleteHandler(new MultipleFieldSortableValueListWidget.DeleteHandler<Term>() {
            @Override
            public void onDelete(Term item) {
                multipleSelectFieldDisplay().delete(indexesMap.get(item));
            }
        });
    }

    private IsMultipleSelectFieldDisplay multipleSelectFieldDisplay() {
        return (IsMultipleSelectFieldDisplay) display;
    }

    public interface StyleName extends FieldWidget.StyleName { }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(MultipleSelectFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new MultipleSelectFieldWidget((IsMultipleSelectFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-select";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-select-multiple";
	    }
    }
}
