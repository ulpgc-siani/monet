package client.widgets.entity.components.composite;

import client.core.model.List;
import client.services.TranslatorService;
import client.widgets.toolbox.ErasableListWidget;
import client.widgets.toolbox.HTMLListWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

public class CompositeErasableItem extends ErasableListWidget.SelectableListItem<List<String>> {

    public CompositeErasableItem(List<String> value, TranslatorService translator) {
        super(value, translator);
    }

    public static CompositeErasableItem clone(CompositeErasableItem item) {
        final CompositeErasableItem clone = new CompositeErasableItem(item.getValue(), item.translator);
        clone.setStyleName(item.getStyleName());
        clone.select(item.isSelected());
        return clone;
    }

    @Override
    protected Widget createComponent() {
        HorizontalPanel panel = new HorizontalPanel();
        for (String field : value)
            createLabel(panel, field);
        return panel;
    }

    private void createLabel(HorizontalPanel panel, String field) {
        final Label label = new Label(field);
        panel.add(label);
        label.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                notifyClick();
            }
        });
        label.setStyleName(StyleName.COMPOSITE_VALUE);
        panel.setCellWidth(label, 100 / value.size() + "%");
    }

    public static class Builder extends ErasableListWidget.ListItem.Builder<List<String>> {

        public Builder(TranslatorService translator) {
            super(translator);
        }

        @Override
        public HTMLListWidget.ListItem<List<String>> build(List<String> value, HTMLListWidget.Mode mode) {
            return new CompositeErasableItem(value, translator);
        }
    }

    public interface StyleName {
        String COMPOSITE_VALUE = "composite-value";
    }
}
