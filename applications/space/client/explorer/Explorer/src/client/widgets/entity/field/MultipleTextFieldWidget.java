package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.TermList;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsMultipleTextFieldDisplay;
import client.presenters.displays.entity.field.MultipleTextFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.MultipleFieldSortableValueListWidget;
import client.widgets.toolbox.ErasableListWidget;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import java.util.HashMap;
import java.util.Map;

import static client.utils.KeyBoardUtils.isDeleteOrBackspaceKey;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class MultipleTextFieldWidget extends TextFieldWidget {

    private Map<String, Integer> indexesMap;
    private MultipleFieldSortableValueListWidget<String> values;

    public MultipleTextFieldWidget(IsMultipleTextFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.MULTIPLE);
    }

    @Override
    protected void createComponent() {
        indexesMap = new HashMap<>();
        createValueList();
        super.createComponent();
        hideClearButton();
    }

	@Override
	protected void createInput() {
		super.createInput();
		input.addKeyDownHandler(keyDownHandler());
	}

    @Override
    protected void bind() {
        bind(values, toRule(StyleName.VALUES));
        super.bind();
    }

    @Override
    protected void refreshComponent() {
        values.clear();
        indexesMap.clear();
        for (String value : multipleTextFieldDisplay().getAllValues()) {
            values.addItem(value);
            indexesMap.put(value, indexesMap.size());
        }
        values.refresh();
        input.setText("");
    }

    @Override
    protected void update(String value) {
        if (value.isEmpty()) return;
        addValue(value);
    }

    @Override
    protected MultipleTextFieldDisplay.Hook createHook() {
        return new MultipleTextFieldDisplay.Hook() {
            @Override
            public void values() {
                refresh();
            }

            @Override
            public void value() {
                refresh();
            }

            @Override
            public void error(String error) {
            }

            @Override
            public void history(TermList options) {
	            popup.refreshOptions(options);
            }

            @Override
            public void historyFailure() {
                showFailureLoadingHistory();
            }
        };
    }

    @Override
    protected void addKeyUpHandler() {
    }

    private void createValueList() {
        values = new MultipleFieldSortableValueListWidget<>(createList());
        values.setChangePositionHandler(new MultipleFieldSortableValueListWidget.ChangePositionHandler<String>() {
            @Override
            public void onPositionChange(String item, int newPosition) {
                multipleTextFieldDisplay().changeOrder(item, newPosition);
            }
        });
        values.setDeleteHandler(new MultipleFieldSortableValueListWidget.DeleteHandler<String>() {
            @Override
            public void onDelete(String item) {
                multipleTextFieldDisplay().delete(indexesMap.get(item));
            }
        });
    }

    private ErasableListWidget<String> createList() {
        return new ErasableListWidget<>(new ErasableListWidget.ListItem.Builder(translator), translator);
    }

    private KeyDownHandler keyDownHandler() {
        return new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (isDeleteOrBackspaceKey(event.getNativeKeyCode()) && inputValue().isEmpty())
                    deleteLastValue();
            }
        };
    }

    private String inputValue() {
        return input.getValue();
    }

    private void addValue(String value) {
        multipleTextFieldDisplay().add(value);
    }

    private void deleteLastValue() {
        multipleTextFieldDisplay().delete(multipleTextFieldDisplay().size() - 1);
    }

    private IsMultipleTextFieldDisplay multipleTextFieldDisplay() {
        return (IsMultipleTextFieldDisplay) display;
    }

    public interface StyleName extends FieldWidget.StyleName { }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(MultipleTextFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new MultipleTextFieldWidget((IsMultipleTextFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-text";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-text-multiple";
	    }
    }
}
