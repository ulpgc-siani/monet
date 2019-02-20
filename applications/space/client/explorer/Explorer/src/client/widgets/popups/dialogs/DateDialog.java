package client.widgets.popups.dialogs;

import client.services.TranslatorService;
import client.utils.DateUtils;
import client.widgets.popups.DatePopupWidget;
import client.widgets.popups.dialogs.date.DateInputWidget;
import client.widgets.popups.dialogs.date.DateSelectorWidget;
import client.widgets.popups.dialogs.date.TimeSelectorWidget;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import cosmos.types.Date;
import cosmos.utils.date.DateParser;

public class DateDialog extends VerticalPanel {

    private final DateInputWidget input;
    private final DateSelectorWidget dateSelector;
    private TimeSelectorWidget timeSelector;
    private Date date;
    private DatePopupWidget.DateSelectedHandler dateSelectedHandler;
    private ClosePopupHandler closePopupHandler;

    public DateDialog(DateParser parser, TranslatorService translator, DateUtils dateUtils) {
        this.input = new DateInputWidget(translator, parser);
        input.setOnEscapeHandler(new DateInputWidget.OnEscapeHandler() {
            @Override
            public void onEscape() {
                if (closePopupHandler != null)
                    closePopupHandler.closePopup();
            }
        });
        this.dateSelector = new DateSelectorWidget(translator, dateUtils);
        add(input);
        add(dateSelector);
        if (dateUtils.hasTimePrecision())
            addTimeSelector(dateUtils);
    }

    public void setDateSelectedHandler(final DatePopupWidget.DateSelectedHandler dateSelectedHandler) {
        this.dateSelectedHandler = dateSelectedHandler;
        input.setDateSelectedHandler(dateSelectedHandler);
        dateSelector.setDateSelectedHandler(dateSelectedHandler);
        if (timeSelector != null) timeSelector.setDateSelectedHandler(dateSelectedHandler);
    }

    public void setClosePopupHandler(ClosePopupHandler closePopupHandler) {
        this.closePopupHandler = closePopupHandler;
    }

    public void focusInput() {
        input.focus();
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
        input.setDate(date);
        dateSelector.setDate(date);
        if (timeSelector != null) timeSelector.setDate(date);
    }

    public void allowLessPrecision(String message, String confirm) {
        createPrecisionPanel(message, confirm);
        dateSelector.setDateSelectedHandler(new DatePopupWidget.DateSelectedHandler() {
            @Override
            public void dateSelected(Date date) {
                DateDialog.this.date = date;
                dateSelector.setDate(date);
            }
        });
        if (timeSelector == null) return;
        timeSelector.setDateSelectedHandler(new DatePopupWidget.DateSelectedHandler() {
            @Override
            public void dateSelected(Date date) {
                DateDialog.this.date = date;
            }
        });
    }

    private void addTimeSelector(DateUtils dateUtils) {
        timeSelector = new TimeSelectorWidget(dateUtils.getPrecision());
        addSeparator();
        add(timeSelector);
    }

    private void addSeparator() {
        final InlineHTML separator = new InlineHTML();
        separator.setStyleName(StyleName.SEPARATOR);
        add(separator);
    }

    private void createPrecisionPanel(String message, String confirm) {
        final HorizontalPanel panel = new HorizontalPanel();
        panel.addStyleName(StyleName.PRECISION_PANEL);
        panel.add(createMessagePanel(message));
        panel.add(createConfirmButton(confirm));
        add(panel);
    }

    private VerticalPanel createMessagePanel(String message) {
        final VerticalPanel messagePanel = new VerticalPanel();
        for (String s : message.split("\\."))
            messagePanel.add(new Label(s + "."));
        return messagePanel;
    }

    private Anchor createConfirmButton(String confirm) {
        final Anchor confirmButton = new Anchor(confirm);
        confirmButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                dateSelectedHandler.dateSelected(date);
                event.stopPropagation();
            }
        });
        return confirmButton;
    }

    public interface StyleName {
        String PRECISION_PANEL = "precision-panel";
        String SEPARATOR = "separator";
    }

    public interface ClosePopupHandler {
        void closePopup();
    }
}
