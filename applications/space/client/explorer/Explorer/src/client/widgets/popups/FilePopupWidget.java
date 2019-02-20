package client.widgets.popups;

import client.services.TranslatorService;
import client.widgets.popups.dialogs.FileDialog;
import client.widgets.toolbox.FileUploaderWidget;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextBoxBase;

public class FilePopupWidget extends FieldPopupWidget<TextBoxBase, FileDialog> {

    private final TranslatorService translator;
    private final long sizeLimit;

    public FilePopupWidget(String layout, TextBoxBase input, TranslatorService translator, long sizeLimit) {
        super(layout, input);
        this.translator = translator;
        this.sizeLimit = sizeLimit;
        init();
	}

    public void setSubmitRequestHandler(FileUploaderWidget.SubmitRequestHandler handler) {
        content.setSubmitRequestHandler(handler);
    }

    public void deleteFile() {
        content.deleteFile();
    }

    public void submitFinished() {
        content.submitFinished();
        hide();
    }

	@Override
	protected FileDialog createDialog(Element container) {
		FileDialog dialog = new FileDialog(translator, sizeLimit, container.getInnerHTML());
		bindKeepingStyles(dialog, container);
		return dialog;
	}

    public void allowMultipleSelection() {
        content.allowMultipleSelection();
    }
}
