package org.monet.space.explorer.model;

import org.monet.space.kernel.model.Entity;

public interface IndexEntry<T extends Entity> {
	public String getLabel();
	public T getEntity();
}
