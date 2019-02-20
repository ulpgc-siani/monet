package client.widgets.popups;

import client.widgets.entity.field.ValueFieldWidget;
import client.widgets.entity.field.WidgetState;
import client.widgets.popups.dialogs.ButtonNumberControlsDialog;
import client.widgets.popups.dialogs.NumberControlsDialog;
import client.widgets.popups.dialogs.SliderNumberControlsDialog;
import client.widgets.popups.dialogs.number.NumberControlUtils;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.ValueBox;

import static client.core.model.definition.entity.field.NumberFieldDefinition.Edition;

public class NumberControlsPopupWidget extends FieldPopupWidget<ValueBox, NumberControlsDialog> {

    private final WidgetState state;
    private final NumberControlUtils utils;
    private UpdateValueHandler incrementValueHandler;
    private UpdateValueHandler decrementValueHandler;
    private NumberChangeHandler numberChangeHandler;

    public NumberControlsPopupWidget(String layout, ValueBox input, NumberControlUtils utils, WidgetState state) {
        super(layout, input);
        this.utils = utils;
        this.state = state;
        init();
    }

    public void refreshValue(Number number) {
        content.refreshValue(number);
    }

	@Override
	protected NumberControlsDialog createDialog(Element container) {
        if (utils.getEdition() == Edition.SLIDER)
            content = createSliderControl(container);
        else
            content = createButtonsControl(container);
		bindKeepingStyles(content, container);
		content.addStyleName(StyleName.CONTROLS);
		return content;
	}

    private NumberControlsDialog createButtonsControl(Element container) {
        final ButtonNumberControlsDialog controls = new ButtonNumberControlsDialog(container.getInnerHTML());
        controls.setIncrementValueHandler(new ButtonNumberControlsDialog.UpdateValueHandler() {
            @Override
            public String update() {
                return incrementValueHandler.update();
           }
        });
        controls.setDecrementValueHandler(new ButtonNumberControlsDialog.UpdateValueHandler() {
            @Override
            public String update() {
                return decrementValueHandler.update();
            }
        });
        return controls;
    }

    private SliderNumberControlsDialog createSliderControl(Element container) {
        final SliderNumberControlsDialog slider = new SliderNumberControlsDialog(container.getInnerHTML(), utils);
        slider.setNumberChangeHandler(new SliderNumberControlsDialog.NumberChangeHandler() {
            @Override
            public void onChanged(java.lang.Number number) {
                numberChangeHandler.onChanged(number);
            }
        });
        return slider;
    }

    @Override
    public void show() {
        super.show();
        content.draw();
        state.enable();
    }

    public void setIncrementValueHandler(UpdateValueHandler incrementValueHandler) {
        this.incrementValueHandler = incrementValueHandler;
    }

    public void setDecrementValueHandler(UpdateValueHandler decrementValueHandler) {
        this.decrementValueHandler = decrementValueHandler;
    }

    public void setNumberChangeHandler(NumberChangeHandler numberChangeHandler) {
        this.numberChangeHandler = numberChangeHandler;
    }

	public interface StyleName extends ValueFieldWidget.StyleName {
		String CONTROLS = "controls";
	}

    public interface UpdateValueHandler {
        String update();
    }

    public interface NumberChangeHandler {
        void onChanged(java.lang.Number number);
    }
}
