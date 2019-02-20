package client.services.http.builders;

import client.core.model.*;
import client.core.model.definition.views.NodeViewDefinition;
import client.core.model.fields.*;
import client.core.model.types.*;
import client.core.system.MonetList;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.definition.entity.EntityDefinitionBuilder;
import client.services.http.builders.fields.*;
import client.services.http.builders.types.CompositeBuilder;
import client.services.http.builders.types.LinkBuilder;
import client.services.http.builders.types.NumberBuilder;
import client.services.http.builders.types.UriBuilder;
import cosmos.types.Date;

import java.util.HashMap;
import java.util.Map;

public class EntityBuilder<T extends client.core.system.Entity, E extends client.core.model.Entity, L extends List<E>> implements Builder<E, L>, client.core.model.factory.EntityFactory {

	private static final Map<client.core.model.Entity.Type, EntityBuilder> entityBuilders = new HashMap<client.core.model.Entity.Type, EntityBuilder>() {{
		put(Node.Type.FORM, new FormBuilder());
		put(Node.Type.CATALOG, new CatalogBuilder());
		put(Node.Type.COLLECTION, new CollectionBuilder());
		put(Node.Type.CONTAINER, new ContainerBuilder());
		put(Node.Type.DESKTOP, new DesktopBuilder());
		put(Node.Type.DOCUMENT, new DocumentBuilder());
		put(Task.Type.ACTIVITY, new ActivityBuilder());
		put(Task.Type.SERVICE, new ServiceBuilder());
		put(Task.Type.JOB, new JobBuilder());
		put(Command.Type.DEFAULT, new DefaultCommandBuilder());
		put(Command.Type.GROUP, new GroupCommandBuilder());
		put(Field.Type.BOOLEAN, new BooleanFieldBuilder());
		put(Field.Type.CHECK, new CheckFieldBuilder());
		put(Field.Type.COMPOSITE, new CompositeFieldBuilder());
		put(Field.Type.DATE, new DateFieldBuilder());
		put(Field.Type.FILE, new FileFieldBuilder());
		put(Field.Type.LINK, new LinkFieldBuilder());
		put(Field.Type.MEMO, new MemoFieldBuilder());
		put(Field.Type.NODE, new NodeFieldBuilder());
		put(Field.Type.NUMBER, new NumberFieldBuilder());
		put(Field.Type.PICTURE, new PictureFieldBuilder());
		put(Field.Type.SELECT, new SelectFieldBuilder());
		put(Field.Type.SERIAL, new SerialFieldBuilder());
		put(Field.Type.SUMMATION, new SummationFieldBuilder());
		put(Field.Type.TEXT, new TextFieldBuilder());
		put(Field.Type.URI, new UriFieldBuilder());
		put(Entity.Type.DASHBOARD, new DashboardBuilder());
		put(Entity.Type.TASK_BOARD, new TaskListBuilder());
		put(Entity.Type.TASK_TRAY, new TaskListBuilder());
		put(Entity.Type.NEWS, new NewsBuilder());
		put(Entity.Type.TRASH, new TrashBuilder());
		put(Entity.Type.ROLES, new RolesBuilder());
		put(View.Type.NODE_VIEW, new NodeViewBuilder());
		put(View.Type.DASHBOARD_VIEW, new DashboardViewBuilder());
	}};

	private static final Map<client.core.model.Entity.Type, EntityBuilder> multipleEntityBuilders = new HashMap<client.core.model.Entity.Type, EntityBuilder>() {{
		put(Field.Type.BOOLEAN, new NullFieldBuilder());
		put(Field.Type.CHECK, new NullFieldBuilder());
		put(Field.Type.COMPOSITE, new MultipleCompositeFieldBuilder());
		put(Field.Type.DATE, new MultipleDateFieldBuilder());
		put(Field.Type.FILE, new MultipleFileFieldBuilder());
		put(Field.Type.LINK, new MultipleLinkFieldBuilder());
		put(Field.Type.MEMO, new MultipleMemoFieldBuilder());
		put(Field.Type.NODE, new NullFieldBuilder());
		put(Field.Type.NUMBER, new MultipleNumberFieldBuilder());
		put(Field.Type.PICTURE, new MultiplePictureFieldBuilder());
		put(Field.Type.SELECT, new MultipleSelectFieldBuilder());
		put(Field.Type.SERIAL, new MultipleSerialFieldBuilder());
		put(Field.Type.SUMMATION, new NullFieldBuilder());
		put(Field.Type.TEXT, new MultipleTextFieldBuilder());
		put(Field.Type.URI, new NullFieldBuilder());
	}};

