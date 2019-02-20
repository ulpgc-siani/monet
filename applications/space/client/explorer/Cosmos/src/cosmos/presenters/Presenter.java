package cosmos.presenters;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public abstract class Presenter implements Iterable<Presenter> {

	private boolean existsBody = false;
	private Presenter owner;
    private final List<Presenter> children = new ArrayList<>();
	private final List<Hook> hookList = new ArrayList<>();
	private RootDisplay rootDisplay;

	public abstract Type getType();

    public Presenter getOwner() {
        return owner;
    }

    private void setOwner(Presenter owner) {
        if (this.owner != null) this.owner.deleteFromChildrenList(this);
        this.owner = owner;
        if (this.owner != null) this.owner.addToChildrenList(this);
    }

	public int childrenCount() {
		return children.size();
	}

	public Presenter getChild(int i) {
		return children.get(i);
	}

	public <T extends Presenter> T getOwner(Type type) {
		Presenter owner = this.getOwner();

		while (owner != null && !owner.is(type))
			owner = owner.getOwner();

		return (T)owner;
	}

	public <T extends Presenter> T getChild(Type type) {
		for (Presenter child : this) {
			if (child.is(type))
				return (T) child;
		}
		return null;
	}

	public Presenter getChildOfSameType(Type type) {
		for (Presenter child : this) {
			if (child.getType().equals(type))
				return child;
		}
		return null;
	}

	public boolean existsChild(Type type) {
		return getChild(type) != null;
	}

	public boolean existsChildOfSameType(Type type) {
		return getChildOfSameType(type) != null;
	}

	public void clear() {
		children.clear();
	}

	@Override
	public Iterator<Presenter> iterator() {
		return children.iterator();
	}

    public void addChild(Presenter presenter) {
        presenter.setOwner(this);
	    if (!existsBody) return;
		propagateBodyExists(presenter);
	    propagatePresenterAdded(presenter);
    }

	public void addChildren(List<Presenter> presenters) {
		for (Presenter presenter : presenters)
			this.addChild(presenter);
	}

	private void propagateBodyExists(Presenter presenter) {
		presenter.existsBody = true;
		for (Presenter child : presenter)
			propagateBodyExists(child);
	}

	private void propagatePresenterAdded(Presenter presenter) {
		presenterAdded(presenter);
		for (Presenter child : presenter)
			propagatePresenterAdded(child);
	}

	public void remove() {
		for (Presenter child : this) presenterRemoved(child);
		if (!existsBody) return;
		presenterRemoved(this);
		setOwner(null);
	}

	public void removeChild(Type type) {
		Presenter presenter = this.getChild(type);
		if (presenter == null) return;
		presenterRemoved(presenter);
		presenter.setOwner(null);
	}

	public void removeChild(Presenter child) {
		if (child == null) return;
		presenterRemoved(child);
		child.setOwner(null);
	}

	public void addHook(Hook hook) {
		hookList.add(hook);
	}

	public void updateHooks(Notification notification) {
		for (Hook hook : hookList)
			notification.update(hook);
	}

	private void addToChildrenList(Presenter presenter) {
		this.children.add(presenter);
	}

	private void deleteFromChildrenList(Presenter presenter) {
		this.children.remove(presenter);
	}

    protected void presenterAdded(Presenter presenter) {
        if (owner == null) return;
        owner.presenterAdded(presenter);
    }

    public void presenterRemoved(Presenter presenter) {
        if (owner == null) return;
        owner.presenterRemoved(presenter);
    }

	public boolean is(Type presenterType) {
		return this.getType().is(presenterType);
	}

	public void makeBody() {
		presenterAdded(this);
		for (Presenter child : this)
			child.makeBody();
		this.existsBody = true;
	}

	public RootDisplay getRootDisplay() {
		return this.rootDisplay;
	}

	public void setRootDisplay(RootDisplay rootDisplay) {
		this.rootDisplay = rootDisplay;
	}

	public static class Type {
		private final String name;
		private final Type parent;

		private Type(String name) {
			this(name,null);
		}

		public Type(String name, Type parent) {
			this.name = name;
			this.parent = parent;
		}

		public boolean is(Type type) {
			if (this.name.equalsIgnoreCase(type.name)) return true;
			if (this.parent == null) return false;
			return this.parent.is(type);
		}

		@Override
		public boolean equals(Object type) {
			if (!(type instanceof Type)) return false;
			return this.name.equalsIgnoreCase(((Type)type).name);
		}

		@Override
		public String toString() {
			return this.name;
		}
	}

	public static final Type TYPE = new Type("Presenter");

	public interface Hook {
    }

	public interface Notification<T extends Hook> {
		void update(T hook);
	}

}
