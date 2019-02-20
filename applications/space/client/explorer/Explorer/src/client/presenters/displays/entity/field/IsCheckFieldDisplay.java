package client.presenters.displays.entity.field;

import client.core.model.types.Check;
import client.core.model.types.CheckList;
import client.core.model.types.CompositeCheck;

public interface IsCheckFieldDisplay extends IsFieldDisplay<CheckList> {

    void loadOptions();
    void selectAll(CompositeCheck check);
    void selectNone(CompositeCheck check);
    void toggle(Check value);

	void addHook(CheckFieldDisplay.Hook hook);

}