	@Override
	public E build(HttpInstance instance) {
		if (instance == null)
			return null;

		if (instance.getString("type").isEmpty())
			return null;

		client.core.model.Entity.Type type = client.core.model.Entity.Type.fromString(instance.getString("type"));
        Map<client.core.model.Entity.Type, EntityBuilder> builders = instance.getBoolean("multiple") ? multipleEntityBuilders : entityBuilders;

		return (E) builders.get(type).build(instance);
	}

	@Override
	public void initialize(client.core.model.Entity object, HttpInstance instance) {
		T entity = (T)object;
		entity.setId(instance.getString("id"));
		entity.setLabel(instance.getString("label"));
		entity.setDescription(instance.getString("description"));
		entity.setOwner(new NodeBuilder().build(instance.getObject("owner")));
		entity.setAncestors(new NodeBuilder().buildList(instance.getList("ancestors")));
		entity.setDefinition(new EntityDefinitionBuilder().build(instance.getObject("definition")));

		initializeKey(entity, instance);
	}

	private void initializeKey(T entity, HttpInstance instance) {
		client.core.system.Key key = new client.core.system.Key();

		if (instance.getString("code").isEmpty() && instance.getString("name").isEmpty())
			return;

		if (!instance.getString("code").isEmpty())
			key.setCode(instance.getString("code"));

		if (!instance.getString("name").isEmpty())
			key.setName(instance.getString("name"));

		entity.setKey(key);
	}

	@Override
	public L buildList(HttpList instance) {
		List<E> result = new MonetList<>();

		for (int i=0; i< instance.getItems().length(); i++)
			result.add(build(instance.getItems().get(i)));

		return (L)result;
	}

	@Override
	public Node createNode(String id) {
		return new client.core.system.Node(id) {
			@Override
			protected NodeView loadView(NodeViewDefinition viewDefinition) {
				return null;
			}

			@Override
			public boolean isEnvironment() {
				return false;
			}

			@Override
			public ClassName getClassName() {
				return null;
			}
		};
	}

	@Override
	public Container createContainer(String id) {
		HttpInstance instance = createEntity(id);
		instance.setString("type", Node.Type.CONTAINER.toString());
		return new ContainerBuilder().build(instance);
	}

	@Override
	public Task createTask(String id) {
		return new client.core.system.Task(id, "") {
			@Override
			protected ViewList<TaskView> loadViews() {
				return null;
			}

			@Override
			public ClassName getClassName() {
				return Task.CLASS_NAME;
			}
		};
	}

	@Override
	public Activity createActivity(String id) {
		HttpInstance instance = createEntity(id);
		instance.setString("type", Task.Type.ACTIVITY.toString());
		return new ActivityBuilder().build(instance);
	}

	@Override
	public News createNews(String label) {
		HttpInstance instance = createEntity("news");
		instance.setString("label", label);
		return new NewsBuilder().build(instance);
	}

	@Override
	public Index createIndex(String id) {
		return new IndexBuilder().build(createEntity(id));
	}

	@Override
	public Source createSource(String id) {
		return new SourceBuilder().build(createEntity(id));
	}

	@Override
	public TaskList createTaskList() {
		return new TaskListBuilder().build(createEntity("taskList"));
	}

	@Override
	public <Type extends client.core.model.Field<?,?>> Type createField(String code, String label, Class<?> type) {
		return (Type)new FieldBuilder<>().build(createField(code, label, getFieldType(type)));
	}

	@Override
	public <Type extends Field<?, ?>> Type createFieldByClassName(String code, String label, Instance.ClassName className) {
		return (Type)new FieldBuilder<>().build(createField(code, label, getFieldType(className)));
	}

