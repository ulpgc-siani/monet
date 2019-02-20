package org.monet.space.kernel.bpi.java.importer;

import java.util.Iterator;

public class ImportIterable<T> implements Iterable<T> {

	private ImportIterator<T> instance;
	private boolean active = true;

	public ImportIterable(ImportIterator<T> instance) {
		this.instance = instance;
	}

	@Override
	public Iterator<T> iterator() {
		if (this.active) {
			this.active = false;
			return this.instance;
		} else {
			throw new RuntimeException("Can't get more than one iterator per import.");
		}
	}

}  
