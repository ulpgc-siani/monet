package client.widgets.popups.dialogs.date;

import client.services.TranslatorService;
import client.widgets.popups.DatePopupWidget;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.ui.TextBox;
import cosmos.types.Date;
import cosmos.utils.date.DateParser;
import cosmos.utils.date.MalformedDateException;

import static client.utils.KeyBoardUtils.isEnter;
import static client.utils.KeyBoardUtils.isEscape;

public class DateInputWidget extends TextBox {

    private final DateParser parser;
    private DatePopupWidget.DateSelectedHandler dateSelectedHandler;
    private Date date;
    private OnEscapeHandler onEscapeHandler;

    public DateInputWidget(TranslatorService translator, DateParser parser) {
        this.parser = parser;
        createInput(translator);
    }

    public void setDateSelectedHandler(DatePopupWidget.DateSelectedHandler dateSelectedHandler) {
        this.dateSelectedHandler = dateSelectedHandler;
    }

    public void focus() {
        setFocus(true);
    }

    public void setDate(Date date) {
        this.date = date;
    }

    private void createInput(TranslatorService translator) {
        addKeyUpHandler(new KeyUpHandler() {
            @Override
            public void onKeyUp(KeyUpEvent event) {
                if (isEscape(event.getNativeKeyCode())) {
                    if (onEscapeHandler != null) onEscapeHandler.onEscape();
                    return;
                }
                if (!isEnter(event.getNativeKeyCode()) || getText().isEmpty()) return;
                parseInput();
                setText("");
            }
        });
        getElement().setPropertyString("placeholder", translator.translate(TranslatorService.Label.DATE_PARSER));
    }

    private void parseInput() {
        try {
            dateSelectedHandler.dateSelected(parser.parse(getText(), date));
        } catch (MalformedDateException ex) {
        }
    }

    public void setOnEscapeHandler(OnEscapeHandler onEscapeHandler) {
        this.onEscapeHandler = onEscapeHandler;
    }

    public interface OnEscapeHandler {
        void onEscape();
    }
}
