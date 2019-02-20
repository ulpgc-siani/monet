package org.monet.space.mobile.model;

import org.monet.space.kernel.model.Node;

public class MimeType {

	public static String get(Node node) {
		String type;
		if (node.isCatalog())
			type = "catalog";
		else if (node.isCollection())
			type = "collection";
		else if (node.isContainer())
			type = "container";
		else if (node.isDesktop())
			type = "desktop";
		else if (node.isDocument())
			type = "document";
		else if (node.isForm())
			type = "form";
		else
			type = "unknown";
		return String.format("vnd.android.cursor.item/vnd.monet.%s", type);
	}

}
