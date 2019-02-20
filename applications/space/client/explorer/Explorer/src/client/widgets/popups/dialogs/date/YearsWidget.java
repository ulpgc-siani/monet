package client.widgets.popups.dialogs.date;

import client.services.TranslatorService;
import client.utils.DateUtils;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.VerticalPanel;
import cosmos.types.Date;
import cosmos.types.DateComponent;
import cosmos.types.DatePrecision;

import java.util.List;

public class YearsWidget extends HorizontalPanel implements DateComponentSelectorWidget {

    private final DateUtils dateUtils;
    private final TranslatorService translator;
    private Date date;
    private DateSelectedHandler handler;

    private static final int COLUMNS_COUNT = 2;

    public YearsWidget(TranslatorService translator, DateUtils dateUtils) {
        this.translator = translator;
        this.dateUtils = dateUtils;
        addStyleName(StyleName.YEARS_PANEL);
    }

    @Override
    public Date nextDate() {
        return date.nextDecade();
    }

    @Override
    public Date previousDate() {
        return date.previousDecade();
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
        refreshView();
    }

    @Override
    public String getHeaderText() {
        return translator.translateDateWithPrecision(date, DatePrecision.DECADE);
    }

    @Override
    public void setOnDateSelected(DateSelectedHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean previousDatesAreAvailable() {
        return dateUtils.decadeInsideRange(date.previousDecade().getDecade());
    }

    @Override
    public boolean nextDatesAreAvailable() {
        return dateUtils.decadeInsideRange(date.nextDecade().getDecade());
    }

    private void refreshView() {
        clear();
        final List<Integer> decade = date.getYearsInDecade();
        int decadeSize = decade.size();
        int columnSize = decadeSize / COLUMNS_COUNT;

        add(createYearsColumn(decade.subList(0, columnSize)));
        add(createYearsColumn(decade.subList(columnSize, decadeSize)));
    }

    private VerticalPanel createYearsColumn(List<Integer> years) {
        final VerticalPanel yearsColumn = new VerticalPanel();
        for (Integer year : years)
            yearsColumn.add(createYearPanel(year));
        return yearsColumn;
    }

    private IsWidget createYearPanel(final int year) {
        HTML yearPanel = new HTML(String.valueOf(year));
        yearPanel.setStyleName(StyleName.OPTION);
        if (!dateUtils.yearInsideRange(new Date(year))) yearPanel.addStyleName(StyleName.INVALID_OPTION);
        else {
            addHandler(year, yearPanel);
            if (date.getYear() == year)
                yearPanel.addStyleName(StyleName.SELECTED);
        }
        return yearPanel;
    }

    private void addHandler(final int year, HTML yearPanel) {
        yearPanel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (handler == null) return;
                if (year == date.getYear())
                    handler.onDateSelected(date);
                else
                    handler.onDateSelected(date.replaceYear(year).removeComponentsFrom(DateComponent.MONTH));
            }
        });
    }

    public interface StyleName {
        String YEARS_PANEL = "years-panel";
        String OPTION = "option";
        String SELECTED = "selected";
        String INVALID_OPTION = "invalid-option";
    }
}
