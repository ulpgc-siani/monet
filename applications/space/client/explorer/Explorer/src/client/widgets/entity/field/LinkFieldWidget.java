package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.definition.views.ViewDefinition;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsLinkFieldDisplay;
import client.presenters.displays.entity.field.LinkFieldDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.InputKeyFilter;
import client.widgets.index.IndexWidget;
import client.widgets.popups.LinkPopupWidget;
import client.widgets.popups.dialogs.LinkFieldIndexDialog;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.query.client.Function;
import com.google.gwt.query.client.GQuery;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.widgets.CosmosHtmlPanel;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;
import static cosmos.gwt.utils.WidgetUtils.bindWidgetToElement;

public class LinkFieldWidget extends ValueFieldWidget<InputKeyFilter, LinkPopupWidget> {

    private HTMLPanel embeddedEntity;
    private Anchor closeButton;
    private GQuery editButton;

    public LinkFieldWidget(IsLinkFieldDisplay display, String layout, TranslatorService translator) {
		super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.LINK);
    }

    @Override
    protected void createComponent() {
        super.createComponent();
        createEmbeddedEntity();
        createEditButton();
    }

    @Override
    protected void createInput() {
        input = new InputKeyFilter();
        $(getElement()).find(toRule(StyleName.SELECT)).click(new Function() {
            @Override
            public void f() {
                showPopup();
            }
        });
    }

    @Override
	protected void createPopup() {
	}

    protected void createPopupDeferred() {
        popup = new LinkPopupWidget(getPopupLayout(), linkFieldDisplay().getLinkFieldIndexDisplay(), Builder.getLinkFieldIndexWidgetBuilder(), input);
        super.createPopup();
        bindWidgetToElement(this, popup, $(getElement()).find(toRule(StyleName.POPUP)).get(0));
    }

    @Override
    protected void bind() {
        bindKeepingStyles(embeddedEntity, toRule(StyleName.ENTITY));
        bindKeepingStyles(closeButton, toRule(StyleName.EMBEDDED_ENTITY, StyleName.CLOSE));
        super.bind();
    }

    @Override
    protected void refreshComponent() {
        super.refreshComponent();
        if (popup != null) popup.refresh();
    }

    @Override
    protected void refreshWhenHasValue() {
        input.setText(linkFieldDisplay().getValue().getLabel());
        input.setTitle(linkFieldDisplay().getValue().getLabel());
        if (linkFieldDisplay().allowEdit()) editButton.css("display", "");
        if (popup != null) popup.refresh();
        super.refreshWhenHasValue();
    }

    @Override
    protected void refreshWhenEmpty() {
        editButton.hide();
        super.refreshWhenEmpty();
    }

    @Override
    protected void onClickInput() {
        if (!display.isReadonly())
            super.onClickInput();
        else
            editEntity();
    }

    @Override
    protected boolean popupShouldBeAdded() {
        return false;
    }

    @Override
    protected FieldDisplay.Hook createHook() {
        return new LinkFieldDisplay.Hook() {
            @Override
            public void value() {
                refresh();

	            if (popup != null)
	                popup.hide();
            }

            @Override
            public void error(String error) {
            }

            @Override
            public void indexLoaded() {
                if (!display.isReadonly())
                    createPopupDeferred();
            }

            @Override
            public void entityLoaded(ViewDisplay display) {
                embeddedEntity.add(IndexWidget.Builder.getViewBuilder().build(display, ViewDefinition.Design.DOCUMENT.toString(), ViewDefinition.Design.DOCUMENT.toString()));
                showEntity();
            }
        };
    }

    protected void showEntity() {
        hideLoading();
        editButton.hide();
        closeButton.setVisible(true);
        $(getElement()).find(toRule(StyleName.EMBEDDED_ENTITY)).first().addClass(StyleName.VISIBLE);
    }

    private void createEmbeddedEntity() {
        embeddedEntity = new CosmosHtmlPanel();
        $(getElement()).find(toRule(StyleName.EMBEDDED_ENTITY)).insertAfter($(getElement()).find(toRule(StyleName.POPUP)));
        createCloseButton();
    }

    private void createEditButton() {
        editButton = $(getElement()).find(toRule(StyleName.TOOLBAR, StyleName.EDIT));
        if (linkFieldDisplay().allowEdit())
            editButton.click(new Function() {
                @Override
                public void f() {
                    editEntity();
                }
            });
        else
            editButton.hide();
    }

    private void createCloseButton() {
        closeButton = new Anchor(translator.translate(TranslatorService.Label.HIDE_SECTION));
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                $(getElement()).find(toRule(StyleName.EMBEDDED_ENTITY)).removeClass(StyleName.VISIBLE);
                closeButton.setVisible(false);
                editButton.css("display", "");
            }
        });
        closeButton.setVisible(false);
    }

    private void editEntity() {
        if (entityIsLoaded())
            showEntity();
        else
            loadEntity();
    }

    private boolean entityIsLoaded() {
        return embeddedEntity.getWidgetCount() > 0;
    }

    private void loadEntity() {
        showLoading();
        linkFieldDisplay().showEntity();
    }

    private void showLoading() {
        $(getElement()).find(toRule(StyleName.EMBEDDED_ENTITY)).addClass(StyleName.VISIBLE, StyleName.LOADING);
    }

    private void hideLoading() {
        $(getElement()).find(toRule(StyleName.EMBEDDED_ENTITY)).removeClass(StyleName.LOADING);
    }

    private IsLinkFieldDisplay linkFieldDisplay() {
        return (IsLinkFieldDisplay) display;
    }

    public static class Builder extends FieldWidget.Builder {
        private static LinkFieldIndexDialog.Builder linkFieldIndexWidgetBuilder;

	    public Builder() {
		    super();
	    }

	    public static void register(){
            registerBuilder(LinkFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            createBuilders();
            return new LinkFieldWidget((IsLinkFieldDisplay) presenter, layout, translator);
        }

        protected void createBuilders() {
            if (linkFieldIndexWidgetBuilder != null)
                return;

            linkFieldIndexWidgetBuilder = new LinkFieldIndexDialog.Builder();
            linkFieldIndexWidgetBuilder.inject(translator);
            linkFieldIndexWidgetBuilder.inject(theme);
        }

        public static LinkFieldIndexDialog.Builder getLinkFieldIndexWidgetBuilder() {
            return linkFieldIndexWidgetBuilder;
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-link";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-link";
	    }

    }

    public interface StyleName extends ValueFieldWidget.StyleName {
        String CLOSE = "close";
        String EDIT = "edit";
        String EMBEDDED_ENTITY = "embedded-entity";
        String ENTITY = "entity";
        String LINK = "link";
        String LOADING = "loading";
        String SELECT = "select";
        String VISIBLE = "visible";
    }
}
