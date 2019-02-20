package client.presenters.displays.view;

import client.core.model.*;
import client.core.model.definition.Dictionary;
import client.core.model.definition.Ref;
import client.core.model.definition.views.SetViewDefinition;
import client.core.model.definition.views.ViewDefinition;
import client.core.model.factory.EntityFactory;
import client.core.system.MonetList;
import client.presenters.displays.SetIndexDisplay;
import client.presenters.operations.*;
import client.services.IndexService;
import client.services.TranslatorService;
import client.services.callback.ReportCallback;
import cosmos.model.ServerError;
import cosmos.presenters.Display;
import cosmos.presenters.Presenter;
import cosmos.presenters.RootDisplay;

import java.util.logging.Level;
import java.util.logging.Logger;

public abstract class SetViewDisplay extends NodeViewDisplay<SetView> {

	public static final Type TYPE = new Type("SetViewDisplay", NodeViewDisplay.TYPE);

	public SetViewDisplay(Node node, SetView nodeView) {
		super(node, nodeView);
	}

	@Override
	protected void onInjectServices() {
		if (getView().getIndex() != null)
			addIndexes();
	}

	public List<ShowNodeOperation> getShows() {
		List<ShowNodeOperation> shows = new MonetList<>();

		for (Presenter child : this) {
			if (child instanceof ShowNodeOperation)
				shows.add((ShowNodeOperation) child);
		}

		return shows;
	}

	public SetIndexDisplay[] getIndexes() {
		List<SetIndexDisplay> result = new MonetList<>();

		for (Object child : this) {
			if (child instanceof SetIndexDisplay)
				result.add((SetIndexDisplay) child);
		}

		return result.toArray(new SetIndexDisplay[result.size()]);
	}

	@Override
	public Presenter.Type getType() {
		return TYPE;
	}

	@Override
	public void addHook(Presenter.Hook hook) {
		super.addHook(hook);
	}

	private void addIndexes() {
		ViewDefinition.Design design = getDesign();
		final EntityFactory entityFactory = getEntityFactory();
		final Dictionary dictionary = services.getSpaceService().load().getDictionary();
		IndexService indexService = services.getIndexService();

		if (design != null && design == ViewDefinition.Design.DOCUMENT) {
			client.core.system.Filter filter = (client.core.system.Filter) entityFactory.createTypeFilter();
			notifyLoading();

			indexService.getFilterOptionsReport(getView().getIndex(), createScope(), filter, new ReportCallback() {
				@Override
				public void success(Report object) {
					for (String add : getAddList()) {
						client.core.system.Filter filter = (client.core.system.Filter) entityFactory.createTypeFilter();
						addIndex(filter, entityFactory.createFilterOption(dictionary.getDefinitionCode(add), dictionary.getDefinitionLabel(add)), object);
					}
					notifyUpdate();
				}

				@Override
				public void failure(String error) {
					Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not load filter report " + getView().getIndex().getLabel());
				}
			});
		} else
			addIndex(null, null, null);
	}

	private IndexService.Scope createScope() {
		return new IndexService.NodeScope() {
			@Override
			public Set getSet() {
				return (Set) getEntity();
			}

			@Override
			public Key getSetView() {
				return getView().getKey();
			}

			@Override
			public String getIndexView() {
				SetViewDefinition.ShowDefinition.IndexDefinition index = getView().getDefinition().getShow().getIndex();

				if (index == null)
					return null;

				return index.getWithView();
			}

		};
	}

