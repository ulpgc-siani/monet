package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.factory.TypeFactory;
import client.core.model.types.Term;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.BooleanFieldDisplay;
import client.presenters.displays.entity.field.IsBooleanFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.InputKeyFilter;
import client.widgets.popups.BooleanFieldOptionsPopupWidget;
import client.widgets.popups.dialogs.OptionListDialog;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static client.services.TranslatorService.Label;
import static client.utils.KeyBoardUtils.isEnter;
import static client.widgets.popups.dialogs.OptionListDialog.TextOption;

public class BooleanFieldWidget extends ValueFieldWidget<InputKeyFilter, BooleanFieldOptionsPopupWidget> {

    public BooleanFieldWidget(IsBooleanFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.BOOLEAN);
    }

    @Override
    protected void createInput() {
        input = new InputKeyFilter().allowArrows();
    }

    @Override
    protected void createPopup() {
        TypeFactory factory = display.getTypeFactory();
	    popup = new BooleanFieldOptionsPopupWidget(getPopupLayout(), input, state, translator);
        super.createPopup();
	    popup.refreshOptions(factory.createTermList(new Term[] { factory.createTerm("true", translator.translate(Label.YES)), factory.createTerm("false", translator.translate(Label.NO))}));
        addPopupHandlers();
    }

    @Override
    protected void update(String value) {
        if (value.isEmpty())
            display.removeValue();
        else
            booleanFieldDisplay().setValue(translator.translate(Label.YES).equals(value));
    }

    @Override
    protected void refreshWhenHasValue() {
        super.refreshWhenHasValue();
        if (booleanFieldDisplay().getValue())
            input.setValue(translator.translate(Label.YES));
        else
            input.setValue(translator.translate(Label.NO));
    }

    private IsBooleanFieldDisplay booleanFieldDisplay() {
        return (IsBooleanFieldDisplay) display;
    }

    @Override
    protected void addInputHandlers() {
        input.addKeyDownHandler(keyDownHandler());
        input.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                input.setValue(value());
            }
        });
        super.addInputHandlers();
    }

    private void addPopupHandlers() {
	    popup.addKeyDownHandler(keyDownHandler());
	    popup.setOnOptionSelected(new OptionListDialog.OptionSelectedHandler() {
            @Override
            public void onSelected(TextOption option) {
                update(option.getText());
	            popup.focusInput();
            }
        });
    }

    private KeyDownHandler keyDownHandler() {
        return new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (isEnter(event.getNativeKeyCode())) popup.focusInput();
            }
        };
    }

    private String value() {
        if (!display.hasValue()) return "";
        if (booleanFieldDisplay().getValue())
            return translator.translate(Label.YES);
        return translator.translate(Label.NO);
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(BooleanFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {

            return new BooleanFieldWidget((IsBooleanFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-boolean";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return display.isPoll() ? "component-boolean-poll" : "component-boolean";
	    }
    }

    public interface StyleName extends ValueFieldWidget.StyleName {
        String BOOLEAN = "boolean";
    }
}
