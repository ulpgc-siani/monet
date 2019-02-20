package client.presenters.displays;

import client.core.model.Entity;
import client.core.model.Instance;
import client.core.model.factory.EntityFactory;
import client.presenters.operations.Operation;
import client.services.Services;
import cosmos.presenters.Presenter;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public abstract class Display extends cosmos.presenters.Display {
	private static final Map<String, Builder> factory = new HashMap<>();
    protected Services services;

	public static final Type TYPE = new Type("Display", cosmos.presenters.Display.TYPE);

	public void inject(Services services) {
		if (this.services != null)
			return;

        this.services = services;
		onInjectServices();
    }

	public void showMessage(String message, String shortMessage) {
        ((ApplicationDisplay) getRootDisplay()).getMessageDisplay().showMessage(message, shortMessage);
	}

	protected abstract void onInjectServices();

	protected static void registerBuilder(String type, Builder builder) {
		factory.put(type, builder);
	}

	protected static Builder getBuilder(Instance.ClassName... classNames) {
		String key = constructBuilderKey(classNames);

		if (key.isEmpty())
			return null;

		if (!factory.containsKey(key))
			return getBuilder(Arrays.copyOfRange(classNames, 0, classNames.length-1));

		return factory.get(key);
	}

	protected static String constructBuilderKey(Instance.ClassName... classNames) {
		String key = "";
		for (Entity.ClassName className : classNames)
			if (className != null)
			    key += className.toString();
		return key;
	}

	protected Operation getVisitingDisplayOperation() {
		VisitingDisplay display = getOwner(VisitingDisplay.TYPE);

		if (display == null)
			return null;

		return display.toOperation();
	}

	public OperationListDisplay getOperationListDisplay() {
		OperationListDisplay display = getChild(OperationListDisplay.TYPE);

		if (display != null)
			return display;

		Presenter owner = getOwner();
		if (owner != null && owner instanceof Display)
			return ((Display)owner).getOperationListDisplay();

		return null;
	}

	public MessageDisplay getMessageDisplay() {
		return ((ApplicationDisplay)getRootDisplay()).getMessageDisplay();
	}

	public LayoutDisplay getLayoutDisplay() {
		return ((ApplicationDisplay)getRootDisplay()).getLayoutDisplay();
	}

	public EntityFactory getEntityFactory() {
		return services.getSpaceService().getEntityFactory();
	}

	public static class Builder {

	}

	protected cosmos.presenters.Operation.Context getOperationContext() {
		return new cosmos.presenters.Operation.Context() {
			@Override
			public Presenter getCanvas() {
				return Display.this.getRootDisplay();
			}

			@Override
			public cosmos.presenters.Operation getReferral() {
				return getVisitingDisplayOperation();
			}
		};
	}

}
