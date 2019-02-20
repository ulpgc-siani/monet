package client.widgets.entity.field;

import client.core.model.definition.entity.FieldDefinition;
import client.core.model.types.Picture;
import client.presenters.displays.entity.FieldDisplay;
import client.presenters.displays.entity.field.IsPictureFieldDisplay;
import client.presenters.displays.entity.field.PictureFieldDisplay;
import client.services.TranslatorService;
import client.widgets.entity.FieldWidget;
import client.widgets.popups.FullSizePicturePopUpWidget;
import client.widgets.popups.PicturePopupWidget;
import client.widgets.toolbox.FileUploaderWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import cosmos.presenters.Presenter;

import static cosmos.gwt.utils.StyleUtils.toRule;

public class PictureFieldWidget extends ValueFieldWidget<TextBox, PicturePopupWidget> {

    private HTML downloadButton;
    private HTML showImageButton;
    private Image previewImage;

    protected PictureFieldWidget(IsPictureFieldDisplay display, String layout, TranslatorService translator) {
        super(display, layout, translator);
    }

    @Override
    protected void addStyleNames() {
        super.addStyleNames();
        addStyleName(StyleName.PICTURE);
    }

    @Override
    protected void createComponent() {
        super.createComponent();
        createPreviewImage();
        createShowImageButton();
        downloadButton = createDownloadButton(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                downloadFile();
            }
        });
    }

    @Override
    protected void createInput() {
        input = new TextBox();
    }

    @Override
    protected void createPopup() {
	    popup = new PicturePopupWidget(getPopupLayout(), input, translator, pictureFieldDisplay().getSize(), pictureFieldDisplay().getLimit());
        super.createPopup();
	    popup.setSubmitRequestHandler(new FileUploaderWidget.SubmitRequestHandler() {
            @Override
            public void request(String filename, String data) {
                if (input.getText().isEmpty())
                    pictureFieldDisplay().savePicture(filename, data);
                else
                    pictureFieldDisplay().savePicture(pictureNameWithOriginalExtension(filename), data);
            }
        });
    }

    protected String pictureNameWithOriginalExtension(String originalPictureName) {
        if (input.getText().endsWith(originalPictureName.substring(originalPictureName.lastIndexOf("."))))
            return input.getText();
        return input.getText() + originalPictureName.substring(originalPictureName.lastIndexOf("."));
    }

    @Override
    protected void update(String value) {
        pictureFieldDisplay().updateFilename(value);
    }

    protected void update(Picture value) {
        pictureFieldDisplay().setValue(value);
	    popup.deletePicture();
	    popup.hide();
    }

    @Override
    protected void bind() {
        bindKeepingStyles(previewImage, toRule(StyleName.PREVIEW));
        if (hasToolbar()) {
            bindKeepingStyles(showImageButton, toRule(StyleName.TOOLBAR, StyleName.SHOW));
            bindKeepingStyles(downloadButton, toRule(StyleName.TOOLBAR, StyleName.DOWNLOAD));
        }
        super.bind();
    }

    protected boolean hasToolbar() {
        return true;
    }

    @Override
    protected void refreshWhenHasValue() {
        super.refreshWhenHasValue();
        input.setReadOnly(display.isReadonly());
        showImageButton.setVisible(true);
        downloadButton.setVisible(true);
        input.setText(pictureFieldDisplay().getPictureLabel());
        previewImage.setTitle(pictureFieldDisplay().getPictureLabel());
        previewImage.setUrl(pictureFieldDisplay().getThumbnail());
    }

    @Override
    protected void refreshWhenEmpty() {
        super.refreshWhenEmpty();
        input.setReadOnly(true);
	    popup.deletePicture();
        showImageButton.setVisible(false);
        downloadButton.setVisible(false);
        previewImage.setUrl(pictureFieldDisplay().getThumbnail());
    }

    private void createPreviewImage() {
        previewImage = new Image(pictureFieldDisplay().getThumbnail());
        previewImage.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                event.stopPropagation();
                showPopup();
            }
        });
    }

    protected HTML createDownloadButton(ClickHandler clickHandler) {
        HTML downloadButton = new HTML();
        downloadButton.addClickHandler(clickHandler);
        return downloadButton;
    }

    private void createShowImageButton() {
        showImageButton = new HTML();
        showImageButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                RootPanel.get().add(new FullSizePicturePopUpWidget(pictureFieldDisplay().getPictureLabel(), pictureFieldDisplay().getPictureLink()));
            }
        });
    }

    private void downloadFile() {
        if (display.hasValue())
            Window.open(pictureFieldDisplay().getPictureLink(), "_self", "enabled");
    }

    private IsPictureFieldDisplay pictureFieldDisplay() {
        return (IsPictureFieldDisplay) display;
    }

    public interface StyleName extends ValueFieldWidget.StyleName {
        String DOWNLOAD = "download";
        String PICTURE = "picture";
        String PREVIEW = "preview";
        String SHOW = "show";
    }

    public static class Builder extends FieldWidget.Builder {

	    public static void register(){
            registerBuilder(PictureFieldDisplay.TYPE.toString() + FieldDefinition.FieldType.NORMAL.toString(), new Builder());
        }

        @Override
        public Widget build(Presenter presenter, String design, String layout) {
            return new PictureFieldWidget((IsPictureFieldDisplay) presenter, layout, translator);
        }

	    @Override
	    protected String getDialogClass(FieldDisplay display) {
		    return "dialog-picture";
	    }

	    @Override
	    protected String getComponentClass(FieldDisplay display) {
		    return "component-picture";
	    }

    }
}
