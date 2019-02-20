package org.monet.mocks.businessunit.control;

import com.google.inject.Inject;
import org.monet.mocks.businessunit.guice.InjectorFactory;

import java.util.Map;

public class ActionsFactory {

	public static final String ACTION_REQUEST_SERVICE = "request";
	public static final String ACTION_CALLBACK = "callback";

	@Inject
	protected Map<String, Class<? extends Action>> actionsMap;

	public Action create(String key) {
		return InjectorFactory.get().getInstance(this.actionsMap.get(key));
	}

}
