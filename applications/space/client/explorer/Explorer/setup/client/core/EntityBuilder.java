package client.core;

import client.core.model.*;
import client.core.model.definition.Definition;
import client.core.model.definition.Dictionary;
import client.core.model.fields.*;
import client.core.model.types.*;
import client.core.model.types.Number;
import client.core.system.MonetList;
import cosmos.types.Date;

import java.util.HashMap;
import java.util.Map;

public class EntityBuilder {

	public static EntityFactory buildEntityFactory() {
		return new EntityFactory();
	}

	public static News buildNews(final String label) {
		return new client.core.system.News("news", label);
	}

	public static Index buildIndex(final String id) {
		return new client.core.system.Index(id);
	}

	public static Notification buildNotification(final String label, final Date date) {
		return new client.core.system.Notification("notification", label, date);
	}

	public static BusinessUnit buildBusinessUnit(final BusinessUnit.Type type, final String label, final String url) {
		return new client.core.system.BusinessUnit("businessUnit", label, type, url);
	}

	public static Role buildRole(String id, Role.Type type, String label) {
		if (type == Role.Type.SERVICE)
			return new client.core.system.ServiceRole(id, label);

		if (type == Role.Type.USER)
			return new client.core.system.UserRole(id, label);

		return null;
	}

	public static Account buildAccount(User user, Node rootNode) {
		return new client.core.system.Account(user, rootNode);
	}

	public static User buildUser(String fullName, String email, String photo) {
		return new client.core.system.User(fullName, email, photo);
	}

	public static Command buildCommand(String code, String label, boolean enabled) {
		return new client.core.system.Command(new client.core.system.Key(code), label, enabled);
	}

	public static Command buildGroupCommand(String code, String label, boolean enabled, List<Command> commands) {
		return new client.core.system.GroupCommand(new client.core.system.Key(code), label, enabled, commands);
	}

	public static client.core.model.TaskList buildTaskList(String label, ViewList<client.core.model.TaskListView> views) {
		return new client.core.system.TaskList(label, views);
	}

	public static Source buildSource(String id, String label) {
		return new client.core.system.Source(id, label);
	}

	public static Source buildSource() {
		return buildSource(null, null);
	}

	public static Dictionary buildDictionary() {
		return buildDictionary(new MonetList<Definition>());
	}

	public static Dictionary buildDictionary(List<Definition> definitions) {
		return new client.core.system.definition.Dictionary(definitions);
	}

	// CLASSES
	protected static class EntityFactory implements client.core.model.factory.EntityFactory {

		private static final Map<Instance.ClassName, Builder> fieldByClassName = new HashMap<>();
		static {
			fieldByClassName.put(CheckField.CLASS_NAME, new Builder() {
				@Override
				public Field build(String code, String label) {
					return FieldBuilder.buildCheck(code, label);
				}
			});
			fieldByClassName.put(CompositeField.CLASS_NAME, new Builder() {
				@Override
				public Field build(String code, String label) {
					return FieldBuilder.buildComposite(code, label);
				}
			});
			fieldByClassName.put(DateField.CLASS_NAME, new Builder() {
				@Override
				public Field build(String code, String label) {
					return FieldBuilder.buildDate(code, label);
				}
			});
			fieldByClassName.put(FileField.CLASS_NAME, new Builder() {
				@Override
				public Field build(String code, String label) {
					return FieldBuilder.buildFile(code, label);
				}
			});
			fieldByClassName.put(LinkField.CLASS_NAME, new Builder() {
				@Override
				public Field build(String code, String label) {
					return FieldBuilder.buildLink(code, label);
				}
			});
			fieldByClassName.put(MemoField.CLASS_NAME, new Builder() {
				@Override
				public Field build(String code, String label) {
					return FieldBuilder.buildMemo(code, label);
				}
			});
			fieldByClassName.put(NumberField.CLASS_NAME, new Builder() {
				@Override
				public Field build(String code, String label) {
					return FieldBuilder.buildNumber(code, label);
				}
			});
			fieldByClassName.put(PictureField.CLASS_NAME, new Builder() {
				@Override
				public Field build(String code, String label) {
					return FieldBuilder.buildPicture(code, label);
				}
			});
			fieldByClassName.put(SelectField.CLASS_NAME, new Builder() {
				@Override
				public Field build(String code, String label) {
					return FieldBuilder.buildSelect(code, label);
				}
			});
			fieldByClassName.put(SerialField.CLASS_NAME, new Builder() {
				@Override
				public Field build(String code, String label) {
					return FieldBuilder.buildSerial(code, label);
				}
			});
			fieldByClassName.put(TextField.CLASS_NAME, new Builder() {
				@Override
				public Field build(String code, String label) {
					return FieldBuilder.buildText(code, label);
				}
			});
		}

