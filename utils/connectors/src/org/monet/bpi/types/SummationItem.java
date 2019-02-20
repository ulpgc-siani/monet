package org.monet.bpi.types;

import org.monet.metamodel.SummationItemProperty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class SummationItem extends Observable implements Observer {

	private boolean isCalculated = false;
	private org.monet.v3.model.SummationItem value;
	private HashMap<String, ArrayList<SummationItem>> children = new HashMap<String, ArrayList<SummationItem>>();
	private SummationItemProperty declaration = null;

	public SummationItem(org.monet.v3.model.SummationItem value, SummationItemProperty declaration) {
		this.declaration = declaration;
		this.value = value;
		this.isCalculated = this.value.Children.size() > 0;
		for (org.monet.v3.model.SummationItem child : this.value.Children) {
			SummationItem item = new SummationItem(child, declaration.getChild(child.Key));
			item.addObserver(this);
			ArrayList<SummationItem> items = this.children.get(item.getKey());
			if (items == null) {
				items = new ArrayList<SummationItem>();
				this.children.put(item.getKey(), items);
			}

			items.add(item);
		}
	}

	private void calculate() {
		double calculatedValue = 0;
		for (ArrayList<SummationItem> childs : this.children.values()) {
			for (SummationItem child : childs) {
				if (child.value.IsNegative)
					calculatedValue -= child.value.Value;
				else
					calculatedValue += child.value.Value;
			}
		}
		if (calculatedValue != this.value.Value) {
			this.value.Value = calculatedValue;
			this.setChanged();
		}

		this.notifyObservers();
	}

	@Override
	public void update(Observable o, Object arg) {
		this.calculate();
	}

	public String getKey() {
		return this.value.Key;
	}

	public String getLabel() {
		return this.value.Label;
	}

	public double getValue() {
		return this.value.Value;
	}

	public boolean isMultiple() {
		return this.value.IsMultiple;
	}

	public boolean isNegative() {
		return this.value.IsNegative;
	}

	public void setLabel(String label) {
		if (this.value.IsMultiple) {
			this.value.Label = label;
		}
	}

	public void setValue(double value) {
		if (this.isCalculated)
			return;
		this.value.Value = value;
		this.setChanged();
		this.notifyObservers();
	}

	public SummationItem getItem(String itemKey) {
		ArrayList<SummationItem> items = this.children.get(itemKey);
		if (items != null && items.size() > 0)
			return items.get(0);
		return null;
	}

	public SummationItem addNew(String key) {
		SummationItemProperty declaration = this.declaration.getChild(key);
		if (declaration == null || !declaration.isMultiple())
			return null;

		org.monet.v3.model.SummationItem internalItem = new org.monet.v3.model.SummationItem();
		internalItem.IsMultiple = declaration.isMultiple();
		internalItem.IsNegative = declaration.isNegative();
		internalItem.Label = declaration.getLabel().toString();
		internalItem.Key = declaration.getKey();
		internalItem.Value = 0;
		SummationItem item = new SummationItem(internalItem, declaration);
		item.addObserver(this);
		ArrayList<SummationItem> items = this.children.get(item.getKey());
		if (items == null) {
			items = new ArrayList<SummationItem>();
			this.children.put(item.getKey(), items);
		}

		items.add(item);

		return item;
	}

	public void delete(SummationItem item) {
		if (!item.declaration.isMultiple())
			return;

		ArrayList<SummationItem> items = this.children.get(item.getKey());
		if (items != null) {
			int index = items.indexOf(item);
			if (index > -1) {
				items.remove(index);
				item.deleteObserver(this);
				this.calculate();
			}
		}
	}

	public void commitChanges() {
		this.value.Children.clear();
		for (SummationItemProperty declaration : this.declaration.getSummationItemPropertyList()) {
			ArrayList<SummationItem> items = this.children.get(declaration.getKey());
			if (items != null) {
				for (SummationItem child : items) {
					this.value.Children.add(child.value);
					child.commitChanges();
				}
			}
		}
	}

}
