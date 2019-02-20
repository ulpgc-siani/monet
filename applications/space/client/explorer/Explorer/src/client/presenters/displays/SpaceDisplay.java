package client.presenters.displays;

import client.core.model.Space;
import client.presenters.operations.Operation;
import client.presenters.operations.ShowHomeOperation;
import client.services.TranslatorService;
import cosmos.gwt.model.Theme;
import cosmos.presenters.Presenter;

public class SpaceDisplay extends Display {
	private Space space;
	private Theme theme;

	public static final Type TYPE = new Presenter.Type("SpaceDisplay", Display.TYPE);

	public SpaceDisplay(Space space, Theme theme) {
		this.space = space;
		this.theme = theme;
	}

	@Override
	protected void onInjectServices() {
		addToggleLayoutModeOperation();
	}

	private void addToggleLayoutModeOperation() {
		Operation operation = new Operation(new cosmos.presenters.Operation.NullContext()) {

			@Override
			public Type getType() {
				return new Presenter.Type("ToggleLayoutModeOperation", Operation.TYPE);
			}

			@Override
			public void doExecute() {
				getLayoutDisplay().toggleMode();
			}

			@Override
			public String getDefaultLabel() {
				return services.getTranslatorService().translate(TranslatorService.OperationLabel.TOGGLE_LAYOUT_MODE);
			}
		};
		addChild(operation);
	}

	public String getName() {
		return space.getName();
	}

	public void showHome() {
		ShowHomeOperation operation = new ShowHomeOperation(new cosmos.presenters.Operation.Context() {
			@Override
			public <T extends Presenter> T getCanvas() {
				return (T) SpaceDisplay.this.getRootDisplay();
			}

			@Override
			public cosmos.presenters.Operation getReferral() {
				return getVisitingDisplayOperation();
			}
		});
		operation.inject(services);
		operation.execute();
	}

	@Override
	public Type getType() { return TYPE; }

	public String getLogo() {
		return (getLayoutDisplay().isReducedMode())?theme.getLogoReducedPath():theme.getLogoPath();
	}
}
