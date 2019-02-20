package org.monet.metamodel;

import java.util.HashSet;

/**
 * JobDefinition Un trabajo o job es una pequeña tarea o actividad que se
 * realiza en un entorno de mobilidad
 */

public class JobDefinition extends JobDefinitionBase {

	public HashSet<String> getUsedThesaurus() {
		HashSet<String> sources = new HashSet<String>();

		for (StepProperty stepProperty : this.getStepList()) {
			for (StepEditProperty edit : stepProperty.getAllStepEditPropertyList()) {
				if (edit instanceof EditSelectStepProperty) {
					EditSelectStepProperty editSelect = (EditSelectStepProperty) edit;
					if (editSelect.getSource() != null)
						sources.add(editSelect.getSource().getValue());
				} else if (edit instanceof EditCheckStepProperty) {
					EditCheckStepProperty editCheck = (EditCheckStepProperty) edit;
					if (editCheck.getSource() != null)
						sources.add(editCheck.getSource().getValue());
				}
			}
		}

		return sources;
	}

}
