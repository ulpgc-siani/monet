package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.Picture;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.MultipleFieldDisplay;
import client.presenters.displays.entity.field.IsMultiplePictureFieldDisplay;
import client.presenters.displays.entity.field.IsPictureFieldDisplay;
import client.presenters.displays.entity.field.MultiplePictureFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.MultipleFieldSortableValueListWidget;
import client.widgets.entity.components.MultipleFieldSortableValueListWidget.ChangePositionHandler;
import client.widgets.entity.components.MultipleFieldSortableValueListWidget.DeleteHandler;
import client.widgets.popups.FullSizePicturePopUpWidget;
import client.widgets.toolbox.ErasableListWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import cosmos.presenters.Presenter;

import java.util.HashMap;
import java.util.Map;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class MultiplePictureFieldWidget extends PictureFieldWidget {

    private MultipleFieldSortableValueListWidget<HorizontalPanel> values;
    private Map<HorizontalPanel, Integer> indexesMap;
    private Map<HorizontalPanel, Picture> panelToFileMap;

    protected MultiplePictureFieldWidget(IsPictureFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.MULTIPLE);
    }

    @Override
    protected void createField() {
        indexesMap = new HashMap<>();
        panelToFileMap = new HashMap<>();
        createValueList();
        super.createField();
    }

    @Override
    protected void update(String value) {
    }

    @Override
    protected void update(Picture value) {
        multiplePictureFieldDisplay().add(value);
	    popup.deletePicture();
    }

    @Override
    protected void bind() {
        bind(values, toRule(StyleName.VALUES));
        super.bind();
    }

    @Override
    protected boolean hasToolbar() {
        return false;
    }

    @Override
    protected void refreshComponent() {
        values.clear();
        indexesMap.clear();
        panelToFileMap.clear();
        for (Picture value : multiplePictureFieldDisplay().getAllValues())
            createItem(value);
        values.refresh();
    }

    private void createItem(final Picture value) {
        HorizontalPanel picturePanel = createPicturePanel(value);
        values.addItem(picturePanel);
        indexesMap.put(picturePanel, indexesMap.size());
        panelToFileMap.put(picturePanel, value);
        values.getItem(picturePanel).addOperation(createDownloadButton(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Window.open(multiplePictureFieldDisplay().getPictureLink(value), "_self", "enabled");
            }
        }));
    }

    private HorizontalPanel createPicturePanel(Picture value) {
        HorizontalPanel panel = new HorizontalPanel();
        panel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        panel.add(createPreview(value));
        panel.add(new Label(value.getLabel()));
        return panel;
    }

    private IsWidget createPreview(final Picture picture) {
        Image preview = new Image(multiplePictureFieldDisplay().getThumbnail(picture));
        preview.setStyleName(PictureFieldWidget.StyleName.PREVIEW);
        preview.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RootPanel.get().add(new FullSizePicturePopUpWidget(picture.getLabel(), multiplePictureFieldDisplay().getPictureLink(picture)));
            }
        });
        return preview;
    }

    @Override
    protected FieldDisplay.Hook createHook() {
        return new MultipleFieldDisplay.Hook() {
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

    private void createValueList() {
        values = new MultipleFieldSortableValueListWidget<>(createList());
        values.setDeleteHandler(new DeleteHandler<HorizontalPanel>() {
            @Override
            public void onDelete(HorizontalPanel value) {
                multiplePictureFieldDisplay().delete(indexesMap.get(value));
            }
        });
        values.setChangePositionHandler(new ChangePositionHandler<HorizontalPanel>() {
            @Override
            public void onPositionChange(HorizontalPanel item, int newPosition) {
                multiplePictureFieldDisplay().changeOrder(panelToFileMap.get(item), newPosition);
            }
        });
    }

    private ErasableListWidget<HorizontalPanel> createList() {
        return new ErasableListWidget<>(new ErasableListWidget.WidgetListItem.Builder(translator), translator);
    }

    private IsMultiplePictureFieldDisplay multiplePictureFieldDisplay() {
        return (IsMultiplePictureFieldDisplay) display;
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(MultiplePictureFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new MultiplePictureFieldWidget((IsMultiplePictureFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-picture";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-picture-multiple";
	    }
    }
}
