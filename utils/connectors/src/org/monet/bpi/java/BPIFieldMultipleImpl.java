package org.monet.bpi.java;

import org.monet.api.backservice.impl.model.Attribute;
import org.monet.api.backservice.impl.model.AttributeList;
import org.monet.api.backservice.impl.model.Node;
import org.monet.bpi.BPIFieldMultiple;
import org.monet.bpi.BPIField;
import org.monet.v2.metamodel.FieldDeclaration;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

@SuppressWarnings("unchecked")
public class BPIFieldMultipleImpl<T extends BPIField<?>,V> implements BPIFieldMultiple<T, V>, Iterable<V> {

  protected String definitionName;
  protected Node node;
  protected FieldDeclaration declaration;
  protected Attribute attribute;
  protected ArrayList<T> fields;
  protected BPIFieldFactory fieldFactory;

  void injectDefinitionName(String definitionName) {
    this.definitionName = definitionName;
  }
  
  void injectNode(Node node) {
    this.node = node;
  }
  
  void injectAttribute(Attribute attribute) {
    this.attribute = attribute;
  }
  
  void injectFields(ArrayList<T> fields) {
    this.fields = fields;
  }
  
  void injectFactory(BPIFieldFactory fieldFactory) {
    this.fieldFactory = fieldFactory;
  }
  
  void injectFieldDeclaration(FieldDeclaration fieldDeclaration) {
    this.declaration = fieldDeclaration;
  }

  private BPIFieldImpl<V> createField(V newValue) {
    BPIFieldImpl<V> field = (BPIFieldImpl<V>)this.fieldFactory.get(this.definitionName, this.declaration, new Attribute(), node);
    field.set(newValue);
    int count = this.attribute.getAttributeList().getCount();
    field.attribute.setId(UUID.randomUUID().toString());
    field.attribute.setCode(this.attribute.getCode());
    field.attribute.setOrder(count);
    return field;
  }
  
  @Override
  public T add() {
  	return this.add(null);
  }
  
  @Override
  public T add(V newValue) {
    BPIFieldImpl<V> field = createField(newValue);
    this.attribute.getAttributeList().add(field.attribute);    
    this.fields.add((T)field);
    return (T)field;
  }

  @Override
  public T insert(int index) {
  	return this.insert(index, null);
  }
  
  @Override
  public T insert(int index, V newValue) {
    BPIFieldImpl<V> field = createField(newValue);
    this.attribute.getAttributeList().add(field.attribute);
    this.fields.add(index, (T)field);
    refreshOrder();
    return (T)field;
  }
  
  @Override
  public void remove(int index) {
    if(index > -1) {
      BPIFieldImpl<V> field = (BPIFieldImpl<V>)this.fields.get(index);
      attribute.getAttributeList().delete(field.attribute.getId());
      this.fields.remove(index);
    }
    refreshOrder();
  }
  
  @Override
  public void remove(V newValue) {
    int index = this.getAll().indexOf(newValue);
    if(index > -1) {
      BPIFieldImpl<V> field = (BPIFieldImpl<V>)this.fields.get(index);
      attribute.getAttributeList().delete(field.attribute.getId());
      this.fields.remove(index);
    }
    refreshOrder();
  }
  
  @Override
  public void removeAll() {
    AttributeList attributeList = attribute.getAttributeList();
    for(T field : this.fields) {
      BPIFieldImpl<V> fieldImpl = (BPIFieldImpl<V>)field;
      attributeList.delete(fieldImpl.attribute.getId());
    }
    this.fields.clear();
  }
  
  void refreshOrder() {
    int pos = 0;
    for(T field : this.fields) {
      BPIFieldImpl<V> fieldImpl = (BPIFieldImpl<V>)field;
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
    ArrayList<V> values = new ArrayList<V>();
    for(T field : this.fields)
      values.add((V) field.get());
      
    return values;
  }
  
  @Override
  public ArrayList<T> getAllFields() {
    ArrayList<T> values = new ArrayList<T>();
    for(T field : this.fields)
      values.add(field);
      
    return values;
  }

  @Override
  public Iterator<V> iterator() {
    return this.getAll().iterator();
  }

}
