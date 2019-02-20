package client.presenters.displays.entity.field;

import client.core.model.types.Term;

public interface IsSelectFieldDisplay extends IsFieldDisplay<Term> {

    Term createTerm(String value, String label);

	void loadOptions();
	void loadOptions(String filter);
    boolean isTermSelected(Term option);
    boolean isEmbedded();
	boolean showCode();

    boolean allowHistory();
    void loadHistory();
    void loadHistory(String filter);

    boolean allowOther();
    boolean allowSearch();
    String getValueForTermOther();
    String getValueAsString();
    String getValueFormatted();

    void nextPage();
    void reloadPage();
}
