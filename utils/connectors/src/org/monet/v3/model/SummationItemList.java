package org.monet.v3.model;

import org.monet.metamodel.SummationItemProperty;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.List;

@Root(name = "itemlist")
public class SummationItemList {
	public
	@ElementList(inline = true, entry = "item")
	ArrayList<SummationItem> Items = new ArrayList<SummationItem>();

	public SummationItemList() {
	}

	public SummationItemList(List<SummationItemProperty> itemDeclarationList) {
		this.Items.clear();

		for (SummationItemProperty itemDeclaration : itemDeclarationList) {
			this.Items.add(new SummationItem(itemDeclaration));
		}
	}

}
