package client.widgets.entity.field;

import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.MultipleFieldDisplay;
import client.presenters.displays.entity.field.IsFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.popups.FieldPopupWidget;
import client.widgets.popups.PopUpWidget;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.ValueBoxBase;
import cosmos.gwt.utils.WidgetUtils;
import cosmos.gwt.widgets.Focusable;
import cosmos.gwt.widgets.Focusable.NavigationHandler.Key;

import static client.utils.KeyBoardUtils.*;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public abstract class ValueFieldWidget<BoxType extends ValueBoxBase, DialogType extends FieldPopupWidget> extends FieldWidget<FieldDisplay.Hook> implements Focusable {

    protected BoxType input;
    protected DialogType popup;
    protected int cursorPosition;
    private HTML clearButton;
    private NavigationHandler navigationHandler;

    protected ValueFieldWidget(IsFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void createComponent() {
        createInput();
        createClearButton();
        input.setReadOnly(display.isReadonly());

        addInputClickHandler();
        if (display.isReadonly())
		    removePopupFromLayout();
	    else {
            createPopup();
            addInputHandlers();
        }
    }

    protected abstract void createInput();

    private void createClearButton() {
        clearButton = new HTML("clear");
        clearButton.setVisible(!display.isReadonly() && display.hasValue());
        if (!display.isReadonly())
            clearButton.addClickHandler(clearButtonHandler());
    }

    protected void createPopup() {
        popup.setSizeCalculator(new PopUpWidget.SizeCalculator() {
	        @Override
	        public int getWidth() {
		        return popupWidth();
	        }

	        @Override
	        public int getHeight() {
		        return 0;
	        }
        });
    }

	protected String getPopupLayout() {
		return $(this).find(toRule(StyleName.POPUP)).get(0).getInnerHTML();
	}

    protected int popupWidth() {
        return $(getElement()).find(toRule(StyleName.COMPONENT)).width();
    }

    protected void update(String value) {
    }

    protected boolean popupShouldBeAdded() {
		return !display.isReadonly();
	}

    protected void hideClearButton() {
        clearButton.setVisible(false);
    }

    @Override
    protected void bind() {
        bind(input, toRule(StyleName.COMPONENT, StyleName.INPUT));

        if (clearShouldBeAdded())
            bindKeepingStyles(clearButton, toRule(StyleName.TOOLBAR, StyleName.CLEAR));

	    if (popupShouldBeAdded())
            bind(popup, toRule(StyleName.POPUP));

	    super.bind();
    }

    protected void removePopupFromLayout() {
        $(getElement()).find(toRule(StyleName.POPUP)).remove();
    }

    protected void refreshComponent() {
        if (display.hasValue())
            refreshWhenHasValue();
        else
            refreshWhenEmpty();
    }

    protected void refreshWhenHasValue() {
        clearButton.setVisible(!display.isReadonly());
    }

    protected void refreshWhenEmpty() {
        clearButton.setVisible(false);
        input.setText("");
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
            }
        };
    }

    protected boolean clearShouldBeAdded() {
        return !display.isReadonly() && !(display instanceof MultipleFieldDisplay);
    }

    protected boolean popupShouldBeShown() {
        return popup != null;
    }

    protected void showPopup() {
        if (popupShouldBeShown() && !popup.isVisible())
            popup.show();
    }

    @Override
    public void focus() {
        input.setFocus(true);
    }

    @Override
    public boolean isNavigable() {
        return !display.isReadonly() && WidgetUtils.isVisible(this);
    }

    @Override
    public void setNavigationHandler(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    protected void addInputHandlers() {
        input.addBlurHandler(new BlurHandler() {
	        @Override
	        public void onBlur(BlurEvent event) {
		        state.disable();
                update(input.getText());
                if (!popupIsVisible()) display.blur();
	        }
        });
        input.addFocusHandler(new FocusHandler() {
            @Override
            public void onFocus(FocusEvent event) {
                Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
                    @Override
                    public void execute() {
                        input.selectAll();
                    }
                });
                display.focus();
            }
        });
        input.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                fixFirefoxBugWhenPressingEscape(event.getNativeKeyCode());
                if (isEscape(event.getNativeKeyCode()) || isTab(event.getNativeKeyCode()))
                    hidePopup();
                else if (isEnter(event.getNativeKeyCode())) {
                    onEnterDown();
                    event.stopPropagation();
                } else if (isArrowKey(event.getNativeKeyCode()) && !state.isEditing())
                    navigate(event.getNativeKeyCode());
            }
        });
    }

    private void addInputClickHandler() {
        input.addClickHandler(new ClickHandler() {
	        @Override
	        public void onClick(ClickEvent event) {
		        state.enable();
                onClickInput();
            }
        });
    }

    protected void onClickInput() {
        if (popupShouldBeShown())
            showPopup();
    }

    private void navigate(int key) {
        if (isArrowUp(key))
            navigationHandler.onNavigate(Key.ARROW_UP);
        else if (isArrowDown(key))
            navigationHandler.onNavigate(Key.ARROW_DOWN);
        else if (isArrowLeft(key))
            navigationHandler.onNavigate(Key.ARROW_LEFT);
        else if (isArrowRight(key))
            navigationHandler.onNavigate(Key.ARROW_RIGHT);
    }

    private void fixFirefoxBugWhenPressingEscape(int keyCode) {
        if (!isEscape(keyCode)) return;
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                if (display.hasValue()) input.setText(display.toString());
            }
        });
    }

    protected void onEnterDown() {
        if (state.isEditing()) update(input.getText());
        state.toggleEdition();
        if (state.isEditing())
            showPopup();
        else
            hidePopup();
    }

    protected void hidePopup() {
        state.disable();
        display.blur();
        if (popup != null) popup.hide();
    }

    private boolean popupIsVisible() {
        return popup != null && popup.isVisible();
    }

    private ClickHandler clearButtonHandler() {
        return new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                display.removeValue();
            }
        };
    }

    public interface StyleName extends FieldWidget.StyleName {
        String TOOLBAR = "toolbar";
        String CLEAR = "clear";
    }
}
