package org.monet.editor.dsl.ui.outline.tasks;

import org.eclipse.xtext.nodemodel.INode;

public interface ITaskElementChecker {

	/**
	 * return null if argNode does not represent a "task element"
	 * if it is one, return the text prefix to be ignored in the message text
	 * (e.g. //TODO) 
	 * */
	String getPrefixToIgnore(INode argNode);
}
