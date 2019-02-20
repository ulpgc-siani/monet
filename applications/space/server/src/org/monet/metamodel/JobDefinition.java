package org.monet.metamodel;

import org.monet.metamodel.EditCheckStepProperty.ShowProperty;
import org.monet.metamodel.EditCheckStepProperty.ShowProperty.FlattenEnumeration;
import org.monet.metamodel.EditPictureStepProperty.SizeProperty;
import org.monet.metamodel.internal.Ref;
import org.monet.mobile.model.TaskDefinition.Step;
import org.monet.mobile.model.TaskDefinition.Step.Edit;
import org.monet.mobile.model.TaskDefinition.Step.Edit.PictureSize;
import org.monet.mobile.model.TaskDefinition.Step.Edit.UseGlossary;
import org.monet.mobile.model.TaskDefinition.Step.Edit.UseGlossary.Flatten;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.Language;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

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

	public org.monet.mobile.model.TaskDefinition toMobileDefinition() {
		org.monet.mobile.model.JobDefinition definition = new org.monet.mobile.model.JobDefinition();
		this.fillJobDefinition(definition);
		return definition;
	}

	protected void fillJobDefinition(org.monet.mobile.model.TaskDefinition definition) {
		definition.code = this.getCode();
		definition.label = this.getLabelString();
		definition.description = this.getDescriptionString();
		definition.haveToCheckPosition = this.getCheckPosition() != null;

		Language language = Language.getInstance();
		org.monet.space.kernel.model.Dictionary dictionary = org.monet.space.kernel.model.Dictionary.getInstance();

		for (StepProperty stepProperty : this.getStepList()) {
			Step step = new Step();
			step.label = language.getModelResource(stepProperty.getLabel());
			step.name = stepProperty.getName();
			step.isMultiple = stepProperty.isMultiple();
			step.textToShow = stepProperty.getShowText();
			step.captureDate = stepProperty.getCaptureDate() != null ? stepProperty.getCaptureDate().getName() : null;
			step.capturePosition = stepProperty.getCapturePosition() != null ? stepProperty.getCapturePosition().getName() : null;

			if (stepProperty.getView() != null) {
				for (Ref editRef : stepProperty.getView().getShow()) {
					StepEditProperty editProperty = stepProperty.getStepEdit(editRef.getValue());

					Step.Edit edit = new Step.Edit();
					edit.code = editProperty.getCode();
					edit.name = editProperty.getName();
					edit.label = language.getModelResource(editProperty.getLabel());
					edit.isMultiple = editProperty.isMultiple();
					edit.isRequired = editProperty.isRequired();
					edit.isReadOnly = editProperty.isReadOnly();
					edit.type = this.getEditType(editProperty);
					switch (edit.type) {
						case PICTURE:
							EditPictureStepProperty editPicture = (EditPictureStepProperty) editProperty;
							this.fillEditStep(language, dictionary, edit, editPicture);
							break;
						case SELECT:
							EditSelectStepProperty editSelect = (EditSelectStepProperty) editProperty;
							edit.isEmbedded = editSelect.isEmbedded();
							this.fillEditStep(language, dictionary, edit, editSelect);
							break;
						case CHECK:
							this.fillEditStep(language, dictionary, edit, (EditCheckStepProperty) editProperty);
							break;
						default:
							break;
					}
					step.edits.add(edit);
				}
			}
			definition.stepList.add(step);
		}
	}

	private void fillEditStep(Language language, Dictionary dictionary, Step.Edit edit, EditPictureStepProperty editPicture) {
		SizeProperty size = editPicture.getSize();
		if (size == null) return;
		edit.pictureSize = new PictureSize();
		edit.pictureSize.height = size.getHeight();
		edit.pictureSize.width = size.getWidth();
	}

	private void fillEditStep(Language language, org.monet.space.kernel.model.Dictionary dictionary, Step.Edit edit, EditCheckStepProperty editCheck) {
		if (editCheck.getTerms() != null) {
			ArrayList<TermProperty> terms = editCheck.getTerms().getTermPropertyList();
			this.fillTerms(terms, edit.terms, language);
		} else if (editCheck.getSource() != null) {
			ShowProperty showDefinition = editCheck.getShow();

			edit.useGlossary = new UseGlossary();
			edit.useGlossary.source = dictionary.getDefinitionCode(editCheck.getSource().getValue());
			edit.useGlossary.depth = (showDefinition != null && showDefinition.getDepth() != null) ? showDefinition.getDepth().intValue() : -1;

			if (showDefinition != null && showDefinition.getFilter() != null && showDefinition.getFilter().getTag() != null) {
				for (Object tagRef : showDefinition.getFilter().getTag()) {
					if (tagRef instanceof Ref) {
						Ref ref = (Ref) tagRef;

						String tagFileterDefinitionName = this.getName();
						while (!tagFileterDefinitionName.equals(ref.getDefinition())) {
							tagFileterDefinitionName = dictionary.getDefinition(tagFileterDefinitionName).getParent();
							if (tagFileterDefinitionName == null)
								break;
						}

						if (tagFileterDefinitionName != null) {
							String valuePath = ref.getFullQualifiedName().substring(tagFileterDefinitionName.length() + 1);
							edit.useGlossary.filters.add("$L$" + valuePath);
						}
					} else {
						edit.useGlossary.filters.add("$V$" + (String) tagRef);
					}
				}
			}
			edit.useGlossary.flatten = getFlattenType(showDefinition != null ? showDefinition.getFlatten() : null);
			edit.useGlossary.from = showDefinition != null ? language.getModelResource(showDefinition.getRoot()) : null;
		}
	}

	private void fillTerms(List<TermProperty> in, List<org.monet.mobile.model.TaskDefinition.Step.Edit.Term> out, Language language) {
		for (TermProperty term : in) {
			org.monet.mobile.model.TaskDefinition.Step.Edit.Term t = new org.monet.mobile.model.TaskDefinition.Step.Edit.Term();
			t.key = term.getKey();
			t.label = language.getModelResource(term.getLabel());
			out.add(t);
			if (term.getTermPropertyList().size() > 0) {
				t.terms = new ArrayList<org.monet.mobile.model.TaskDefinition.Step.Edit.Term>();
				fillTerms(term.getTermPropertyList(), t.terms, language);
			}
		}
	}

	private org.monet.mobile.model.JobDefinition.Step.Edit.Type getEditType(StepEditProperty editProperty) {
		if (editProperty instanceof EditSelectStepProperty) {
			return Edit.Type.SELECT;
		} else if (editProperty instanceof EditCheckStepProperty) {
			return Edit.Type.CHECK;
		} else if (editProperty instanceof EditBooleanStepProperty) {
			return Edit.Type.BOOLEAN;
		} else if (editProperty instanceof EditDateStepProperty) {
			return Edit.Type.DATE;
		} else if (editProperty instanceof EditMemoStepProperty) {
			return Edit.Type.MEMO;
		} else if (editProperty instanceof EditNumberStepProperty) {
			return Edit.Type.NUMBER;
		} else if (editProperty instanceof EditPictureStepProperty) {
			if (((EditPictureStepProperty) editProperty).isHandWritten())
				return Edit.Type.PICTURE_HAND;
			else
				return Edit.Type.PICTURE;
		} else if (editProperty instanceof EditPositionStepProperty) {
			return Edit.Type.POSITION;
		} else if (editProperty instanceof EditTextStepProperty) {
			return Edit.Type.TEXT;
		} else if (editProperty instanceof EditVideoStepProperty) {
			return Edit.Type.VIDEO;
		}

		return null;
	}

	private Flatten getFlattenType(FlattenEnumeration flatten) {
		if (flatten == null)
			return Flatten.ALL;
		switch (flatten) {
			case ALL:
				return Flatten.ALL;
			case INTERNAL:
				return Flatten.INTERNAL;
			case LEAF:
				return Flatten.LEAF;
			case LEVEL:
				return Flatten.LEVEL;
			case NONE:
				return Flatten.NONE;
			default:
				break;
		}
		return Flatten.ALL;
	}

}
