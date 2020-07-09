package org.monet.bpi.java;

import org.monet.bpi.Field;
import org.monet.bpi.FieldMultiple;
import org.monet.bpi.types.Link;
import org.monet.bpi.types.Term;
import org.monet.metamodel.FieldProperty;
import org.monet.metamodel.LinkFieldProperty;
import org.monet.metamodel.SelectFieldProperty;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.AttributeList;
import org.monet.space.kernel.model.Node;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class FieldMultipleImpl<T extends Field<?>, V> implements FieldMultiple<T, V>, Iterable<V> {

	protected String definitionName;
	protected Node _node;
	protected FieldProperty definition;
	protected Attribute attribute;
	protected List<T> fields;
	protected FieldFactory fieldFactory;

	void injectDefinitionName(String definitionName) {
		this.definitionName = definitionName;
	}

	void injectNode(Node node) {
		this._node = node;
	}

	void injectAttribute(Attribute attribute) {
		this.attribute = attribute;
	}

	void injectFields(List<T> fields) {
		this.fields = fields;
	}

	void injectFactory(FieldFactory fieldFactory) {
		this.fieldFactory = fieldFactory;
	}

	void injectFieldDeclaration(FieldProperty fieldDeclaration) {
		this.definition = fieldDeclaration;
	}

	private FieldImpl<V> createField(V newValue) {
		FieldImpl<V> field = (FieldImpl<V>) this.fieldFactory.get(this.definitionName, this.definition, new Attribute(), FieldImpl.node(this._node.getId()));
		field.set(newValue);
		int count = this.attribute.getAttributeList().getCount();
		field.attribute.setId(UUID.randomUUID().toString());
		field.attribute.setCode(this.attribute.getCode());
		field.attribute.setOrder(count);
		return field;
	}

	@Override
	public T addNew() {
		return this.addNew(null);
	}

	@Override
	public T addNew(V newValue) {
		FieldImpl<V> field = createField(newValue);
		this.attribute.getAttributeList().add(field.attribute);
		this.fields.add((T) field);
		return (T) field;
	}

	@Override
	public List<T> addNewAll(List<V> newValues) {
		List<T> result = new ArrayList<T>();

		for (V newValue : newValues) {
			result.add(this.addNew(newValue));
		}

		return result;
	}

	@Override
	public T insert(int index) {
		return this.insert(index, null);
	}

	@Override
	public T insert(int index, V newValue) {
		FieldImpl<V> field = createField(newValue);
		this.attribute.getAttributeList().insert(index, field.attribute);
		this.fields.add(index, (T) field);
		refreshOrder();
		return (T) field;
	}

	@Override
	public void remove(int index) {
		if (index > -1) {
			FieldImpl<V> field = (FieldImpl<V>) this.fields.get(index);
			attribute.getAttributeList().delete(field.attribute.getId());
			this.fields.remove(index);
		}
		refreshOrder();
	}

	@Override
	public void remove(V newValue) {
		int index = this.getAll().indexOf(newValue);
		if (index > -1) {
			FieldImpl<V> field = (FieldImpl<V>) this.fields.get(index);
			attribute.getAttributeList().delete(field.attribute.getId());
			this.fields.remove(index);
		}
		refreshOrder();
	}

	@Override
	public void removeAll() {
		AttributeList attributeList = attribute.getAttributeList();
		for (T field : this.fields) {
			FieldImpl<V> fieldImpl = (FieldImpl<V>) field;
			attributeList.delete(fieldImpl.attribute.getId());
		}
		this.fields.clear();
	}

	void refreshOrder() {
		int pos = 0;
		for (T field : this.fields) {
			FieldImpl<V> fieldImpl = (FieldImpl<V>) field;
			fieldImpl.attribute.setOrder(pos);
			pos++;
		}
	}

	@Override
	public V get(int index) {
		return (V) this.fields.get(index).get();
	}

	@Override
	public T getAsField(int index) {
		return this.fields.get(index);
	}

	@Override
	public int getCount() {
		return this.fields.size();
	}

	@Override
	public ArrayList<V> getAll() {
		ArrayList<V> values = new ArrayList<>();
		for (T field : this.fields)
			values.add((V) field.get());

		return values;
	}

	@Override
	public ArrayList<Term> getAllAsTerm() {

		if (this.definition instanceof SelectFieldProperty)
			return (ArrayList<Term>)this.getAll();

		if (!(this.definition instanceof LinkFieldProperty))
			return null;

		ArrayList<Term> values = new ArrayList<Term>();
		for (T field : this.fields)
			values.add(((Link) field.get()).toTerm());

		return values;
	}

	@Override
	public ArrayList<T> getAllFields() {
		ArrayList<T> values = new ArrayList<T>();
		for (T field : this.fields)
			values.add(field);

		return values;
	}

	@Override
	public Iterator<V> iterator() {
		return this.getAll().iterator();
	}

	@Override
	public String toString() {
		return toString(", ");
	}

	public String toString(String separator) {
		StringBuilder builder = new StringBuilder();
		for (V value : getAll()) {
			if (value != null) {
				builder.append(value.toString());
				builder.append(separator);
			}
		}
		if (builder.length() > 0) {
			int length = builder.length();
			builder.delete(length - separator.length(), length);
		}
		return builder.toString();
	}

}
