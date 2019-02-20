package org.monet.bpi;

import org.monet.bpi.types.Term;

public interface BehaviorSource {

	/**
	 * Event called when source is initialized with terms
	 */
	public void onPopulated();

	/**
	 * Event called when source is synchronized with other source declared in federation
	 */
	public void onSynchronized();

	/**
	 * Event called when new term is added to source
	 */
	public void onTermAdded(Term term);

	/**
	 * Event called when term is modified
	 */
	public void onTermModified(Term term);

}