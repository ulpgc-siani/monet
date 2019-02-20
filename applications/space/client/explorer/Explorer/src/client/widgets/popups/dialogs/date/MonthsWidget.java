package client.widgets.popups.dialogs.date;

import client.utils.DateUtils;
import client.services.TranslatorService;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import cosmos.types.Date;
import cosmos.types.DateComponent;
import cosmos.types.DatePrecision;

import java.util.Arrays;
import java.util.List;

public class MonthsWidget extends HorizontalPanel implements DateComponentSelectorWidget {

    private final TranslatorService translator;
    private final DateUtils dateUtils;
    private Date date;
    private DateSelectedHandler handler;

    private static final int COLUMNS_COUNT = 3;

    public MonthsWidget(TranslatorService translator, DateUtils dateUtils) {
        this.translator = translator;
        this.dateUtils = dateUtils;
        addStyleName(StyleName.MONTHS_PANEL);
    }

    @Override
    public Date nextDate() {
        return date.nextYear();
    }

    @Override
    public Date previousDate() {
        return date.previousYear();
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
        refreshView();
    }

    private void refreshView() {
        clear();
        final List<Integer> months = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);
        int monthsSize = months.size();
        int columnsSize = monthsSize / COLUMNS_COUNT;

        add(createMonthColumn(date.getYear(), months.subList(0, columnsSize)));
        add(createMonthColumn(date.getYear(), months.subList(columnsSize, columnsSize*2)));
        add(createMonthColumn(date.getYear(), months.subList(columnsSize*2, monthsSize)));
    }

    private VerticalPanel createMonthColumn(int year, List<Integer> months) {
        final VerticalPanel yearsColumn = new VerticalPanel();
        for (Integer month : months)
            yearsColumn.add(createMonthPanel(year, month));
        return yearsColumn;
    }

    private IsWidget createMonthPanel(final int year, final int month) {
        HTML monthPanel = new HTML(translator.monthNumberToString(month));
        monthPanel.setStyleName(StyleName.OPTION);
        if (!dateUtils.monthInsideRange(new Date(year, month)))  monthPanel.addStyleName(StyleName.INVALID_OPTION);
        else {
            addHandler(month, monthPanel);
            if (date.getMonth() == month)
                monthPanel.addStyleName(StyleName.SELECTED);
        }
        return monthPanel;
    }

    private void addHandler(final int month, HTML monthPanel) {
        monthPanel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (handler == null) return;
                if (date.isSet(DateComponent.MONTH) && month == date.getMonth())
                    handler.onDateSelected(date);
                else
                    handler.onDateSelected(date.replaceMonth(month).removeComponentsFrom(DateComponent.DAY));
            }
        });
    }

    @Override
    public String getHeaderText() {
        return translator.translateDateWithPrecision(date, DatePrecision.YEAR);
    }

    @Override
    public void setOnDateSelected(DateSelectedHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean previousDatesAreAvailable() {
        return dateUtils.yearInsideRange(date.previousYear());
    }

    @Override
    public boolean nextDatesAreAvailable() {
        return dateUtils.yearInsideRange(date.nextYear());
    }

    public interface StyleName {
        String MONTHS_PANEL = "months-panel";
        String SELECTED = "selected";
        String OPTION = "option";
        String INVALID_OPTION = "invalid-option";
    }
}
