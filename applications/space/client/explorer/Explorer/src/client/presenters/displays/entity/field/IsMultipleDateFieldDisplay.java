package client.presenters.displays.entity.field;

import client.presenters.displays.IsMultipleDisplay;
import cosmos.types.Date;

public interface IsMultipleDateFieldDisplay extends IsDateFieldDisplay, IsMultipleDisplay<Date> {
    String formatDate(Date date);

    boolean allowLessPrecision();
}
