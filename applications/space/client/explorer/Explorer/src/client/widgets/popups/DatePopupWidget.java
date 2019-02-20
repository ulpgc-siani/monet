package client.widgets.popups;

import client.services.TranslatorService;
import client.utils.DateUtils;
import client.widgets.entity.field.ValueFieldWidget;
import client.widgets.popups.dialogs.DateDialog;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.TextBox;
import cosmos.types.Date;
import cosmos.utils.date.DateBuilder;
import cosmos.utils.date.DateParser;

public class DatePopupWidget extends FieldPopupWidget<TextBox, DateDialog> {

    public DatePopupWidget(String layout, TextBox input, DateBuilder builder, TranslatorService translator, DateUtils dateUtils) {
        super(layout, new DateDialog(new DateParser(translator, builder, dateUtils.getPrecision()), translator, dateUtils), input);
        init();
    }

	@Override
	protected DateDialog createDialog(Element container) {
		bindKeepingStyles(content, container);
        content.setClosePopupHandler(new DateDialog.ClosePopupHandler() {
            @Override
            public void closePopup() {
                hide();
                input.setFocus(true);
            }
        });
		return content;
	}

	@Override
    public void show() {
        super.show();
        content.focusInput();
    }

    public void allowLessPrecision(String message, String confirm) {
        content.allowLessPrecision(message, confirm);
    }

    public void setDate(Date date) {
        content.setDate(date);
    }

    public void setDateSelectedHandler(DateSelectedHandler dateSelectedHandler) {
        content.setDateSelectedHandler(dateSelectedHandler);
    }

    public Date getDate() {
        return content.getDate();
    }

	public void hasTime() {
		addStyleName(StyleName.WITH_TIME);
	}

	public interface DateSelectedHandler {
        void dateSelected(Date date);
    }

	public interface StyleName extends ValueFieldWidget.StyleName {
		String WITH_TIME = "with-time";
	}

}
