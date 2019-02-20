package client.widgets.popups;

import client.core.model.definition.entity.field.PictureFieldDefinition;
import client.services.TranslatorService;
import client.widgets.popups.dialogs.PictureDialog;
import client.widgets.toolbox.FileUploaderWidget;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextBoxBase;

public class PicturePopupWidget extends FieldPopupWidget<TextBoxBase, PictureDialog> {

    private final TranslatorService translator;
    private final PictureFieldDefinition.SizeDefinition imageMaxSize;
    private final long sizeLimit;

    public PicturePopupWidget(String layout, TextBoxBase input, TranslatorService translator, PictureFieldDefinition.SizeDefinition imageMaxSize, long sizeLimit) {
        super(layout, input);
        this.translator = translator;
        this.imageMaxSize = imageMaxSize;
        this.sizeLimit = sizeLimit;
        init();
    }

    public void setSubmitRequestHandler(FileUploaderWidget.SubmitRequestHandler handler) {
        content.setSubmitRequestHandler(handler);
    }

    public void deletePicture() {
        content.deleteFile();
    }

	@Override
	protected PictureDialog createDialog(Element container) {
        PictureDialog dialog = new PictureDialog(translator, sizeLimit, imageMaxSize, container.getInnerHTML());
		bindKeepingStyles(dialog, container);
		return dialog;
	}
}
