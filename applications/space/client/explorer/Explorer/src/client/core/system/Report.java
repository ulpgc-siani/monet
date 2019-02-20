package client.core.system;

import java.util.HashMap;
import java.util.Map;

public class Report implements client.core.model.Report {
	private Map<String, Integer> values;

	public Report() {
		this(new HashMap<String, Integer>());
	}

	public Report(Map<String, Integer> values) {
		this.values = values;
	}

	@Override
	public int get(String code) {

		if (!values.containsKey(code))
			return 0;

		return values.get(code);
	}

	public void add(String code, int value) {
		values.put(code, value);
	}
}
