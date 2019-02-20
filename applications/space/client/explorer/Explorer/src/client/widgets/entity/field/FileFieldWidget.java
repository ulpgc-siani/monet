package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.File;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.MultipleFieldDisplay;
import client.presenters.displays.entity.field.FileFieldDisplay;
import client.presenters.displays.entity.field.IsFileFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.popups.FilePopupWidget;
import client.widgets.toolbox.FileUploaderWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import cosmos.presenters.Presenter;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class FileFieldWidget extends ValueFieldWidget<TextBox, FilePopupWidget> {

    private HTML downloadButton;

    public FileFieldWidget(IsFileFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.FILE);
    }

    @Override
    protected void createComponent() {
        super.createComponent();
        createDownloadButton();
    }

    @Override
    protected void createInput() {
        input = new TextBox();
    }

    @Override
    protected void createPopup() {
	    popup = new FilePopupWidget(getPopupLayout(), input, translator, fileFieldDisplay().getLimit());
        super.createPopup();
	    popup.setSubmitRequestHandler(saveSubmittedFile());
    }

    protected FileUploaderWidget.SubmitRequestHandler saveSubmittedFile() {
        return new FileUploaderWidget.SubmitRequestHandler() {
            @Override
            public void request(String filename, String data) {
                if (input.getText().isEmpty())
                    fileFieldDisplay().saveFile(filename, data);
                else
                    fileFieldDisplay().saveFile(fileNameWithOriginalExtension(filename), data);

            }
        };
    }

    protected String fileNameWithOriginalExtension(String originalFileName) {
        if (input.getText().endsWith(originalFileName.substring(originalFileName.lastIndexOf("."))))
            return input.getText();
        return input.getText() + originalFileName.substring(originalFileName.lastIndexOf("."));
    }

    @Override
    protected void update(String value) {
        fileFieldDisplay().updateFilename(value);
    }

    protected void update(File value) {
        fileFieldDisplay().setValue(value);
    }

    @Override
    protected void bind() {
        if (!(display instanceof MultipleFieldDisplay))
            bindKeepingStyles(downloadButton, toRule(StyleName.TOOLBAR, StyleName.DOWNLOAD));
        super.bind();
    }

    @Override
    protected void refreshWhenHasValue() {
        super.refreshWhenHasValue();
        input.setText(fileFieldDisplay().getFileLabel());
        downloadButton.setVisible(true);
        popup.submitFinished();
    }

    @Override
    protected void refreshWhenEmpty() {
        super.refreshWhenEmpty();
        input.setText("");
	    popup.deleteFile();
        downloadButton.setVisible(false);
    }

    private IsFileFieldDisplay fileFieldDisplay() {
        return (IsFileFieldDisplay) display;
    }

    private void createDownloadButton() {
        downloadButton = new HTML("download");
        downloadButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                downloadFile();
            }
        });
    }

    private void downloadFile() {
        if (!display.hasValue()) return;
        Window.open(fileFieldDisplay().getFileId(), "_self", "enabled");
    }

    public interface StyleName extends ValueFieldWidget.StyleName {
        String FILE = "file";
        String DOWNLOAD = "download";
    }

    public static class Builder extends FieldWidget.Builder {

	    public Builder() {
		    super();
	    }

	    public static void register(){
            registerBuilder(FileFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new FileFieldWidget((IsFileFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-file";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-file";
	    }

    }
}
