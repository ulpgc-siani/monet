package client.presenters.displays.entity.field;

public interface IsSummationFieldDisplay extends IsFieldDisplay<String> {

	void addHook(SummationFieldDisplay.Hook hook);

}
