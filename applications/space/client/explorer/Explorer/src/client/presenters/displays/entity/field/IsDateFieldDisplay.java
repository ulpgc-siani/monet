package client.presenters.displays.entity.field;

import cosmos.types.Date;
import cosmos.types.DatePrecision;
import cosmos.utils.date.DateBuilder;

import static client.core.model.definition.entity.field.DateFieldDefinition.RangeDefinition;

public interface IsDateFieldDisplay extends IsFieldDisplay<Date>{

    Date createDate();
    boolean dateIsValid(Date date);
    boolean allowLessPrecision();
    boolean hasTime();

    boolean rangeWrong();
    RangeDefinition getRange();

    String getValueAsString();

    DateBuilder getDateBuilder();

    DatePrecision getPrecision();
    boolean isNearDate();

}
