package client.widgets.entity.field;

import client.core.model.List;
import client.core.model.types.Composite;
import client.core.system.MonetList;
import client.presenters.displays.entity.field.IsCompositeFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.components.composite.CompositeBoundaryPanel;
import client.widgets.entity.components.composite.CompositeErasableItem;
import client.widgets.toolbox.ErasableListWidget;
import client.widgets.toolbox.HTMLListWidget;
import client.widgets.toolbox.SortableHTMLListController;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MultipleCompositeSummaryFieldWidget extends MultipleCompositeFieldWidget<CompositeBoundaryPanel> {

    private Map<HTMLListWidget.ListItem<List<String>>, Integer> indexesMap;
    private Map<HTMLListWidget.ListItem<List<String>>, Composite> itemToComposite;

    public MultipleCompositeSummaryFieldWidget(IsCompositeFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.SUMMARY);
    }

    @Override
    protected void createComponent() {
        itemToComposite = new HashMap<>();
        indexesMap = new HashMap<>();
        super.createComponent();
    }

    @Override
    protected void refreshCompositeContent() {
        refreshSummary();
        refreshCurrentFields();
        refreshShowAll();
    }

    @Override
    protected void createCompositeContent() {
        compositesContainer = new CompositeBoundaryPanel(translator);
    }

    @Override
    protected List<Integer> getSelectedIndexes() {
        List<Integer> indexes = new MonetList<>();
        for (List<String> item : compositesContainer.getSelectedComposites())
            indexes.add(indexesMap.get(compositesContainer.getItem(item)));
        Collections.sort(indexes);
        Collections.reverse(indexes);
        return indexes;
    }

    @Override
    protected void addCompositeContainerHandlers() {
        compositesContainer.addClickHandler(new HTMLListWidget.ClickHandler<List<String>>() {
            @Override
            public void onClick(int index, List<String> value) {
                if (!multipleCompositeFieldDisplay().isCurrentComposite(itemToComposite.get(compositesContainer.getItem(value))))
                    multipleCompositeFieldDisplay().setCurrentComposite(itemToComposite.get(compositesContainer.getItem(value)));
                else
                    multipleCompositeFieldDisplay().setCurrentComposite(null);
                refreshCurrentFields();
            }
        });
        if (display.isReadonly()) return;
        super.addCompositeContainerHandlers();
        compositesContainer.addChangePositionEvent(new SortableHTMLListController.ChangePositionEvent<CompositeErasableItem>() {
            @Override
            public void onPositionChange(CompositeErasableItem item, int newPosition) {
                updatePosition(item, newPosition);
            }
        });
        compositesContainer.addDeleteHandler(new ErasableListWidget.DeleteHandler<List<String>>() {
            @Override
            public void onDelete(List<String> value) {
                multipleCompositeFieldDisplay().delete(indexesMap.get(compositesContainer.getItem(value)));
            }
        });
    }

    private void updatePosition(CompositeErasableItem item, int newPosition) {
        multipleCompositeFieldDisplay().changeOrder(itemToComposite.get(item), newPosition);
    }

    private void refreshSummary() {
        compositesContainer.clear();
        indexesMap.clear();
        for (Composite composite : multipleCompositeFieldDisplay().getAllValues())
            addComposite(indexesMap.size(), composite);
    }

    private void refreshCurrentFields() {
        if (multipleCompositeFieldDisplay().hasSelectedComposite())
            showContent();
        else
            hideContent();
        widgets.clear();
        refreshFields();
        refreshExtendedFields();
    }

    @Override
    protected void refreshShowAll() {
        super.refreshShowAll();
        showAll.setVisible(multipleCompositeFieldDisplay().isExtensible() && !multipleCompositeFieldDisplay().isEmpty());
    }

    @Override
    protected void refreshCompositesContainer(Composite composite) {
        compositesContainer.refreshComposite(multipleCompositeFieldDisplay().getValuesToShow(composite));
    }

    private void addComposite(int index, Composite composite) {
        final HTMLListWidget.ListItem<List<String>> item = addItemToContainer(index, composite);
        indexesMap.put(item, index);
        itemToComposite.put(item, composite);
    }

    private HTMLListWidget.ListItem<List<String>> addItemToContainer(int index, Composite composite) {
        if (multipleCompositeFieldDisplay().isCurrentComposite(composite))
            return compositesContainer.addItemSelected(index, multipleCompositeFieldDisplay().getValuesToShow(composite));
        return compositesContainer.addItem(index, multipleCompositeFieldDisplay().getValuesToShow(composite));
    }

    public interface StyleName extends MultipleCompositeFieldWidget.StyleName {
        String SUMMARY = "summary";
    }
}
