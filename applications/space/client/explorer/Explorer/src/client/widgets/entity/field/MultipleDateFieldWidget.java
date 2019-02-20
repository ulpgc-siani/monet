package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.MultipleFieldDisplay;
import client.presenters.displays.entity.field.IsDateFieldDisplay;
import client.presenters.displays.entity.field.IsMultipleDateFieldDisplay;
import client.presenters.displays.entity.field.MultipleDateFieldDisplay;
import client.services.TranslatorService;
import client.services.TranslatorService.OperationLabel;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.MultipleFieldSortableValueListWidget;
import client.widgets.popups.PopUpWidget;
import client.widgets.toolbox.ErasableListWidget;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;
import cosmos.types.Date;

import java.util.HashMap;
import java.util.Map;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class MultipleDateFieldWidget extends DateFieldWidget {

    private MultipleFieldSortableValueListWidget<String> values;
    private Map<String, Integer> indexesMap;
    private Map<String, Date> stringToDate;

    public MultipleDateFieldWidget(IsDateFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(MultipleDateFieldWidget.StyleName.MULTIPLE);
    }

    @Override
    protected void createField() {
        indexesMap = new HashMap<>();
        stringToDate = new HashMap<>();
        createValueList();
        super.createField();
    }

    @Override
    protected void createInput() {
        super.createInput();
        input.getElement().setPropertyString("placeholder", "+");
    }

    @Override
    protected void createPopup() {
        super.createPopup();
        if (multipleDateFieldDisplay().allowLessPrecision())
	        popup.allowLessPrecision(translator.translate(TranslatorService.Label.ALLOW_LESS_PRECISION), translator.translate(OperationLabel.ACCEPT));
        else
	        popup.addHandler(new PopUpWidget.PopupHandler() {
                @Override
                public void onOutsideClick() {
                    if (multipleDateFieldDisplay().dateIsValid(popup.getDate()))
                        multipleDateFieldDisplay().add(popup.getDate());
                }
            });
    }

    @Override
    protected void update(Date value) {
        if (value == null) return;
	    popup.setDate(value);
        if (!multipleDateFieldDisplay().dateIsValid(value)) return;
        input.setText(multipleDateFieldDisplay().formatDate(value));
        if (multipleDateFieldDisplay().allowLessPrecision() || !multipleDateFieldDisplay().hasTime())
            multipleDateFieldDisplay().add(value);

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
        for (Date value : multipleDateFieldDisplay().getAllValues()) {
            values.addItem(multipleDateFieldDisplay().formatDate(value));
            indexesMap.put(multipleDateFieldDisplay().formatDate(value), indexesMap.size());
            stringToDate.put(multipleDateFieldDisplay().formatDate(value), value);
        }
        values.refresh();
        initValue();
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

    @Override
    protected void initValue() {
        Date date = multipleDateFieldDisplay().createDate();
        if (!multipleDateFieldDisplay().isNearDate())
            date = date.removeMonth().removeDay();
	    popup.setDate(date);
        input.setText("");
        $(this).find(StyleName.INPUT).get(0).setPropertyString("placeholder", "+");
    }

    private void createValueList() {
        values = new MultipleFieldSortableValueListWidget<>(createList());
        values.setChangePositionHandler(new MultipleFieldSortableValueListWidget.ChangePositionHandler<String>() {
            @Override
            public void onPositionChange(String item, int newPosition) {
                multipleDateFieldDisplay().changeOrder(stringToDate.get(item), newPosition);
            }
        });
        values.setDeleteHandler(new MultipleFieldSortableValueListWidget.DeleteHandler<String>() {
            @Override
            public void onDelete(String item) {
                multipleDateFieldDisplay().delete(indexesMap.get(item));
            }
        });
    }

    private ErasableListWidget<String> createList() {
        return new ErasableListWidget<>(new ErasableListWidget.ListItem.Builder(translator), translator);
    }

    private IsMultipleDateFieldDisplay multipleDateFieldDisplay() {
        return (IsMultipleDateFieldDisplay) display;
    }

    public interface StyleName extends FieldWidget.StyleName { }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(MultipleDateFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new MultipleDateFieldWidget((IsMultipleDateFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-date";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-date-multiple";
	    }
    }
}
