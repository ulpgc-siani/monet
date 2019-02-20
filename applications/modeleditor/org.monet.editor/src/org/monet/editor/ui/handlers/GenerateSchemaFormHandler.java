package org.monet.editor.ui.handlers;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.monet.editor.core.framework.CodeHelper;
import org.monet.editor.core.parsers.FormParser;
import org.monet.editor.core.parsers.form.Property;
import org.monet.editor.core.parsers.form.PropertyList;
import org.monet.editor.ui.dialogs.GenerateSchemaFormDialog;
import org.monet.editor.ui.framework.FormContributionHandler;

public class GenerateSchemaFormHandler extends FormContributionHandler {

	@Override
	public void execute() {
		FormParser parser = new FormParser(document.get());
		if (!parser.isForm()) {
			MessageDialog.openInformation(getShell(), "Information", "Current definition is not a form");
	        return;
		}
		
		GenerateSchemaFormDialog dialog = new GenerateSchemaFormDialog(getShell());
		dialog.setFieldList(parser.getForm());
		if (dialog.open() != Window.OK) return;
		
		String snippet = "";
		snippet += CodeHelper.ident("schema {", 2);
		snippet += generateSnippet(dialog.getSelection(), 2);
		snippet += CodeHelper.ident("}", 2);
		insert(snippet);
	}

	public String generateSnippet(PropertyList fieldList, int tabs) {
		String snippet = "";
		tabs++;
		for (Property field : fieldList) {
			snippet += field.isMultiple() ? CodeHelper.ident("many ", tabs) : CodeHelper.ident("", tabs); 
			snippet += field.getName() + " from-field " + field.getName();
			if (field.size() > 0) {
				snippet += " {";
				snippet += generateSnippet(field, tabs);
				snippet += CodeHelper.ident("}", tabs);
			}
			else {
				snippet += ";";
			}
				
		}
		return snippet;
	}

}
