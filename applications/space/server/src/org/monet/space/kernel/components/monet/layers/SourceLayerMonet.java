package org.monet.space.kernel.components.monet.layers;

import org.monet.metamodel.SourceDefinition;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.constants.Producers;
import org.monet.space.kernel.exceptions.DataException;
import org.monet.space.kernel.model.*;
import org.monet.space.kernel.producers.ProducerSource;
import org.monet.space.kernel.producers.ProducerSourceList;
import org.monet.space.kernel.producers.ProducerSourceStore;

import java.util.List;
import java.util.Map;

public class SourceLayerMonet extends PersistenceLayerMonet implements SourceLayer {

	public SourceLayerMonet(ComponentPersistence componentPersistence) {
		super(componentPersistence);
	}

	@Override
	public SourceList loadSourceList() {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceList producerSourceList = producersFactory.get(Producers.SOURCELIST);

		return producerSourceList.load();
	}

	@Override
	public SourceList loadGlossaryList() {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceList producerSourceList = producersFactory.get(Producers.SOURCELIST);

		return producerSourceList.loadGlossaries();
	}

	@Override
	public SourceList loadSourceList(String code, String partner) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceList producerSourceList = producersFactory.get(Producers.SOURCELIST);

		return producerSourceList.load(code, partner);
	}

	@Override
	public int loadSourceListCount(String code) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceList producerSourceList = producersFactory.get(Producers.SOURCELIST);

		return producerSourceList.loadCount();
	}

	@Override
	public void removeSourceList(SourceList sourceList) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSource producerSource = producersFactory.get(Producers.SOURCE);

		for (Source<SourceDefinition> source : sourceList)
			producerSource.remove(source);
	}

	@Override
	public List<String> loadSourceListCodes() {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceList producerSourceList = producersFactory.get(Producers.SOURCELIST);

		return producerSourceList.loadCodes();
	}

	@Override
	public Map<String, FederationUnit> loadSourceListPartners(List<String> ontologies) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceList producerSourceList = producersFactory.get(Producers.SOURCELIST);

		return producerSourceList.loadPartners(ontologies);
	}

	@Override
	public Source<SourceDefinition> loadSource(String id) {
        if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSource producerSource = producersFactory.get(Producers.SOURCE);

        return producerSource.load(id);
	}

	@Override
	public boolean existsSource(String code, FeederUri uri) {
        if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSource producerSource = producersFactory.get(Producers.SOURCE);

        return producerSource.exists(code, uri);
	}

	@Override
	public Source<SourceDefinition> locateSource(String code, FeederUri uri) {
        if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSource producerSource = producersFactory.get(Producers.SOURCE);

        return producerSource.locate(code, uri);
	}

	@Override
	public Source<SourceDefinition> createSource(String code) {
        if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSource producerSource = producersFactory.get(Producers.SOURCE);

        return producerSource.create(code);
	}

	@Override
	public void saveSource(Source<SourceDefinition> source) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSource producerSource = producersFactory.get(Producers.SOURCE);
		producerSource.save(source);
	}

	@Override
	public void removeSource(Source<SourceDefinition> source) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSource producerSource = producersFactory.get(Producers.SOURCE);
		producerSource.remove(source);
	}

	@Override
	public void populateSource(Source<SourceDefinition> source) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSource producerSource = producersFactory.get(Producers.SOURCE);
		producerSource.populate(source);
	}

	@Override
	public void synchronizeSource(Source<SourceDefinition> source) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSource producerSource = producersFactory.get(Producers.SOURCE);
		producerSource.synchronize(source);
	}

	@Override
	public boolean existsSourceTerm(Source<SourceDefinition> source, String code) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceStore producerSource = producersFactory.get(Producers.SOURCESTORE);
		return producerSource.existsTerm(source, code);
	}

	@Override
	public TermList loadSourceNewTerms(Source<SourceDefinition> source) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceStore producerSource = producersFactory.get(Producers.SOURCESTORE);

		return producerSource.loadNewTerms(source);
	}

	@Override
	public TermList loadSourceTerms(Source<SourceDefinition> source, DataRequest dataRequest, boolean onlyPublished) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceStore producerSource = producersFactory.get(Producers.SOURCESTORE);

        return producerSource.loadTerms(source, dataRequest, onlyPublished);
	}

	public TermList loadSourceTerms(Source<SourceDefinition> source, boolean onlyPublished) {
        if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceStore producerSource = producersFactory.get(Producers.SOURCESTORE);
		DataRequest dataRequest = new DataRequest();
		dataRequest.setLimit(-1);
		dataRequest.addParameter(DataRequest.MODE, DataRequest.Mode.TREE);

        return producerSource.loadTerms(source, dataRequest, onlyPublished);
	}

	public List<Term> loadSourceAncestorsTerms(Source<SourceDefinition> source, Term term) {
        if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceStore producerSource = producersFactory.get(Producers.SOURCESTORE);

        return producerSource.loadAncestorsTerms(source, term);
	}

	@Override
	public String locateSourceTermCode(Source<SourceDefinition> source, String value) {
        if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceStore producerSource = producersFactory.get(Producers.SOURCESTORE);

        return producerSource.locateTermCode(source, value);
	}

	@Override
	public String locateSourceTermValue(Source<SourceDefinition> source, String code) {
        if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceStore producerSource = producersFactory.get(Producers.SOURCESTORE);

        return producerSource.locateTermLabel(source, code);
	}

	@Override
	public TermList searchSourceTerms(Source<SourceDefinition> source, DataRequest dataRequest) {
        if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceStore producerSourceStore = producersFactory.get(Producers.SOURCESTORE);

        return producerSourceStore.searchTerms(source, dataRequest);
	}

	@Override
	public void addSourceTerm(Source<SourceDefinition> source, Term term, String codeParent) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceStore producerSource = producersFactory.get(Producers.SOURCESTORE);
		producerSource.addTerm(source, term, codeParent);
	}

	@Override
	public Term loadSourceTerm(Source<SourceDefinition> source, String code) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceStore producerSource = producersFactory.get(Producers.SOURCESTORE);

		return producerSource.loadTerm(source, code);
	}

	@Override
	public void updateSourceTerm(Source<SourceDefinition> source, Term term) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceStore producerSource = producersFactory.get(Producers.SOURCESTORE);
		producerSource.updateTerm(source, term);
	}

	@Override
	public void deleteSourceTerm(Source<SourceDefinition> source, String code) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceStore producerSource = producersFactory.get(Producers.SOURCESTORE);
		producerSource.deleteTerm(source, code);
	}

	@Override
	public boolean publishSourceTerms(Source<SourceDefinition> source, String[] terms) {
		if (!isStarted())
			throw new DataException(ErrorCode.BUSINESS_UNIT_STOPPED, null);

        ProducerSourceStore producerSource = producersFactory.get(Producers.SOURCESTORE);

		return producerSource.publishTerms(source, terms);
	}

}
