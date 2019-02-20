package client.presenters.displays.entity.field;

public interface IsSerialFieldDisplay extends IsFieldDisplay<String> {

	void addHook(SerialFieldDisplay.Hook hook);

}
