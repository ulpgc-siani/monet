package org.monet.metamodel;

import org.monet.space.kernel.model.Language;
import org.monet.space.kernel.model.Term;
import org.monet.space.kernel.model.TermList;

import java.util.List;
import java.util.Set;

public class ThesaurusDefinition extends ThesaurusDefinitionBase {

	public TermList getTermList() {
		TermsProperty termIndexDeclaration = this.getTerms();
		TermList termList = new TermList();
		if (termIndexDeclaration != null) {
			Language language = Language.getInstance();
			for (TermProperty termDefinition : termIndexDeclaration.getTermPropertyList()) {
				Term term = this.getTerm(termDefinition, language, null);
				termList.add(term);
			}
		}

		return termList;
	}

	private Term getTerm(TermProperty termDefinition, Language language, TermProperty parentTermDefinition) {
		Term term = new Term();
        Set<Term> termList = term.getTermList();
		List<TermProperty> termDefinitionList = termDefinition.getTermPropertyList();

        term.setCode(termDefinition.getKey());
		term.setLabel(language.getModelResource(termDefinition.getLabel()));
		term.setTags(ThesaurusDefinition.getTags(termDefinition));
		term.setType(getTermType(termDefinition));
		term.setLeaf(termDefinitionList.size() == 0);
		term.setTags(SourceDefinition.getTags(termDefinition));
		term.setParent((parentTermDefinition != null) ? parentTermDefinition.getKey() : null);

		for (TermProperty childTermDefinition : termDefinitionList)
            termList.add(getTerm(childTermDefinition, language, termDefinition));

		return term;
	}

    private int getTermType(TermProperty termDefinition) {
        if (termDefinition.isCategory())
            return Term.CATEGORY;
        if (termDefinition.getTermPropertyList().size() > 0)
            return Term.SUPER_TERM;
        return Term.TERM;
    }

}