package client.presenters.operations;

import client.core.model.*;
import client.presenters.displays.*;
import client.services.TranslatorService;
import cosmos.model.ServerError;
import cosmos.presenters.Presenter;

import java.util.logging.Level;
import java.util.logging.Logger;

public class SearchOperation extends Operation {
	private final Node target;
	private String condition;

	public static final Type TYPE = new Type("SearchOperation", Operation.TYPE);

	public SearchOperation(Context context, Node target) {
		super(context);
		this.target = target;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public String getCondition() {
		return this.condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	@Override
	public void doExecute() {
		Logger.getLogger("ApplicationLogger").log(Level.FINEST, this.getType().toString() + " called. Node: " + target.getLabel());

		if (condition == null || condition.isEmpty() || condition.trim().isEmpty())
			return;

		refresh();
	}

	protected void refresh() {
		refreshCanvas();
		executePerformed();
		notifyCondition(condition);
	}

	protected void refreshCanvas() {
		SearchIndexDisplay searchDisplay = new SearchIndexDisplay(target, createHandler());
		SearchVisitingDisplay visitingDisplay = new SearchVisitingDisplay(searchDisplay, condition, context.getReferral());
		Presenter display = context.getCanvas();

		if (display.existsChild(VisitingDisplay.TYPE))
			display.removeChild(VisitingDisplay.TYPE);

		display.addChild(visitingDisplay);
		searchDisplay.setCondition(condition);
	}

	private SearchIndexDisplay.Handler createHandler() {
		return new SearchIndexDisplay.Handler() {

			@Override
			public void onActivate(final SearchIndexDisplay display, NodeIndexEntry entry) {
				Node entity = entry.getEntity();
				ViewList<NodeView> views = entity.getViews();
				Operation operation = new ShowNodeViewOperation(new Context() {
					@Override
					public <T extends Presenter> T getCanvas() {
						return (T) display;
					}

					@Override
					public cosmos.presenters.Operation getReferral() {
						return context.getReferral();
					}
				}, entity, views.getDefaultView());
				operation.inject(services);
				operation.execute();
			}

			@Override
			public void onSelect(SearchIndexDisplay display, NodeIndexEntry entry) {
				refreshOperations(display);
			}

			@Override
			public void onDelete(final SearchIndexDisplay display, final NodeIndexEntry entry) {
				Operation operation = new DeleteNodeOperation(context, entry.getEntity());
				operation.addHook(new cosmos.presenters.Operation.Hook() {
					@Override
					public void show() {
					}

					@Override
					public void hide() {
					}

					@Override
					public void enable() {
					}

					@Override
					public void disable() {
					}

					@Override
					public void executePerformed() {
						display.reloadPage();
						refreshOperations(display);
					}

					@Override
					public void executeFailed(ServerError details) {

					}
				});
				operation.inject(services);
				operation.execute();
			}

			@Override
			public void onUnSelect(SearchIndexDisplay display, NodeIndexEntry entry) {
				refreshOperations(display);
			}

			@Override
			public void onUnSelectAll(SearchIndexDisplay display) {
				refreshOperations(display);
			}

			private void refreshOperations(SearchIndexDisplay indexDisplay) {
				boolean enabled = indexDisplay.getSelectionCount() > 0;
				List<cosmos.presenters.Operation> operations = indexDisplay.getOperations();
				for (cosmos.presenters.Operation operation : operations) {
					if (operation instanceof DeleteNodesOperation)
						operation.setEnabled(enabled);
				}
			}
		};
	}

	@Override
	public boolean disableButtonWhenExecute() {
		return true;
	}

	@Override
	public String getDefaultLabel() {
		return services.getTranslatorService().translate(TranslatorService.Label.SEARCH);
	}

	public interface Hook extends Operation.Hook {
		void condition(String condition);
	}

	private void notifyCondition(final String condition) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.condition(condition);
			}
		});
	}

}
