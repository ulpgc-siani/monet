package client.widgets.entity.field;

import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.system.MonetList;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.CompositeFieldDisplay;
import client.presenters.displays.entity.field.IsCompositeFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.InlineHTML;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.widgets.*;
import cosmos.presenters.Presenter;

import static client.services.TranslatorService.Label;
import static client.services.TranslatorService.OperationLabel;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class CompositeFieldWidget extends FieldWidget<CompositeFieldDisplay.Hook> implements FocusableContainer {

    protected List<FieldWidget> widgets;
    protected LayoutWidget fieldsPanel;
    protected LayoutWidget extendedFieldsPanel;
    protected HTMLPanel showAll;
    private InlineHTML showAllIcon;
    private CheckBox checkBox;
    private NavigationHandler navigationHandler;

    public CompositeFieldWidget(IsCompositeFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.COMPOSITE);
    }

    @Override
    protected void createComponent() {
        widgets = new MonetList<>();
        createFieldsPanel();
        createShowAll();
        createCheckbox();
    }

    @Override
    protected void bind() {
        if (contentShouldBeAdded())
            bindContent();
        super.bind();
    }

    @Override
    public void refresh() {
        super.refresh();
        for (FieldWidget widget : widgets)
            widget.refresh();
    }

    @Override
    protected void refreshComponent() {
        refreshCondition();
        refreshShowAll();
    }

    private void refreshCondition() {
        if (!compositeFieldDisplay().isConditional()) return;
        checkBox.setValue(compositeFieldDisplay().getConditioned());
        if (compositeFieldDisplay().getConditioned())
            showContent();
        else
            hideContent();
    }

    protected void refreshShowAll() {
        showAll.getElement().setInnerHTML(showAllIcon.getElement().getString() + compositeFieldDisplay().getShowAllLabel());
    }

    @Override
    protected void refreshMessages() {
        addMessageWhenInvalid(display.getMessageWhenInvalid());
        if (display.isReadonly()) setDesignWhenReadOnly();
    }

    @Override
    protected void addDesignWhenInvalid() {
        label.addStyleName(StyleName.INVALID);
    }

    @Override
    protected void removeDesignWhenInvalid() {
        label.removeStyleName(StyleName.INVALID);
    }

    @Override
    protected CompositeFieldDisplay.Hook createHook() {
        return new CompositeFieldDisplay.Hook() {
            @Override
            public void value() {
            }

            @Override
            public void error(String error) {
            }

            @Override
            public void show() {
                refresh();
            }

            @Override
            public void scroll() {
                if (compositeFieldDisplay().getShowAllValue())
                    compositeFieldDisplay().scroll(showAll.getAbsoluteTop() - 150);
                else
                    compositeFieldDisplay().scroll(fieldsPanel.getAbsoluteTop() - 150);
            }
        };
    }

    protected boolean contentShouldBeAdded() {
        return true;
    }

    private void bindContent() {
        bindKeepingStyles(checkBox, toRule(StyleName.CONDITION));
        bindKeepingStyles(showAll, $(getElement()).find(toRule(StyleName.COMPONENT)).children(toRule(StyleName.SHOW_ALL)).get(0));
        bindKeepingStyles(fieldsPanel, toRule(StyleName.FIELDS));
        bind(extendedFieldsPanel, toRule(StyleName.EXTENDED_FIELDS));
    }

    private void createFieldsPanel() {
        createNonExtendedFields();
        createExtendedFields();
    }

    private void createNonExtendedFields() {
        fieldsPanel = compositeFieldDisplay().hasLayout() ? createRenderer(compositeFieldDisplay().getShowLayout()) : new LayoutWidget();
        addWidgets(fieldsPanel, compositeFieldDisplay().getNonExtendedDisplays());

        fieldsPanel.setNavigationHandler(new NavigationHandler() {
            @Override
            public void onNavigate(Key key) {
                if (key == Key.ARROW_DOWN) extendedFieldsPanel.focusFirst();
                if (key == Key.ARROW_UP && navigationHandler != null) navigationHandler.onNavigate(Key.ARROW_UP);
            }
        });
    }

    private void createExtendedFields() {
        extendedFieldsPanel = compositeFieldDisplay().hasLayoutExtended() ? createRenderer(compositeFieldDisplay().getShowLayoutExtended()) : new LayoutWidget();
        extendedFieldsPanel.setStyleName(StyleName.FIELDS);
        addWidgets(extendedFieldsPanel, compositeFieldDisplay().getExtendedDisplays());
        extendedFieldsPanel.setVisible(false);
        extendedFieldsPanel.setNavigationHandler(new NavigationHandler() {
            @Override
            public void onNavigate(Key key) {
                if (key == Key.ARROW_UP) fieldsPanel.focusLast();
                if (key == Key.ARROW_DOWN && navigationHandler != null) navigationHandler.onNavigate(Key.ARROW_DOWN);
            }
        });
    }

    private LayoutRendererWidget createRenderer(String layout) {
        return new LayoutRendererWidget(layout);
    }

    private void createCheckbox() {
        checkBox = new CheckBox(translator.translate(Label.CLICK_TO_CONFIRM));
        checkBox.setTitle(translator.translate(OperationLabel.SHOW_COMPOSITE_FIELDS));
        checkBox.addValueChangeHandler(valueChangeHandler());
        checkBox.setVisible(compositeFieldDisplay().isConditional());
    }

    private void createShowAll() {
        showAllIcon = new InlineHTML();
        showAllIcon.setStyleName(StyleName.SHOW_ALL_ICON);
        showAll = new HTMLPanel(compositeFieldDisplay().getShowAllLabel());
        showAll.addDomHandler(showAllClickHandler(), ClickEvent.getType());
        showAll.setVisible(compositeFieldDisplay().isExtensible());
        showAll.add(showAllIcon);
    }

    protected void showContent() {
        fieldsPanel.setVisible(true);
        showAll.setVisible(compositeFieldDisplay().isExtensible());
        extendedFieldsPanel.setVisible(compositeFieldDisplay().getShowAllValue());
    }

    protected void hideContent() {
        fieldsPanel.setVisible(false);
        showAll.setVisible(false);
        extendedFieldsPanel.setVisible(false);
    }

    protected void refreshFields() {
        refreshFieldsPanel(compositeFieldDisplay().getNonExtendedDisplays());
    }

    protected void refreshExtendedFields() {
        refreshExtendedFieldsPanel(compositeFieldDisplay().getExtendedDisplays());
    }

    protected void refreshFieldsPanel(List<FieldDisplay> displays) {
        fieldsPanel.clear();
        for (FieldDisplay fieldDisplay : displays)
            buildWidget(fieldsPanel, fieldDisplay);
    }

    protected void addWidgets(LayoutWidget container, List<FieldDisplay> displays) {
        container.clear();
        for (FieldDisplay fieldDisplay : displays)
            buildWidget(container, fieldDisplay);
    }

    protected void refreshExtendedFieldsPanel(List<FieldDisplay> displays) {
        extendedFieldsPanel.clear();
        extendedFieldsPanel.setVisible(!displays.isEmpty());
        for (FieldDisplay fieldDisplay : displays)
            buildWidget(extendedFieldsPanel, fieldDisplay);
    }

    protected void buildWidget(LayoutWidget container, FieldDisplay fieldDisplay) {
        FieldWidget widget = (FieldWidget) Builder.get().build(fieldDisplay, null, null);
        if (widget == null) return;
        container.add(widget);
        widgets.add(widget);
    }

    private ValueChangeHandler<Boolean> valueChangeHandler() {
        return new ValueChangeHandler<Boolean>() {
            @Override
            public void onValueChange(ValueChangeEvent<Boolean> event) {
                if (event.getValue())
                    showContent();
                else
                    hideContent();
                compositeFieldDisplay().toggleCondition();
            }
        };
    }

    private ClickHandler showAllClickHandler() {
        return new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                $(showAllIcon).toggleClass(StyleName.SHOW_ALL_ICON, StyleName.SHOW_LESS_ICON);
                extendedFieldsPanel.setVisible(!extendedFieldsPanel.isVisible());
                compositeFieldDisplay().toggleShowMore();
            }
        };
    }

    private IsCompositeFieldDisplay compositeFieldDisplay() {
        return (IsCompositeFieldDisplay) display;
    }

    @Override
    public void focusFirst() {
        fieldsPanel.focusFirst();
    }

    @Override
    public void focusLast() {
        if (extendedFieldsPanel.isVisible() && extendedFieldsPanel.isNavigable())
            extendedFieldsPanel.focusLast();
        else
            fieldsPanel.focusLast();
    }

    @Override
    public void focus() {
    }

    @Override
    public void setNavigationHandler(NavigationHandler navigationHandler) {
        this.navigationHandler = navigationHandler;
    }

    @Override
    public boolean isNavigable() {
        for (FieldWidget widget : widgets)
            if (widget instanceof Focusable && ((Focusable) widget).isNavigable()) return true;
        return false;
    }

    public interface StyleName extends FieldWidget.StyleName {
        String COMPOSITE = "composite";
        String CONDITION = "condition";
        String EXTENDED_FIELDS = "extended-fields";
        String FIELDS = "fields";
        String SHOW_ALL = "show-all";
        String SHOW_ALL_ICON = "show-all-icon";
        String SHOW_LESS_ICON = "show-less-icon";
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(CompositeFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new CompositeFieldWidget((IsCompositeFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-composite";
	    }
    }
}
