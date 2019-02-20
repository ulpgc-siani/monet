package client.presenters.displays;

import client.core.model.Entity;
import client.core.model.View;
import client.presenters.operations.EntityOperation;
import client.presenters.operations.Operation;
import client.services.Services;
import cosmos.presenters.Presenter;

import java.util.ArrayList;
import java.util.List;

public class ExplorationDisplay extends Display {
	private ExplorationItemDisplay activeOperation;
	private ExplorationItemDisplay rootOperation;

	public static final Type TYPE = new Type("ExplorationDisplay", Display.TYPE);

	public ExplorationDisplay() {
		super();
	}

	public ExplorationItemDisplay get(int position) {
		return (ExplorationItemDisplay) this.getChild(position);
	}

	public ExplorationItemDisplay get(Operation operation) {
		return get(operation, this);
	}

	@Override
	protected void onInjectServices() {
	}

	public boolean isRoot(Operation operation) {
		ExplorationItemDisplay itemDisplay = get(operation);

		if (itemDisplay == null)
			return false;

		return itemDisplay == getRoot(get(operation));
	}

	public ExplorationItemDisplay getRoot() {
		return this.rootOperation;
	}

	public ExplorationItemDisplay getRoot(ExplorationItemDisplay explorationItemDisplay) {
		ExplorationItemDisplay root = explorationItemDisplay;

		while (root.getOwner() != null && root.getOwner() instanceof ExplorationItemDisplay)
			root = (ExplorationItemDisplay) root.getOwner();

		return root != null ? root : explorationItemDisplay;
	}

	public ExplorationItemDisplay add(Operation operation) {
		return this.add(operation, false, false);
	}

	public ExplorationItemDisplay add(Operation operation, boolean spaced) {
		return this.add(operation, false, spaced);
	}

	public ExplorationItemDisplay add(Operation operation, boolean linked, boolean spaced) {
		ExplorationItemDisplay explorationItemDisplay = new ExplorationItemDisplay(operation, linked, spaced);

		explorationItemDisplay.setAddHandler(new ExplorationItemDisplay.AddHandler() {
			@Override
			public void onAdd(ExplorationItemDisplay item) {
				notifyUpdate();
			}
		});
		explorationItemDisplay.setLinkHandler(new ExplorationItemDisplay.LinkHandler() {
			@Override
			public void onLink(ExplorationItemDisplay item) {
				notifyLink(item);
			}

			@Override
			public void onUnLink(ExplorationItemDisplay item) {
				notifyUnLink(item);

				if (!isItemInActiveOperation(item)) {
					item.remove();
					notifyUpdate();
				}
			}
		});

		this.addChild(explorationItemDisplay);

		notifyUpdate();

		return explorationItemDisplay;
	}

	public ExplorationItemDisplay addDeeply(Operation[] operations) {
		return this.addDeeply(operations, null, false, false);
	}

	public ExplorationItemDisplay addDeeply(Operation[] operations, ExplorationItemDisplay rootItemDisplay) {
		return this.addDeeply(operations, rootItemDisplay, false, false);
	}

	public ExplorationItemDisplay addDeeply(Operation[] operations, ExplorationItemDisplay rootItemDisplay, boolean linked, boolean spaced) {
		ExplorationItemDisplay itemDisplay = null;

		for (Operation operation : operations) {
			ExplorationItemDisplay currentItemDisplay = get(operation);

			if (currentItemDisplay != null) {
				itemDisplay = currentItemDisplay;
				continue;
			}

			if (itemDisplay == null)
				itemDisplay = rootItemDisplay!=null?rootItemDisplay.add(operation, linked):add(operation, linked, spaced);
			else
				itemDisplay = itemDisplay.add(operation);
		}

		notifyUpdate();

		return itemDisplay;
	}

	@Override
	public void addChild(Presenter presenter) {
		if (childrenCount() == 0)
			this.activeOperation = (ExplorationItemDisplay) presenter;

		super.addChild(presenter);
	}

	public void clearChildren() {
		List<ExplorationItemDisplay> notLinkedItems = findNotLinkedItems(this);

		for (ExplorationItemDisplay notLinkedItem : notLinkedItems)
			notLinkedItem.remove();

		notifyUpdate();
	}

	private List<ExplorationItemDisplay> findNotLinkedItems(Presenter display) {
		List<ExplorationItemDisplay> result = new ArrayList<>();

		for (Presenter presenter : display) {
			if (!(presenter instanceof ExplorationItemDisplay))
				continue;

			result.addAll(findNotLinkedItems(presenter));
		}

		if (!(display instanceof ExplorationItemDisplay))
			return result;

		ExplorationItemDisplay explorationItemDisplay = ((ExplorationItemDisplay)display);

		if (!explorationItemDisplay.isLinked() && explorationItemDisplay.getOwner() instanceof ExplorationItemDisplay)
			result.add(explorationItemDisplay);

		return result;
	}

	@Override
	public Type getType() {
		return TYPE;
	}

	public ExplorationItemDisplay getActive() {
		return this.activeOperation;
	}

	public int getCount() {
		return childrenCount();
	}

	public void explore(final ExplorationItemDisplay operation) {
		activate(operation);
		operation.execute();
	}

	public void link(final Operation operation) {
		get(operation).link();
	}

	public void unLink(final Operation operation) {
		get(operation).unLink();
	}

	public void activate(final Operation operation) {
		ExplorationItemDisplay itemDisplay = get(operation);

		if (itemDisplay == null)
			return;

		activate(itemDisplay);
	}

	public void activate(final ExplorationItemDisplay explorationItemDisplay) {
		this.activeOperation = explorationItemDisplay;
		this.notifyActivate(this.activeOperation);
	}

