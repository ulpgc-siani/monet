package client.presenters.displays.view;

import client.core.model.Showable;
import client.core.model.Entity;
import client.core.model.View;
import client.core.model.ViewList;
import client.presenters.displays.EntityDisplay;
import cosmos.presenters.Display;
import cosmos.presenters.Presenter;

import java.util.ArrayList;

public class ViewListDisplay<T extends Entity> extends EntityDisplay<T, View> {
	private boolean initialized = false;
	protected final ViewList<View> viewList;
	private ViewDisplay activeView = null;

	public static final Type TYPE = new Type("ViewListDisplay", Display.TYPE);

	public ViewListDisplay(T entity, ViewList viewList) {
        super(entity, null);
		this.viewList = viewList;
	}

	@Override
	protected void onInjectServices() {
		if (initialized)
			return;

		initialized = true;
		addViews();
	}

	public String getLabel() {
		T entity = getEntity();

		if (entity instanceof Showable)
			return entity.getLabel();

		return "";
	}

	public ViewDisplay[] getViews() {
		ArrayList<ViewDisplay> result = new ArrayList<>();

		for (Presenter child : this) {
			if (child instanceof ViewDisplay)
				result.add((ViewDisplay)child);
		}

		return result.toArray(new ViewDisplay[result.size()]);
	}

	public int getViewsCount() {
		return getViews().length;
	}

	public boolean isActiveView(ViewDisplay viewDisplay) {
		return getActiveView() == viewDisplay;
	}

	public void setActiveView(View view) {
		final ViewDisplay viewDisplay = getDisplay(view);
		this.activeView = viewDisplay;
		notifyActiveView();
	}

	public ViewDisplay getActiveView() {
		if (this.activeView == null)
			this.activeView = getDisplay(getDefaultView());

		return this.activeView;
	}

	public void activateView(final ViewDisplay viewDisplay) {
		this.activeView = viewDisplay;
		notifyActiveView();
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public interface Hook extends EntityDisplay.Hook {
		void activate(ViewDisplay viewDisplay);
	}

	protected void addViews() {
		for (View view : viewList)
			this.addChild(new ViewDisplay.Builder().build(getEntity(), view));
	}

	private ViewDisplay getDisplay(View view) {
		ViewDisplay[] viewDisplays = getViews();

		for (ViewDisplay viewDisplay : viewDisplays) {
			if (viewDisplay.getView().equals(view))
				return viewDisplay;
		}

		return null;
	}

	private View getDefaultView() {
		View defaultView = viewList.getDefaultView();

		if (defaultView == null)
			defaultView = viewList.get(0);

		return defaultView;
	}

	private void notifyActiveView() {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.activate(activeView);
			}
		});
	}

}
