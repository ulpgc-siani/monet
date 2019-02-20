/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.monet.editor.dsl.ui.syntaxcoloring;

import java.util.Iterator;

import org.eclipse.xtext.nodemodel.ILeafNode;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.monet.editor.dsl.monetModelingLanguage.Code;
import org.monet.editor.dsl.monetModelingLanguage.DoubleLiteral;
import org.monet.editor.dsl.monetModelingLanguage.EnumLiteral;
import org.monet.editor.dsl.monetModelingLanguage.FloatLiteral;
import org.monet.editor.dsl.monetModelingLanguage.LocalizedText;
import org.monet.editor.dsl.ui.outline.tasks.ITaskElementChecker;

import com.google.inject.Inject;

public class SemanticHighlightingCalculator implements ISemanticHighlightingCalculator {
	
	@Inject
	ITaskElementChecker objElementChecker;

	
	public void provideHighlightingFor(final XtextResource resource, IHighlightedPositionAcceptor acceptor) {
		if (resource == null)
			return;
		
		Iterator<INode> allNodes = resource.getParseResult().getRootNode().getAsTreeIterable().iterator();
		while(allNodes.hasNext()) {
			INode node = allNodes.next();
			if (node.getSemanticElement() instanceof Code) {
				highlightNode(node, SemanticHighlightingConfiguration.CODE, acceptor);
			
			} else if (node.getSemanticElement() instanceof DoubleLiteral || 
			           node.getSemanticElement() instanceof FloatLiteral) {
			  highlightNode(node, SemanticHighlightingConfiguration.DOUBLE_LITERAL, acceptor);
			
			} else if(node.getSemanticElement() instanceof EnumLiteral && !(node.getParent().getSemanticElement() instanceof LocalizedText)) {
			  highlightNode(node, SemanticHighlightingConfiguration.ENUM_VALUE, acceptor);
  		
			} else if (node instanceof ILeafNode) {
				String varIgnorePrefix = objElementChecker.getPrefixToIgnore(node);
				if (varIgnorePrefix != null) {
					int start = node.getOffset() + node.getText().indexOf(varIgnorePrefix);
					acceptor.addPosition(start, varIgnorePrefix.length(), SemanticHighlightingConfiguration.TODO);
				}				
			}
		}
	}
	
	private void highlightNode(INode node, String styleId, IHighlightedPositionAcceptor acceptor) {
		if (node == null)
			return;
		if (node instanceof ILeafNode) {
			acceptor.addPosition(node.getOffset(), node.getLength(), styleId);
		} else {
			for(ILeafNode leaf: node.getLeafNodes()) {
				if (!leaf.isHidden()) {
					acceptor.addPosition(leaf.getOffset(), leaf.getLength(), styleId);
				}
			}
		}
	}

}