package client.core.model;

public interface TaskShortcutView extends TaskView {

	ClassName CLASS_NAME = new ClassName("TaskShortcutView");

	ClassName getClassName();
	Node getShortcut();

}
