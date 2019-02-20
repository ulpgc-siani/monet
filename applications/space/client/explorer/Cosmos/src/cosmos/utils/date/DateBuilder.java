package cosmos.utils.date;

import cosmos.types.Date;

import java.util.List;

public interface DateBuilder {
    Date build(List<Integer> components);

    Date build(List<Integer> components, Date context);
}