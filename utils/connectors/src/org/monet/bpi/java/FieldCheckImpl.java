package org.monet.bpi.java;

import org.monet.api.space.backservice.impl.model.*;
import org.monet.bpi.FieldCheck;
import org.monet.bpi.types.Check;
import org.monet.bpi.types.CheckList;
import org.monet.bpi.types.TermList;
import org.monet.metamodel.CheckFieldProperty;

public class FieldCheckImpl extends FieldImpl<CheckList> implements FieldCheck {

	@Override
	public CheckList get() {
		CheckList checkList = new CheckList();

		if (this.attribute == null) return checkList;

		for (Attribute attribute : this.attribute.getAttributeList()) {
			String checked = this.getIndicatorValue(attribute, Indicator.CHECKED);
			String code = this.getIndicatorValue(attribute, Indicator.CODE);
			String value = this.getIndicatorValue(attribute, Indicator.VALUE);
			Boolean checkedValue;

			if (checked.equals("")) checkedValue = false;
			else checkedValue = Boolean.parseBoolean(checked);

			checkList.add(new Check(checkedValue, code, value));
		}

		checkList.setSource(this.getIndicatorValue(this.attribute, Indicator.SOURCE));

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
		Node node = this.api.openNode(this.nodeId);

		AttributeList attributeList = this.attribute.getAttributeList();
		for (Check check : value.getAll()) {
			Attribute attribute = attributeList.get(check.getCode());

			if (attribute == null) {
				attribute = new Attribute();
				attribute.setCode(check.getCode());
				attributeList.add(attribute);
			}

			IndicatorList indicatorList = attribute.getIndicatorList();
			indicatorList.clear();
			indicatorList.add(new Indicator(Indicator.CHECKED, 0, String.valueOf(check.isChecked())));
			indicatorList.add(new Indicator(Indicator.CODE, 1, check.getCode()));
			indicatorList.add(new Indicator(Indicator.VALUE, 2, check.getLabel()));
		}

		if (value.getSource() != null)
			this.attribute.getIndicatorList().add(new Indicator(Indicator.SOURCE, 0, value.getSource()));

		this.attribute.setAttributeList(attributeList);
	}

	public void fill(CheckList value) {
		int index = 0;

		if (this.attribute == null) return;

		AttributeList newAttributeList = new AttributeList();
		for (Check check : value.getAll()) {
			Attribute attribute = new Attribute();
			attribute.setCode(Attribute.OPTION);
			attribute.setOrder(index);
			attribute.getIndicatorList().add(new Indicator(Indicator.CHECKED, 0, String.valueOf(check.isChecked())));
			attribute.getIndicatorList().add(new Indicator(Indicator.CODE, 1, check.getCode()));
			attribute.getIndicatorList().add(new Indicator(Indicator.VALUE, 2, check.getLabel()));
			newAttributeList.add(attribute);
			index++;
		}

		if (value.getSource() != null)
			this.attribute.getIndicatorList().add(new Indicator(Indicator.SOURCE, 0, value.getSource()));

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
		if (value instanceof CheckList)
			return this.get().equals((CheckList) value);
		else
			return false;
	}

	@Override
	public void clear() {
		this.set(new CheckList());
	}

}