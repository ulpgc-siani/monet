package client.core.constructors;

import client.core.model.List;
import client.core.model.definition.entity.field.SelectFieldDefinition;
import client.core.model.types.CompositeTerm;
import client.core.system.types.TermCategory;
import client.core.system.types.SuperTerm;
import client.core.system.types.Term;
import client.core.system.types.TermList;

public class SelectFieldTermConstructor {

	public static client.core.model.types.Term construct(SelectFieldDefinition.TermDefinition termDefinition) {
		if (termDefinition.isCategory())
			return constructCategory(termDefinition);
		if (!termDefinition.getTerms().isEmpty())
			return constructSuperTerm(termDefinition);
		return constructTerm(termDefinition);
	}

	public static TermList constructList(List<SelectFieldDefinition.TermDefinition> termDefinitions) {
		TermList termList = new TermList();
		for (SelectFieldDefinition.TermDefinition termDefinition : termDefinitions)
			termList.add(construct(termDefinition));
		return termList;
	}

	private static client.core.model.types.Term constructCategory(SelectFieldDefinition.TermDefinition termDefinition) {
		final CompositeTerm category = new TermCategory();
		initializeCompositeTerm(termDefinition, category);
		return category;
	}

	private static client.core.model.types.Term constructSuperTerm(SelectFieldDefinition.TermDefinition termDefinition) {
		final CompositeTerm superTerm = new SuperTerm();
		initializeCompositeTerm(termDefinition, superTerm);
		return superTerm;
	}

	private static client.core.model.types.Term constructTerm(SelectFieldDefinition.TermDefinition termDefinition) {
		Term term = new Term();
		initializeTerm(termDefinition, term);
		return term;
	}

	private static void initializeTerm(SelectFieldDefinition.TermDefinition termDefinition, client.core.model.types.Term term) {
		term.setValue(termDefinition.getKey());
		term.setLabel(termDefinition.getLabel());
	}

	private static void initializeCompositeTerm(SelectFieldDefinition.TermDefinition termDefinition, CompositeTerm category) {
		initializeTerm(termDefinition, category);
		category.setTerms(constructList(termDefinition.getTerms()));
	}
}
