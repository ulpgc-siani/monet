package client.core.system;

import client.core.model.definition.Ref;
import client.core.model.definition.views.DesktopViewDefinition;

public class DesktopView extends NodeView<DesktopViewDefinition> implements client.core.model.DesktopView {
	private client.core.model.List<client.core.model.Entity> shows = null;

	public DesktopView() {
		super();
	}

	public DesktopView(Key key, String label, boolean isDefault, client.core.model.Node scope) {
		super(key, label, isDefault, scope);
	}

	@Override
	public ClassName getClassName() {
		return client.core.model.DesktopView.CLASS_NAME;
	}

	@Override
	public client.core.model.List<client.core.model.Entity> getShows() {

		if (shows != null)
			return shows;

		loadShows();

		return shows;
	}

	public void setShows(client.core.model.List<client.core.model.Entity> shows) {
		this.shows = shows;
	}

	private void loadShows() {
		Desktop scope = getScope();
		DesktopViewDefinition.ShowDefinition definition = getDefinition().getShow();

		shows = new MonetList<>();
		for (Ref link : definition.getLinks())
			shows.add(scope.getSingleton(link.getValue()));
	}

}
