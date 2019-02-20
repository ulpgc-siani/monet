package client.presenters.displays.entity.field;

import client.core.model.types.Term;
import client.presenters.displays.IsMultipleDisplay;

public interface IsMultipleSelectFieldDisplay extends IsSelectFieldDisplay, IsMultipleDisplay<Term> {
	void toggle(Term term);
	void addHook(MultipleSelectFieldDisplay.Hook hook);
}
