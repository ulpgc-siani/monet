package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.entity.field.NumberFieldDefinition;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsNumberFieldDisplay;
import client.presenters.displays.entity.field.NumberFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.popups.NumberControlsPopupWidget;
import client.widgets.popups.dialogs.number.NumberControlUtils;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.DoubleBox;
import com.google.gwt.user.client.ui.LongBox;
import com.google.gwt.user.client.ui.ValueBox;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;
import cosmos.utils.StringUtils;

import static client.core.model.definition.entity.field.NumberFieldDefinition.Edition;

public class NumberFieldWidget extends ValueFieldWidget<ValueBox, NumberControlsPopupWidget> {

    private Timer timer;
    private String oldFormattedValue;

    public NumberFieldWidget(IsNumberFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.NUMBER);
    }

    @Override
    protected void createInput() {
        input = numberFieldDisplay().isDouble() ? new DoubleBox() : new LongBox();
    }

    @Override
    protected void addInputHandlers() {
        super.addInputHandlers();
        input.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                input.setText(input.getText().replace(numberFieldDisplay().getUnit(), "").trim());
            }
        });
        input.addBlurHandler(new BlurHandler() {
            @Override
            public void onBlur(BlurEvent event) {
                if (!numberFieldDisplay().hasValue()) return;
                input.setText(formatWithValueAndUnit(numberFieldDisplay().getValue().getValue()));
            }
        });
    }

    @Override
    protected void createPopup() {
        final NumberControlUtils utils = new NumberControlUtils(getEdition(), getRange(), numberFieldDisplay().getMinStep());
        popup = new NumberControlsPopupWidget(getPopupLayout(), input, utils, state);
        super.createPopup();
        popup.setIncrementValueHandler(new NumberControlsPopupWidget.UpdateValueHandler() {
            @Override
            public String update() {
                return formatWithValueAndUnit(increment());
            }
        });
        popup.setDecrementValueHandler(new NumberControlsPopupWidget.UpdateValueHandler() {
            @Override
            public String update() {
                return formatWithValueAndUnit(decrement());
            }
        });
        popup.setNumberChangeHandler(new NumberControlsPopupWidget.NumberChangeHandler() {
            @Override
            public void onChanged(java.lang.Number number) {
                sliderChanged(numberFieldDisplay().isDouble() ? number.doubleValue() : number.longValue());
            }
        });
    }

    private Edition getEdition() {
        return Edition.fromString(numberFieldDisplay().getEdition());
    }

    private NumberFieldDefinition.RangeDefinition getRange() {
        return numberFieldDisplay().hasRange() ? numberFieldDisplay().getRange() : numberFieldDisplay().getDefaultRange();
    }

    protected void sliderChanged(final Number number) {
        input.setText(formatNumberValue(number));
        updateDisplayWithDelay(number, 100);
    }

    private void updateDisplayValue(Number number) {
        oldFormattedValue = numberFieldDisplay().getValueAsString();
        numberFieldDisplay().setFormattedValue(formatWithValueAndUnit(number));
        numberFieldDisplay().setValue(numberFieldDisplay().createNumber(number));
    }

    protected java.lang.Number increment() {
        final Number number = numberFieldDisplay().increment().getValue();
        updateDisplayWithDelay(number, 500);
        return number;
    }

    protected java.lang.Number decrement() {
        final Number number = numberFieldDisplay().decrement().getValue();
        updateDisplayWithDelay(number, 500);
        return number;
    }

    private void updateDisplayWithDelay(final Number number, int delay) {
        if (timer != null) timer.cancel();
        refreshValue(number);
        timer = new Timer() {
            @Override
            public void run() {
                updateDisplayValue(number);
            }
        };
        timer.schedule(delay);
    }

    @Override
    protected void update(String number) {
        if (!isValidNumber(number)) return;
        if (display.hasValue() && number.equals(numberFieldDisplay().getValueAsString())) return;
        updateDisplayValue(numberFieldDisplay().isDouble() ? Double.valueOf(number) : Long.valueOf(number));
    }

    @Override
    protected void refreshWhenHasValue() {
        super.refreshWhenHasValue();
        refreshValue(numberFieldDisplay().getValue().getValue());
    }

    private void refreshValue(Number value) {
        input.setText(formatWithValueAndUnit(value));
        popup.refreshValue(value);
    }

    protected String formatWithValueAndUnit(java.lang.Number number) {
        return format(number, numberFieldDisplay().getFormat());
    }

    protected String formatNumberValue(java.lang.Number number) {
        return format(number, numberFieldDisplay().getFormatNumber());
    }

    private String format(java.lang.Number number, String format) {
        return numberFieldDisplay().hasFormat() ? number.toString() : NumberFormat.getFormat(format).format(number);
    }

    private boolean isValidNumber(String number) {
        return numberFieldDisplay().isDouble() ? StringUtils.isDouble(number) : StringUtils.isLong(number);
    }

    @Override
    protected FieldDisplay.Hook createHook() {
        return new FieldDisplay.Hook() {
            @Override
            public void value() {
                refresh();
            }

            @Override
            public void error(String error) {
                numberFieldDisplay().setFormattedValue(oldFormattedValue);
            }
        };
    }

    private IsNumberFieldDisplay numberFieldDisplay() {
        return (IsNumberFieldDisplay) display;
    }

    public interface StyleName extends ValueFieldWidget.StyleName {
        String NUMBER = "number";
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(NumberFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new NumberFieldWidget((IsNumberFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-number" + (!display.getEdition().isEmpty() ? "_" + display.getEdition().toLowerCase() : "");
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-number";
	    }
    }
}
