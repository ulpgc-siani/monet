package org.monet.bpi.java;

import org.monet.bpi.FieldCheck;
import org.monet.bpi.types.Check;
import org.monet.bpi.types.CheckList;
import org.monet.bpi.types.TermList;
import org.monet.metamodel.CheckFieldProperty;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.NodeLayer;
import org.monet.space.kernel.model.*;

public class FieldCheckImpl extends FieldImpl<CheckList> implements FieldCheck {

	@Override
	public CheckList get() {
		CheckList checkList = new CheckList();

		if (this.attribute == null) return checkList;

		String source = this.getIndicatorValue(this.attribute, Indicator.SOURCE);

		for (Attribute attribute : this.attribute.getAttributeList()) {
			String checked = this.getIndicatorValue(attribute, Indicator.CHECKED);
			String code = this.getIndicatorValue(attribute, Indicator.CODE);
			String value = this.getIndicatorValue(attribute, Indicator.VALUE);
			Boolean checkedValue = !checked.isEmpty() && Boolean.parseBoolean(checked);

			checkList.add(new Check(checkedValue, code, value, source));
		}

		checkList.setSource(source);

		return checkList;
	}

	@Override
	public CheckList getChecked() {
		CheckList checkList = new CheckList();

		for (Check check : this.get().getAll()) {
			if (check.isChecked())
				checkList.add(check);
		}

		checkList.setSource(this.getIndicatorValue(this.attribute, Indicator.SOURCE));

		return checkList;
	}

	@Override
	public TermList getCheckedAsTermList() {
		TermList termList = new TermList();

		for (Check check : this.get().getAll()) {
			if (check.isChecked())
				termList.add(check.toTerm());
		}

		return termList;
	}

	@Override
	public void set(CheckList value) {
		if (this.attribute == null) return;

		CheckFieldProperty checkFieldDefinition = (CheckFieldProperty) this.fieldDefinition;
		NodeLayer nodeLayer = ComponentPersistence.getInstance().getNodeLayer();
		Node node = nodeLayer.loadNode(this.nodeId);
		AttributeList attributeList = nodeLayer.getNodeFormCheckFieldOptions(node, null, checkFieldDefinition, null);
		String source = value.getSource();

		for (Attribute attribute : attributeList) {
			String checkCode = attribute.getIndicatorValue(Indicator.CODE);
			for (Check check : value.getAll()) {
				if (check.getCode().equals(checkCode)) {
					IndicatorList indicatorList = attribute.getIndicatorList();
					indicatorList.clear();
					indicatorList.add(new Indicator(Indicator.CHECKED, 0, String.valueOf(check.isChecked())));
					indicatorList.add(new Indicator(Indicator.CODE, 1, check.getCode()));
					indicatorList.add(new Indicator(Indicator.VALUE, 2, check.getLabel()));
					if (source != null)
					  indicatorList.add(new Indicator(Indicator.SOURCE, 2, source));
					break;
				}
			}
		}

		if (source != null)
			this.attribute.getIndicatorList().add(new Indicator(Indicator.SOURCE, 0, source));

		this.attribute.setAttributeList(attributeList);
	}

	@Override
	public void fill(CheckList value) {
		int index = 0;

		if (this.attribute == null) return;

		String source = value.getSource();
		AttributeList newAttributeList = new AttributeList();
		for (Check check : value.getAll()) {
			Attribute attribute = new Attribute();
			attribute.setCode(Attribute.OPTION);
			attribute.setOrder(index);
			attribute.getIndicatorList().add(new Indicator(Indicator.CHECKED, 0, String.valueOf(check.isChecked())));
			attribute.getIndicatorList().add(new Indicator(Indicator.CODE, 1, check.getCode()));
			attribute.getIndicatorList().add(new Indicator(Indicator.VALUE, 2, check.getLabel()));
			if (source != null)
			  attribute.getIndicatorList().add(new Indicator(Indicator.SOURCE, 2, source));
			newAttributeList.add(attribute);
			index++;
		}

		if (source != null)
			this.attribute.getIndicatorList().add(new Indicator(Indicator.SOURCE, 0, source));

		this.attribute.setAttributeList(newAttributeList);
	}

	@Override
	public void fillFromTermList(TermList termList) {
		this.fill(termList.toCheckList());
	}

	@Override
	public String getFrom() {
		return this.getIndicatorValue(this.attribute, Indicator.FROM);
	}

	@Override
	public String getSource() {
		return this.getIndicatorValue(this.attribute, Indicator.SOURCE);
	}

	@Override
	public void setFrom(String from) {
		IndicatorList indicatorList = this.attribute.getIndicatorList();
		Indicator indicator = indicatorList.get(Indicator.FROM);

		if (indicator == null) {
			indicator = new Indicator(Indicator.FROM, 0, from);
			indicatorList.add(indicator);
		}

		indicator.setData(from);
	}

	@Override
	public boolean equals(Object value) {
		return value instanceof CheckList && this.get().equals((CheckList) value);
	}

	@Override
	public void clear() {
		this.set(new CheckList());
	}

}