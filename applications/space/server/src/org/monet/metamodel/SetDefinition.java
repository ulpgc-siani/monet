package org.monet.metamodel;

import org.monet.space.kernel.library.LibraryString;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class SetDefinition extends SetDefinitionBase {

	public static class SetViewProperty extends SetViewPropertyBase {
		public static class AnalyzeProperty extends AnalyzePropertyBase {
			public static String[] getValues(FilterProperty filter, Map<String, String> parameters) {
				Pattern pattern = Pattern.compile("parameter\\(([^\\)]*)\\)");
				Matcher matcher;
				String result = filter.getValue();

				matcher = pattern.matcher(result);
				while (matcher.find()) {
					String name = matcher.group(1);
					String value = (parameters.containsKey(name) ? parameters.get(name) : "");
					result = LibraryString.replaceAll(result, matcher.group(0), value);
				}

				return result.contains("|") ? result.split("\\|") : new String[] { result };
			}
		}
	}
}