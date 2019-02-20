package client.widgets.popups;

import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.user.client.ui.ValueBoxBase;
import com.google.gwt.user.client.ui.Widget;

import static com.google.gwt.dom.client.Style.Unit;

public abstract class FieldPopupWidget<Input extends ValueBoxBase, WidgetType extends Widget> extends PopUpWidget<WidgetType> {

    protected final Input input;

    public FieldPopupWidget(String layout, Input input) {
        super(layout);
        this.input = input;
    }

	public FieldPopupWidget(String layout, WidgetType dialog, Input input) {
		super(layout, dialog);
		this.input = input;
	}

	@Override
	protected final WidgetType createContent(Element container) {
		return createDialog(container);
	}

	protected abstract WidgetType createDialog(Element container);

    @Override
    protected void updateWidth() {
        getElement().getStyle().setWidth(calculateWidth(), Unit.PX);
    }

    @Override
    protected boolean clickedOutsideContent(NativeEvent event) {
        return clickedOutside(content, event) && clickedOutside(input, event);
    }

    private int calculateWidth() {
        return sizeCalculator.getWidth();
    }
}
