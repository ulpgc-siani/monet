package client.widgets.entity.field;

import client.core.model.List;
import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.Composite;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.CompositeFieldDisplay;
import client.presenters.displays.entity.field.IsCompositeFieldDisplay;
import client.presenters.displays.entity.field.IsMultipleCompositeFieldDisplay;
import client.presenters.displays.entity.field.MultipleCompositeFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.composite.CompositeErasableTableWidget;
import client.widgets.entity.components.composite.CompositesContainer;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public abstract class MultipleCompositeFieldWidget<Container extends CompositesContainer> extends CompositeFieldWidget {

    protected Container compositesContainer;
    private Anchor addComposite;
    private Anchor deleteComposites;

    public MultipleCompositeFieldWidget(IsCompositeFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.MULTIPLE);
    }

    @Override
    protected void createComponent() {
        createCompositeContent();
        createToolbar();
        super.createComponent();
        addCompositeContainerHandlers();
        if (multipleCompositeFieldDisplay().isReadonly())
            $(getElement()).find(toRule(StyleName.TOOLBAR)).remove();
    }

    protected void addCompositeContainerHandlers() {
        compositesContainer.setItemSelectionChangeHandler(new CompositeErasableTableWidget.ItemSelectionChangeHandler() {
            @Override
            public void select() {
                enableDeleteComposites();
            }

            @Override
            public void selectNone() {
                disableDeleteComposites();
            }
        });
    }

    @Override
    protected void bind() {
        bindKeepingStyles(compositesContainer.asWidget(), toRule(StyleName.VALUES));
        if (!multipleCompositeFieldDisplay().isReadonly()) {
            bindKeepingStyles(addComposite, toRule(StyleName.ADD_COMPOSITE));
            bindKeepingStyles(deleteComposites, toRule(StyleName.REMOVE_COMPOSITES));
        }
        super.bind();
    }

    @Override
    protected void refreshComponent() {
        refreshCompositeContent();
        if (!multipleCompositeFieldDisplay().isReadonly()) refreshToolbar();
    }

    @Override
    protected CompositeFieldDisplay.Hook createHook() {
        return new MultipleCompositeFieldDisplay.Hook() {
            @Override
            public void values() {
                refresh();
            }

            @Override
            public void value() {
                refresh();
            }

            @Override
            public void show() {
                refresh();
            }

            @Override
            public void error(String error) {
            }

            @Override
            public void scroll() {
                if (multipleCompositeFieldDisplay().getShowAllValue())
                    multipleCompositeFieldDisplay().scroll(showAll.getAbsoluteTop());
                else
                    multipleCompositeFieldDisplay().scroll(fieldsPanel.getAbsoluteTop());
            }

            @Override
            public void compositeValue(Composite composite) {
                refreshCompositesContainer(composite);
            }
        };
    }

    protected void refreshCompositesContainer(Composite composite) {
    }

    private void createToolbar() {
        if (multipleCompositeFieldDisplay().isReadonly()) return;
        createAddComposite();
        createDeleteComposites();
    }

    private void createAddComposite() {
        addComposite = new Anchor(translator.translate(TranslatorService.OperationLabel.ADD));
        addComposite.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                multipleCompositeFieldDisplay().createComposite();
            }
        });
    }

    private void createDeleteComposites() {
        deleteComposites = new Anchor(translator.translate(TranslatorService.OperationLabel.DELETE_SELECTION));
        deleteComposites.addClickHandler(createDeleteHandler());
    }

    private ClickHandler createDeleteHandler() {
        return new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!compositesContainer.getSelectedComposites().isEmpty())
                    deleteComposites();
            }
        };
    }

    private void refreshToolbar() {
        refreshAddComposite();
        refreshDeleteComposites();
    }

    private void refreshAddComposite() {
        if (multipleCompositeFieldDisplay().hasMaxNumberOfComposites() || multipleCompositeFieldDisplay().hasCompositeWithoutValues())
            addComposite.addStyleName(StyleName.DISABLE_ADD_COMPOSITE);
        else
            addComposite.removeStyleName(StyleName.DISABLE_ADD_COMPOSITE);
        addComposite.setEnabled(!(multipleCompositeFieldDisplay().hasMaxNumberOfComposites() || multipleCompositeFieldDisplay().hasCompositeWithoutValues()));
    }

    private void refreshDeleteComposites() {
        refreshDeleteCompositesVisibility();
        if (deleteComposites.isVisible())
            refreshDeleteCompositesState();
    }

    private void refreshDeleteCompositesVisibility() {
        deleteComposites.setVisible(!multipleCompositeFieldDisplay().isEmpty());
    }

    private void refreshDeleteCompositesState() {
        if (compositesContainer.getSelectedComposites().isEmpty())
            disableDeleteComposites();
        else
            enableDeleteComposites();
    }

    protected void enableDeleteComposites() {
        deleteComposites.removeStyleName(StyleName.DISABLE_REMOVE_COMPOSITES);
    }

    protected void disableDeleteComposites() {
        deleteComposites.addStyleName(StyleName.DISABLE_REMOVE_COMPOSITES);
    }

    protected abstract void createCompositeContent();

    private void deleteComposites() {
        for (Integer index : getSelectedIndexes())
            multipleCompositeFieldDisplay().delete(index);
    }

    protected abstract List<Integer> getSelectedIndexes();

    protected abstract void refreshCompositeContent();

    protected IsMultipleCompositeFieldDisplay multipleCompositeFieldDisplay() {
        return (IsMultipleCompositeFieldDisplay) display;
    }

    public interface StyleName extends CompositeFieldWidget.StyleName {
        String ADD_COMPOSITE = "add-composite";
        String REMOVE_COMPOSITES = "remove-composites";
        String DISABLE_ADD_COMPOSITE = "disable-add-composite";
        String DISABLE_REMOVE_COMPOSITES = "disable-remove-composites";
        String TOOLBAR = "toolbar";
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(MultipleCompositeFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            final IsMultipleCompositeFieldDisplay display = (IsMultipleCompositeFieldDisplay) presenter;
            if (display.isSummary())
                return new MultipleCompositeSummaryFieldWidget(display, layout, translator);
            return new MultipleCompositeTableFieldWidget(display, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return ((MultipleCompositeFieldDisplay)display).isSummary()?"component-composite-multiple-summary":"component-composite-multiple-table";
	    }

    }
}
