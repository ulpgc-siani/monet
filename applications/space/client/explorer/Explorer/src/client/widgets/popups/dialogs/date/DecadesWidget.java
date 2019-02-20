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
import cosmos.types.DatePrecision;
import cosmos.types.Decade;

import java.util.List;

public class DecadesWidget extends HorizontalPanel implements DateComponentSelectorWidget {

    private final TranslatorService translator;
    private final DateUtils dateUtils;
    private Date date;
    private DateSelectedHandler handler;

    public DecadesWidget(TranslatorService translator, DateUtils dateUtils) {
        this.translator = translator;
        this.dateUtils = dateUtils;
        addStyleName(StyleName.DECADES_PANEL);
    }

    @Override
    public Date nextDate() {
        return date.nextCentury();
    }

    @Override
    public Date previousDate() {
        return date.previousCentury();
    }

    @Override
    public void setDate(Date date) {
        this.date = date;
        refreshView();
    }

    @Override
    public String getHeaderText() {
        return translator.translateDateWithPrecision(date, DatePrecision.CENTURY);
    }

    @Override
    public void setOnDateSelected(DateSelectedHandler handler) {
        this.handler = handler;
    }

    @Override
    public boolean previousDatesAreAvailable() {
        return dateUtils.centuryInsideRange(date.previousCentury().getCentury());
    }

    @Override
    public boolean nextDatesAreAvailable() {
        return dateUtils.centuryInsideRange(date.nextCentury().getCentury());
    }

    private void refreshView() {
        clear();
        final List<Decade> decades = date.getDecadesInCentury();
        add(createDecadesColumn(decades.subList(0, (decades.size() + 1) / 2)));
        add(createDecadesColumn(decades.subList((decades.size() + 1) / 2, decades.size())));
    }

    private VerticalPanel createDecadesColumn(List<Decade> decades) {
        final VerticalPanel decadesColumn = new VerticalPanel();
        for (Decade decade : decades)
            decadesColumn.add(createDecadePanel(decade));
        return decadesColumn;
    }

    private IsWidget createDecadePanel(final Decade decade) {
        HTML yearPanel = new HTML(decade.toString());
        yearPanel.setStyleName(StyleName.OPTION);
        if (!dateUtils.decadeInsideRange(decade)) {
            yearPanel.addStyleName(StyleName.INVALID_OPTION);
        } else {
            addHandler(decade, yearPanel);
            if (date.getDecade().equals(decade))
                yearPanel.addStyleName(StyleName.SELECTED);
        }
        return yearPanel;
    }

    private void addHandler(final Decade decade, HTML yearPanel) {
        yearPanel.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (handler == null) return;
                if (date.getDecade().equals(decade))
                    handler.onDateSelected(date);
                else
                    handler.onDateSelected(date.replaceYear((decade.getYears().get(0)))
                            .removeMonth()
                            .removeDay());
            }
        });
    }

    public interface StyleName {
        String DECADES_PANEL = "decades-panel";
        String OPTION = "option";
        String SELECTED = "selected";
        String INVALID_OPTION = "invalid-option";
    }
}
