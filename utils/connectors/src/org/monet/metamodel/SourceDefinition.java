package org.monet.metamodel;

import java.util.LinkedHashSet;

public abstract class SourceDefinition extends SourceDefinitionBase {

	public static LinkedHashSet<String> getTags(TermProperty termDefinition) {
		LinkedHashSet<String> result = new LinkedHashSet<String>();

		for (String tag : termDefinition.getTag())
			result.add(tag);

		return result;
	}

}
