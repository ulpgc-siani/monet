package client.widgets.popups.dialogs.date;

import client.services.TranslatorService;
import client.utils.DateUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import cosmos.types.Date;
import cosmos.types.DateComponent;
import cosmos.types.DatePrecision;
import cosmos.types.DayOfWeek;

import java.util.HashMap;
import java.util.Map;

public class DaysWidget extends Grid implements DateComponentSelectorWidget {

    private final TranslatorService translator;
    private final DateUtils dateUtils;
    private Date date;
    private DateSelectedHandler handler;

    public DaysWidget(TranslatorService translator, DateUtils dateUtils) {
        super(7, 7);
        this.translator = translator;
        this.dateUtils = dateUtils;
        addStyleName(StyleName.DAYS_PANEL);
    }

    @Override
    public Date nextDate() {
        return date.nextMonth();
    }

    @Override
    public Date previousDate() {
        return date.previousMonth();
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
        addDays();
    }

    @Override
    public String getHeaderText() {
        return translator.translateDateWithPrecision(date, DatePrecision.MONTH);
    }

    @Override
    public void setOnDateSelected(DateSelectedHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean previousDatesAreAvailable() {
        return dateUtils.monthInsideRange(date.previousMonth());
    }

    @Override
    public boolean nextDatesAreAvailable() {
        return dateUtils.monthInsideRange(date.nextMonth());
    }

    private void addDays() {
        clear();
        createDayHeaders();
        int startDay = translator.dayOfWeekToNumber(date.atFirstDayOfMonth().getDayOfWeek());
        int currentDay = 1;
        int limitDay = date.getDaysInMonth();
        for (int row = 1; row < 7; row++) {
            for (int column = startDay; column < 7; column++) {
                if (limitDay < currentDay) return;
                setWidget(row, column, createDay(currentDay++));
            }
            startDay = 0;
        }
    }

    private void createDayHeaders() {
        Map<Integer, String> dayLabels = new HashMap<>();
        dayLabels.put(translator.dayOfWeekToNumber(DayOfWeek.MONDAY), translator.translate(TranslatorService.Label.MONDAY));
        dayLabels.put(translator.dayOfWeekToNumber(DayOfWeek.TUESDAY), translator.translate(TranslatorService.Label.TUESDAY));
        dayLabels.put(translator.dayOfWeekToNumber(DayOfWeek.WEDNESDAY), translator.translate(TranslatorService.Label.WEDNESDAY));
        dayLabels.put(translator.dayOfWeekToNumber(DayOfWeek.THURSDAY), translator.translate(TranslatorService.Label.THURSDAY));
        dayLabels.put(translator.dayOfWeekToNumber(DayOfWeek.FRIDAY), translator.translate(TranslatorService.Label.FRIDAY));
        dayLabels.put(translator.dayOfWeekToNumber(DayOfWeek.SATURDAY), translator.translate(TranslatorService.Label.SATURDAY));
        dayLabels.put(translator.dayOfWeekToNumber(DayOfWeek.SUNDAY), translator.translate(TranslatorService.Label.SUNDAY));
        for (int column = 0; column < 7; column++)
            setWidget(0, column, createDayHeader(dayLabels.get(column)));
    }

    private HTMLPanel createDayHeader(final String header) {
        return new HTMLPanel(header) {{
            setStyleName(StyleName.DAY_HEADER);
        }};
    }

    private HTML createDay(final int day) {
        final HTML dayPanel = new HTML(String.valueOf(day));
        dayPanel.setStyleName(StyleName.OPTION);
        if (!dateUtils.dayInsideRange(date.replaceDay(day)))
            dayPanel.setStyleName(StyleName.INVALID_OPTION);
        else {
            addHandler(day, dayPanel);
            if (date.isSet(DateComponent.DAY) && date.getDay() == day)
                dayPanel.addStyleName(StyleName.SELECTED);
        }
        return dayPanel;
    }

    private void addHandler(final int day, HTML dayPanel) {
        dayPanel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (handler == null) return;
                if (date.isSet(DateComponent.DAY) && day == date.getDay())
                    handler.onDateSelected(date);
                else
                    handler.onDateSelected(date.replaceDay(day)
                            .replaceHours(date.getHours())
                            .replaceMinutes(date.getMinutes())
                            .replaceSeconds(date.getSeconds()));
            }
        });
    }

    public interface StyleName {
        String OPTION = "option";
        String SELECTED = "selected";
        String INVALID_OPTION = "invalid-option";
        String DAY_HEADER = "day-header";
        String DAYS_PANEL = "days-panel";
    }
}
