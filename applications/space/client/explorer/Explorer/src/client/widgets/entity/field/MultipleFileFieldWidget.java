package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.File;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.MultipleFieldDisplay;
import client.presenters.displays.entity.field.IsFileFieldDisplay;
import client.presenters.displays.entity.field.IsMultipleFileFieldDisplay;
import client.presenters.displays.entity.field.MultipleFileFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.entity.components.MultipleFieldSortableValueListWidget;
import client.widgets.toolbox.ErasableListWidget;
import client.widgets.toolbox.FileUploaderWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import java.util.HashMap;
import java.util.Map;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class MultipleFileFieldWidget extends FileFieldWidget {

    private MultipleFieldSortableValueListWidget<String> values;
    private Map<String, Integer> indexesMap;
    private Map<String, File> filenameToFileMap;

    public MultipleFileFieldWidget(IsFileFieldDisplay display, String layout, TranslatorService translator) {
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
        filenameToFileMap = new HashMap<>();
        createValueList();
        super.createComponent();
    }

    @Override
    protected void createPopup() {
        super.createPopup();
        popup.allowMultipleSelection();
    }

    @Override
    protected FileUploaderWidget.SubmitRequestHandler saveSubmittedFile() {
        return new FileUploaderWidget.SubmitRequestHandler() {
            @Override
            public void request(String filename, String data) {
                if (input.getText().isEmpty())
                    multipleFileFieldDisplay().saveFile(filename, data);
                else
                    multipleFileFieldDisplay().saveFile(fileNameWithOriginalExtension(filename), data);
                input.setText("");
            }
        };
    }

    @Override
    protected void update(String value) {
    }

    @Override
    protected void update(File value) {
        multipleFileFieldDisplay().add(value);
	    popup.deleteFile();
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
        filenameToFileMap.clear();
        for (File value : multipleFileFieldDisplay().getAllValues())
            createItem(value);
        values.refresh();
    }

    private void createItem(File value) {
        if (value.getLabel() == null || value.getLabel().isEmpty())
            addItemToList(value, value.getId());
        else
            addItemToList(value, value.getLabel());
    }

    private void addItemToList(File value, String label) {
        values.addItem(label);
        indexesMap.put(label, indexesMap.size());
        filenameToFileMap.put(label, value);
        values.getItem(label).addOperation(createDownloadButton(multipleFileFieldDisplay().getFileId(value)));
    }

    private HTML createDownloadButton(final String url) {
        HTML downloadButton = new HTML("download");
        downloadButton.setStyleName(StyleName.DOWNLOAD);
        downloadButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                Window.open(url, "_self", "enabled");
            }
        });
        return downloadButton;
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
        values.setChangePositionHandler(new MultipleFieldSortableValueListWidget.ChangePositionHandler<String>() {
            @Override
            public void onPositionChange(String item, int newPosition) {
                multipleFileFieldDisplay().changeOrder(filenameToFileMap.get(item), newPosition);
            }
        });
        values.setDeleteHandler(new MultipleFieldSortableValueListWidget.DeleteHandler<String>() {
            @Override
            public void onDelete(String item) {
                multipleFileFieldDisplay().delete(indexesMap.get(item));
            }
        });
    }

    private ErasableListWidget<String> createList() {
        return new ErasableListWidget<>(new ErasableListWidget.ListItem.Builder(translator), translator);
    }

    private IsMultipleFileFieldDisplay multipleFileFieldDisplay() {
        return (IsMultipleFileFieldDisplay) display;
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(MultipleFileFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new MultipleFileFieldWidget((IsMultipleFileFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-file";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-file-multiple";
	    }
    }
}