	@Override
	public NodeIndexEntry createNodeIndexEntry(Node node, String label) {
		client.core.system.NodeIndexEntry entry = new client.core.system.NodeIndexEntry();
		entry.setLabel(label);
		entry.setEntity(node);
		return entry;
	}

	@Override
	public TaskListIndexEntry createTaskListIndexEntry(Task task, String label) {
		HttpInstance instance = HttpInstance.createInstance();
		instance.set("node", createEntity(task.getId()));
		instance.setString("label", label);
		return new TaskListIndexEntryBuilder().build(instance);
	}

	@Override
	public Filter createTypeFilter() {
		HttpInstance instance = HttpInstance.createInstance();
		instance.set("name", Index.TypeAttribute.getName());
		instance.set("label", Index.TypeAttribute.getLabel());
		return new FilterBuilder().build(instance);
	}

	@Override
	public Filter createFilter(String name, String label) {
		return new client.core.system.Filter(name, label);
	}

	@Override
	public Filter.Option createFilterOption(String value, String label) {
		return new client.core.system.Filter.Option(value, label);
	}

	@Override
	public Order createOrder(String name, String label, Order.Mode mode) {
		return new client.core.system.Order(name, label, mode);
	}

	@Override
	public TermList createTermList() {
		return new client.core.system.types.TermList();
	}

	@Override
	public TermList createTermList(Term[] terms) {
		TermList termList = createTermList();

		for (Term term : terms)
			termList.add(term);

		return termList;
	}

	@Override
	public Term createTerm(String value, String label) {
		return new client.core.system.types.Term(value, label);
	}

	@Override
	public Link createLink(String id, String label) {
		HttpInstance instance = HttpInstance.createInstance();
		instance.setString("id", id);
		instance.setString("label", label);
		return new LinkBuilder().build(instance);
	}

	@Override
	public Composite createComposite() {
		return new CompositeBuilder().build(HttpInstance.createInstance());
	}

	@Override
	public File createFile(String label) {
		return createFile("", label);
	}

	@Override
	public File createFile(String id, String label) {
		return new client.core.system.types.File(id, label);
	}

	@Override
	public Picture createPicture(String label) {
		return createPicture("", label);
	}

	@Override
	public Picture createPicture(String id, String label) {
		return new client.core.system.types.Picture(id, label);
	}

	@Override
	public Uri createUri(String value) {
		HttpInstance instance = HttpInstance.createInstance();
		instance.setString("value", value);
		return new UriBuilder().build(instance);
	}

	@Override
	public <T extends java.lang.Number> client.core.model.types.Number createNumber(T value) {
		HttpInstance instance = HttpInstance.createInstance();
		instance.setString("value", value.toString());
		return new NumberBuilder().build(instance);
	}

	private HttpInstance createEntity(String id) {
		HttpInstance instance = HttpInstance.createInstance();
		instance.setString("id", id);
		return instance;
	}

	private HttpInstance createField(String code, String label, Field.Type type) {
		HttpInstance instance = HttpInstance.createInstance();

		instance.setString("code", code);
		instance.setString("label", label);
		instance.setString("type", type != null ? type.toString() : "");

		return instance;
	}

	private Field.Type getFieldType(Class<?> type) {

		if (type == String.class)
			return Field.Type.TEXT;

		if (type == Term.class)
			return Field.Type.SELECT;

		if (type == NodeIndexEntry.class)
			return Field.Type.LINK;

		if (type == Date.class)
			return Field.Type.DATE;

		if (type == File.class)
			return Field.Type.FILE;

		return null;
	}

	private Field.Type getFieldType(Instance.ClassName className) {

		if (className == CompositeField.CLASS_NAME)
			return Field.Type.COMPOSITE;

		if (className == TextField.CLASS_NAME)
			return Field.Type.TEXT;

		if (className == MemoField.CLASS_NAME)
			return Field.Type.MEMO;

		if (className == SelectField.CLASS_NAME)
			return Field.Type.SELECT;

		if (className == LinkField.CLASS_NAME)
			return Field.Type.LINK;

		if (className == DateField.CLASS_NAME)
			return Field.Type.DATE;

		if (className == CheckField.CLASS_NAME)
			return Field.Type.CHECK;

		if (className == FileField.CLASS_NAME)
			return Field.Type.FILE;

		return null;
	}
}
