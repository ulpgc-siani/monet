package org.monet.bpi.java;

import org.monet.bpi.BehaviorSource;
import org.monet.bpi.Source;
import org.monet.bpi.types.Term;
import org.monet.bpi.types.TermList;
import org.monet.metamodel.SourceDefinition;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.model.DataRequest;

public class SourceImpl implements Source, BehaviorSource {

	org.monet.space.kernel.model.Source<SourceDefinition> source;
	SourceLayer sourceLayer = ComponentPersistence.getInstance().getSourceLayer();

	void injectSource(org.monet.space.kernel.model.Source<SourceDefinition> source) {
		this.source = source;
	}

	private void injectTermList(TermList targetTermList, org.monet.space.kernel.model.TermList sourceTermList) {
		targetTermList.clear();
		for (org.monet.space.kernel.model.Term term : sourceTermList.get().values()) {
			Term targetTerm = new Term();
			targetTerm.setKey(term.getCode());
			targetTerm.setLabel(term.getLabel());
			targetTermList.add(targetTerm);
		}
	}

	@Override
	public String getPartnerName() {
		return this.source.getPartnerName();
	}

	@Override
	public String getPartnerLabel() {
		return this.source.getPartnerLabel();
	}

	@Override
	public TermList getTermList() {
		return this.getTermList("-1");
	}

	@Override
	public TermList getTermList(String parent) {
		TermList bpiTermList = new TermList();
		DataRequest dataRequest = new DataRequest();
		dataRequest.setLimit(-1);
		dataRequest.addParameter(DataRequest.MODE, DataRequest.Mode.ALL);
		org.monet.space.kernel.model.TermList termList;

		dataRequest.addParameter(DataRequest.FROM, parent);
		termList = this.sourceLayer.loadSourceTerms(this.source, dataRequest, true);
		this.injectTermList(bpiTermList, termList);

		return bpiTermList;
	}

	public Term getParentTerm(String code) {

		if (!this.sourceLayer.existsSourceTerm(this.source, code))
			return null;

		org.monet.space.kernel.model.Term term = this.sourceLayer.loadSourceTerm(this.source, code);

		if (term.getParent() == null)
			return null;

		org.monet.space.kernel.model.Term parentTerm = this.sourceLayer.loadSourceTerm(source, term.getParent());
		Term bpiTerm = new Term();
		bpiTerm.setKey(parentTerm.getCode());
		bpiTerm.setLabel(parentTerm.getLabel());

		return bpiTerm;
	}

	public boolean existsTerm(String key) {
		String code = this.sourceLayer.locateSourceTermCode(source, key);

		if (code == null || code.isEmpty())
			code = key;

		return this.sourceLayer.existsSourceTerm(this.source, code);
	}

	public Term getTerm(String key) {
		String code = this.sourceLayer.locateSourceTermCode(source, key);

		if (code == null || code.isEmpty())
			code = key;

		if (!this.sourceLayer.existsSourceTerm(this.source, code))
			return null;

		org.monet.space.kernel.model.Term term = this.sourceLayer.loadSourceTerm(this.source, code);

		Term bpiTerm = new Term();
		bpiTerm.setKey(term.getCode());
		bpiTerm.setLabel(term.getLabel());

		return bpiTerm;
	}

	public boolean isTerm(String code) {
		if (!this.sourceLayer.existsSourceTerm(this.source, code)) return false;
		org.monet.space.kernel.model.Term term = this.sourceLayer.loadSourceTerm(this.source, code);
		return term.isTerm();
	}

	public boolean isTermSuperTerm(String code) {
		if (!this.sourceLayer.existsSourceTerm(this.source, code)) return false;
		org.monet.space.kernel.model.Term term = this.sourceLayer.loadSourceTerm(this.source, code);
		return term.isSuperTerm();
	}

	public void setIsTermSuperTerm(String code, boolean value) {
		if (!this.sourceLayer.existsSourceTerm(this.source, code)) return;
		org.monet.space.kernel.model.Term term = this.sourceLayer.loadSourceTerm(this.source, code);

		term.setType(value ? Term.SUPER_TERM : Term.TERM);
		this.sourceLayer.updateSourceTerm(source, term);
	}

	public boolean isTermCategory(String code) {
		if (!this.sourceLayer.existsSourceTerm(this.source, code)) return false;
		org.monet.space.kernel.model.Term term = this.sourceLayer.loadSourceTerm(this.source, code);
		return term.isCategory();
	}

	public void setIsTermCategory(String code, boolean value) {
		if (!this.sourceLayer.existsSourceTerm(this.source, code)) return;
		org.monet.space.kernel.model.Term term = this.sourceLayer.loadSourceTerm(this.source, code);

		term.setType(value ? Term.CATEGORY : Term.TERM);
		this.sourceLayer.updateSourceTerm(source, term);
	}

	public boolean isTermEnabled(String code) {
		if (!this.sourceLayer.existsSourceTerm(this.source, code)) return false;
		org.monet.space.kernel.model.Term term = this.sourceLayer.loadSourceTerm(this.source, code);
		return term.isEnabled();
	}

	public boolean isTermDisabled(String code) {
		if (!this.sourceLayer.existsSourceTerm(this.source, code)) return false;
		org.monet.space.kernel.model.Term term = this.sourceLayer.loadSourceTerm(this.source, code);
		return !term.isEnabled();
	}

	public Term addTerm(Term term) {
		this.sourceLayer.addSourceTerm(this.source, toMonetTerm(term), null);
		term.setSource(this);
		return term;
	}

	public Term addTerm(Term term, String parentCode) {
		this.sourceLayer.addSourceTerm(this.source, toMonetTerm(term), parentCode);
		term.setSource(this);
		return term;
	}

	public Term addTerm(Term term, Term parent) {
		this.sourceLayer.addSourceTerm(this.source, toMonetTerm(term), parent.getKey());
		term.setSource(this);
		return term;
	}

	public static org.monet.space.kernel.model.Term toMonetTerm(Term term) {
		org.monet.space.kernel.model.Term monetTerm = new org.monet.space.kernel.model.Term();
		monetTerm.setCode(term.getKey());
		monetTerm.setLabel(term.getLabel());
		monetTerm.setTags(term.getTags());
		monetTerm.setEnabled(true);
		monetTerm.setType(term.getType());
		return monetTerm;
	}

	@Override
	public void onPopulated() {
	}

	@Override
	public void onSynchronized() {
	}

	@Override
	public void onTermAdded(Term term) {
	}

	@Override
	public void onTermModified(Term term) {
	}

}