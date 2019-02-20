package client.widgets.popups.dialogs;

import client.widgets.popups.dialogs.number.NumberControlUtils;
import client.widgets.popups.dialogs.number.Slider;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class SliderNumberControlsDialog extends NumberControlsDialog {
    private final Slider slider;

    public SliderNumberControlsDialog(String layout, NumberControlUtils utils) {
        super(layout);
        this.slider = new Slider(utils.getRange().getMin(), utils.getRange().getMax());
        slider.setStepSize(utils.getMinStep());
        slider.setValue((double) ((utils.getRange().getMin() + utils.getRange().getMax()) / 2));
        slider.setNumberOfTicks(10);
        slider.setNumberOfLabels(5);
        bind(slider, $(getElement()).children(toRule(StyleName.SLIDER)).get(0));
    }

    public void setNumberChangeHandler(final NumberChangeHandler handler) {
        slider.addValueChangeHandler(new ValueChangeHandler<java.lang.Number>() {
            @Override
            public void onValueChange(ValueChangeEvent<java.lang.Number> event) {
                handler.onChanged(event.getValue());
            }
        });
    }

    @Override
    public void refreshValue(java.lang.Number value) {
        slider.setValue(value.doubleValue());
    }

    @Override
    public void draw() {
        slider.draw();
    }

    @Override
    public void focus() {
        slider.setFocus(true);
    }

    public interface NumberChangeHandler {
        void onChanged(java.lang.Number number);
    }

    public interface StyleName {
        String SLIDER = "slider";
    }
}