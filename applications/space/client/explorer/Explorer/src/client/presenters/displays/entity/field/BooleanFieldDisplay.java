package client.presenters.displays.entity.field;

import client.core.model.Node;
import client.core.model.definition.entity.field.BooleanFieldDefinition;
import client.core.model.definition.entity.field.BooleanFieldDefinition.Edition;
import client.core.model.fields.BooleanField;
import client.core.model.types.Term;
import client.core.model.types.TermList;
import client.presenters.displays.entity.FieldDisplay;
import client.services.TranslatorService;

import static client.services.TranslatorService.Label;

public class BooleanFieldDisplay extends FieldDisplay<BooleanField, BooleanFieldDefinition,Boolean> implements IsBooleanFieldDisplay {

	public static final Type TYPE = new Type("BooleanFieldDisplay", FieldDisplay.TYPE);

	public BooleanFieldDisplay(Node node, BooleanField field) {
		super(node, field);
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public String getValueAsString() {
		return services.getTranslatorService().translate(getValue() ? Label.YES : Label.NO);
	}

	@Override
	public boolean isCheckEdition() {
		return getDefinition().getEdition() == Edition.CHECK;
	}

	@Override
	protected Boolean format(Boolean value) {
		return value;
	}

    @Override
    public TermList createBooleanTermList() {
        final TermList options = getEntityFactory().createTermList();
        options.add(createTrueTerm());
        options.add(createFalseTerm());
        return options;
    }

    @Override
    public Term createTrueTerm() {
        return getEntityFactory().createTerm(Label.YES.toString(), services.getTranslatorService().translate(Label.YES));
    }

    @Override
    public Term createFalseTerm() {
        return getEntityFactory().createTerm(Label.NO.toString(), services.getTranslatorService().translate(Label.NO));
    }

    @Override
    public Term booleanToTerm(boolean value) {
        return value ? createTrueTerm() : createFalseTerm();
    }

    @Override
    public boolean termToBoolean(Term term) {
        return term.getValue().equals(TranslatorService.Label.YES.toString());
    }

    @Override
	public void addHook(Hook hook) {
		super.addHook(hook);
	}

	public interface Hook extends FieldDisplay.Hook {
		void value();
	}
}
