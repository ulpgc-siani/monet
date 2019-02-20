package client.presenters.displays;

import client.core.model.*;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.definition.entity.IndexDefinition;
import client.core.system.MonetList;
import client.presenters.displays.view.ViewDisplay;
import client.services.NotificationService;
import client.services.callback.DefinitionCallback;
import cosmos.presenters.Operation;
import cosmos.presenters.Presenter;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static cosmos.gwt.utils.ArrayUtils.union;

public abstract class IndexDisplay<T extends Entity, E extends IndexEntry> extends Display {
	private final T entity;
	protected final Handler handler;
	private String title = null;
	private int currentPage = 1;
	private int pageSize;
	private String condition = null;
	private Map<String, Filter> injectedFilters = new HashMap<>();
	private Map<String, Filter> selectedFilters = new HashMap<>();
	private OrderList injectedOrders = new client.core.system.OrderList();
	private OrderList selectedOrders = new client.core.system.OrderList();
	private List<E> selectedEntries = new MonetList<>();
	private boolean loading = false;
	protected E active;
    private boolean executingSearch = false;
    private String pendingCondition;

	protected static final int PAGE_SIZE = 10;
	public static final Type TYPE = new Type("IndexDisplay", Display.TYPE);

	public IndexDisplay() {
		this(null, null);
	}

	public IndexDisplay(Handler handler) {
		this(null, handler);
	}

	public IndexDisplay(T entity) {
		this(entity, null);
	}

	public IndexDisplay(T entity, Handler handler) {
		this(entity, handler, PAGE_SIZE);
	}

	public IndexDisplay(T entity, Handler handler, int pageSize) {
		this.entity = entity;
		this.handler = handler;
		this.pageSize = pageSize;
	}

