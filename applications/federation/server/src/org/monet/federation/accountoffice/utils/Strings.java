package org.monet.federation.accountoffice.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Strings {

	public String[] getRegularExpression(String expression, String text, Integer matcherIndex) throws Exception {

		Pattern pattern = Pattern.compile(expression);
		Matcher matcher = pattern.matcher(text);
		
		String[] result = new String[matcher.groupCount()];

		Integer index = 0;
		while (index <= matcherIndex) {
			index++;
			if (matcher.find()) {
				for (int x = 1; x <= matcher.groupCount(); x++)
					result[x - 1] = matcher.group(x);
			} else {
				throw new Exception("Error: expression '" + expression + "' not found, text: " + text);
			}
		}
		
		return result;
	}
	
	public String readString(String label) {
		System.out.print(label + ": ");
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		String value = null;
		try {
			value = br.readLine();
		} catch (IOException ioe) {
			System.out.println("IO error trying to read your name!");
			System.exit(1);
		}
		return value;
	}

	public boolean isNumeric(String str)
	{
	  return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
	}
}
