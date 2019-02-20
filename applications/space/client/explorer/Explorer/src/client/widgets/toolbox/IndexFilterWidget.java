package client.widgets.toolbox;


import client.core.model.Filter;
import client.core.model.List;
import client.services.TranslatorService;
import client.widgets.popups.PopUpWidget;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;
import cosmos.utils.StringUtils;

import static client.utils.KeyBoardUtils.isArrowKey;
import static client.utils.KeyBoardUtils.isEnter;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;

public class IndexFilterWidget extends ListItemWidget {

    protected final Filter filter;
    protected final Delegate delegate;
    protected final TranslatorService translator;
    private Handler handler;
    protected FlowPanel selectedOptionsPanel;
    protected CheckListWidget<Filter.Option> optionsPanel;
    private Anchor anchor;
    protected Dialog dialog;

    private static final int MAX_LENGTH = 28;

    public IndexFilterWidget(Filter filter, TranslatorService translator, Delegate delegate) {
        this.filter = filter;
        this.translator = translator;
        this.delegate = delegate;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }

    public void addClickHandler(ClickHandler handler) {
        anchor.addClickHandler(handler);
    }

    public void refreshSelectedOptions(List<Filter.Option> options) {
        selectedOptionsPanel.clear();

        String labelTitle = "";
        for (Filter.Option option : options) {
            if (!labelTitle.isEmpty()) labelTitle = labelTitle + ", ";
            labelTitle = labelTitle + option.getLabel();
        }

        String labelValue;
        if (options.size() == 0)
            labelValue = translator.translate(TranslatorService.ListLabel.ALL);
        else if (options.size() == 1)
            if (labelTitle.length() > MAX_LENGTH)
                labelValue = StringUtils.shortContent(labelTitle, MAX_LENGTH);
            else
                labelValue = labelTitle;
        else
        if (labelTitle.length() > MAX_LENGTH)
            labelValue = translator.translate(TranslatorService.ListLabel.MORE_THAN_ONE);
        else
            labelValue = labelTitle;


        Label label = new Label(labelValue);
        label.setTitle(labelTitle);
        selectedOptionsPanel.add(label);
    }

    public void refreshOptions(List<Filter.Option> options) {
        optionsPanel.clear();
        for (Filter.Option option : options)
            optionsPanel.addItem(option);
    }

    public interface Handler {
        void activateFilter();
        void deactivateFilter();
        void onSelectOptions(List<Filter.Option> options);
        void onClearOptions();
    }

    public static abstract class Builder {

        public IndexFilterWidget build(Filter filter, TranslatorService translator, Delegate delegate) {
            return new IndexFilterWidget(filter, translator, delegate);
        }
    }

    protected void init() {
        createAnchor();
        createLabel();
        createSelectedOptions();
        createOptions();
        createDialog();
    }

