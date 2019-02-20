package org.monet.editor.ui.handlers;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.monet.editor.core.framework.CodeHelper;
import org.monet.editor.core.parsers.FormParser;
import org.monet.editor.core.parsers.form.Property;
import org.monet.editor.core.parsers.form.PropertyList;
import org.monet.editor.ui.dialogs.GenerateViewFormDialog;
import org.monet.editor.ui.framework.FormContributionHandler;

public class GenerateViewFormHandler extends FormContributionHandler {

	@Override
	public void execute() {
		FormParser parser = new FormParser(document.get());
		if (!parser.isForm()) {
			MessageDialog.openInformation(getShell(), "Information", "Current definition is not a form");
	        return;
		}
		
		GenerateViewFormDialog dialog = new GenerateViewFormDialog(getShell());
		dialog.setFieldList(parser.getForm());
		if (dialog.open() != Window.OK) return;
		
		insert(generateSnippet(dialog.isWidget(), dialog.getName(), dialog.getSelection()));
	}

	private String generateSnippet(boolean isWidget, String name, PropertyList fieldList) {
		String snippet = "";
		snippet += CodeHelper.ident("[::code::]", 2);
		if (isWidget) {
			snippet += CodeHelper.ident("view " + name + " {", 2);
			snippet += CodeHelper.ident("is-widget;", 3);
		}
		else {
			snippet += CodeHelper.ident("view {", 2);
			snippet += CodeHelper.ident(CodeHelper.assign("label", name), 3);
		}
		snippet += CodeHelper.ident("show {", 3);
		for (Property field : fieldList)
			snippet += CodeHelper.ident("field = ref " + field.getName() + ";", 4);
		snippet += CodeHelper.ident("}", 3);
		snippet += CodeHelper.ident("}", 2);
		return snippet;
	}

}
