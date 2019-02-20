package client.widgets.popups.dialogs.date;

import com.google.gwt.user.client.ui.IsWidget;
import cosmos.types.Date;

public interface DateComponentSelectorWidget extends IsWidget {

    Date previousDate();
    Date nextDate();

    void setDate(Date date);

    String getHeaderText();

    boolean previousDatesAreAvailable();
    boolean nextDatesAreAvailable();

    void setOnDateSelected(DateSelectedHandler handler);

    interface DateSelectedHandler {
        void onDateSelected(Date date);
    }
}
