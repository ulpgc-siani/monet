package client.core.system;

import client.core.model.View;

public class ViewList<T extends View> extends MonetList<T> implements client.core.model.ViewList<T> {

	public ViewList() {
	}

	public ViewList(T[] elements) {
		super(elements);
	}

	@Override
	public final T getDefaultView() {

		for (T view : this)
			if (view.isDefault())
				return view;

		if (size() > 0)
			return get(0);

		return null;
	}

	@Override
	public void update(T view) {
		set(indexOf(view), view);
	}

}