		@Override
		public Node createNode(String id) {
			return NodeBuilder.buildNode(id);
		}

		@Override
		public Container createContainer(String id) {
			return NodeBuilder.buildContainer(id);
		}

		@Override
		public Task createTask(String id) {
			return TaskBuilder.buildTask(id);
		}

		@Override
		public Activity createActivity(String id) {
			return TaskBuilder.buildActivity(id, "");
		}

		@Override
		public News createNews(String label) {
			return EntityBuilder.buildNews(label);
		}

		@Override
		public Index createIndex(String id) {
			return EntityBuilder.buildIndex(id);
		}

		@Override
		public Source createSource(String id) {
			return EntityBuilder.buildSource(id, "");
		}

		@Override
		public TaskList createTaskList() {
			return EntityBuilder.buildTaskList("Lista de tareas", new client.core.system.ViewList<TaskListView>());
		}

		@Override
		public TermList createTermList() {
			return TypeBuilder.buildTermList(new Term[0]);
		}

		@Override
		public TermList createTermList(Term[] terms) {
			return TypeBuilder.buildTermList(terms);
		}

		@Override
		public <T extends java.lang.Number> Number createNumber(T value) {
			client.core.system.types.Number number = TypeBuilder.buildNumber(value);
			number.setTypeFactory(this);
			return number;
		}

		@Override
		public Term createTerm(String value, String label) {
			return TypeBuilder.buildTerm(value, label);
		}

		@Override
		public Link createLink(String id, String label) {
			return TypeBuilder.buildLink(id, label);
		}

		@Override
		public Composite createComposite() {
			return TypeBuilder.buildComposite(new Field[0]);
		}

		@Override
		public File createFile(String label) {
			return createFile("", label);
		}

		@Override
		public File createFile(String id, String label) {
			return TypeBuilder.buildFile(id, label);
		}

		@Override
		public Picture createPicture(String label) {
			return createPicture("", label);
		}

		@Override
		public Picture createPicture(String id, String label) {
			return TypeBuilder.buildPicture(id, label);
		}

		@Override
		public Uri createUri(String value) {
			return TypeBuilder.buildUri(value);
		}

		@Override
		public NodeIndexEntry createNodeIndexEntry(Node node, String label) {
			return IndexBuilder.buildNodeIndexEntry(node, label);
		}

		@Override
		public TaskListIndexEntry createTaskListIndexEntry(Task task, String label) {
			return IndexBuilder.buildTaskListIndexEntry(task, label);
		}

		@Override
		public Filter createTypeFilter() {
			return new client.core.system.Filter(Index.TypeAttribute.getName(), Index.TypeAttribute.getLabel());
		}

		@Override
		public Filter createFilter(String name, String label) {
			return new client.core.system.Filter(name, label);
		}

		@Override
		public Filter.Option createFilterOption(String value, String label) {
			return IndexBuilder.buildFilterOption(value, label);
		}

		@Override
		public Order createOrder(String name, String label, Order.Mode mode) {
			return new client.core.system.Order(name, label, mode);
		}

		@Override
		public <Type extends Field<?, ?>> Type createField(String code, String label, Class<?> type) {
			if (type == String.class)
				return (Type) FieldBuilder.buildText(code, label);

			if (type == Term.class)
				return (Type) FieldBuilder.buildSelect(code, label);

			if (type == NodeIndexEntry.class)
				return (Type) FieldBuilder.buildLink(code, label);

			if (type == Date.class)
				return (Type) FieldBuilder.buildDate(code, label);

			return null;
		}

		@Override
		public <Type extends Field<?, ?>> Type createFieldByClassName(String code, String label, Instance.ClassName type) {
			return (Type) fieldByClassName.get(type).build(code, label);
		}

		private interface Builder {
			Field build(String code, String label);
		}
	}

}
