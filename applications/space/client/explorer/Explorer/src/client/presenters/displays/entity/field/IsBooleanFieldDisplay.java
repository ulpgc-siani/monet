package client.presenters.displays.entity.field;

import client.core.model.factory.EntityFactory;
import client.core.model.types.Term;
import client.core.model.types.TermList;

public interface IsBooleanFieldDisplay extends IsFieldDisplay<Boolean> {

	EntityFactory getEntityFactory();
	boolean isCheckEdition();

    TermList createBooleanTermList();

    Term createTrueTerm();
    Term createFalseTerm();
    Term booleanToTerm(boolean value);
    boolean termToBoolean(Term term);
    void addHook(BooleanFieldDisplay.Hook hook);
}
