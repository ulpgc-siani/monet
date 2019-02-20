package org.monet.space.kernel.components.layers;

import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.HistoryStore;
import org.monet.space.kernel.model.MonetHashMap;
import org.monet.space.kernel.model.TermList;

public interface HistoryStoreLayer extends Layer {

	public HistoryStore loadHistoryStore(String code);

	public void addHistoryStoreTerm(String codeStore, String code, String label);

	public TermList getHistoryStoreTerms(String codeStore, DataRequest dataRequest);

	public void addHistoryStoreDefaultValue(String codeStore, String codeNodeType, String codeProperty, String label);

	public MonetHashMap<String> getHistoryStoreDefaultValues(String codeStore, String codeNodeType);

	public String getHistoryStoreDefaultValue(String codeStore, String codeNodeType, String codeProperty);

}
