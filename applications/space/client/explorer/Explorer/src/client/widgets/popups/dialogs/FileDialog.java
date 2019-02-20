package client.widgets.popups.dialogs;

import client.services.TranslatorService;
import client.widgets.toolbox.FileUploaderWidget;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;
import cosmos.gwt.widgets.CosmosHtmlPanel;

import static com.google.gwt.query.client.GQuery.$;
import static cosmos.gwt.utils.StyleUtils.toRule;

public class FileDialog extends CosmosHtmlPanel {

    protected FileUploaderWidget fileSelector;
    protected FileUploaderWidget.SubmitRequestHandler submitRequestHandler;

    public FileDialog(TranslatorService translator, long sizeLimit, String layout) {
        super(layout);
        final Element element = $(getElement()).children(toRule(StyleName.UPLOAD_COMPONENT)).get(0);
        fileSelector = new FileUploaderWidget(translator, sizeLimit, element.getInnerHTML());
        bindKeepingStyles(fileSelector, element);
    }

    public void addOperation(String label, FileUploaderWidget.Operation operation) {
        fileSelector.addOperation(label, operation);
    }

    public void deleteFile() {
        fileSelector.deleteFile();
    }

    public void setSubmitRequestHandler(final FileUploaderWidget.SubmitRequestHandler submitRequestHandler) {
        this.submitRequestHandler = submitRequestHandler;
        fileSelector.setSubmitRequestHandler(new FileUploaderWidget.SubmitRequestHandler() {
            @Override
            public void request(String filename, String data) {
                notifyRequest(filename, data);
            }
        });
    }

    public void submitFinished() {
        fileSelector.submitFinished();
    }

    public void addComponent(String selector, Widget component) {
        fileSelector.addElement(selector, component);
    }

    protected void notifyRequest(String filename, String data) {
        submitRequestHandler.request(filename, data);
    }

    public void allowMultipleSelection() {
        fileSelector.allowMultipleSelection();
    }

    public interface StyleName {
        String UPLOAD_COMPONENT = "upload-component";
    }
}
