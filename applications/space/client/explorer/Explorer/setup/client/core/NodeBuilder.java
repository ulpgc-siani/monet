package client.core;

import client.core.model.*;
import client.core.model.definition.Ref;
import client.core.model.definition.entity.*;
import client.core.model.definition.views.NodeViewDefinition;
import client.core.model.definition.views.SetViewDefinition;
import client.core.system.Document;
import client.core.system.MonetList;
import client.core.system.definition.entity.DocumentDefinition;
import client.core.system.definition.entity.FormDefinition;

public class NodeBuilder {

	public static Node buildNode(String id) {
		client.core.system.Node node = new client.core.system.Node(id, "", false, Node.Type.NODE) {
			@Override
			public boolean isEnvironment() {
				return false;
			}

			@Override
			protected NodeView loadView(NodeViewDefinition viewDefinition) {
				return null;
			}

			@Override
			public ClassName getClassName() {
				return null;
			}
		};
		fillNode(node, "", ListBuilder.EmptyNodeViewList, ListBuilder.EmptyCommandList, null);
		return node;
	}

	public static Desktop buildDesktop(String id, final String label, final String description, final ViewList<NodeView> views, final List<Command> commands) {
		client.core.system.Desktop desktop = new client.core.system.Desktop<>(id, label);
		fillNode(desktop, description, views, commands, new DesktopDefinition() {
			@Override
			public List<OperationDefinition> getOperations() {
				return null;
			}

			@Override
			public List getViews() {
				return null;
			}

			@Override
			public NodeViewDefinition getView(String key) {
				return null;
			}

			@Override
			public boolean isReadonly() {
				return false;
			}

			@Override
			public String getCode() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return description;
			}

			@Override
			public Instance.ClassName getClassName() {
				return DesktopDefinition.CLASS_NAME;
			}
		});
		return desktop;
	}

	public static Container buildContainer(String id, final String label, final String description, final ViewList<NodeView> views, List<Command> commands, final boolean isEnvironment) {
		client.core.system.Container container = new client.core.system.Container<>(id, label);
		fillNode(container, description, views, commands, new ContainerDefinition() {
			@Override
			public List<OperationDefinition> getOperations() {
				return null;
			}

			@Override
			public List getViews() {
				return null;
			}

			@Override
			public NodeViewDefinition getView(String key) {
				return null;
			}

			@Override
			public boolean isReadonly() {
				return false;
			}

			@Override
			public boolean isEnvironment() {
				return isEnvironment;
			}

			@Override
			public String getCode() {
				return "code";
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return description;
			}

			@Override
			public Instance.ClassName getClassName() {
				return ContainerDefinition.CLASS_NAME;
			}
		});
		return container;
	}

	public static Container buildContainer(String id, final String label, final String description, final ViewList<NodeView> views, final boolean isEnvironment) {
		return buildContainer(id, label, description, views, ListBuilder.EmptyCommandList, isEnvironment);
	}

	public static Container buildContainer(String id, String label) {
		return buildContainer(id, label, "", ListBuilder.EmptyNodeViewList, false);
	}

	public static Container buildContainer(String id) {
		return buildContainer(id, "");
	}

	public static Form buildForm(final String id, final String label, String description, final ViewList<NodeView> views, final Field[] fields, boolean isComponent) {
		client.core.system.Form form = new client.core.system.Form(id, label, isComponent);
		form.set(new MonetList<>(fields));
		client.core.system.definition.entity.FormDefinition definition = new FormDefinition(null, null, label, label);
		List<FieldDefinition> fieldDefinitions = new MonetList<>();

		for (Field field : fields)
			fieldDefinitions.add((FieldDefinition) field.getDefinition());

		definition.setFields(fieldDefinitions);

		fillNode(form, description, views, ListBuilder.EmptyCommandList, definition);
		return form;
	}

	public static Form buildForm(final String id, final String label, String description, boolean isComponent) {
		return buildForm(id, label, description, ListBuilder.buildNodeViewList(new FormView[0]), new Field[0], isComponent);
	}

	public static Form buildForm(final String id, final String label, boolean isComponent) {
		return buildForm(id, label, "", isComponent);
	}

	public static Collection buildCollection(final String id, final String label, String description, final ViewList<NodeView> views, final Index index, final List<Command> commands, final List<Ref> addList, final List<Filter> filters, final List<Order> orders, boolean isComponent) {
		client.core.system.Collection collection = new client.core.system.Collection<>(id, label, isComponent);
		fillNode(collection, description, views, commands, new CollectionDefinition() {
			@Override
			public List<OperationDefinition> getOperations() {
				return null;
			}

			@Override
			public List getViews() {
				return null;
			}

			@Override
			public SetViewDefinition getView(String key) {
				return null;
			}

			@Override
			public boolean isReadonly() {
				return false;
			}

			@Override
			public AddDefinition getAdd() {
				return new AddDefinition() {
					@Override
					public List<Ref> getNode() {
						return addList;
					}
				};
			}

			@Override
			public String getCode() {
				return null;
			}

			@Override
			public String getName() {
				return null;
			}

			@Override
			public String getLabel() {
				return label;
			}

			@Override
			public String getDescription() {
				return label;
			}

			@Override
			public Instance.ClassName getClassName() {
				return CollectionDefinition.CLASS_NAME;
			}
		});

		collection.setIndex(index);

		for (NodeView view : views) {
			client.core.model.Key key = view.getKey();
			collection.setViewFilters(key.getCode(), filters);
			collection.setViewOrders(key.getCode(), orders);
		}

		return collection;
	}

	public static Collection buildCollection(final String id, final String label, String description, boolean isComponent) {
		return buildCollection(id, label, description, ListBuilder.buildNodeViewList(new CollectionView[0]), null, ListBuilder.buildCommandList(new Command[0]), new MonetList<Ref>(), new MonetList<Filter>(), new MonetList<Order>(), isComponent);
	}

	public static Collection buildCollection(String id, String label, boolean isComponent) {
		return buildCollection(id, label, "", isComponent);
	}

	private static void fillNode(client.core.system.Node node, String description, ViewList<NodeView> views, List<Command> commands, NodeDefinition definition) {

		for (NodeView view : views) {
			if (view.getScope() == null)
				view.setScope(node);
		}

		node.setDescription(description);
		node.setViews(views);
		node.setCommands(commands);
		node.setDefinition(definition);
	}

	public static Document buildDocument(String id, String label, String description, ViewList<NodeView> views) {
		final Document document = new Document(id, label);
		fillNode(document, description, views, new MonetList<Command>(), new DocumentDefinition());
		return document;
	}
}
