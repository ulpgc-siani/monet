package client.presenters.displays;

import client.core.model.*;
import client.core.model.definition.Ref;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.definition.entity.IndexDefinition;
import client.core.model.definition.entity.field.LinkFieldDefinition;
import client.core.model.definition.views.IndexViewDefinition;
import client.core.system.MonetList;
import client.presenters.displays.view.ViewDisplay;
import client.services.IndexService;
import client.services.TranslatorService;
import client.services.callback.DefinitionCallback;
import client.services.callback.FilterOptionsCallback;
import client.services.callback.NodeIndexEntriesCallback;

public class LinkFieldIndexDisplay extends IndexDisplay<Index, NodeIndexEntry> {

	protected static final int MAX_HISTORY_ITEMS = 5;
	private static final int MIN_LENGTH_FOR_SEARCHING = 2;
	private LinkFieldDefinition linkFieldDefinition;
	private int entriesCount = -1;

	public static final Type TYPE = new Type("LinkFieldIndexDisplay", IndexDisplay.TYPE);
	private List<Header> header = new MonetList<>();
	private int lastConditionLength = 0;

	public LinkFieldIndexDisplay(final LinkFieldDefinition linkFieldDefinition, final Index index, Handler handler) {
		super(index, handler);
		this.linkFieldDefinition = linkFieldDefinition;
	}

	@Override
	protected void onInjectServices() {
		loadDefinition(getFieldSourceDefinition().getIndex(), new DefinitionCallback<EntityDefinition>() {
			@Override
			public void success(EntityDefinition object) {
				saveHeaders((IndexDefinition) object);
				updateHooks(new Notification<Hook>() {
					@Override
					public void update(Hook hook) {
						hook.headers();
					}
				});
			}

			@Override
			public void failure(String error) {
				notifyIndexFailure(getFieldSourceDefinition().getIndex(), error);
			}
		});
	}

	public List<Header> getHeaders() {
		return header;
	}

	public void loadFirstPage() {
		firstPage();
	}

	@Override
	public boolean isEditable() {
		return false;
	}

	@Override
	public List<Filter> getFilters() {
		return new MonetList<>();
	}

	@Override
	public List<Order> getOrders() {
		return null;
	}

	public void deactive() {
		active = null;
	}

	@Override
	public void activate(NodeIndexEntry entry) {
		if (!entry.equals(active))
			super.activate(entry);
	}

	@Override
	public void loadFilterOptions(final Filter filter, String condition) {
        FilterOptionsCallback callback = new FilterOptionsCallback() {
			@Override
			public void success(List<Filter.Option> options) {
				notifyOptions(filter, options);
			}

			@Override
			public void failure(String error) {
				notifyFilterOptionsFailure(filter);
			}
		};

		if (condition == null || condition.isEmpty())
			services.getIndexService().getFilterOptions(getEntity(), createScope(), filter, callback);
		else
			services.getIndexService().searchFilterOptions(getEntity(), createScope(), filter, condition, callback);
	}

	@Override
	public int getEntriesCount() {
		return entriesCount;
	}

	@Override
	public void setEntriesCount(int count) {
		this.entriesCount = count;
	}

	@Override
	protected void updateEntriesCount(int count) {
		if (count == entriesCount)
			return;

		this.entriesCount = count;
		notifyPagesCount(getPagesCount());
	}

	@Override
	protected void loadEntries(int start, int limit) {
		if (getCondition() == null)
			services.getIndexService().getEntries(getEntity(), createScope(), getSelectedFilters(), getSelectedOrders(), start, limit, getEntriesCallback());
		else
			services.getIndexService().searchEntries(getEntity(), createScope(), getCondition(), getSelectedFilters(), getSelectedOrders(), start, limit, searchEntriesCallback());
	}

	@Override
	public void setCondition(String condition) {
		if (getCondition() != null && condition != null && getCondition().equals(condition)) return;
		if (!conditionIsValid(condition)) {
			if (lastConditionLength == 0) return;
			removeCondition();
		} else {
			lastConditionLength = condition.length();
			super.setCondition(condition);
		}
	}

	public boolean allowHistory() {
		return linkFieldDefinition.allowHistory();
	}

	public void loadHistory() {
		services.getIndexService().getHistory(getEntity(), createScope(), linkFieldDefinition.getAllowHistory().getDataStore(), 0, MAX_HISTORY_ITEMS, createHistoryCallback());
	}

	public void loadHistory(String filter) {
		services.getIndexService().searchHistory(getEntity(), createScope(), linkFieldDefinition.getAllowHistory().getDataStore(), filter, 0, MAX_HISTORY_ITEMS, createHistoryCallback());
	}

