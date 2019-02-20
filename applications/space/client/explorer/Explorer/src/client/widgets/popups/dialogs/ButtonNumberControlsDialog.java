package client.widgets.popups.dialogs;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class ButtonNumberControlsDialog extends NumberControlsDialog {

    private final HTML valueLabel;
    private UpdateValueHandler incrementValueHandler;
    private UpdateValueHandler decrementValueHandler;
    private Anchor incrementButton;
    private Anchor decrementButton;

    public ButtonNumberControlsDialog(String layout) {
        super(layout);
        valueLabel = new HTML("");
        create();
    }

    private void create() {
        createControls();
        bind();
    }

    private void bind() {
        bindKeepingStyles(incrementButton, toRule(StyleName.INCREMENT));
        bindKeepingStyles(decrementButton, toRule(StyleName.DECREMENT));
        bindKeepingStyles(valueLabel, toRule(StyleName.VALUE));
    }

    public void setIncrementValueHandler(UpdateValueHandler incrementValueHandler) {
        this.incrementValueHandler = incrementValueHandler;
    }

    public void setDecrementValueHandler(UpdateValueHandler decrementValueHandler) {
        this.decrementValueHandler = decrementValueHandler;
    }

    @Override
    public void refreshValue(Number number) {
        valueLabel.setText(number.toString());
    }

    private void createControls() {
        incrementButton = new Anchor("+") {{
            addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    valueLabel.setText(incrementValueHandler.update());
                }
            });
        }};
        decrementButton = new Anchor("-") {{
            addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    valueLabel.setText(decrementValueHandler.update());
                }
            });
        }};
    }

    public interface UpdateValueHandler {
        String update();
    }

    public interface StyleName {
        String DECREMENT = "decrement";
        String INCREMENT = "increment";
        String VALUE = "value";
    }
}
