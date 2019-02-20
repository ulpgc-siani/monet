package client.presenters.displays.entity.field;

public interface IsTextFieldDisplay extends IsFieldDisplay<String> {

    boolean allowHistory();
    void loadHistory();
    void loadHistory(String filter);

    int getMaxLength();
	boolean isReadonly();
	boolean minLengthWrong();

	void addHook(TextFieldDisplay.Hook hook);

}
