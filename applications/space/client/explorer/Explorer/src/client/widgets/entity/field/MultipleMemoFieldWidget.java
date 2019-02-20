package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.TermList;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.*;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.MultipleFieldSortableValueListWidget;
import client.widgets.toolbox.ErasableListWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import java.util.HashMap;
import java.util.Map;

import static client.utils.KeyBoardUtils.isEnter;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class MultipleMemoFieldWidget extends MemoFieldWidget {

    private Map<String, Integer> indexesMap;
    private Anchor addMemo;
    private MultipleFieldSortableValueListWidget<String> values;

    public MultipleMemoFieldWidget(IsMemoFieldDisplay display, String layout, TranslatorService translator) {
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
        createAddMemo();
        super.createComponent();
    }

    private void createAddMemo() {
        addMemo = new Anchor(translator.translate(TranslatorService.OperationLabel.ADD));
        addMemo.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!input.getText().isEmpty())
                    multipleMemoFieldDisplay().add(input.getValue());
            }
        });
    }

    @Override
    protected void createInput() {
        super.createInput();
        input.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (event.getNativeEvent().getCtrlKey() && isEnter(event.getNativeKeyCode()))
                    multipleMemoFieldDisplay().add(input.getValue());
            }
        });
    }

    @Override
    protected void bind() {
        bind(values, toRule(StyleName.VALUES));
        bindKeepingStyles(addMemo, toRule(StyleName.ADD_MEMO));
        super.bind();
    }

    @Override
    protected void update(String value) {
        input.setText(value);
    }

    @Override
    protected void refreshComponent() {
        values.clear();
        indexesMap.clear();
        for (String value : multipleMemoFieldDisplay().getAllValues()) {
            values.addItem(value);
            indexesMap.put(value, indexesMap.size());
        }
        input.setText("");
        values.refresh();
    }

    private void createValueList() {
        values = new MultipleFieldSortableValueListWidget<>(createList());
        values.setChangePositionHandler(new MultipleFieldSortableValueListWidget.ChangePositionHandler<String>() {
            @Override
            public void onPositionChange(String item, int newPosition) {
                multipleMemoFieldDisplay().changeOrder(item, newPosition);
            }
        });
        values.setDeleteHandler(new MultipleFieldSortableValueListWidget.DeleteHandler<String>() {
            @Override
            public void onDelete(String item) {
                multipleMemoFieldDisplay().delete(indexesMap.get(item));
            }
        });
    }

    private ErasableListWidget<String> createList() {
        return new ErasableListWidget<>(new ErasableListWidget.ListItem.Builder<String>(translator), translator);
    }

    @Override
    protected TextFieldDisplay.Hook createHook() {
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

    private IsMultipleMemoFieldDisplay multipleMemoFieldDisplay() {
        return (IsMultipleMemoFieldDisplay) display;
    }

    public interface StyleName extends MemoFieldWidget.StyleName {
        String ADD_MEMO = "add-memo";
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(MultipleMemoFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new MultipleMemoFieldWidget((IsMultipleMemoFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-memo";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-memo-multiple";
	    }

    }
}
