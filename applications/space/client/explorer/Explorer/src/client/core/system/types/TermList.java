package client.core.system.types;

import client.core.model.List;
import client.core.model.types.CompositeCheck;
import client.core.model.types.Term;
import client.core.system.MonetList;

public class TermList extends MonetList<client.core.model.types.Term> implements client.core.model.types.TermList {
	public TermList() {
	}

	public TermList(client.core.model.types.Term[] elements) {
		super(elements);
	}

	@Override
	public client.core.model.types.CheckList toCheckList(List<String> checkedList) {
		client.core.model.types.CheckList result = new CheckList();

		for (client.core.model.types.Term term : this)
			result.add(createCheck(term, checkedList));

		return result;
	}

	private client.core.model.types.Check createCheck(Term term, List<String> checkedList) {
		if (term.isLeaf())
            return new Check(term.getValue(), term.getLabel(), checkedList.contains(term.getValue()));
		return createCompositeCheckWithChildren((CompositeTerm)term, checkedList);
	}

	private client.core.model.types.Check createCompositeCheckWithChildren(CompositeTerm term, List<String> checkedList) {
		CompositeCheck compositeCheck;
		if (term.isSelectable())
            compositeCheck = new SuperCheck(term.getValue(), term.getLabel(), checkedList.contains(term.getValue()));
        else
            compositeCheck = new CheckCategory(term.getValue(), term.getLabel(), checkedList.contains(term.getValue()));
		compositeCheck.setChecks(children(term, checkedList));
		return compositeCheck;
	}

	private client.core.model.types.CheckList children(CompositeTerm term, List<String> checkedList) {
		final CheckList children = new CheckList();
		for (Term child : term.getTerms())
			children.add(createCheck(child, checkedList));
		return children;
	}

	@Override
	public client.core.model.types.TermList filter(String filter) {
		final TermList terms = new TermList();
		for (Term term : this) {
			if (containsFilter(filter.toLowerCase(), term.getLabel().toLowerCase()))
				terms.add(term);
		}
		return terms;
	}

    private boolean containsFilter(String filter, String label) {
        for (String s : filter.split(" ")) {
            if (s.trim().isEmpty()) continue;
            if (!label.contains(s.trim())) return false;
        }
        return true;
    }

}