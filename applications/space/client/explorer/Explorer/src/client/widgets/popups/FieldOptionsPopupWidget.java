package client.widgets.popups;

import client.core.model.List;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.core.system.MonetList;
import client.services.TranslatorService;
import client.widgets.entity.field.WidgetState;
import client.widgets.popups.dialogs.OptionListDialog;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBoxBase;

import static client.utils.KeyBoardUtils.isArrowDown;
import static client.utils.KeyBoardUtils.isArrowUp;
import static client.widgets.popups.dialogs.OptionListDialog.TextOption;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class FieldOptionsPopupWidget extends FieldPopupWidget<TextBoxBase, OptionListDialog> {
    private final WidgetState state;
    protected final TranslatorService translator;
    private boolean inputIsFocused = true;

    public FieldOptionsPopupWidget(String layout, final TextBoxBase input, WidgetState state, TranslatorService translator) {
        super(layout, input);
	    this.state = state;
        this.translator = translator;
        init();
	    addHandlers();
    }

	@Override
	protected OptionListDialog createDialog(Element container) {
		OptionListDialog dialog = new OptionListDialog(translator.translate(TranslatorService.Label.LIST_EMPTY), translator.translate(TranslatorService.Label.LOADING));
		bindKeepingStyles(dialog, toRule(StyleName.OPTIONS));
		return dialog;
	}

	@Override
    public void show() {
        super.show();
        input.setFocus(true);
        inputIsFocused = true;
    }

    public void setOnOptionSelected(final OptionListDialog.OptionSelectedHandler optionSelectedHandler) {
        content.setOptionSelectedHandler(new OptionListDialog.OptionSelectedHandler() {
            @Override
            public void onSelected(TextOption option) {
                focusInput();
                optionSelectedHandler.onSelected(option);
                hide();
            }
        });
    }

    public void refreshOptions(TermList newOptions) {
        if (newOptions.isEmpty())
            hide();
        else
            content.refreshOptionsAndHideScroll(createOptions(newOptions));
    }

    private List<TextOption> createOptions(TermList newOptions) {
        List<TextOption> options = new MonetList<>();
        for (Term option : newOptions)
            options.add(new TextOption(option.getLabel()));
        return options;
    }

    public void showFailureLoading(String failure) {
        content.clear();
        content.refreshOptions(new MonetList<>(new TextOption(failure)));
    }

    public void setLoadingIndicator(String message) {
        content.removeAll();
        content.add(message);
    }

    public void focusInput() {
        inputIsFocused = true;
        content.noOptionSelected();
        input.setFocus(inputIsFocused);
    }

    private void addHandlers() {
        final KeyUpHandler handler = keyUpHandler();
        input.addKeyUpHandler(handler);
        addKeyUpHandler(handler);
    }

    private void selectNext() {
        if (content.isLastSelected())
            focusInput();
        else {
            inputIsFocused = false;
            content.selectBottom();
            input.setValue(content.getSelectedOption().getText());
        }
    }

    private void selectPrev() {
        if (inputIsFocused) return;
        if (content.isFirstSelected())
            focusInput();
        else {
            content.selectUpper();
            input.setValue(content.getSelectedOption().getText());
        }
    }

    private KeyUpHandler keyUpHandler() {
        return new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (!state.isEditing()) return;
                if (isArrowUp(event.getNativeKeyCode()))
                    selectPrev();
                else if (isArrowDown(event.getNativeKeyCode()))
                    arrowDown();
            }

            private void arrowDown() {
                if (!isVisible())
                    show();
                else
                    selectNext();
            }
        };
    }

	public interface StyleName extends PopUpWidget.StyleName {
		String OPTIONS = "options";
	}

}
