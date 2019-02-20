package org.monet.space.kernel.components.monet.layers;

import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.HistoryStoreLayer;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.DataRequest;
import org.monet.space.kernel.model.HistoryStore;
import org.monet.space.kernel.model.MonetHashMap;
import org.monet.space.kernel.model.TermList;
import org.monet.space.kernel.producers.ProducerHistoryStore;

public class HistoryStoreLayerMonet extends PersistenceLayerMonet implements HistoryStoreLayer {

	public HistoryStoreLayerMonet(ComponentPersistence componentPersistence) {
		super(componentPersistence);
	}

	@Override
	public HistoryStore loadHistoryStore(String code) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		return historyStore().load(code);
	}

	@Override
	public TermList getHistoryStoreTerms(String codeStore, DataRequest dataRequest) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		return historyStore().loadTerms(codeStore, dataRequest);
	}

	@Override
	public void addHistoryStoreTerm(String codeStore, String code, String label) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		historyStore().addTerm(codeStore, code, label);
	}

	@Override
	public MonetHashMap<String> getHistoryStoreDefaultValues(String codeStore, String codeNodeType) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		return historyStore().loadDefaultValues(codeStore, codeNodeType);
	}

	@Override
	public String getHistoryStoreDefaultValue(String codeStore, String codeNodeType, String codeProperty) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		return historyStore().loadDefaultValue(codeStore, codeNodeType, codeProperty);
	}

	@Override
	public void addHistoryStoreDefaultValue(String codeStore, String codeNodeType, String codeProperty, String label) {
		if (!this.isStarted()) {
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);
		}
		historyStore().addDefaultValue(codeStore, codeNodeType, codeProperty, label);
	}

	private ProducerHistoryStore historyStore() {
		return producersFactory.get(Producers.HISTORY_STORE);
	}
}
