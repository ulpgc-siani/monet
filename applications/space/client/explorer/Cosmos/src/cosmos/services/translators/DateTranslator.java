package cosmos.services.translators;

import cosmos.types.Date;
import cosmos.types.DatePrecision;
import cosmos.types.DayOfWeek;

public interface DateTranslator {

    int dayOfWeekToNumber(DayOfWeek dayOfWeek);
    int monthToNumber(String month);
    String monthNumberToString(Integer month);
    String[] getDateSeparators();
    String translateDateWithPrecision(Date date, DatePrecision precision);
    String translateFullDate(Date date);
    String translateFullDateByUser(Date date, String user);
}
