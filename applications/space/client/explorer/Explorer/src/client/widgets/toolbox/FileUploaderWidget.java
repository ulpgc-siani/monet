package client.widgets.toolbox;

import client.services.TranslatorService;
import client.services.TranslatorService.ErrorLabel;
import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import cosmos.gwt.widgets.CosmosHtmlPanel;

import static client.services.TranslatorService.OperationLabel;
import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class FileUploaderWidget extends CosmosHtmlPanel {

    private static final int SIZE = 0;
    private static final int FILENAME = 1;
    private static final int BASE64_DATA = 2;
    private final Label informationLabel;
    private final HorizontalPanel operationsPanel;
    private final String uploadingString;
    private final String fileTooLargeString;
    private final int sizeLimit;
    private FileUpload fileUploader;
    private FileSelectedHandler fileSelectedHandler;
    private SubmitRequestHandler submitRequestHandler;
    private Anchor uploadAnchor;

    public FileUploaderWidget(TranslatorService translator, long sizeLimit, String layout) {
        super(layout);
        this.sizeLimit = (int) sizeLimit;
        this.uploadingString = translator.translate(OperationLabel.UPLOADING);
        this.fileTooLargeString = translator.translate(ErrorLabel.FILE_SIZE) + " ( " + fromBytesToMegaBytes(sizeLimit) + " MB)";
        informationLabel = createInformationLabel();
        operationsPanel = createOperationsPanel(translator.translate(OperationLabel.UPLOAD));
        bind();
    }

    private int fromBytesToMegaBytes(long sizeLimit) {
        return (int) ((sizeLimit / 1024) / 1024);
    }

    public void submitFinished() {
        disableOperations();
        informationLabel.setVisible(false);
    }

    public void deleteFile() {
        $(fileUploader).val("");
        submitFinished();
    }

    public void setFileSelectedHandler(FileSelectedHandler fileSelectedHandler) {
        this.fileSelectedHandler = fileSelectedHandler;
    }

    public void setSubmitRequestHandler(SubmitRequestHandler submitRequestHandler) {
        this.submitRequestHandler = submitRequestHandler;
    }

    public void addOperation(String label, final Operation operation) {
        final Anchor anchor = new Anchor(label);
        operationsPanel.add(anchor);
        anchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                operation.apply();
            }
        });
    }

    private void bind() {
        bindKeepingStyles(createUploader(), $(getElement()).children(toRule(StyleName.UPLOADER)).get(0));
        bindKeepingStyles(informationLabel, $(getElement()).children(toRule(StyleName.INFO)).get(0));
        bindKeepingStyles(operationsPanel, $(getElement()).children(toRule(StyleName.FILE_OPERATIONS)).get(0));
    }

    private void showUploading() {
        disableOperations();
        informationLabel.setText(uploadingString);
    }

    private HorizontalPanel createOperationsPanel(String upload) {
        final HorizontalPanel panel = new HorizontalPanel();
        panel.add(createUploadAnchor(upload));
        panel.addStyleName(StyleName.FILE_OPERATIONS);
        return panel;
    }

    private Widget createUploader() {
        fileUploader = new FileUpload();
        fileUploader.setName("file-uploader");
        fileUploader.addChangeHandler(new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                enableOperations();
                loadFileSelectedInfo(fileUploader.getElement(), new SuccessCallback<JsArrayString>() {
                    @Override
                    public void onSuccess(JsArrayString result) {
                        notifySelection(Integer.valueOf(result.get(SIZE)), result.get(FILENAME), result.get(BASE64_DATA));
                    }
                });
            }
        });
        return fileUploader;
    }

    private Anchor createUploadAnchor(String label) {
        uploadAnchor = new Anchor(label);
        uploadAnchor.addStyleName(StyleName.UPLOAD);
        uploadAnchor.addStyleName(StyleName.DISABLED);
        uploadAnchor.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                loadFileSelectedInfo(fileUploader.getElement(), new SuccessCallback<JsArrayString>() {
                    @Override
                    public void onSuccess(JsArrayString result) {
                        showUploading();
                        if (submitRequestHandler != null)
                            submitRequestHandler.request(result.get(FILENAME), result.get(BASE64_DATA));
                    }
                });
            }
        });
        return uploadAnchor;
    }

    private Label createInformationLabel() {
        final Label label = new Label(uploadingString);
        label.setVisible(false);
        return label;
    }

    private void notifySelection(int size, String filename, String data) {
        informationLabel.setVisible(false);
        operationsPanel.setVisible(true);
        if (sizeLimit > 0 && size > sizeLimit)
            showErrorFileTooLarge();
        else if (fileSelectedHandler != null)
            fileSelectedHandler.onSelect(filename, data);
    }

    private void showErrorFileTooLarge() {
        deleteFile();
        operationsPanel.setVisible(false);
        informationLabel.setVisible(true);
        informationLabel.setText(fileTooLargeString);
    }

    private void enableOperations() {
        for (Widget widget : operationsPanel)
            widget.setVisible(true);
        uploadAnchor.removeStyleName(StyleName.DISABLED);
    }

    private void disableOperations() {
        for (Widget widget : operationsPanel)
            if (widget != uploadAnchor) widget.setVisible(false);
        uploadAnchor.addStyleName(StyleName.DISABLED);
    }

    private native void loadFileSelectedInfo(Element input, Callback<JsArrayString, String> callback) /*-{
        if (!input.files || !input.files[0]) return;
        for (var i = 0; i < input.files.length; i++)
            this.@client.widgets.toolbox.FileUploaderWidget::loadFileInfo(*)(input.files[i], callback);
    }-*/;

    private native void loadFileInfo(Object file, Callback<JsArrayString, String> callback) /*-{
        var reader = new FileReader();
        reader.onloadend = function (e) {
            callback.@com.google.gwt.core.client.Callback::onSuccess(Ljava/lang/Object;)([file.size, file.name, e.target.result]);
        };
        reader.readAsDataURL(file);
    }-*/;

    public void addElement(String selector, Widget component) {
        bindKeepingStyles(component, $(getElement()).children(selector).get(0));
    }

    public void allowMultipleSelection() {
        fileUploader.getElement().setAttribute("multiple", "multiple");
    }

    private abstract class SuccessCallback<T> implements Callback<T, String> {
        @Override
        public void onFailure(String reason) {
        }
    }

    public interface FileSelectedHandler {
        void onSelect(String filename, String data);
    }

    public interface SubmitRequestHandler {
        void request(String filename, String data);
    }

    public interface Operation {
        void apply();
    }

    public interface StyleName {
        String DISABLED = "disabled";
        String FILE_OPERATIONS = "file-operations";
        String INFO = "info";
        String UPLOAD = "upload";
        String UPLOADER = "uploader";
    }
}
