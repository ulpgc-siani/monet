package client.core.model.types;

import client.core.model.List;

public interface CheckList extends List<Check> {

	client.core.model.types.CheckList checkedValues();
	List<String> toCheckedValues();
}