	@Override
	public void addChild(Presenter presenter) {
		super.addChild(presenter);

		if (presenter instanceof ViewDisplay)
			notifyShowEntityView((ViewDisplay) presenter);
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void clear() {
		notifyClear();
	}

	public void loadPage(int page) {
		if (!check(page) || loading)
			return;

		currentPage = page;
		notifyLoadingPage();
		loadEntries();
	}

	public void reloadPage(int start, int limit) {
		loadEntries(start, limit);
	}

	public void reloadPage() {
		clear();
		loadEntries();
	}

	public void firstPage() {
		notifyClear();
		loadPage(0);
	}

	public void nextPage() {
		loadPage(currentPage + 1);
	}

	public int getPagesCount() {
		int count = getEntriesCount();

		if (count <= 0)
			return 1;

		return Math.round(count / pageSize) + ((count % pageSize) > 0 ? 1 : 0);
	}

	public int getPageSize() {
		return pageSize;
	}

	public List<Operation> getOperations() {
		List<Operation> result = new MonetList<>();

		for (Object child : this) {
			if (child instanceof Operation)
				result.add((Operation)child);
		}

		return result;
	}

	public void add(E entry) {
		setEntriesCount(getEntriesCount() + 1);
		notifyAddPageEntry(entry);
        subscribeToNotificationService(entry);
	}

	public void delete(E entry) {
		if (handler != null)
			handler.onDelete(this, entry);
		notifyDeletePageEntry();
	}

	public E getActive() {
		return active;
	}

	public List<Order> getSelectedOrders() {
		return new MonetList<>(union(toOrderArray(selectedOrders), toOrderArray(injectedOrders)));
	}

	private Order[] toOrderArray(OrderList selectedOrders) {
		return selectedOrders.toArray(new Order[selectedOrders.size()]);
	}

	public void activate(E entry) {
		active = entry;

		if (handler != null)
			handler.onActivate(this, entry);
	}

	public List<E> getSelection() {
		return selectedEntries;
	}

	public int getSelectionCount() {
		return selectedEntries.size();
	}

	public boolean canSearch() {
		return getEntriesCount() > PAGE_SIZE || (getCondition() != null && !getCondition().isEmpty() && !getCondition().trim().isEmpty());
	}

	public abstract boolean isEditable();

	public abstract client.core.model.List<Filter> getFilters();

	public abstract client.core.model.List<Order> getOrders();

	public void select(E entry) {
		this.selectedEntries.add(entry);
		notifySelectedEntries();
		if (handler != null)
			handler.onSelect(this, entry);
	}

	public void select(Filter filter) {
		selectedFilters.put(filter.getName(), filter);
		notifySelectedOptions(filter);
		notifyClear();
		loadPage(0);
	}

	public void select(Order order) {
		selectedOrders.add(order);
		notifyClear();
		loadPage(0);
	}

	public void unSelect(E entry) {
		this.selectedEntries.remove(entry);
		notifySelectedEntries();
		if (handler != null)
			handler.onUnSelect(this, entry);
	}

	public void unSelectAll() {
		this.selectedEntries.clear();
		notifySelectedEntries();
		if (handler != null)
			handler.onUnSelectAll(this);
	}

	public void unSelectAllFilter(Filter filter) {
		this.selectedFilters.remove(filter.getName());
		filter.setOptions(new MonetList<Filter.Option>());
		notifySelectedOptions(filter);
		notifyClear();
		loadPage(0);
	}

	public void unSelectAllOrders() {
		this.selectedOrders.clear();
		notifyClear();
		loadPage(0);
	}

	public void setCondition(String condition) {
        if (executingSearch) {
            pendingCondition = condition;
            return;
        }
        executingSearch = true;
		this.condition = condition;
		notifyClear();
		loadPage(0);
	}

	public void inject(Filter filter) {
		this.injectedFilters.put(filter.getName(), filter);
	}

	public void inject(Order order) {
		this.injectedOrders.add(order);
	}

	public abstract void loadFilterOptions(final Filter filter, String condition);

	protected void loadDefinition(String index, DefinitionCallback<EntityDefinition> callback) {
		services.getSpaceService().loadDefinition(index, IndexDefinition.CLASS_NAME, callback);
	}

	protected T getEntity() {
		return entity;
	}

	protected String getCondition() {
		return condition;
	}

	protected List<Filter> getSelectedFilters() {
		Map<String, Filter> result = new HashMap<>(selectedFilters);

		for (String filterName : injectedFilters.keySet()) {
			if (result.containsKey(filterName)) {
				List<Filter.Option> resultFilterOptions = result.get(filterName).getOptions();
				List<Filter.Option> injectedFilterOptions = injectedFilters.get(filterName).getOptions();
				result.get(filterName).setOptions(new MonetList<>(union(toArray(resultFilterOptions), toArray(injectedFilterOptions))));
			}
			else
				result.put(filterName, injectedFilters.get(filterName));
		}

		return new MonetList<>(result.values());
	}

	private Filter.Option[] toArray(List<Filter.Option> list) {
		return list.toArray(new Filter.Option[list.size()]);
	}

	public abstract int getEntriesCount();
	public abstract void setEntriesCount(int count);

	protected abstract void updateEntriesCount(int count);
	protected abstract void loadEntries(int start, int limit);
	protected abstract void loadEntry(Entity entity);

	protected void notifyShowEntityView(final ViewDisplay entityViewDisplay) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.entityView(entityViewDisplay);
			}
		});
	}

	protected void notifyClear() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.clear();
			}
		});
	}

	protected void notifyLoadingPage() {
		loading = true;
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.loadingPage();
			}
		});
	}

	protected void notifyPage(final List<E> entries) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.page(new Page() {
					@Override
					public int getId() {
						return currentPage;
					}

					@Override
					public List<E> getEntries() {
						return entries;
					}
				});
				loading = false;
			}
		});
	}

	protected void notifyPagesCount(final int count) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.pagesCount(count);
			}
		});
	}

	protected void notifyAddPageEntry(final E entry) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.pageEntryAdded(entry);
			}
		});
	}

	protected void notifyDeletePageEntry() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.pageEntryDeleted();
			}
		});
	}

	protected void notifyUpdatePageEntry(final E entry) {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.pageEntryUpdated(entry);
			}
		});
	}

	protected void notifyPageFailure() {
		Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not load list" + (entity != null ? " " + entity.getId() + " with page " + currentPage : ""));
		loading = false;
        executingSearch = false;
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.pageFailure();
			}
		});
	}

	protected void notifyLoadingOptions() {
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.loadingOptions();
			}
		});
	}

	protected void notifyOptions(final Filter filter, final List<Filter.Option> options) {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.options(filter, options);
			}
		});
	}

	protected void notifySelectedEntries() {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.selectEntries(selectedEntries);
			}
		});
	}

	protected void notifySelectedOptions(final Filter filter) {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.selectOptions(filter);
			}
		});
	}

	protected void notifyFilterOptionsFailure(Filter filter) {
		Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not load list" + (entity != null ? " " + entity.getId() + " filter " + filter.getLabel() + " options ":""));
		updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.pageFailure();
			}
		});
	}

	protected void notifyIndexFailure(String index, String error) {
		Logger.getLogger("ApplicationLogger").log(Level.SEVERE, "Could not get index " + index + " " + error);
	}

	protected void successCallback(List<E> result) {
		updateEntriesCount(result.getTotalCount());
        for (E entry : result)
            subscribeToNotificationService(entry);
        notifyPage(result);
        loading = false;
        executingSearch = false;

        if (pendingCondition != null && !pendingCondition.equals(condition))
			setCondition(pendingCondition);

        pendingCondition = null;
	}

    private void subscribeToNotificationService(final E entry) {
        services.getNotificationService().registerListener(new NotificationService.UpdateNodeListener() {
            @Override
            public void notify(Node node) {
                if (node.equals(entry.getEntity()))
                    loadEntry(node);
            }
        });
    }

    private boolean check(int page) {
		return page >= 0 && page < getPagesCount();
	}

	private void loadEntries() {
		loadEntries(getStart(), getLimit());
	}

	private int getStart() {
		return currentPage * pageSize;
	}

	private int getLimit() {
		return pageSize;
	}

	public interface Hook<E extends IndexEntry> extends EntityDisplay.Hook {
		void clear();
		void loadingPage();
		void page(Page<E> page);
		void pagesCount(int count);
		void pageEntryAdded(E entry);
		void pageEntryDeleted();
		void pageFailure();
		void pageEntryUpdated(E entry);
		void entityView(ViewDisplay entityViewDisplay);
		void selectEntries(List<E> entries);
		void selectOptions(Filter filter);
		void loadingOptions();
		void options(Filter filter, List<Filter.Option> options);
	}

	public interface Page<E extends IndexEntry> {
		int getId();
		List<E> getEntries();
	}

	public interface Handler<L extends IndexDisplay, E extends IndexEntry> {
		void onActivate(L display, E entry);
		void onSelect(L display, E entry);
		void onDelete(L display, E entry);
		void onUnSelect(L display, E entry);
		void onUnSelectAll(L display);
	}

}
