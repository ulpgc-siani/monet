package client.presenters.displays;

import client.core.model.HtmlPage;
import client.core.model.Space;
import client.services.Services;
import cosmos.presenters.Presenter;
import cosmos.presenters.PresenterBodyMaker;
import cosmos.presenters.RootDisplay;

public class ApplicationDisplay extends RootDisplay {
	private final Space space;
	private final Services services;

	public static final Type TYPE = new Presenter.Type("ApplicationDisplay", Display.TYPE);

    public ApplicationDisplay(Services services, Space space, PresenterBodyMaker[] makers) {
        super(makers);
        this.services = services;
	    this.space = space;
    }

	@Override
	public Type getType() {
		return TYPE;
	}

	@Override
	public void makeBody() {
		propagateServices(this);
		propagateRootDisplay(this);
		super.makeBody();
	}

	public String getTitle() {
		return space.getName() + " . " + space.getAccount().getFullName();
	}

	public String getEnvironmentCode() {
		return space.getAccount().getRootNode().getDefinition().getCode();
	}

	@Override
	public void presenterAdded(Presenter presenter) {
		propagateServices(presenter);
		propagateRootDisplay(presenter);
		super.presenterAdded(presenter);
	}

    @Override
	public void presenterRemoved(Presenter presenter) {
		propagatePresenterRemoved(presenter);
		super.presenterRemoved(presenter);
	}

	public VisitingDisplay getVisitingDisplay() {
		for (Presenter child : this)
			if (child.is(VisitingDisplay.TYPE))
				return (VisitingDisplay) child;
		return null;
	}

	public HelperDisplay getHelperDisplay() {
		for (Presenter child : this)
			if (child.is(HelperDisplay.TYPE))
				return (HelperDisplay) child;
		return null;
	}

	public boolean isHelperVisible() {
		HelperDisplay helperDisplay = getHelperDisplay();
		return helperDisplay != null && helperDisplay.isVisible();
	}

	public void removeVisitingDisplay() {
		Presenter display = getVisitingDisplay();

		if (display != null)
			this.removeChild(display);
	}

	public void addVisitingDisplay(Presenter presenter) {
		this.addChild(presenter);
	}

	public ExplorationDisplay getExplorationDisplay() {
		return (ExplorationDisplay)getChild(ExplorationDisplay.TYPE);
	}

	public MessageDisplay getMessageDisplay() {
		return (MessageDisplay)getChild(MessageDisplay.TYPE);
	}

	public LayoutDisplay getLayoutDisplay() {
		return (LayoutDisplay)getChild(LayoutDisplay.TYPE);
	}

	private void injectServices(Presenter presenter) {
		if (presenter instanceof client.presenters.displays.Display)
			((client.presenters.displays.Display)presenter).inject(services);

		if (presenter instanceof client.presenters.operations.Operation)
			((client.presenters.operations.Operation)presenter).inject(services);
	}

	private void propagateServices(Presenter presenter) {

		for (Presenter child : presenter)
			propagateServices(child);

		injectServices(presenter);
	}

	private void propagateRootDisplay(Presenter presenter) {

		for (Presenter child : presenter)
			propagateRootDisplay(child);

		presenter.setRootDisplay(this);
	}

	private void propagatePresenterRemoved(Presenter presenter) {
		for (Presenter child : presenter) {
			propagatePresenterRemoved(child);
			presenter.presenterRemoved(child);
		}
	}

	@Override
	public void addChild(Presenter presenter) {
		super.addChild(presenter);

		if (presenter instanceof HelperDisplay)
			((HelperDisplay)presenter).setListener(new HelperDisplay.Listener() {
				@Override
				public void onChangePage(HtmlPage page) {
					notifyHelperPage(page);
				}
			});
    }

	private void notifyHelperPage(final HtmlPage page) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.helperPage(page);
			}
		});
	}

	public interface Hook extends RootDisplay.Hook {
		void helperPage(HtmlPage page);
	}
}