	public void setRootOperation(ExplorationItemDisplay rootOperation) {
		this.rootOperation = rootOperation;
	}

	public boolean isItemInActiveOperation(ExplorationItemDisplay item) {
		ExplorationItemDisplay active = getActive();

		if (active == item)
			return true;

		if (isAncestorItemInActiveOperation(item, active)) return true;
        return isChildItemInActiveOperation(item, active);

    }

	public int getPosition(ExplorationItemDisplay value) {
		int pos = 0;

		for (Presenter presenter : this) {
			if (presenter == value)
				return pos;
			pos++;
		}

		return -1;
	}

	public static class ExplorationItemDisplay extends Display {
		private AddHandler addHandler;
		private LinkHandler linkHandler;
		private final Operation operation;
		private boolean linked = false;
		private boolean spaced = false;

		public static final Type TYPE = new Type("ExplorationItemDisplay", Display.TYPE);

		public ExplorationItemDisplay(Operation operation, boolean linked, boolean spaced) {
			this.operation = operation;
			this.linked = linked;
			this.spaced = spaced;
		}

		@Override
		protected void onInjectServices() {
		}

		public String getLabel() {
			return operation.getMenuLabel(linked);
		}

		public boolean isFor(Operation operation) {

			if (this.operation == operation)
				return true;

			if (this.operation instanceof EntityOperation && operation instanceof EntityOperation) {
				Entity selfEntity = ((EntityOperation) this.operation).getEntity();
				Entity entity = ((EntityOperation) operation).getEntity();
				View selfView = ((EntityOperation) this.operation).getView();
				View view = ((EntityOperation) operation).getView();

				boolean isFor = false;
				if (selfEntity != null && entity != null)
					isFor = selfEntity.equals(entity);

				if (!isFor)
					return false;

				if (selfView != null && view != null)
					isFor = selfView.equals(view);

				return isFor;
			}

			return false;
		}

		public boolean isLinked() {
			return isLinked(true);
		}

		public boolean isLinked(boolean checkChildren) {

			if (this.linked)
				return true;

            return checkChildren && hasLinkedChildren();

        }

		public boolean isSpaced() {
			return this.spaced;
		}

		private boolean hasLinkedChildren() {
			for (Presenter child : this) {
				if (child instanceof ExplorationItemDisplay) {
					boolean linked = ((ExplorationItemDisplay) child).isLinked();
					if (linked)
						return true;
				}
			}
			return false;
		}

		public void link() {
			this.linked = true;

			if (this.linkHandler != null)
				this.linkHandler.onLink(this);
		}

		public void unLink() {
			this.linked = false;

			if (this.linkHandler != null)
				this.linkHandler.onUnLink(this);
		}

		public void setAddHandler(AddHandler addHandler) {
			this.addHandler = addHandler;
		}

		public void setLinkHandler(LinkHandler linkHandler) {
			this.linkHandler = linkHandler;
		}

		public ExplorationItemDisplay get(int position) {
			return (ExplorationItemDisplay) this.getChild(position);
		}

		public ExplorationItemDisplay add(Operation operation) {
			return this.add(operation, false);
		}

		public ExplorationItemDisplay add(Operation operation, boolean linked) {
			ExplorationItemDisplay result = new ExplorationItemDisplay(operation, linked, false);

			result.setAddHandler(addHandler);
			result.setLinkHandler(linkHandler);
			this.addChild(result);

			if (this.addHandler != null)
				this.addHandler.onAdd(this);

			return result;
		}

		@Override
		public void inject(Services services) {
			super.inject(services);
			this.operation.inject(services);
		}

		@Override
		public Type getType() {
			return TYPE;
		}

		public void execute() {
			this.operation.execute();
		}

		public interface AddHandler {
			void onAdd(ExplorationItemDisplay item);
		}

		public interface LinkHandler {
			void onLink(ExplorationItemDisplay item);

			void onUnLink(ExplorationItemDisplay item);
		}
	}

	public interface Hook extends Display.Hook {
		void update();

		void activate(ExplorationItemDisplay item);

		void link(ExplorationItemDisplay item);

		void unLink(ExplorationItemDisplay item);
	}

	private ExplorationItemDisplay get(Operation operation, Presenter presenter) {

		if (presenter instanceof ExplorationItemDisplay && ((ExplorationItemDisplay) presenter).isFor(operation))
			return (ExplorationItemDisplay) presenter;

		for (Presenter child : presenter) {
			ExplorationItemDisplay result = get(operation, child);

			if (result != null)
				return result;
		}

		return null;
	}

	private void notifyUpdate() {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.update();
			}
		});
	}

	private void notifyActivate(final ExplorationItemDisplay item) {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.activate(item);
			}
		});
	}

	private void notifyLink(final ExplorationItemDisplay item) {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.link(item);
			}
		});
	}

	private void notifyUnLink(final ExplorationItemDisplay item) {
		this.updateHooks(new Notification<Hook>() {
			@Override
			public void update(Hook hook) {
				hook.unLink(item);
			}
		});
	}

	private boolean isAncestorItemInActiveOperation(ExplorationItemDisplay item, ExplorationItemDisplay active) {
		Presenter owner = active.getOwner();
		while (owner != null && owner instanceof ExplorationItemDisplay) {
			if (active == item)
				return true;
			owner = owner.getOwner();
		}
		return false;
	}

	private boolean isChildItemInActiveOperation(ExplorationItemDisplay item, ExplorationItemDisplay active) {

		if (item == active)
			return true;

		for (Presenter child : item) {
			if (!(child instanceof ExplorationItemDisplay))
				continue;

			boolean isChildItem = isChildItemInActiveOperation((ExplorationItemDisplay)child, active);
			if (isChildItem)
				return true;
		}

		return false;
	}

}
