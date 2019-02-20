package client.core.model.types;

import client.core.model.Instance;
import client.core.model.List;

public interface TermList extends List<Term> {
	Instance.ClassName CLASS_NAME = new Instance.ClassName("TermList");

	CheckList toCheckList(List<String> checkedList);
	TermList filter(String filter);
}