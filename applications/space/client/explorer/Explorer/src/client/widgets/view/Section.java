package client.widgets.view;

import client.core.model.NodeIndexEntry;
import client.core.model.definition.views.ViewDefinition;
import client.presenters.displays.SetIndexDisplay;
import client.services.TranslatorService;
import client.widgets.index.IndexFilterToolsWidget;
import client.widgets.index.IndexToolbarWidget;
import client.widgets.index.IndexWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.utils.StyleUtils;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.*;

class Section extends CosmosHtmlPanel {
	private final SetIndexDisplay display;
	private final TranslatorService translator;
	private boolean isLoaded = false;
    private Widget index;
    private IndexToolbarWidget headerToolbar;
    private IndexToolbarWidget footerToolbar;
    private Anchor toggle;
    private IndexFilterToolsWidget<SetIndexDisplay, NodeIndexEntry> filterToolsWidget;

    Section(SetIndexDisplay display, String layout, IndexFilterToolsWidget.Builder filterToolsBuilder, TranslatorService translator) {
        super(layout);
		this.display = display;
		this.filterToolsWidget = (IndexFilterToolsWidget<SetIndexDisplay, NodeIndexEntry>) filterToolsBuilder.build(display, IndexFilterToolsWidget.Builder.DESIGN, "");
		this.translator = translator;
        init();
    }

    private void init() {
        addStyleName(StyleName.SECTION);
        createTitle();
        createFilterTools();
        createToggleButton();
        createIndexWidget();
        createToolbars();
    }

    private void createTitle() {
        HTMLPanel panel = new CosmosHtmlPanel();
        panel.addStyleName(StyleName.SECTION_TITLE);

        InlineHTML label = new InlineHTML();
        label.setText(display.getTitle());
        label.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                toggle(display);
            }
        });
        panel.add(label);
        panel.add(createTitleReportWidget());

        bind(panel, toRule(StyleName.SECTION_TITLE));
    }

    private void createFilterTools() {
        filterToolsWidget.setVisible(false);
        bindKeepingStyles(filterToolsWidget, toRule(StyleName.FILTER_TOOLS));
    }

    private void createToggleButton() {
        toggle = new Anchor();
        toggle.setText(isExpanded() ? translator.translate(TranslatorService.Label.HIDE_SECTION) : translator.translate(TranslatorService.Label.SHOW_SECTION));
        toggle.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                toggle(display);
            }
        });

        bind(toggle, toRuleCheckingTags(StyleName.SECTION_TOGGLE, StyleUtils.ANCHOR));
    }

    private Widget createTitleReportWidget() {
        Widget index = EntityViewWidget.Builder.getIndexBuilder().build(display, IndexWidget.Builder.REPORT, ViewDefinition.Design.DOCUMENT.toString());
        index.addStyleName(StyleName.SECTION_COUNT);
        return index;
    }

    private void createIndexWidget() {
        index = EntityViewWidget.Builder.getIndexBuilder().build(display, IndexWidget.Builder.DESIGN, ViewDefinition.Design.DOCUMENT.toString());
        index.setVisible(false);

        bindKeepingStyles(index, $(getElement()).children(toRule(StyleName.SECTION_ITEMS)).get(0));
    }

    private void createToolbars() {
        headerToolbar = createToolbar(display, StyleName.SECTION_TOOLBAR, StyleName.SECTION_HEADER_TOOLBAR);
        footerToolbar = createToolbar(display, StyleName.SECTION_TOOLBAR, StyleName.SECTION_FOOTER_TOOLBAR);
    }

    private IndexToolbarWidget createToolbar(SetIndexDisplay setIndexDisplay, String... styles) {

        if ($(getElement()).find(toCombinedRule(styles)).isEmpty())
            return null;

        IndexToolbarWidget.Builder indexToolbarBuilder = EntityViewWidget.Builder.getIndexToolbarBuilder();

        IndexToolbarWidget toolbar = (IndexToolbarWidget) indexToolbarBuilder.build(setIndexDisplay, IndexToolbarWidget.Builder.DESIGN, ViewDefinition.Design.DOCUMENT.toString());
        $(toolbar).addClass(styles);
        toolbar.setVisible(false);

        bind(toolbar, toCombinedRule(styles));

        return toolbar;
    }

    public boolean isExpanded() {
        return $(this).hasClass(StyleName.SECTION_EXPANDED);
    }

    public void expand() {
        addStyleName(StyleName.SECTION_EXPANDED);
        index.setVisible(true);
        showToolbars();
        filterToolsWidget.setVisible(true);
        toggle.setText(translator.translate(TranslatorService.Label.HIDE_SECTION));
    }

    public void collapse() {
        new Timer() {
            @Override
            public void run() {
                removeStyleName(StyleName.SECTION_EXPANDED);
                toggle.setText(translator.translate(TranslatorService.Label.SHOW_SECTION));
            }
        }.schedule(400);
        $(index).slideUp(300);
        $(filterToolsWidget).slideUp(300);
        hideToolbars();
    }

    public void toggle(SetIndexDisplay setIndexDisplay) {
        if (isExpanded())
            collapse();
        else {
            expand();
            load(setIndexDisplay);
        }
    }

    private void showToolbars() {
        if (headerToolbar != null)
            headerToolbar.setVisible(true);

        if (footerToolbar != null)
            footerToolbar.setVisible(true);
    }

    private void hideToolbars() {
        if (headerToolbar != null)
            $(headerToolbar).slideUp(400);

        if (footerToolbar != null)
            $(footerToolbar).slideUp(400);
    }

    private void load(SetIndexDisplay setIndexDisplay) {
        if (isLoaded)
            return;
        setIndexDisplay.loadPage(0);
        isLoaded = true;
    }

    public static class Builder extends SetViewWidget.Builder {

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return super.build(presenter, design, layout);
        }

        public Section build(SetIndexDisplay display) {
            return new Section(display, getSectionLayout(), setUpBuilder(new IndexFilterToolsWidget.Builder()), translator);
        }

        private String getSectionLayout() {
            return getLayout("view-set", "document section");
        }
    }

	private interface StyleName {
        String FILTER_TOOLS = "filter-tools";
		String SECTION = "section";
		String SECTION_TITLE = "title";
		String SECTION_COUNT = "count";
		String SECTION_ITEMS = "items";
		String SECTION_TOOLBAR = "toolbar";
		String SECTION_HEADER_TOOLBAR = "th";
		String SECTION_FOOTER_TOOLBAR = "tf";
		String SECTION_EXPANDED = "expanded";
		String SECTION_TOGGLE = "toggle";
	}
}
