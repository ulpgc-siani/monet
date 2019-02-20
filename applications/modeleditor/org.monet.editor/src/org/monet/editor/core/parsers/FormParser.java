package org.monet.editor.core.parsers;

import java.util.Stack;

import org.monet.editor.core.parsers.form.Property;
import org.monet.editor.core.parsers.form.PropertyList;
import org.monet.editor.core.parsers.form.Declaration;

public class FormParser extends CodeParser {

	private boolean isForm;
	private Stack<PropertyList> stack;
	private PropertyList scope;
	private Declaration form;
	private Property field;
	private int state;
	private String type;

	public FormParser(String code) {
		super(code);
	}

	protected void start() {
		this.scope = null;
		this.stack = new Stack<PropertyList>();
		this.form = new Declaration();
		this.field = null;
		this.state = 0;
	}

	@Override
	protected void analyze(String token) {
		int index;

		switch (state) {
		case 0:
			if (token.equals("definition"))
				state = 1;
			break;
		case 1:
			if (token.equals("is"))
				state = 2;
			break;
		case 2:
			isForm = token.equals("form");
			if (!isForm)
				stop();
			scope = form;
			state = 3;
			break;
		case 3:
			index = token.indexOf("field-");
			if (index >= 0) {
				type = token.substring(index);
				state = 4;
			}

			index = token.indexOf("is-multiple");
			if (index >= 0) {
				if (scope instanceof Property) {
					field = (Property) scope;
					field.isMultiple(true);
				}
			}

			break;
		case 4:
			field = new Property(token, type);
			scope.add(field);

			state = 3;
			break;
		}
	}
	
	protected void push() {
		if (scope == null) return;
		stack.push(scope);
		if (field != null)
			scope = field;
	}

	protected void pop() {
		if (scope == null) return;
		if (stack.size() == 0) return;
		
		scope = stack.pop();
		if (scope instanceof Property)
			field = (Property) scope;
		return;
	}
	

	public boolean isForm() {
		return isForm;
	}

	public Declaration getForm() {
		return form;
	}

}
