package org.monet.bpi;

import org.apache.commons.lang.NotImplementedException;

import java.util.HashMap;

public abstract class Schema {

	public String toJson() {
		throw new NotImplementedException();
	}

	public String toString() {
		throw new NotImplementedException();
	}

	public abstract HashMap<String, Object> getAll();

	public abstract void set(String key, Object value);

}
