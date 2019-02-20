package client.widgets.popups.dialogs;

import client.core.model.Filter;
import client.core.model.List;
import client.core.model.NodeIndexEntry;
import client.presenters.displays.IndexDisplay;
import client.presenters.displays.LinkFieldIndexDisplay;
import client.services.TranslatorService;
import client.widgets.entity.field.LinkFieldWidget;
import client.widgets.popups.dialogs.link.EntryListDialog;
import client.widgets.popups.dialogs.link.LinkFieldIndexHeaderDialog;
import client.widgets.toolbox.FluidListController;
import client.widgets.toolbox.IndexFilterWidget;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.presenters.Presenter;

import java.util.HashMap;
import java.util.Map;

import static client.utils.KeyBoardUtils.*;
import static client.widgets.index.IndexFilterToolsWidget.LayoutHelper;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class LinkFieldIndexDialog extends CosmosHtmlPanel {

    private final LinkFieldIndexDisplay display;
    private final LayoutHelper layoutHelper;
    private final TranslatorService translator;
    private final Map<Filter, IndexFilterWidget> filters = new HashMap<>();
    private EntryListDialog history;
    private EntryListDialog entries;
    private boolean loading;
    private TextBox searchInput;
    private boolean inputIsFocused;
    private Grid headers;
    private ClosePopupHandler closePopupHandler;

    public LinkFieldIndexDialog(LinkFieldIndexDisplay display, String layout, LayoutHelper layoutHelper, TranslatorService translator) {
        super(layout);
        this.display = display;
        this.layoutHelper = layoutHelper;
        this.translator = translator;
        create();
        hook();
    }

    public void show() {
        showLoadingEntries();
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                display.loadFirstPage();
                if (display.allowHistory())
                    display.loadHistory();
                entries.setVerticalScrollPosition(0);
                searchInput.setFocus(true);
                inputIsFocused = true;
            }
        });
    }

    private void showLoadingEntries() {
        entries.showLoading();
        loading = true;
    }

    private void create() {
        createSearchInput();
        headers = new Grid();
        if (display.allowHistory())
            history = new EntryListDialog(translator.translate(TranslatorService.ListLabel.MESSAGE_WHEN_EMPTY), translator.translate(TranslatorService.Label.LOADING));
        entries = new EntryListDialog(translator.translate(TranslatorService.ListLabel.MESSAGE_WHEN_EMPTY), translator.translate(TranslatorService.Label.LOADING));
        addHeaders(display.getHeaders());
        createEntries();
        new FluidListController<>(entries, new FluidListController.PageHandler() {
            @Override
            public void nextPage() {
                display.nextPage();
            }

            @Override
            public void reloadPage() {
                display.reloadPage();
            }
        });
        new SectionNavigationController<>(history, entries);
        bind();
    }

    private void createSearchInput() {
        searchInput = new TextBox();
        searchInput.getElement().setPropertyString("placeholder", translator.translate(TranslatorService.ListLabel.FILTER_MESSAGE_WHEN_EMPTY));
        searchInput.addKeyUpHandler(new SearchInputKeyUpHandler());
    }

    private void addHeaders(List<LinkFieldIndexDisplay.Header> headerList) {
        int column = 0;
        headers.clear();
        headers.resize(1, headerList.size());
        for (LinkFieldIndexDisplay.Header header : display.getHeaders())
            headers.setWidget(0, column++, createHeaderWidget(header));
    }

    private void createEntries() {
        if (display.allowHistory()) {
            history.setEntrySelectedHandler(new EntryListDialog.EntrySelectedHandler() {
                @Override
                public void selectedEntry(NodeIndexEntry entry) {
                    display.activate(entry);
                }
            });
        }
        entries.setEntrySelectedHandler(new EntryListDialog.EntrySelectedHandler() {
            @Override
            public void selectedEntry(NodeIndexEntry entry) {
                display.activate(entry);
            }
        });
    }

    private void bind() {
        bindKeepingStyles(searchInput, $(getElement()).find(toRule(StyleName.SEARCH_BOX)).children(toRule(StyleName.SEARCH_INPUT)).get(0));
        bindKeepingStyles(headers, toRule(StyleName.HEADERS));
        if (display.allowHistory())
            bindKeepingStyles(history, toRule(StyleName.HISTORY));
        else
            $(getElement()).find(toRule(StyleName.HISTORY_PANEL)).hide();
        bindKeepingStyles(entries, toRule(StyleName.ENTRIES));
    }

    private Widget createHeaderWidget(final LinkFieldIndexDisplay.Header header) {
        final LinkFieldIndexHeaderDialog headerWidget = new LinkFieldIndexHeaderDialog(display, header, translator, layoutHelper);
        if (header.isFilterable())
            createFilter(header, headerWidget);
        return headerWidget;
    }

    private void createFilter(final LinkFieldIndexDisplay.Header header, final LinkFieldIndexHeaderDialog headerWidget) {
        headerWidget.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                display.select(display.getEntityFactory().createOrder(header.getCode(), header.getLabel(), headerWidget.getOrder()));
            }
        });
        filters.put(header.getFilter(), headerWidget);
    }

    public void refreshEntriesState() {
        entries.refresh(display.getActive());
    }

    public void setClosePopupHandler(ClosePopupHandler closePopupHandler) {
        this.closePopupHandler = closePopupHandler;
    }

    private void hook() {
        display.addHook(new LinkFieldIndexDisplay.Hook() {

            @Override
            public void headers() {
                addHeaders(display.getHeaders());
            }

            @Override
            public void history(List<NodeIndexEntry> entries) {
                history.removeAll();
                if (!entries.isEmpty())
                    history.addEntries(entries);
                else
                    $(getElement()).find(toRule(StyleName.HISTORY_PANEL)).hide();
            }

            @Override
            public void loadingPage() {
                showLoadingEntries();
            }

            @Override
            public void clear() {
                entries.clear();
            }

            @Override
            public void page(IndexDisplay.Page<NodeIndexEntry> page) {
                hideLoadingEntries();
                addPage(page);
            }

            @Override
            public void pageFailure() {
                entries.showMessage(display.getIndexErrorMessage());
            }

            @Override
            public void options(Filter filter, List<Filter.Option> options) {
                if (filters.containsKey(filter))
                    filters.get(filter).refreshOptions(options);
            }
        });
    }

    private void hideLoadingEntries() {
        if (!loading) return;
        loading = false;
        entries.hideLoading();
    }

    private void addPage(IndexDisplay.Page<NodeIndexEntry> page) {
        entries.addPage(page);
        refreshEntriesState();
    }

    public interface StyleName {
        String ENTRIES = "entries";
        String HEADERS = "headers";
        String HISTORY = "history";
        String HISTORY_PANEL = "history-panel";
	    String SEARCH_BOX = "search-box";
        String SEARCH_INPUT = "search-input";
    }

    private void selectNext() {
        if (entries.isLastSelected())
            focusInput();
        else {
            inputIsFocused = false;
            entries.selectBottom();
        }
    }

    private void selectPrev() {
        if (inputIsFocused) return;
        if (entries.isFirstSelected())
            focusInput();
        else
            entries.selectUpper();
    }

    private void focusInput() {
        inputIsFocused = true;
        entries.noOptionSelected();
        searchInput.setFocus(inputIsFocused);
    }

    public static class Builder extends LinkFieldWidget.Builder {

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            if (!(presenter instanceof LinkFieldIndexDisplay))
                return null;
            return new LinkFieldIndexDialog((LinkFieldIndexDisplay) presenter, layout, createLayoutHelper(), translator);
        }

        protected LayoutHelper createLayoutHelper() {
            return new LayoutHelper() {

                @Override
                public String getLayout() {
                    return theme.getLayout("index-filter-tools");
                }

                @Override
                public String getFilterLayout() {
                    return theme.getLayout("index-filter-tools-filter");
                }

            };
        }
    }

    public interface ClosePopupHandler {
        void onClose();
    }

    private class SearchInputKeyUpHandler implements KeyUpHandler {

        private Timer timer = new Timer() {
            @Override
            public void run() {
                display.setCondition(searchInput.getText());
            }
        };

        @Override
        public void onKeyUp(KeyUpEvent event) {
            if (isVerticalArrow(event.getNativeKeyCode()))
                handleNavigation(event.getNativeKeyCode());
            else if (isEnter(event.getNativeKeyCode()))
                display.activate(entries.getSelectedOption().getEntry());
            else if (isEscape(event.getNativeKeyCode()) && closePopupHandler != null)
                closePopupHandler.onClose();
            else
                handleCondition();
        }

        private void handleCondition() {
            timer.cancel();
            timer.schedule(200);
        }

        private void handleNavigation(int arrow) {
            if (isArrowUp(arrow))
                selectPrev();
            else
                selectNext();
        }
    }
}
