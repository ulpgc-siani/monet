package client.widgets.popups.dialogs;

import client.core.model.List;
import client.core.model.types.CompositeTerm;
import client.core.model.types.Term;
import client.core.system.MonetList;
import client.presenters.displays.entity.field.SelectFieldDisplay;
import client.services.TranslatorService;
import client.widgets.popups.SelectOptionsPopupWidget;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import cosmos.gwt.widgets.CosmosHtmlPanel;

import java.util.LinkedHashMap;
import java.util.Map;

import static client.utils.KeyBoardUtils.*;
import static client.widgets.popups.dialogs.OptionListDialog.TextOption;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class SelectOptionsDialog extends CosmosHtmlPanel {

    private final TranslatorService translator;
    private final SelectDialogDefinition definition;
    private TextBox searchInput;
    private OptionListDialog optionList;
	private OptionListDialog historyList;
	private SectionNavigationController<OptionListDialog> sectionNavigationController;
    private Map<TextOption, Term> history;
    private Map<TextOption, Term> options;

    private TextBox otherInput;
    private SelectOptionsPopupWidget.SelectOptionsHandler handler;

    public SelectOptionsDialog(String layout, TranslatorService translator, SelectDialogDefinition definition) {
        super(layout);
        this.translator = translator;
        this.definition = definition;
        this.history = new LinkedHashMap<>();
	    this.options = new LinkedHashMap<>();
	    createComponents();
    }

    public void refreshHistoryOptions(SelectFieldDisplay.HistoryOptions historyOptions) {
        history = translateTermsToString(historyOptions.getHistory());
        options = translateTermsToString(historyOptions.getOptions());
        optionList.refreshOptions(new MonetList<>(options.keySet()));
        historyList.refreshOptionsAndHideScroll(new MonetList<>(history.keySet()));
        if (history.isEmpty())
            $(getElement()).children(toRule(StyleName.HISTORY_PANEL)).hide();
        else
            $(getElement()).children(toRule(StyleName.HISTORY_PANEL)).show();
    }

    public void setOnOptionSelectedHandler(final OnOptionSelectedHandler handler) {
        sectionNavigationController.setOptionSelectedHandler(new OptionListDialog.OptionSelectedHandler() {
            @Override
            public void onSelected(TextOption option) {
                handler.onSelected(optionToTerm(option), false);
                SelectOptionsDialog.this.handler.closePopUp();
            }
        });
    }

    public void focus() {
        searchInput.setFocus(true);
    }

    public void setSelectOptionsHandler(SelectOptionsPopupWidget.SelectOptionsHandler handler) {
        this.handler = handler;
    }

    public void showFailureLoading(Term term) {
        optionList.clear();
        optionList.add(definition.formatTerm(term));
    }

    public ScrollPanel getSelectOptionsScrollPanel() {
        return optionList;
    }

    public void addOptions(List<Term> options) {
        final Map<TextOption, Term> optionTermMap = translateTermsToString(options);
        this.options.putAll(optionTermMap);
        optionList.addOptions(new MonetList<>(optionTermMap.keySet()));
    }

    public void showLoading() {
        optionList.showLoading();
    }

    public void hideLoading() {
        optionList.hideLoading();
    }

    private void createComponents() {
		createSearchInput();
		createOptions();
		createHistory();
		createOtherInput();
        addDomHandler(new MouseOverHandler() {
            @Override
            public void onMouseOver(MouseOverEvent event) {
                if (Window.Navigator.getPlatform().contains("Win"))
                    RootPanel.get().addStyleName(StyleName.NO_SCROLL_WIN);
                else
                    RootPanel.get().addStyleName(StyleName.NO_SCROLL_MAC);
            }
        }, MouseOverEvent.getType());
        addDomHandler(new MouseOutHandler() {
            @Override
            public void onMouseOut(MouseOutEvent event) {
                RootPanel.get().removeStyleName(StyleName.NO_SCROLL_WIN);
                RootPanel.get().removeStyleName(StyleName.NO_SCROLL_MAC);
            }
        }, MouseOutEvent.getType());
        addInputHandler(new InputHandler());

		sectionNavigationController = new SectionNavigationController<>(optionList, historyList);
	}

    private void createSearchInput() {
		searchInput = new TextBox();
        searchInput.addStyleName(StyleName.SEARCH_INPUT);
		searchInput.getElement().setPropertyString("placeholder", translator.translate(TranslatorService.ListLabel.FILTER_MESSAGE_WHEN_EMPTY));

        $(getElement()).children(toRule(StyleName.SEARCH_BOX)).show();
        bind(searchInput, $(getElement()).children(toRule(StyleName.SEARCH_BOX)).children(toRule(StyleName.SEARCH_INPUT)).get(0));
	}

	private void createOtherInput() {
		otherInput = new TextBox();
		otherInput.setVisible(false);
        otherInput.addKeyDownHandler(new KeyDownHandler() {
            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (!isEnter(event.getNativeKeyCode())) return;
                handler.selectedOther(otherInput.getText());
                handler.closePopUp();
            }
        });
        if (!definition.allowOther())
            $(getElement()).children(toRule(StyleName.OTHER_PANEL)).hide();
        else
            $(getElement()).children(toRule(StyleName.OTHER_PANEL)).show();

		bindKeepingStyles(otherPanel(translator.translate(TranslatorService.Label.OTHER)), $(getElement()).children(toRule(StyleName.OTHER_PANEL)).children(toRule(StyleName.OTHER)).get(0));
	}

	private void createOptions() {
		optionList = new OptionListDialog(translator.translate(TranslatorService.Label.LIST_EMPTY), translator.translate(TranslatorService.Label.LOADING));
        optionList.addDomHandler(new InputHandler(), KeyUpEvent.getType());
		bindKeepingStyles(optionList, $(getElement()).children(toRule(StyleName.OPTIONS_PANEL)).children(toRule(StyleName.OPTIONS)).get(0));
	}

	private void createHistory() {
		historyList = new OptionListDialog(translator.translate(TranslatorService.Label.LIST_EMPTY), translator.translate(TranslatorService.Label.LOADING));

		final InputHandler handler = new InputHandler();
		historyList.addDomHandler(handler, KeyUpEvent.getType());

		bindKeepingStyles(historyList, $(this).children(toRule(StyleName.HISTORY_PANEL)).children(toRule(StyleName.HISTORY)).get(0));
        $(getElement()).children(toRule(StyleName.HISTORY_PANEL)).hide();
	}

    private void addInputHandler(InputHandler handler) {
        searchInput.addKeyUpHandler(handler);
        searchInput.addKeyDownHandler(handler);
    }

    private CheckBox checkBox(String label) {
        final CheckBox checkBox = new CheckBox(label);
        checkBox.addValueChangeHandler(new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                otherInput.setVisible(event.getValue());
                otherInput.setFocus(event.getValue());
            }
        });
        return checkBox;
    }

    private Widget otherPanel(final String label) {
        final HorizontalPanel panel = new HorizontalPanel();
        panel.add(checkBox(label));
        panel.add(otherInput);
        return panel;
    }

    private Map<TextOption, Term> translateTermsToString(List<Term> terms) {
        return translateTermToString(terms);
    }

    private Map<TextOption, Term> translateTermToString(List<Term> terms) {
        Map<TextOption, Term> container = new LinkedHashMap<>();
        for (Term term : terms) {
            container.put(createOptionFromTerm(term), term);
            if (!term.isLeaf())
                container.putAll(translateTermToString(((CompositeTerm)term).getTerms()));
        }
        return container;
    }

    private TextOption createOptionFromTerm(Term term) {
        if (term.isLeaf())
            return new TextOption(definition.formatTerm(term), true);
        return new TextOption(definition.formatTerm(term), ((CompositeTerm)term).isSelectable());
    }

    private Term optionToTerm(TextOption option) {
        return (history.get(option) != null) ? history.get(option) : options.get(option);
    }

    public void clearInput() {
        searchInput.setText("");
    }

    public String getFilter() {
        return searchInput.getText();
    }

    private class InputHandler implements KeyDownHandler, KeyUpHandler {

        @Override
        public void onKeyUp(KeyUpEvent event) {
            if (!isVerticalArrow(event.getNativeKeyCode()))
                updateOptions();
            if (isArrowUp(event.getNativeKeyCode()))
                arrowUp();
            else if (isArrowDown(event.getNativeKeyCode()))
                arrowDown();
            else if (isTab(event.getNativeKeyCode()))
	            sectionNavigationController.selectNextPanel();
        }

        @Override
        public void onKeyDown(KeyDownEvent event) {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER && sectionNavigationController.hasSelection())
                selectOption(optionToTerm(sectionNavigationController.<TextOption>getSelectedOption()));
            if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE || event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
                handler.closePopUp();
        }

        private void updateOptions() {
            handler.filter(searchInput.getText());
        }

        private void arrowUp() {
            if (sectionNavigationController.isFirstSelected())
                focusInput();
            else
                sectionNavigationController.selectUpper();
        }

        private void arrowDown() {
            if (sectionNavigationController.isLastSelected())
                focusInput();
            else
                sectionNavigationController.selectBottom();
        }

        private boolean isVerticalArrow(int keyCode) {
            return keyCode == KeyCodes.KEY_UP || keyCode == KeyCodes.KEY_DOWN;
        }
    }

    private void selectOption(Term option) {
        handler.selectedOption(option);
        sectionNavigationController.noOptionSelected();
    }

    private void focusInput() {
        sectionNavigationController.noOptionSelected();
        searchInput.setFocus(true);
    }

    public interface OnOptionSelectedHandler {
        void onSelected(Term option, boolean isOther);
    }

    public interface SelectDialogDefinition {
        boolean allowOther();
        boolean allowSearch();
        String formatTerm(Term term);
    }

    public interface StyleName {
        String OTHER = "other";
        String OPTIONS = "options";
        String HISTORY = "history";
        String SEARCH_BOX = "search-box";
        String SEARCH_INPUT = "search-input";
        String HISTORY_PANEL = "history-panel";
        String OPTIONS_PANEL = "options-panel";
        String OTHER_PANEL = "other-panel";
        String NO_SCROLL_WIN = "no-scroll-win";
        String NO_SCROLL_MAC = "no-scroll-mac";
    }
}
