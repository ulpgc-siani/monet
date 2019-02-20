package org.monet.editor.core.framework;

public class CodeHelper {
	private final static String CR = "\n";
	private final static String TAB = "\t";
	
	public static String ident(String text, int tabs) {
		for (int i=0;i<tabs;i++)
			text = TAB + text;
		return CR + text;
	}
	
	public static String assign(String attribute, String value) {
		return attribute + " = \"" + value + "\";"; 
	}
}
