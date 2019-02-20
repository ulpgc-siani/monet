package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsMemoFieldDisplay;
import client.presenters.displays.entity.field.MemoFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

public class MemoFieldWidget extends TextFieldWidget {

    private static final int LINES = 5;

    public MemoFieldWidget(IsMemoFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.MEMO);
    }

    @Override
    protected void createInput() {
        input = new TextArea();
        ((TextArea) input).setVisibleLines(LINES);
    }

    @Override
    protected void addMessageWhenEmpty(String message) {
        super.addMessageWhenEmpty(message);
        if (message != null) input.getElement().setPropertyString("placeholder", message);
    }

    @Override
    protected void onEnterDown() {
    }

    public interface StyleName extends ValueFieldWidget.StyleName {
        String MEMO = "memo";
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(MemoFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new MemoFieldWidget((IsMemoFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-memo";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-memo";
	    }

    }
}
