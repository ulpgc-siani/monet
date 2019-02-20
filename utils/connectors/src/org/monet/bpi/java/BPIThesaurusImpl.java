package org.monet.bpi.java;

import org.monet.api.backservice.BackserviceApi;
import org.monet.bpi.BPIThesaurus;
import org.monet.bpi.types.TermList;
import org.monet.v2.BPIClassLocator;
import org.monet.v2.model.Dictionary;

public class BPIThesaurusImpl implements BPIThesaurus {

	private BackserviceApi api;
	private BPIClassLocator bpiClassLocator;
	private Dictionary dictionary;

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectDictionary(Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	@Override
	public TermList getTermList() {
		return null;
	}

	@Override
	public TermList getTermList(String parent) {
		return null;
	}

}