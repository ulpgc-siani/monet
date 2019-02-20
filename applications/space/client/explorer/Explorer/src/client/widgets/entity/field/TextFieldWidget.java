package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.TermList;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsTextFieldDisplay;
import client.presenters.displays.entity.field.TextFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.popups.FieldOptionsPopupWidget;
import client.widgets.popups.dialogs.OptionListDialog;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static client.services.TranslatorService.ErrorLabel;
import static client.services.TranslatorService.Label;
import static client.utils.KeyBoardUtils.KeyFunctionMapper;
import static client.utils.KeyBoardUtils.KeyFunctionMapper.Function;
import static client.utils.KeyBoardUtils.isArrowKey;
import static client.widgets.popups.dialogs.OptionListDialog.TextOption;

public class TextFieldWidget extends ValueFieldWidget<TextBoxBase, FieldOptionsPopupWidget> {

    private InputHandler handler;

    public TextFieldWidget(IsTextFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.TEXT);
    }

    @Override
    protected void createComponent() {
        handler = new InputHandler();
        super.createComponent();
    }

    @Override
    protected void createInput() {
        input = new TextBox();

        if (textFieldDisplay().getMaxLength() > 0)
            ((TextBox) input).setMaxLength(textFieldDisplay().getMaxLength());
    }

    @Override
    protected void createPopup() {
        if (textFieldDisplay().allowHistory())
            createHistoryPopup();
	    else
	        removePopupFromLayout();
    }

    private void createHistoryPopup() {
        popup = new FieldOptionsPopupWidget(getPopupLayout(), input, state, translator);
        super.createPopup();
        popup.setLoadingIndicator(translator.translate(Label.LOADING));
        addPopupHandlers();
    }

    @Override
    protected void update(String value) {
        cursorPosition = input.getCursorPos();
        if (value.equals(textFieldDisplay().getValue())) return;
        textFieldDisplay().setValue(value);
        if (!textFieldDisplay().allowHistory() || textFieldDisplay().isReadonly()) return;
        filter(textFieldDisplay().getValue());
    }

	@Override
	protected boolean popupShouldBeAdded() {
		return super.popupShouldBeAdded() && textFieldDisplay().allowHistory();
	}

    @Override
    protected void refreshWhenHasValue() {
        super.refreshWhenHasValue();
        input.setValue(textFieldDisplay().getValue());
        if (input.getValue().length() > cursorPosition)
            input.setCursorPos(cursorPosition);
    }

    @Override
    protected TextFieldDisplay.Hook createHook() {
        return new TextFieldDisplay.Hook() {
            @Override
            public void history(TermList options) {
	            popup.refreshOptions(options);
            }

            @Override
            public void historyFailure() {
                showFailureLoadingHistory();
            }

            @Override
            public void value() {
                refresh();
            }

            @Override
            public void error(String error) {

            }
        };
    }

    @Override
    protected boolean popupShouldBeShown() {
        return super.popupShouldBeShown() && textFieldDisplay().allowHistory();
    }

    @Override
    protected void showPopup() {
        showDialog(textFieldDisplay().getValue());
    }

    protected void showDialog(String text) {
        if (!popupShouldBeShown()) return;
        super.showPopup();
        filter(text);
    }

    protected void filter(String condition) {
        textFieldDisplay().loadHistory(condition);
    }

    protected void showFailureLoadingHistory() {
        if (popup == null) return;
	    popup.showFailureLoading(translator.translate(ErrorLabel.SOURCE_OPTIONS));
    }

    @Override
    protected void addInputHandlers() {
        input.addKeyPressHandler(handler);
        input.addKeyDownHandler(handler);
        input.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                input.setValue(textFieldDisplay().getValue());
            }
        });
        addKeyUpHandler();
        super.addInputHandlers();
    }

    protected void addKeyUpHandler() {
        input.addKeyUpHandler(handler);
    }

    private void addPopupHandlers() {
	    popup.addKeyDownHandler(handler);
	    popup.addKeyUpHandler(handler);
	    popup.setOnOptionSelected(new OptionListDialog.OptionSelectedHandler() {
            @Override
            public void onSelected(TextOption option) {
                update(option.getText());
            }
        });
    }

    private class InputHandler implements KeyDownHandler, KeyUpHandler, KeyPressHandler {

        private final KeyFunctionMapper keyFunctionMapper;

        private InputHandler() {
            keyFunctionMapper = new KeyFunctionMapper();
            keyFunctionMapper.addFunction(KeyCodes.KEY_TAB, new Function() {
                @Override
                public void apply(GwtEvent event) { }
            });
            keyFunctionMapper.addFunction(KeyCodes.KEY_ESCAPE, new Function() {
                @Override
                public void apply(GwtEvent event) { }
            });
            keyFunctionMapper.addFunction(KeyCodes.KEY_ENTER, new Function() {
                @Override
                public void apply(GwtEvent event) {
                    onEnter();
                }
            });
            keyFunctionMapper.addFunction(KeyCodes.KEY_DOWN, new Function() {
                @Override
                public void apply(GwtEvent event) {
                    if (popup != null && popup.isVisible()) input.cancelKey();
                }
            });
            keyFunctionMapper.addFunction(KeyCodes.KEY_UP, new Function() {
                @Override
                public void apply(GwtEvent event) {
                    if (popup != null && popup.isVisible()) input.cancelKey();
                }
            });
            keyFunctionMapper.addFunction(KeyCodes.KEY_LEFT, new Function() {
                @Override
                public void apply(GwtEvent event) { }
            });
            keyFunctionMapper.addFunction(KeyCodes.KEY_RIGHT, new Function() {
                @Override
                public void apply(GwtEvent event) { }
            });
        }

        @Override
        public void onKeyPress(KeyPressEvent event) {
            if (keyFunctionMapper.isMapped(event.getNativeEvent().getKeyCode())) return;
            state.enable();
        }

        @Override
        public void onKeyUp(KeyUpEvent event) {
            if (!state.isEditing() || isArrowKey(event.getNativeKeyCode())) return;
            showDialog(input.getValue());
        }

        @Override
        public void onKeyDown(KeyDownEvent event) {
            if (!keyFunctionMapper.isMapped(event.getNativeKeyCode()))
                state.enable();
            else
                keyFunctionMapper.executeFunction(event.getNativeKeyCode(), event);
        }

        private void onEnter() {
            if (popup != null)
	            popup.focusInput();
        }
    }

    private IsTextFieldDisplay textFieldDisplay() {
        return (IsTextFieldDisplay) display;
    }

    public interface StyleName extends ValueFieldWidget.StyleName {
		String TEXT = "text";
	}

    public static class Builder extends FieldWidget.Builder {

	    public Builder() {
		    super();
	    }

	    public static void register(){
            registerBuilder(TextFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new TextFieldWidget((IsTextFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-text";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-text";
	    }
    }
}
