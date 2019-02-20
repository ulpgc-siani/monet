package org.monet.metamodel;

import org.monet.space.kernel.model.DefinitionType;

import java.util.LinkedHashSet;

public abstract class SourceDefinition extends SourceDefinitionBase {

	public static LinkedHashSet<String> getTags(TermProperty termDefinition) {
		LinkedHashSet<String> result = new LinkedHashSet<String>();

		for (String tag : termDefinition.getTag())
			result.add(tag);

		return result;
	}

	@Override
	public DefinitionType getType() {
		if (this.isThesaurus())
			return DefinitionType.thesaurus;
		else if (this.isGlossary())
			return DefinitionType.glossary;
		else if (this.isFeeder())
			return DefinitionType.feeder;
		return null;
	}

	public boolean isThesaurus() {
		return this instanceof ThesaurusDefinition;
	}

	public boolean isGlossary() {
		return this instanceof GlossaryDefinition;
	}

	public boolean isFeeder() {
		return this instanceof FeederDefinition;
	}

}