    protected void createAnchor() {
        anchor = new Anchor();
        anchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                anchorClickHandler(event);
            }
        });
        add(anchor, $(getElement()).get(0));
    }

    private void anchorClickHandler(ClickEvent event) {
        if (isActive())
            handler.deactivateFilter();
        else
            handler.activateFilter();

        anchorEventPropagation(event);
        event.stopPropagation();
    }

    protected void anchorEventPropagation(ClickEvent event) {
        dialog.propagateEventToWidgets(event, $(getElement()).parents(".popup").find(".popup").not(dialog.getElement()).widgets());
    }

    private boolean isActive() {
        return $(this).hasClass(StyleName.ACTIVE);
    }

    public void activate() {
        if (!$(this).hasClass(StyleName.ACTIVE))
            dialog.show();
    }

    public void deactivate() {
        dialog.hide();
    }

    protected void createLabel() {
        Label label = new Label(filter.getLabel());
        add(label, $(this.getElement()).get(0));
    }

    protected void createSelectedOptions() {
        selectedOptionsPanel = new FlowPanel();
        add(selectedOptionsPanel, $(this.getElement()).get(0));
    }

    protected void createOptions() {
        optionsPanel = new CheckListWidget<>(translator);
    }

    protected void createDialog() {
        dialog = new Dialog(filter, optionsPanel);
        setUpDialog();
    }

    protected void setUpDialog() {
        dialog.addStyleName(StyleName.DIALOG);
        dialog.setSizeCalculator(new PopUpWidget.SizeCalculator() {
            @Override
            public int getWidth() {
                return getOffsetWidth() * 2;
            }

            @Override
            public int getHeight() {
                return getOffsetHeight() * 5;
            }
        });
        dialog.addHandler(new PopUpWidget.PopupHandler() {
            @Override
            public void onOutsideClick() {
                handler.deactivateFilter();
            }
        });
        add(dialog, $(getElement()).get(0));
    }

    protected class Dialog extends PopUpWidget<HTMLPanel> {
        private final Filter filter;
        private final CheckListWidget options;
        private boolean optionsLoaded;
        private TextBox searchBox;
        private Anchor clearSelection;

        public Dialog(Filter filter, CheckListWidget options) {
            super(new HTMLPanel(""));
            this.filter = filter;
            this.options = options;
            init();
        }

	    @Override
	    protected void init() {
		    super.init();
		    create();
		    refresh();
	    }

	    @Override
	    protected HTMLPanel createContent(Element container) {
		    bindWidgetToElement(this, content, container);
		    return content;
	    }

	    @Override
        public void show() {
            super.show();
            IndexFilterWidget.this.addStyleName(IndexFilterWidget.StyleName.ACTIVE);

            if (optionsLoaded)
                return;

            optionsLoaded = true;
            delegate.loadFilterOptions(filter, null);
            Element parent = RootPanel.get().getElement();
            if (parent.getAbsoluteLeft() + parent.getOffsetWidth() < getElement().getAbsoluteLeft() + getElement().getOffsetWidth())
                addStyleName(IndexFilterWidget.StyleName.ALIGN_RIGHT);
            else
                addStyleName(IndexFilterWidget.StyleName.ALIGN_LEFT);
        }

        @Override
        public void hide() {
            super.hide();
            IndexFilterWidget.this.removeStyleName(IndexFilterWidget.StyleName.ACTIVE);
        }

        private void create() {
            createSearchBox();
            createCheckList();
            createToolbar();
        }

        private void createSearchBox() {
            searchBox = new TextBox();
            searchBox.addStyleName(IndexFilterWidget.StyleName.SEARCH_BOX);
            searchBox.getElement().setPropertyString("placeholder", translator.translate(TranslatorService.ListLabel.FILTER_MESSAGE_WHEN_EMPTY));
            addSearchBoxHandlers(searchBox);
            getContent().add(searchBox);
        }

        protected void addSearchBoxHandlers(TextBox searchBox) {
            InputHandler inputHandler = new InputHandler();
            searchBox.addKeyUpHandler(inputHandler);
            searchBox.addKeyDownHandler(inputHandler);
        }

        private void createCheckList() {

            options.addToggleHandler(new CheckListWidget.ToggleHandler<Filter.Option>() {
                @Override
                public void onToggle(Filter.Option option, boolean checked) {
                }

                @Override
                public void onChange(List<Filter.Option> selection) {
                    setSelection(selection);
                }
            }, 500);

            optionsLoaded = false;

            getContent().add(options);
        }

        private void createToolbar() {
            HorizontalPanel toolbar = new HorizontalPanel();
            toolbar.addStyleName(IndexFilterWidget.StyleName.TOOLBAR);
            toolbar.add(createClearSelectionAnchor());
            getContent().add(toolbar);
        }

        private Widget createClearSelectionAnchor() {
            clearSelection = new Anchor();
            clearSelection.setText(translator.translate(TranslatorService.ListLabel.SELECT_NONE));
            clearSelection.addClickHandler(new ClickHandler() {
                @Override
                public void onClick(ClickEvent event) {
                    options.selectNone();
                }
            });

            HTMLPanel command = new HTMLPanel("");
            command.addStyleName(IndexFilterWidget.StyleName.COMMAND);
            command.add(clearSelection);

            return command;
        }

        private void refresh() {
            refreshToolbar(options.getCheckedCount());
        }

        private void refreshToolbar(int checkedOptionsCount) {
            clearSelection.setEnabled(checkedOptionsCount > 0);

            clearSelection.removeStyleName(IndexFilterWidget.StyleName.COMMAND_DISABLED);
            if (checkedOptionsCount <= 0)
                clearSelection.addStyleName(IndexFilterWidget.StyleName.COMMAND_DISABLED);
        }

        private void setSelection(List<Filter.Option> options) {
            handler.onSelectOptions(options);
            refreshToolbar(options.size());
            refreshSelectedOptions(options);
        }

        private class InputHandler implements KeyUpHandler, KeyDownHandler {

            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (!isArrowKey(event.getNativeKeyCode()))
                    delegate.loadFilterOptions(filter, searchBox.getValue());
            }

            @Override
            public void onKeyDown(KeyDownEvent event) {
                if (isEnter(event.getNativeKeyCode()))
                    delegate.loadFilterOptions(filter, searchBox.getValue());
            }
        }
    }

    public interface Delegate {
        void loadFilterOptions(Filter filter, String condition);
    }

    public interface StyleName {
        String ACTIVE = "active";
        String ALIGN_LEFT = "align-left";
        String ALIGN_RIGHT = "align-right";
        String COMMAND = "command";
        String COMMAND_DISABLED = "disabled";
        String DIALOG = "dialog";
        String SEARCH_BOX = "searchBox";
        String TOOLBAR = "toolbar";
    }

}