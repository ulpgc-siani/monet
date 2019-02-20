package org.monet.bpi.java;

import org.monet.bpi.FieldSummation;
import org.monet.bpi.types.Number;
import org.monet.bpi.types.SummationItem;
import org.monet.metamodel.SummationFieldProperty;
import org.monet.metamodel.SummationItemProperty;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.kernel.model.SummationItemList;
import org.monet.space.kernel.utils.PersisterHelper;
import org.monet.space.mobile.model.Language;

import java.io.StringWriter;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class FieldSummationImpl extends FieldNumberImpl implements FieldSummation, Observer {

	Number totalValue;
	org.monet.space.kernel.model.SummationItemList value;
	SummationFieldProperty definition;
	HashMap<String, ArrayList<SummationItem>> itemMap = new HashMap<String, ArrayList<SummationItem>>();

	@Override
	public void clear() {

	}

	@Override
	void setAttribute(Attribute attribute) {
		super.setAttribute(attribute);
		reloadValue();
	}

	protected void reloadValue() {
		this.totalValue = super.get();

		for (ArrayList<SummationItem> childs : this.itemMap.values())
			for (SummationItem bpiItem : childs)
				bpiItem.deleteObserver(this);
		this.itemMap.clear();

		Indicator details = this.attribute.getIndicator(Indicator.DETAILS);
		try {
			this.definition = (SummationFieldProperty) this.fieldDefinition;
			this.value = PersisterHelper.load(details.getData(), org.monet.space.kernel.model.SummationItemList.class);
			for (org.monet.space.kernel.model.SummationItem item : value.Items) {
				SummationItem bpiItem = new SummationItem(item, this.definition.getChild(item.Key));
				bpiItem.addObserver(this);
				ArrayList<SummationItem> items = this.itemMap.get(bpiItem.getKey());
				if (items == null) {
					items = new ArrayList<SummationItem>();
					this.itemMap.put(item.Key, items);
				}
				items.add(bpiItem);
			}
		} catch (Exception e) {
			this.value = new SummationItemList();
			AgentLogger.getInstance().error(e);
		}
	}

	@Override
	public Number get() {
		return this.totalValue;
	}

	@Override
	public void set(Number value) {
	}

	public SummationItem getItem(String itemKey) {
		ArrayList<SummationItem> items = this.itemMap.get(itemKey);
		if (items != null && items.size() > 0)
			return items.get(0);
		return null;
	}

	public SummationItem addNew(String key) {
		SummationItemProperty declaration = this.definition.getChild(key);
		if (declaration == null || !declaration.isMultiple())
			return null;

		org.monet.space.kernel.model.SummationItem internalItem = new org.monet.space.kernel.model.SummationItem();
		internalItem.IsMultiple = declaration.isMultiple();
		internalItem.IsNegative = declaration.isNegative();
		internalItem.Label = Language.getInstance().getModelResource(declaration.getLabel());
		internalItem.Key = declaration.getKey();
		internalItem.Value = 0;
		SummationItem item = new SummationItem(internalItem, declaration);
		item.addObserver(this);
		ArrayList<SummationItem> items = this.itemMap.get(item.getKey());
		if (items == null) {
			items = new ArrayList<SummationItem>();
			this.itemMap.put(item.getKey(), items);
		}

		items.add(item);

		return item;
	}

	public void delete(SummationItem item) {
		if (!item.isMultiple())
			return;

		ArrayList<SummationItem> items = this.itemMap.get(item.getKey());
		if (items != null) {
			int index = items.indexOf(item);
			if (index > -1) {
				items.remove(index);
				item.deleteObserver(this);
				this.calculate();
			}
		}
	}

	public void commitChanges() throws Exception {
		super.set(this.totalValue);
		for (ArrayList<SummationItem> childs : this.itemMap.values())
			for (SummationItem child : childs)
				child.commitChanges();
		StringWriter detailsWriter = new StringWriter();
		PersisterHelper.save(detailsWriter, this.value);
		this.setIndicatorValue(Indicator.DETAILS, detailsWriter.toString());
	}

	public void discardChanges() {
		this.reloadValue();
	}

	@Override
	public void update(Observable o, Object arg) {
		this.calculate();
	}

	private void calculate() {
		double calculatedValue = 0;
		for (ArrayList<SummationItem> childs : this.itemMap.values()) {
			for (SummationItem child : childs) {
				if (child.isNegative())
					calculatedValue -= child.getValue();
				else
					calculatedValue += child.getValue();
			}
		}

		if (calculatedValue != this.totalValue.doubleValue()) {
			this.totalValue.setValue(calculatedValue);
//SUMMATION NOT USED			String formatDefinition = ((SummationFieldProperty) this.fieldDefinition).getFormat();
			String format = "0.##";

//SUMMATION NOT USED			if (formatDefinition != null)
//				format = formatDefinition;

			DecimalFormat formatter = new DecimalFormat(format);
			this.totalValue.setValue(formatter.format(calculatedValue));
		}
	}

}