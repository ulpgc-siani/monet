package client.core.system;

import client.core.model.List;
import client.core.model.NodeView;
import client.core.model.definition.entity.NodeDefinition;
import client.core.model.definition.views.NodeViewDefinition;
import client.core.model.factory.EntityFactory;

import java.util.HashMap;
import java.util.Map;

import static client.core.model.definition.entity.NodeDefinition.*;

public abstract class Node<Definition extends NodeDefinition> extends Entity<Definition> implements client.core.model.Node<Definition> {
	private Map<String, String> notes = new HashMap<>();
	private boolean isComponent;
	private boolean locked;
	protected client.core.model.List<client.core.model.Command> commands = null;
	protected client.core.model.ViewList<client.core.model.NodeView> views = null;
	private EntityFactory entityFactory;

	public Node() {
	}

	public Node(String id) {
		this(id, null, false, null);
	}

	public Node(String id, String label, boolean isComponent, Type type) {
		super(id, label);
		this.isComponent = isComponent;
		this.notes = new HashMap<>();
		this.type = type;
	}

	public void setEntityFactory(EntityFactory entityFactory) {
		this.entityFactory = entityFactory;
	}

	@Override
	public Map<String, String> getNotes() {
		return notes;
	}

	@Override
	public String getNote(String name) {
		return notes.get(name);
	}

	@Override
	public void setNotes(Map<String, String> notes) {
		this.notes = notes;
	}

	@Override
	public void addNote(String name, String value) {
		this.notes.put(name, value);
	}

	@Override
	public List<client.core.model.Command> getCommands() {

		if (commands != null)
			return commands;

		loadCommands();

		return commands;
	}

	public void setCommands(List<client.core.model.Command> commands) {
		this.commands = commands;
	}

	@Override
	public client.core.model.ViewList<client.core.model.NodeView> getViews() {

		if (views != null)
			return views;

		loadViews();

		return views;
	}

	protected abstract client.core.model.NodeView loadView(NodeViewDefinition viewDefinition);

	public void setViews(client.core.model.ViewList<client.core.model.NodeView> views) {
		this.views = views;
	}

	@Override
	public client.core.model.NodeView getView(client.core.model.Key key) {
		return getViews().get(key);
	}

	@Override
	public client.core.model.NodeView getView(String key) {
		return getViews().get(key);
	}

	@Override
	public String getDefinitionClass() {
		if (isContainer()) return Type.CONTAINER.toString();
		if (isCatalog()) return Type.CATALOG.toString();
		if (isCollection()) return Type.COLLECTION.toString();
		if (isDesktop()) return Type.DESKTOP.toString();
		if (isForm()) return Type.FORM.toString();
		return Type.NODE.toString();
	}

	@Override
	public boolean isContainer() {
		return (this instanceof Container);
	}

	@Override
	public boolean isCollection() {
		return (this instanceof Collection);
	}

	@Override
	public boolean isCatalog() {
		return (this instanceof Catalog);
	}

	@Override
	public boolean isSet() {
		return isCollection() || isCatalog();
	}

	@Override
	public boolean isForm() {
		return (this instanceof Form);
	}

	@Override
	public boolean isDesktop() {
		return (this instanceof Desktop);
	}

	@Override
	public boolean isDocument() {
		return (this instanceof Document);
	}

	@Override
	public boolean isComponent() {
		return isComponent;
	}

	public void setIsComponent(boolean isComponent) {
		this.isComponent = isComponent;
	}

	@Override
	public boolean isEditable() {
		NodeDefinition definition = getDefinition();
		return !isLocked() && !definition.isReadonly();
	}

	@Override
	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	@Override
	public client.core.model.types.Link toLink() {
		return entityFactory.createLink(getId(), getLabel());
	}

	@Override
	public String[] search(String query) {
		return new String[0];
	}

	protected void loadViews() {
		List<NodeViewDefinition> viewDefinitions = getDefinition().getViews();
		client.core.model.ViewList<client.core.model.NodeView> views = new client.core.system.ViewList<>();

		for (NodeViewDefinition viewDefinition : viewDefinitions) {
			NodeView view = loadView(viewDefinition);
			view.setDefinition(viewDefinition);
			views.add(view);
		}

		setViews(views);
	}

	private void loadCommands() {
		List<OperationDefinition> operationDefinitions = getDefinition().getOperations();
		client.core.model.List<client.core.model.Command> commands = new MonetList<>();

		for (OperationDefinition operationDefinition : operationDefinitions) {
			Command command = new Command(new Key(operationDefinition.getCode(), operationDefinition.getName()), operationDefinition.getLabel(), operationDefinition.isEnabled());
			command.setVisible(operationDefinition.isVisible());
			commands.add(command);
		}

		setCommands(commands);
	}

}
