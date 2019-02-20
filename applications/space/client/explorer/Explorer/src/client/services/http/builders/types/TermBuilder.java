package client.services.http.builders.types;

import client.core.model.types.CompositeTerm;
import client.core.system.types.SuperTerm;
import client.core.system.types.Term;
import client.core.system.types.TermCategory;
import client.core.system.types.TermList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

import java.util.HashMap;
import java.util.Map;

public class TermBuilder implements Builder<client.core.model.types.Term, client.core.model.types.TermList> {

	private final Map<String, Builder> builders = new HashMap<>();

	public TermBuilder() {
		builders.put("category", new TermBuilder.Builder() {
			@Override
			public client.core.model.types.Term build(HttpInstance instance) {
				return buildCategory(instance);
			}
		});
		builders.put("super_term", new TermBuilder.Builder() {
			@Override
			public client.core.model.types.Term build(HttpInstance instance) {
				return buildSuperTerm(instance);
			}
		});
		builders.put("term", new TermBuilder.Builder() {
			@Override
			public client.core.model.types.Term build(HttpInstance instance) {
				return buildTerm(instance);
			}
		});
	}

	@Override
	public client.core.model.types.Term build(HttpInstance instance) {
		if (instance == null || (instance.getString("value").isEmpty() && instance.getString("label").isEmpty()))
			return null;

		if (builders.containsKey(instance.getString("type")))
			return builders.get(instance.getString("type")).build(instance);
		return buildTerm(instance);
	}

	@Override
	public void initialize(client.core.model.types.Term object, HttpInstance instance) {
		Term term = (Term)object;
		term.setValue(instance.getString("value"));
		term.setLabel(instance.getString("label"));
		term.setFlattenLabel(instance.getString("flatten_label"));
	}

	@Override
	public client.core.model.types.TermList buildList(HttpList instance) {
		TermList result = new TermList();
		result.setTotalCount(instance.getTotalCount());

		for (int i = 0; i < instance.getItems().length(); i++)
			result.add(build(instance.getItems().get(i)));

		return result;
	}

	private client.core.model.types.Term buildTerm(HttpInstance instance) {
		Term term = new Term();
		initialize(term, instance);
		return term;
	}

	private client.core.model.types.CompositeTerm buildSuperTerm(HttpInstance instance) {
		client.core.model.types.CompositeTerm superTerm = new SuperTerm();
		initializeCompositeTerm(instance, superTerm);
		return superTerm;
	}

	private client.core.model.types.CompositeTerm buildCategory(HttpInstance instance) {
		client.core.model.types.CompositeTerm category = new TermCategory();
		initializeCompositeTerm(instance, category);
		return category;
	}

	private void initializeCompositeTerm(HttpInstance instance, CompositeTerm compositeTerm) {
		initialize(compositeTerm, instance);
		compositeTerm.setTerms(buildList(instance.getList("terms")));
	}

	private interface Builder {
		client.core.model.types.Term build(HttpInstance instance);
	}
}
