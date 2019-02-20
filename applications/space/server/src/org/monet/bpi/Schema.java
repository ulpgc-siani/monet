package org.monet.bpi;

import net.minidev.json.JSONValue;
import org.monet.space.kernel.utils.PersisterHelper;

import java.util.HashMap;

public abstract class Schema {

	public String toJson() {
		return JSONValue.toJSONString(this);
	}

	public String toString() {
		try {
			return PersisterHelper.save(this);
		} catch (Exception e) {
			throw new RuntimeException(String.format("Error printing schema %s: %s", e.toString()), e);
		}
	}

	public abstract HashMap<String, Object> getAll();

	public abstract void set(String key, Object value);

}
