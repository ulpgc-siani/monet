package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsUriFieldDisplay;
import client.presenters.displays.entity.field.UriFieldDisplay;
import client.services.TranslatorService;
import client.widgets.popups.FieldPopupWidget;
import client.widgets.entity.FieldWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

public class UriFieldWidget extends ValueFieldWidget<TextBox, FieldPopupWidget> {

    public UriFieldWidget(IsUriFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.URI);
    }

    @Override
    protected void createInput() {
        input = new TextBox();
        input.setReadOnly(display.isReadonly());
    }

	@Override
	protected void createPopup() {
		removePopupFromLayout();
	}

    @Override
    protected boolean popupShouldBeAdded() {
        return false;
    }

	@Override
    protected void update(String value) {
        uriFieldDisplay().setValue(uriFieldDisplay().createUri(value));
    }

    @Override
    protected void refreshWhenHasValue() {
        super.refreshWhenHasValue();
        input.setText(uriFieldDisplay().getValue().toString());
    }

    private IsUriFieldDisplay uriFieldDisplay() {
        return (IsUriFieldDisplay) display;
    }

    public interface StyleName extends ValueFieldWidget.StyleName {
        String URI = "uri";
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(UriFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new UriFieldWidget((IsUriFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-uri";
	    }
    }
}
