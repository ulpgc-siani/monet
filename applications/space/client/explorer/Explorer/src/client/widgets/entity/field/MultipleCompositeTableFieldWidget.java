package client.widgets.entity.field;

import client.core.model.List;
import client.core.model.types.Composite;
import client.core.system.MonetList;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsCompositeFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.composite.CompositeErasableTableWidget;
import client.widgets.toolbox.ErasableListWidget;
import com.google.gwt.user.client.ui.HorizontalPanel;
import cosmos.gwt.widgets.HorizontalFocusablePanel;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static client.widgets.entity.components.composite.CompositeErasableTableWidget.ChangePositionEvent;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class MultipleCompositeTableFieldWidget extends MultipleCompositeFieldWidget<CompositeErasableTableWidget> {

    private Map<HorizontalPanel, Composite> horizontalPanelToComposite;
    private Map<HorizontalPanel, Integer> indexesMap;
    private boolean refreshing = false;

    public MultipleCompositeTableFieldWidget(IsCompositeFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.TABLE);
    }

    @Override
    protected void createComponent() {
        horizontalPanelToComposite = new HashMap<>();
        indexesMap = new HashMap<>();
        super.createComponent();
    }

    @Override
    protected void refreshComponent() {
        if (refreshing) return;
        refreshing = true;
        super.refreshComponent();
        refreshing = false;
    }

    @Override
    protected void createCompositeContent() {
        compositesContainer = new CompositeErasableTableWidget($(getElement()).find(toRule(StyleName.FIELD_LABEL)).html(), multipleCompositeFieldDisplay().getHeaders(), translator);
        compositesContainer.setStyleName(StyleName.FIELDS);
    }

    @Override
    protected void addCompositeContainerHandlers() {
        if (display.isReadonly()) return;
        super.addCompositeContainerHandlers();
        compositesContainer.addChangePositionEvent(new ChangePositionEvent() {
            @Override
            public void onPositionChange(HorizontalPanel item, int newPosition) {
                updatePosition(item, newPosition);
            }
        });
        compositesContainer.addDeleteHandler(new ErasableListWidget.DeleteHandler<HorizontalFocusablePanel>() {
            @Override
            public void onDelete(HorizontalFocusablePanel value) {
                multipleCompositeFieldDisplay().delete(indexesMap.get(value));
            }
        });
    }

    @Override
    protected List<Integer> getSelectedIndexes() {
        List<Integer> indexes = new MonetList<>();
        for (HorizontalPanel item : compositesContainer.getSelectedComposites())
            indexes.add(indexesMap.get(item));
        Collections.sort(indexes);
        Collections.reverse(indexes);
        return indexes;
    }

    @Override
    protected void refreshCompositeContent() {
        compositesContainer.clearComposites();
        indexesMap.clear();
        horizontalPanelToComposite.clear();
        for (Composite composite : multipleCompositeFieldDisplay().getAllValues())
            addCompositeRow(composite);
    }

    @Override
    protected boolean contentShouldBeAdded() {
        return false;
    }

    @Override
    public void setNavigationHandler(NavigationHandler navigationHandler) {
        compositesContainer.setNavigationHandler(navigationHandler);
    }

    private void updatePosition(HorizontalPanel item, int newPosition) {
        multipleCompositeFieldDisplay().changeOrder(horizontalPanelToComposite.get(item), newPosition);
    }

    private void addCompositeRow(Composite composite) {
        HorizontalFocusablePanel panel = new HorizontalFocusablePanel();
        for (FieldDisplay display : multipleCompositeFieldDisplay().getDisplaysForComposite(composite))
            panel.add(createFieldWidget(display));
        horizontalPanelToComposite.put(panel, composite);
        indexesMap.put(panel, indexesMap.size());
        compositesContainer.addRow(panel);
    }

    private FieldWidget createFieldWidget(FieldDisplay display) {
        final FieldWidget fieldWidget = (FieldWidget) Builder.get().build(display, null, null);
        fieldWidget.removeLabel();
        return fieldWidget;
    }

    public interface StyleName extends MultipleCompositeFieldWidget.StyleName {
        String TABLE = "table";
    }
}
