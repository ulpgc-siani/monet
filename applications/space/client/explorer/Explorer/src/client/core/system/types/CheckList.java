package client.core.system.types;

import client.core.model.List;
import client.core.model.types.Check;
import client.core.system.MonetList;

public class CheckList extends MonetList<Check> implements client.core.model.types.CheckList {

	public CheckList() {
	}

	public CheckList(Check[] elements) {
		super(elements);
	}

	@Override
	public client.core.model.types.CheckList checkedValues() {
		final client.core.model.types.CheckList result = new CheckList();
		for (Check check : this)
			if (check.isChecked())
				result.add(check);
		return result;
	}

	@Override
	public List<String> toCheckedValues() {
		List<String> result = new MonetList<>();
		for (Check check : checkedValues())
			result.add(check.getValue());
		return result;
	}
}