	private void addIndex(Filter filter, Filter.Option option, Report report) {

		if (getView().getIndex() == null)
			return;

		final SetIndexDisplay setIndexDisplay = new SetIndexDisplay(getEntity(), getView().getIndex(), getView(), new SetIndexDisplay.Handler() {

			@Override
			public void onActivate(SetIndexDisplay display, NodeIndexEntry entry) {
				showNode(getOperationContext(display), entry.getEntity());
			}

			@Override
			public void onSelect(SetIndexDisplay display, NodeIndexEntry entry) {
				refreshOperations(display);
			}

			@Override
			public void onDelete(final SetIndexDisplay display, final NodeIndexEntry entry) {
				Operation operation = new DeleteNodeOperation(getOperationContext(display), entry.getEntity());
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
			public void onUnSelect(SetIndexDisplay display, NodeIndexEntry entry) {
				refreshOperations(display);
			}

			@Override
			public void onUnSelectAll(SetIndexDisplay display) {
				refreshOperations(display);
			}

			private void refreshOperations(SetIndexDisplay indexDisplay) {
				boolean enabled = indexDisplay.getSelectionCount() > 0;
				List<cosmos.presenters.Operation> operations = indexDisplay.getOperations();
				for (cosmos.presenters.Operation operation : operations) {
					if (operation instanceof DeleteNodesOperation)
						operation.setEnabled(enabled);
				}
			}

		});

		if (filter != null) {
			filter.setOptions(new MonetList<>(option));
			setIndexDisplay.setTitle(option.getLabel());
			setIndexDisplay.setEntriesCount(report.get(option.getValue()));
			setIndexDisplay.inject(filter);
		}

		addIndexOperations(setIndexDisplay, filter != null ? option : null);
		addChild(setIndexDisplay);
	}

	private void addIndexOperations(SetIndexDisplay setIndexDisplay, Filter.Option filterOption) {

		if (!isEditable())
			return;

		Operation addOperation = createAddOperation(setIndexDisplay, filterOption);
		if (addOperation != null)
			setIndexDisplay.addChild(addOperation);

		setIndexDisplay.addChild(createDeleteNodesOperation(setIndexDisplay));
	}

	private Operation createAddOperation(final SetIndexDisplay setIndexDisplay, final Filter.Option filterOption) {
		cosmos.presenters.Operation.Context context = getOperationContext(setIndexDisplay);

		if (filterOption != null) {
			Operation operation = new Operation(context) {
				@Override
				public String getDefaultLabel() {
					return filterOption.getLabel();
				}

				@Override
				public void doExecute() {
					final AddNodeOperation operation = new AddNodeOperation(context, getEntity(), filterOption.getValue());
					operation.setRedirection(new Operation(context) {
						@Override
						public String getDefaultLabel() {
							return null;
						}

						@Override
						public Type getType() {
							return null;
						}

						@Override
						public void doExecute() {
							Node node = operation.getNode();
							NodeIndexEntry entry = getEntityFactory().createNodeIndexEntry(node, node.getLabel());

							setIndexDisplay.add(entry);
							setIndexDisplay.activate(entry);
						}
					});
					operation.inject(services);
					operation.execute();
				}

				@Override
				public Type getType() {
					return new Type("CustomAddNodeOperation", Operation.TYPE);
				}
			};
			operation.setLabel(services.getTranslatorService().translate(TranslatorService.Label.ADD_NODE));
			return operation;
		}

		return createAddCompositeOperation(context);
	}

	private Operation createAddCompositeOperation(final cosmos.presenters.Operation.Context context) {
		List<String> addList = getAddList();

		if (addList.size() <= 0)
			return null;

		CompositeOperation compositeOperation = new CompositeOperation(context);
		compositeOperation.setLabel(services.getTranslatorService().translate(TranslatorService.Label.ADD_COMPOSITE_NODE));
		compositeOperation.inject(services);

		for (String add : addList) {
			final AddNodeOperation addOperation = new AddNodeOperation(context, getEntity(), add);
			addOperation.inject(services);
			compositeOperation.addChild(addOperation);
		}

		return compositeOperation;
	}

	private Operation createDeleteNodesOperation(final SetIndexDisplay display) {

		DeleteNodesOperation operation = new DeleteNodesOperation(getOperationContext(display), new DeleteNodesOperation.NodeSelection() {
			@Override
			public Node[] get() {
				List<Node> selection = new MonetList<>();

				for (NodeIndexEntry indexEntry : display.getSelection())
					selection.add(indexEntry.getEntity());

				return selection.toArray(new Node[selection.size()]);
			}
		});

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
				display.unSelectAll();
				display.reloadPage();
			}

			@Override
			public void executeFailed(ServerError details) {
			}
		});
		operation.disable();

		return operation;
	}

	private cosmos.presenters.Operation.Context getOperationContext(final SetIndexDisplay setIndexDisplay) {
		return new cosmos.presenters.Operation.Context() {
			@Override
			public Presenter getCanvas() {
				return getCanvasDisplay(setIndexDisplay);
			}

			@Override
			public cosmos.presenters.Operation getReferral() {
				return getVisitingDisplayOperation();
			}
		};
	}

	private void showNode(final cosmos.presenters.Operation.Context context, Node entity) {
		ViewList<NodeView> views = entity.getViews();
		Operation operation;

		if (context.getCanvas() instanceof RootDisplay)
			operation = new ShowNodeOperation(context, entity, views.getDefaultView());
		else
			operation = new ShowNodeViewOperation(context, entity, views.getDefaultView());

		operation.inject(services);
		operation.execute();
	}

	private List<String> getAddList() {

		List<String> viewAddList = getViewAddList();
		if (viewAddList != null && viewAddList.size() > 0)
			return viewAddList;

		List<String> entityAddList = getEntityAddList();
		if (entityAddList != null)
			return entityAddList;

		return new MonetList<>();
	}

	private Display getCanvasDisplay(SetIndexDisplay setIndexDisplay) {
		ViewDefinition.Design design = getDesign();
		Display display;

		if (design == ViewDefinition.Design.DOCUMENT)
			display = setIndexDisplay;
		else
			display = SetViewDisplay.this.getRootDisplay();

		return display;
	}

	protected abstract List<String> getEntityAddList();

	protected List<String> getViewAddList() {
		Dictionary dictionary = services.getSpaceService().load().getDictionary();
		List<Ref> result = new MonetList<>();
		SetView view = getView();
		SetViewDefinition definition = view.getDefinition();

		if (definition != null && definition.getSelect() != null)
			result = definition.getSelect().getNode();

		return dictionary.getDefinitionCodes(result);
	}

	public interface Hook extends NodeViewDisplay.Hook {
		void loading();
		void update();
	}

	protected void notifyLoading() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.loading();
			}
		});
	}

	protected void notifyUpdate() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.update();
			}
		});
	}

	protected void notifyFailure(String entity, String error) {
		Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not get entity " + entity + " " + error);
	}

}
