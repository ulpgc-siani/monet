package org.monet.space.explorer.control.displays.serializers;

import com.google.gson.*;
import org.monet.space.explorer.model.ExplorerList;
import org.monet.space.kernel.model.Term;

import java.lang.reflect.Type;
import java.util.Set;

public class TermSerializer extends AbstractSerializer<Term> implements JsonSerializer<Term> {

	public TermSerializer(Helper helper) {
		super(helper);
	}

	@Override
	public JsonElement serializeObject(Term term) {
		GsonBuilder builder = new GsonBuilder().disableHtmlEscaping();
		builder.registerTypeAdapter(Term.class, this);
		return builder.create().toJsonTree(term, Term.class);
	}

	@Override
	public JsonElement serialize(Term term, Type type, JsonSerializationContext jsonSerializationContext) {
		return serializeTerm(term);
	}

	private JsonElement serializeTerm(Term term) {
		JsonObject result = new JsonObject();

		result.addProperty("value", term.getCode());
		result.addProperty("label", term.getLabel());
		result.addProperty("flatten_label", term.getFlattenLabel());
		result.add("terms", listToJsonObject(toExplorerList(term.getTermList())));
		result.addProperty("type", getType(term));

		return result;
	}

	private ExplorerList<Term> toExplorerList(Set<Term> termList) {
		ExplorerList<Term> result = new ExplorerList<>();
		result.setTotalCount(termList.size());

		for (Term term : termList)
			result.add(term);

		return result;
	}

	private String getType(Term term) {
		if (term.isTerm())
			return "term";
		if (term.isCategory())
			return "category";
		if (term.isSuperTerm())
			return "super_term";
		return null;
	}

}
