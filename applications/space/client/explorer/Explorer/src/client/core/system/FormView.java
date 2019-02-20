package client.core.system;

import client.core.model.definition.Ref;
import client.core.model.definition.views.FormViewDefinition;

public class FormView extends NodeView<FormViewDefinition> implements client.core.model.FormView {
	private client.core.model.List<client.core.model.Field> shows = null;

	public FormView() {
	}

	public FormView(Key key, String label, boolean isDefault, client.core.model.Node scope) {
		super(key, label, isDefault, scope);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.FormView.CLASS_NAME;
	}

	@Override
	public client.core.model.List<client.core.model.Field> getShows() {

		if (shows != null)
			return shows;

		loadShows();

		return shows;
	}

	public void setShows(client.core.model.List<client.core.model.Field> shows) {
		this.shows = shows;
	}

	private void loadShows() {
		Form scope = getScope();
		FormViewDefinition.ShowDefinition definition = getDefinition().getShow();

		shows = new MonetList<>();
		for (Ref fieldRef : definition.getFields())
			shows.add(scope.get(fieldRef.getValue()));
	}

}
