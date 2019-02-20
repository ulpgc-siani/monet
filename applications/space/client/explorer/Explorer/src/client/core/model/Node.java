package client.core.model;

import client.core.model.definition.entity.NodeDefinition;
import client.core.model.types.Link;

import java.util.Map;

public interface Node<Definition extends NodeDefinition> extends Entity<Definition>, Searchable, Showable {

	ClassName CLASS_NAME = new ClassName("Node");

	String getLabel();
	void setLabel(String label);

	Map<String, String> getNotes();
	String getNote(String name);
	void setNotes(Map<String, String> notes);
	void addNote(String name, String value);

	List<Command> getCommands();

	<T extends NodeView> ViewList<T> getViews();
	<T extends NodeView> T getView(Key key);
	<T extends NodeView> T getView(String key);

	String getDefinitionClass();
	boolean isContainer();
	boolean isCollection();
    boolean isCatalog();
	boolean isSet();
	boolean isForm();
    boolean isDesktop();
    boolean isDocument();
    boolean isComponent();
	boolean isEnvironment();

	boolean isEditable();
	boolean isLocked();

	Link toLink();

}
