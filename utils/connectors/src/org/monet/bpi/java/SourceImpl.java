package org.monet.bpi.java;

import org.apache.commons.lang.NotImplementedException;
import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.BehaviorSource;
import org.monet.bpi.Source;
import org.monet.bpi.types.Term;
import org.monet.bpi.types.TermList;
import org.monet.metamodel.Dictionary;
import org.monet.v3.BPIClassLocator;

public class SourceImpl implements Source, BehaviorSource {
	org.monet.api.space.backservice.impl.model.Source source;

	BPIClassLocator bpiClassLocator;
	BackserviceApi api;
	Dictionary dictionary;

	public void injectSource(org.monet.api.space.backservice.impl.model.Source source) {
		this.source = source;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	@Override
	public String getId() {
		return source.getId();
	}

	@Override
	public String getName() {
		return source.getName();
	}

	@Override
	public String getCode() {
		return source.getCode();
	}

	@Override
	public String getPartnerName() {
		throw new NotImplementedException();
	}

	@Override
	public String getPartnerLabel() {
		throw new NotImplementedException();
	}

	@Override
	public TermList getTermList() {
		return getTermList(null);
	}

	@Override
	public TermList getTermList(String parent) {
		return termListToBpiTermList(this.api.getSourceTerms(this.source.getId(), parent));
	}

	public Term getParentTerm(String code) {
		return termToBpiTerm(this.api.getSourceParentTerm(this.source.getId(), code));
	}

	public Term getTerm(String key) {
		throw new NotImplementedException();
	}

	public boolean isTerm(String code) {
		throw new NotImplementedException();
	}

	public boolean isTermSuperTerm(String code) {
		throw new NotImplementedException();
	}

	public boolean isTermCategory(String code) {
		throw new NotImplementedException();
	}

	public boolean isTermEnabled(String code) {
		throw new NotImplementedException();
	}

	public boolean isTermDisabled(String code) {
		throw new NotImplementedException();
	}

	public Term addTerm(Term term) {
		this.api.addSourceTerm(this.source.getId(), term.getKey(), term.getLabel(), term.getType(), null, term.getTags());
		term.setSource(this);
		return term;
	}

	public Term addTerm(Term term, String parentCode) {
		this.api.addSourceTerm(this.source.getId(), term.getKey(), term.getLabel(), term.getType(), parentCode, term.getTags());
		term.setSource(this);
		return term;
	}

	public Term addTerm(Term term, Term parent) {
		this.api.addSourceTerm(this.source.getId(), term.getKey(), term.getLabel(), term.getType(), parent.getKey(), term.getTags());
		term.setSource(this);
		return term;
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

	private TermList termListToBpiTermList(org.monet.api.space.backservice.impl.model.TermList sourceTermList) {
		TermList result = new TermList();

		for (org.monet.api.space.backservice.impl.model.Term term : sourceTermList.get().values())
			result.add(termToBpiTerm(term));

		return result;
	}

	private Term termToBpiTerm(org.monet.api.space.backservice.impl.model.Term term) {
		if (term == null)
			return null;

		Term targetTerm = new Term();
		targetTerm.setKey(term.getCode());
		targetTerm.setLabel(term.getLabel());
		return targetTerm;
	}

}