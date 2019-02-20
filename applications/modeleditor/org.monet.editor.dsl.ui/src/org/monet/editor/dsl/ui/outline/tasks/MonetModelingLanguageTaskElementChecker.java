package org.monet.editor.dsl.ui.outline.tasks;

import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.impl.LeafNode;
import org.monet.editor.dsl.services.MonetModelingLanguageGrammarAccess;

import com.google.inject.Inject;

public class MonetModelingLanguageTaskElementChecker implements ITaskElementChecker {

	@Inject
	private MonetModelingLanguageGrammarAccess objGrammarAccess;

	@Override
	public String getPrefixToIgnore(INode argNode) {
		if(objGrammarAccess.getSL_COMMENTRule().equals(argNode.getGrammarElement())){

			if(((LeafNode)argNode).getText().contains(TaskConstants.TODO_PREFIX)){
				return TaskConstants.TODO_PREFIX;
			}
			
			if(((LeafNode)argNode).getText().contains(TaskConstants.MERGE_PREFIX)){
				return TaskConstants.MERGE_PREFIX;
			}

		}
		return null;
	}

}
