package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.DateFieldDisplay;
import client.presenters.displays.entity.field.IsDateFieldDisplay;
import client.presenters.displays.entity.field.IsFieldDisplay;
import client.services.TranslatorService;
import client.utils.DateUtils;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.InputKeyFilter;
import client.widgets.popups.DatePopupWidget;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;
import cosmos.types.Date;
import cosmos.types.DateComponent;

public class DateFieldWidget extends ValueFieldWidget<InputKeyFilter, DatePopupWidget> {

    protected DateUtils dateUtils;

    protected DateFieldWidget(IsFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
        initValue();
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.DATE);
    }

    @Override
    protected void createInput() {
        input = new InputKeyFilter().allowArrows();
    }

    @Override
    protected void createPopup() {
	    dateUtils = new DateUtils(dateFieldDisplay().getRange(), dateFieldDisplay().getPrecision(), dateFieldDisplay().isNearDate());
	    popup = new DatePopupWidget(getPopupLayout(), input, dateFieldDisplay().getDateBuilder(), translator, dateUtils);
        if (dateFieldDisplay().hasTime())
	        popup.hasTime();
        super.createPopup();
	    popup.setDateSelectedHandler(new DatePopupWidget.DateSelectedHandler() {
            @Override
            public void dateSelected(Date date) {
                update(date);
            }
        });
    }

    protected void update(Date value) {
        if (value == null) return;
        if (dateFieldDisplay().dateIsValid(value))
            dateFieldDisplay().setValue(value);
        else
	        popup.setDate(value);
    }

    @Override
    protected void refreshWhenHasValue() {
        super.refreshWhenHasValue();
        input.setText(dateFieldDisplay().getValueAsString());
        if (popup == null) return;
        if (dateFieldDisplay().allowLessPrecision()) return;
        popup.setDate(dateFieldDisplay().getValue());
        popup.hide();
    }

    @Override
    protected void showPopup() {
        super.showPopup();
        if (display.hasValue()) popup.setDate(dateFieldDisplay().getValue());
    }

    private IsDateFieldDisplay dateFieldDisplay() {
        return (IsDateFieldDisplay) display;
    }

    protected void initValue() {
        if (display.hasValue()) return;
        Date date = dateFieldDisplay().createDate();
        if (dateFieldDisplay().isNearDate())
            update(date);
        else if (popup != null)
	        popup.setDate(date.removeComponentsFrom(DateComponent.MONTH));
    }

    public interface StyleName extends ValueFieldWidget.StyleName {
        String DATE = "date";
    }

    public static class Builder extends FieldWidget.Builder {

	    public Builder() {
		    super();
	    }

	    public static void register(){
            registerBuilder(DateFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new DateFieldWidget((IsDateFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-date";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-date";
	    }
    }
}
