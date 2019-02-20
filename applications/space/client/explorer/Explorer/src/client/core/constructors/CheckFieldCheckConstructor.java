package client.core.constructors;

import client.core.model.List;
import client.core.model.definition.entity.field.CheckFieldDefinition;
import client.core.model.types.CompositeCheck;
import client.core.system.types.Check;
import client.core.system.types.CheckCategory;
import client.core.system.types.CheckList;
import client.core.system.types.SuperCheck;

import static client.core.model.definition.entity.field.CheckFieldDefinition.TermDefinition;

public class CheckFieldCheckConstructor {

	public static client.core.model.types.Check construct(TermDefinition termDefinition) {
		if (termDefinition.isCategory())
			return constructCategory(termDefinition);
		if (!termDefinition.getTerms().isEmpty())
			return constructSuperCheck(termDefinition);
		return constructCheck(termDefinition);
	}

	public static CheckList constructList(List<CheckFieldDefinition.TermDefinition> termDefinitions) {
		CheckList checkList = new CheckList();

		for (TermDefinition termDefinition : termDefinitions)
			checkList.add(construct(termDefinition));

		return checkList;
	}

	private static client.core.model.types.Check constructCategory(TermDefinition termDefinition) {
		final CompositeCheck category = new CheckCategory();
		initializeCompositeCheck(termDefinition, category);
		return category;
	}

	private static client.core.model.types.Check constructSuperCheck(TermDefinition termDefinition) {
		final CompositeCheck superCheck = new SuperCheck();
		initializeCompositeCheck(termDefinition, superCheck);
		return superCheck;
	}

	private static client.core.model.types.Check constructCheck(TermDefinition termDefinition) {
		Check check = new Check();
		initializeCheck(termDefinition, check);
		return check;
	}

	private static void initializeCheck(TermDefinition termDefinition, client.core.model.types.Check term) {
		term.setValue(termDefinition.getKey());
		term.setLabel(termDefinition.getLabel());
	}

	private static void initializeCompositeCheck(TermDefinition termDefinition, CompositeCheck category) {
		initializeCheck(termDefinition, category);
		category.setChecks(constructList(termDefinition.getTerms()));
	}
}