	private NodeIndexEntriesCallback createHistoryCallback() {
		return new NodeIndexEntriesCallback() {
			@Override
			public void success(List<NodeIndexEntry> history) {
				notifyHistory(history);
			}

			@Override
			public void failure(String error) {

			}
		};
	}

	private void notifyHistory(final List<NodeIndexEntry> history) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.history(history);
			}
		});
	}

	private void removeCondition() {
		lastConditionLength = 0;
		super.setCondition(null);
	}

	private boolean conditionIsValid(String condition) {
		if (condition == null || condition.isEmpty() || condition.trim().isEmpty())
			return false;
		return condition.length() > MIN_LENGTH_FOR_SEARCHING;
	}

	private NodeIndexEntriesCallback getEntriesCallback() {
		return new NodeIndexEntriesCallback() {
                @Override
                public void success(List<NodeIndexEntry> result) {
					successCallback(result);
                }

                @Override
                public void failure(String error) {
                    notifyPageFailure();
                }
            };
	}

	private NodeIndexEntriesCallback searchEntriesCallback() {
		return new NodeIndexEntriesCallback() {
			@Override
			public void success(List<NodeIndexEntry> result) {
				successCallback(result);
			}

			@Override
			public void failure(String error) {
				notifyPageFailure();
			}
		};
	}

	@Override
	protected void loadEntry(Entity entity) {
	}

	private IndexService.Scope createScope() {
		return new IndexService.FieldScope() {
			@Override
			public String getIndexView() {
				return LinkFieldIndexDisplay.this.getIndexView((IndexDefinition) getEntity().getDefinition()).getCode();
			}

			@Override
			public String getSingleton() {
				return linkFieldDefinition.getSource().getCollection();
			}

			@Override
			public LinkFieldDefinition getFieldDefinition() {
				return linkFieldDefinition;
			}
		};
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	private void saveHeaders(IndexDefinition definition) {
		for (Ref reference : getIndexView(definition).getAttributes())
			header.add(new Header(getAttributeForValue(definition, reference.getValue())));
	}

	private IndexDefinition.ReferenceDefinition.AttributeDefinition getAttributeForValue(IndexDefinition definition, String value) {
		return definition.getReference().getAttribute(value);
	}

	private IndexViewDefinition getIndexView(IndexDefinition indexDefinition) {
		final String view = getFieldSourceDefinition().getView();
		if (view.isEmpty())
			return indexDefinition.getDefaultView();
		return indexDefinition.getView(view);
	}

	private LinkFieldDefinition.SourceDefinition getFieldSourceDefinition() {
		return linkFieldDefinition.getSource();
	}

	public String getIndexErrorMessage() {
		return  services.getTranslatorService().translate(TranslatorService.ErrorLabel.INDEX_OPTIONS);
	}

	public static abstract class Hook implements IndexDisplay.Hook<NodeIndexEntry> {
		public abstract void headers();

		public abstract void history(List<NodeIndexEntry> entries);

		@Override
		public void pagesCount(int count) {
		}

		@Override
		public void pageEntryAdded(NodeIndexEntry entry) {
		}

		@Override
		public void pageEntryDeleted() {
		}

		@Override
		public void pageEntryUpdated(NodeIndexEntry entry) {
		}

		@Override
		public void entityView(ViewDisplay entityViewDisplay) {
		}

		@Override
		public void selectEntries(List<NodeIndexEntry> entries) {
		}

		@Override
		public void selectOptions(Filter filter) {
		}

		@Override
		public void loadingOptions() {
		}
	}

	public static abstract class Handler implements IndexDisplay.Handler<LinkFieldIndexDisplay, NodeIndexEntry> {
		@Override
		public void onSelect(LinkFieldIndexDisplay display, NodeIndexEntry entry) {
		}

		@Override
		public void onDelete(LinkFieldIndexDisplay display, NodeIndexEntry entry) {
		}

		@Override
		public void onUnSelect(LinkFieldIndexDisplay display, NodeIndexEntry entry) {
		}

		@Override
		public void onUnSelectAll(LinkFieldIndexDisplay display) {
		}
	}

	public class Header {
		private final IndexDefinition.ReferenceDefinition.AttributeDefinition definition;
		private final client.core.model.Filter filter;

		public Header(IndexDefinition.ReferenceDefinition.AttributeDefinition definition) {
			this.definition = definition;
			this.filter = getEntityFactory().createFilter(definition.getCode(), definition.getLabel());
		}

		public String getCode() {
			return definition.getCode();
		}

		public String getLabel() {
			return definition.getLabel();
		}

		public Filter getFilter() {
			return filter;
		}

		public boolean isFilterable() {
			return !definition.is(IndexDefinition.ReferenceDefinition.AttributeDefinition.Type.PICTURE);
		}
	}
}
