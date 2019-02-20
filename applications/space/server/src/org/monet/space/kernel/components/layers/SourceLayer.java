package org.monet.space.kernel.components.layers;

import org.monet.metamodel.SourceDefinition;
import org.monet.space.kernel.model.*;

import java.util.List;
import java.util.Map;

public interface SourceLayer extends Layer {
	SourceList loadSourceList();

	SourceList loadGlossaryList();

	SourceList loadSourceList(String code, String partner);

	int loadSourceListCount(String code);

	void removeSourceList(SourceList definitions);

	List<String> loadSourceListCodes();

	Map<String, FederationUnit> loadSourceListPartners(List<String> ontologies);

	Source<SourceDefinition> loadSource(String id);

	boolean existsSource(String code, FeederUri url);

	Source<SourceDefinition> locateSource(String code, FeederUri url);

	Source<SourceDefinition> createSource(String code);

	void saveSource(Source<SourceDefinition> source);

	void removeSource(Source<SourceDefinition> source);

	void populateSource(Source<SourceDefinition> source);

	void synchronizeSource(Source<SourceDefinition> source);

	boolean existsSourceTerm(Source<SourceDefinition> source, String code);

	TermList loadSourceNewTerms(Source<SourceDefinition> source);

	TermList loadSourceTerms(Source<SourceDefinition> source, DataRequest oDataRequest, boolean onlyPublished);

	TermList loadSourceTerms(Source<SourceDefinition> source, boolean onlyPublished);

	List<Term> loadSourceAncestorsTerms(Source<SourceDefinition> source, Term term);

	String locateSourceTermCode(Source<SourceDefinition> source, String value);

	String locateSourceTermValue(Source<SourceDefinition> source, String code);

	TermList searchSourceTerms(Source<SourceDefinition> source, DataRequest dataRequest);

	void addSourceTerm(Source<SourceDefinition> source, Term term, String codeParent);

	Term loadSourceTerm(Source<SourceDefinition> source, String code);

	void updateSourceTerm(Source<SourceDefinition> source, Term term);

	void deleteSourceTerm(Source<SourceDefinition> source, String code);

	boolean publishSourceTerms(Source<SourceDefinition> source, String[] terms);
}
