package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsSerialFieldDisplay;
import client.presenters.displays.entity.field.SerialFieldDisplay;
import client.services.TranslatorService;
import client.widgets.popups.FieldPopupWidget;
import client.widgets.entity.FieldWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

public class SerialFieldWidget extends ValueFieldWidget<TextBox, FieldPopupWidget> {

    public SerialFieldWidget(IsSerialFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.SERIAL);
    }

    @Override
    protected void createInput() {
        input = new TextBox();
    }

    @Override
    protected void createPopup() { }

    @Override
    protected boolean popupShouldBeAdded() {
        return false;
    }

    @Override
    protected void update(String value) {
        serialFieldDisplay().setValue(value);
    }

    @Override
    protected void refreshComponent() {
        super.refreshComponent();
        input.setText(serialFieldDisplay().getValue());
    }

    private IsSerialFieldDisplay serialFieldDisplay() {
        return (IsSerialFieldDisplay) display;
    }

    public interface StyleName extends ValueFieldWidget.StyleName {
        String SERIAL = "serial";
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(SerialFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new SerialFieldWidget((IsSerialFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-serial";
	    }
    }
}
