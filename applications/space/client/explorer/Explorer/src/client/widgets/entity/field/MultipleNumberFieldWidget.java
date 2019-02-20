package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.Number;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.MultipleFieldDisplay;
import client.presenters.displays.entity.field.IsMultipleNumberFieldDisplay;
import client.presenters.displays.entity.field.IsNumberFieldDisplay;
import client.presenters.displays.entity.field.MultipleNumberFieldDisplay;
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

import static client.utils.KeyBoardUtils.isEnter;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class MultipleNumberFieldWidget extends NumberFieldWidget {

    private Map<client.core.model.types.Number, Integer> indexesMap;
    private MultipleFieldSortableValueListWidget<String> values;

    public MultipleNumberFieldWidget(IsNumberFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(FieldWidget.StyleName.MULTIPLE);
    }

    @Override
    protected void createComponent() {
        indexesMap = new HashMap<>();
        createValueList();
        super.createComponent();
        input.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (isEnter(event.getNativeKeyCode()) && multipleNumberFieldDisplay().isValidNumber(input.getText())) {
                    multipleNumberFieldDisplay().add(multipleNumberFieldDisplay().valueOfString(input.getText()));
                    state.enable();
                }
            }
        });
    }

    @Override
    protected void sliderChanged(java.lang.Number number) {
        input.setText(formatNumberValue(number));
    }

    @Override
    protected java.lang.Number increment() {
        Number value = multipleNumberFieldDisplay().increment(input.getText());
        input.setText(formatWithValueAndUnit(value.getValue()));
        return value.getValue();
    }

    @Override
    protected java.lang.Number decrement() {
        Number value = multipleNumberFieldDisplay().decrement(input.getText());
        input.setText(formatWithValueAndUnit(value.getValue()));
        return value.getValue();
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
        for (Number number : multipleNumberFieldDisplay().getAllValues()) {
            values.addItem(formatWithValueAndUnit(number.getValue()));
            indexesMap.put(number, indexesMap.size());
        }
        values.refresh();
    }

    @Override
    protected FieldDisplay.Hook createHook() {
        return new MultipleFieldDisplay.Hook() {
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
        };
    }

    private void createValueList() {
        values = new MultipleFieldSortableValueListWidget<>(createList());
        values.setChangePositionHandler(new MultipleFieldSortableValueListWidget.ChangePositionHandler<String>() {
            @Override
            public void onPositionChange(String item, int newPosition) {
                multipleNumberFieldDisplay().changeOrder(multipleNumberFieldDisplay().valueWithoutUnit(item), newPosition);
            }
        });
        values.setDeleteHandler(new MultipleFieldSortableValueListWidget.DeleteHandler<String>() {
            @Override
            public void onDelete(String item) {
                multipleNumberFieldDisplay().delete(indexesMap.get(multipleNumberFieldDisplay().valueWithoutUnit(item)));
            }
        });
    }

    private ErasableListWidget<String> createList() {
        return new ErasableListWidget<>(new ErasableListWidget.ListItem.Builder(translator), translator);
    }

    private IsMultipleNumberFieldDisplay multipleNumberFieldDisplay() {
        return (IsMultipleNumberFieldDisplay) display;
    }

    public interface StyleName extends FieldWidget.StyleName { }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(MultipleNumberFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new MultipleNumberFieldWidget((IsMultipleNumberFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
            return "dialog-number" + (!display.getEdition().isEmpty() ? "_" + display.getEdition().toLowerCase() : "");
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-number-multiple";
	    }
    }
}
