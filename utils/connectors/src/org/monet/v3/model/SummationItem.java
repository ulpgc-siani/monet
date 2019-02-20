package org.monet.v3.model;

import org.monet.metamodel.SummationItemProperty;
import org.monet.metamodel.SummationItemPropertyBase;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

@Root(name = "item")
public class SummationItem {
	public
	@Attribute(name = "key")
	String Key;
	public
	@Attribute(name = "label")
	String Label;
	public
	@Attribute(name = "value")
	double Value;
	public
	@Attribute(name = "type")
	SummationItemPropertyBase.TypeEnumeration Type = SummationItemPropertyBase.TypeEnumeration.SIMPLE;
	public
	@Attribute(name = "multiple", required = false)
	boolean IsMultiple = false;
	public
	@Attribute(name = "negative", required = false)
	boolean IsNegative = false;
	@ElementList(inline = true, required = false)
	public ArrayList<SummationItem> Children = new ArrayList<SummationItem>();

	public SummationItem() {
	}

	public SummationItem(SummationItemProperty itemDeclaration) {
		this.Key = itemDeclaration.getKey();
		this.Label = itemDeclaration.getLabel().toString();
		this.Value = 0;
		this.Type = itemDeclaration.getType();
		this.IsMultiple = itemDeclaration.isMultiple();
		this.IsNegative = itemDeclaration.isNegative();

		for (SummationItemProperty childProperty : itemDeclaration.getSummationItemPropertyList()) {
			this.Children.add(new SummationItem(childProperty));
		}
	}
}
