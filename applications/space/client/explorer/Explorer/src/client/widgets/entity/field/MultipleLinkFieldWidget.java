package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.Link;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsLinkFieldDisplay;
import client.presenters.displays.entity.field.IsMultipleLinkFieldDisplay;
import client.presenters.displays.entity.field.MultipleLinkFieldDisplay;
import client.presenters.displays.view.ViewDisplay;
import client.services.TranslatorService;
import client.widgets.entity.components.MultipleFieldSortableValueListWidget;
import client.widgets.toolbox.ErasableListWidget;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import java.util.HashMap;
import java.util.Map;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class MultipleLinkFieldWidget extends LinkFieldWidget {

    private MultipleFieldSortableValueListWidget<String> values;
    private Map<String, Integer> indexesMap;

    public MultipleLinkFieldWidget(IsLinkFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.MULTIPLE);
    }

    @Override
    protected void createComponent() {
        indexesMap = new HashMap<>();
        createValueList();
        super.createComponent();
    }

    @Override
    protected void bind() {
        bind(values, toRule(StyleName.VALUES));
        super.bind();
    }

    @Override
    protected void refreshComponent() {
        values.clear();
        indexesMap.clear();
        for (Link value : multipleLinkFieldDisplay().getAllValues())
            createItem(value);
        if (popup != null) popup.refresh();
        values.refresh();
    }

    @Override
    protected FieldDisplay.Hook createHook() {
        return new MultipleLinkFieldDisplay.Hook() {
            @Override
            public void indexLoaded() {
                createPopupDeferred();
            }

            @Override
            public void entityLoaded(ViewDisplay viewDisplay) {
                showEntity();
            }

            @Override
            public void values() {
                refresh();
            }

            @Override
            public void value() {
                refresh();
            }

            @Override
            public void error(String error) {
            }
        };
    }

    private void createItem(Link value) {
        values.addItem(value.getLabel());
        indexesMap.put(value.getLabel(), indexesMap.size());
    }

    private void createValueList() {
        values = new MultipleFieldSortableValueListWidget<>(createList());
        values.setChangePositionHandler(new MultipleFieldSortableValueListWidget.ChangePositionHandler<String>() {
            @Override
            public void onPositionChange(String item, int newPosition) {
                multipleLinkFieldDisplay().changeOrder(multipleLinkFieldDisplay().getLinkByLabel(item), newPosition);
            }
        });
        values.setDeleteHandler(new MultipleFieldSortableValueListWidget.DeleteHandler<String>() {
            @Override
            public void onDelete(String item) {
                multipleLinkFieldDisplay().delete(indexesMap.get(item));
            }
        });
    }

    private ErasableListWidget<String> createList() {
        return new ErasableListWidget<>(new ErasableListWidget.ListItem.Builder(translator), translator);
    }

    private IsMultipleLinkFieldDisplay multipleLinkFieldDisplay() {
        return (IsMultipleLinkFieldDisplay) display;
    }

    public static class Builder extends LinkFieldWidget.Builder {

        public static void register() {
            registerBuilder(MultipleLinkFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            createBuilders();
            return new MultipleLinkFieldWidget((IsLinkFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-link";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-link-multiple";
	    }

    }

    public interface StyleName extends LinkFieldWidget.StyleName {
    }
}
