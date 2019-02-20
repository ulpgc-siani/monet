package client.widgets.popups.dialogs.date;

import client.services.TranslatorService;
import client.utils.DateUtils;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.*;
import cosmos.types.Date;
import cosmos.types.DateComponent;

import java.util.ArrayList;
import java.util.List;

import static client.widgets.popups.DatePopupWidget.DateSelectedHandler;

public class DateSelectorWidget extends HorizontalPanel {

    private final List<DateComponentSelectorWidget> panels;
    private final DateUtils dateUtils;
    private Label headerTitle;
    private VerticalPanel content;
    private int selectedPanel;
    private Date date;
    private DateSelectedHandler dateSelectedHandler;
    private HTML arrowUp;
    private HTML previous;
    private HTML next;

    public DateSelectorWidget(TranslatorService translator, DateUtils dateUtils) {
        super();
        this.panels = new ArrayList<>();
        this.dateUtils = dateUtils;
        createPanels(translator, dateUtils);
        addPanelsHandler();
        selectedPanel = dateUtils.isNearDate() ? panels.size() - 1 : 1;
        create(translator);
    }

    private void create(TranslatorService translator) {
        setVerticalAlignment(ALIGN_MIDDLE);
        add(createPreviousButton(translator.translate(TranslatorService.Label.PREVIOUS)));

        setVerticalAlignment(ALIGN_TOP);
        add(createContent(translator));

        setVerticalAlignment(ALIGN_MIDDLE);
        add(createNextButton(translator.translate(TranslatorService.Label.NEXT)));
    }

    private VerticalPanel createContent(TranslatorService translator) {
        content = new VerticalPanel();
        addContentHeader(translator);
        addContentBody();
        return content;
    }

    private void addContentHeader(TranslatorService translator) {
        HorizontalPanel header = new HorizontalPanel();
        header.addStyleName(StyleName.DATE_HEADER);

        headerTitle = new Label();
        headerTitle.addStyleName(StyleName.DATE_HEADER_TITLE);

        header.add(createArrowUp(translator.translate(TranslatorService.Label.UP)));
        header.add(headerTitle);

        content.add(header);
    }

    private void addContentBody() {
        content.add(panels.get(selectedPanel));
    }

    private void addPanelsHandler() {
        for (DateComponentSelectorWidget panel : panels) {
            panel.setOnDateSelected(new DateComponentSelectorWidget.DateSelectedHandler() {
                @Override
                public void onDateSelected(Date date) {
                    DateSelectorWidget.this.date = date;
                    int previousPanel = selectedPanel;
                    if (dateSelectedHandler != null) dateSelectedHandler.dateSelected(date);
                    if (previousPanel == panels.size() - 1 && dateUtils.isValidDate(date)) return;
                    updateDatePanel(previousPanel + 1);
                }
            });
        }
    }

    private HTML createArrowUp(final String up) {
        arrowUp = new HTML();
        arrowUp.setTitle(up);
        arrowUp.addStyleName(StyleName.UP_COMPONENT);
        arrowUp.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                content.remove(panels.get(selectedPanel));
                selectedPanel--;
                arrowUp.setVisible(selectedPanel != 0);
                content.add(panels.get(selectedPanel));
                if (selectedPanel == 2)
                    date = date.removeDay();
                else if (selectedPanel <= 1)
                    date = date.removeMonth().removeDay();
                updatePanel();
            }
        });
        return arrowUp;
    }

    private HTML createPreviousButton(final String previousTitle) {
        previous = new HTML("<");
        previous.setTitle(previousTitle);
        previous.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!panels.get(selectedPanel).previousDatesAreAvailable()) return;
                date = panels.get(selectedPanel).previousDate();
                updatePanel();
            }
        });
        previous.addStyleName(StyleName.PREVIOUS_COMPONENT);
        setCellVerticalAlignment(previous, HasVerticalAlignment.ALIGN_MIDDLE);
        return previous;
    }

    private void updateHorizontalArrows() {
        if (!panels.get(selectedPanel).previousDatesAreAvailable())
            previous.addStyleName(StyleName.DISABLE);
        else
            previous.removeStyleName(StyleName.DISABLE);
        if (!panels.get(selectedPanel).nextDatesAreAvailable())
            next.addStyleName(StyleName.DISABLE);
        else
            next.removeStyleName(StyleName.DISABLE);
    }

    private HTML createNextButton(final String nextTitle) {
        next = new HTML(">");
        next.setTitle(nextTitle);
        next.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (!panels.get(selectedPanel).nextDatesAreAvailable()) return;
                date = panels.get(selectedPanel).nextDate();
                updatePanel();
            }
        });
        next.addStyleName(StyleName.NEXT_COMPONENT);
        add(next);
        setCellVerticalAlignment(next, HasVerticalAlignment.ALIGN_MIDDLE);
        return next;
    }

    private void updatePanel() {
        arrowUp.setVisible(selectedPanel != 0);
        panels.get(selectedPanel).setDate(date);
        updateHeader();
        updateHorizontalArrows();
    }

    private void createPanels(TranslatorService translator, DateUtils dateUtils) {
        panels.add(new DecadesWidget(translator, dateUtils));
        panels.add(new YearsWidget(translator, dateUtils));
        if (dateUtils.hasMonthPrecision())
            panels.add(new MonthsWidget(translator, dateUtils));
        if (dateUtils.hasDayPrecision())
            panels.add(new DaysWidget(translator, dateUtils));
    }

    private void updateHeader() {
        headerTitle.setText(panels.get(selectedPanel).getHeaderText());
    }

    public void setDateSelectedHandler(DateSelectedHandler dateSelectedHandler) {
        this.dateSelectedHandler = dateSelectedHandler;
    }

    public void setDate(Date date) {
        this.date = date;
        if (panels.size() > 3 && date.isSet(DateComponent.DAY))
            updateDatePanel(3);
        else if (panels.size() > 2 && date.isSet(DateComponent.MONTH))
            updateDatePanel(2);
        else
            updateDatePanel(1);
    }

    private void updateDatePanel(final int nextPanelToSelect) {
        Scheduler.get().scheduleDeferred(new Scheduler.ScheduledCommand() {
            @Override
            public void execute() {
                content.remove(panels.get(selectedPanel));
                selectedPanel = nextPanelToSelect;
                content.add(panels.get(selectedPanel));
                updatePanel();
            }
        });
    }

    public interface StyleName {
        String DATE_HEADER = "date-header";
        String DATE_HEADER_TITLE = "title";
        String UP_COMPONENT = "up-component";
        String PREVIOUS_COMPONENT = "previous-component";
        String NEXT_COMPONENT = "next-component";
        String DISABLE = "disable";
    }
}
