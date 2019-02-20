package client.services.http.builders.workmap;

import client.core.model.List;
import client.core.model.definition.entity.PlaceDefinition;
import client.core.system.MonetList;
import client.core.system.workmap.Action;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;
import client.services.http.builders.definition.DefinitionBuilder;

import java.util.HashMap;
import java.util.Map;

import static client.core.model.workmap.Action.Type;

public class ActionBuilder<T extends client.core.model.workmap.Action> implements Builder<T, List<T>> {

	private static final Map<Type, ActionBuilder> actionCreators = new HashMap<Type, ActionBuilder>() {{
		put(Type.DELEGATION, new DelegationActionBuilder());
		put(Type.EDITION, new EditionActionBuilder());
		//put(Type.ENROLL, new EnrollActionCreator());
		put(Type.LINE, new LineActionBuilder());
		//put(Type.SEND_JOB, new SendJobCreator());
		put(Type.WAIT, new WaitActionBuilder());
	}};

	@Override
	public T build(HttpInstance instance) {
		Type type = Type.fromString(instance.getString("type"));
		return (T)actionCreators.get(type).build(instance);
	}

	@Override
	public void initialize(T object, HttpInstance instance) {
		Action action = (Action)object;
		action.setDefinition(new DefinitionBuilder<PlaceDefinition.ActionDefinition>().build(instance.getObject("definition")));
	}

	@Override
	public List<T> buildList(HttpList instance) {
		return (List<T>)new MonetList<client.core.model.workmap.Action>();
	}

}
