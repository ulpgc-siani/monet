/*******************************************************************************
 * Copyright (c) 2009 itemis AG (http://www.itemis.eu) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 *******************************************************************************/
package org.monet.editor.dsl.ui.syntaxcoloring;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.editor.utils.TextStyle;
import org.eclipse.xtext.xbase.ui.highlighting.XbaseHighlightingConfiguration;

/**
 * @author Sven Efftinge - Initial contribution and API
 *
 */
@SuppressWarnings("restriction")
public class SemanticHighlightingConfiguration extends XbaseHighlightingConfiguration {
	
	public final static String CODE = "Code"; 
	public final static String DOUBLE_LITERAL = "DoubleLiteral"; 
	public final static String ENUM_VALUE = "EnumValue"; 
	public final static String COMMENTS = "Comments"; 
	public static final String TODO = "Todo";


	@Override
	public void configure(IHighlightingConfigurationAcceptor acceptor) {
		super.configure(acceptor);
		acceptor.acceptDefaultHighlighting(CODE, "Code", codeTextStyle());
		acceptor.acceptDefaultHighlighting(DOUBLE_LITERAL, "Double/Float literal", doubleTextStyle());
		acceptor.acceptDefaultHighlighting(ENUM_VALUE, "Enum value", enumValueTextStyle());
		acceptor.acceptDefaultHighlighting(TODO, "Todo", todoTextStyle());
	}

	@Override
	public TextStyle defaultTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setBackgroundColor(new RGB(255, 255, 255));
		textStyle.setColor(new RGB(0, 0, 0));
		return textStyle;
	}
	
	public TextStyle codeTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(204, 204, 204));
		return textStyle;
	}
	
	public TextStyle doubleTextStyle() {
	  TextStyle textStyle = defaultTextStyle().copy();
	  textStyle.setColor(new RGB(102, 102, 102));
	  return textStyle;
	}
	
	public TextStyle enumValueTextStyle() {
	  TextStyle textStyle = defaultTextStyle().copy();
	  textStyle.setColor(new RGB(0, 0, 255));
	  textStyle.setStyle(SWT.ITALIC);
	  return textStyle;
	}

	public TextStyle todoTextStyle() {
		TextStyle textStyle = commentTextStyle().copy();
	  textStyle.setColor(new RGB(127, 159, 191));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

}
