package client.widgets.entity.components.composite;

import client.core.model.types.CheckCategory;
import client.services.TranslatorService;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;

public class CheckCategoryPanelWidget extends HTMLPanel {

    private final TranslatorService translator;
    private SelectionChangeHandler selectionChangeHandler;
    private final DisclosurePanel disclosurePanel;

    public CheckCategoryPanelWidget(TranslatorService translator, CheckCategory category, Widget content) {
        super("");
        addStyleName(StyleName.CATEGORY);
        this.translator = translator;
        disclosurePanel = new DisclosurePanel(category.getLabel());
        disclosurePanel.setContent(content);
        add(disclosurePanel);
        addActions();
    }

    public void setSelectionChangeHandler(SelectionChangeHandler selectionChangeHandler) {
        this.selectionChangeHandler = selectionChangeHandler;
    }

    private void addActions() {
        final HorizontalPanel actions = new HorizontalPanel();
        actions.setStyleName(StyleName.CHECK_ACTIONS);
        actions.add(createSelectAllAction());
        actions.add(createUnSelectAllAction());
        add(actions);
    }

    private Widget createSelectAllAction() {
        final Anchor anchor = new Anchor(translator.translate(TranslatorService.ListLabel.SELECT_ALL));
        anchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                disclosurePanel.setOpen(true);
                if (selectionChangeHandler != null) selectionChangeHandler.all();
            }
        });
        return anchor;
    }

    private Widget createUnSelectAllAction() {
        final Anchor anchor = new Anchor(translator.translate(TranslatorService.ListLabel.SELECT_NONE));
        anchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                disclosurePanel.setOpen(true);
                if (selectionChangeHandler != null) selectionChangeHandler.none();
            }
        });
        return anchor;
    }

    public interface SelectionChangeHandler {
        void all();
        void none();
    }

    public interface StyleName {
        String CATEGORY = "category";
        String CHECK_ACTIONS = "check-actions";
    }
}
