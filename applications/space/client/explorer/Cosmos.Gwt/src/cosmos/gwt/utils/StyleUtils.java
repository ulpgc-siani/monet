package cosmos.gwt.utils;

import java.util.Arrays;
import java.util.List;

public class StyleUtils {

	private static final List<String> elementReference = Arrays.asList("a", "abbr", "acronym", "address", "applet", "area", "article", "aside", "audio", "b", "base", "basefont", "bdi", "bdo", "bgsound", "big", "blink", "blockquote", "body", "br", "button", "canvas", "caption", "center", "cite", "code", "col", "colgroup", "content", "data", "datalist", "dd", "decorator", "del", "details", "dfn", "dialog", "dir", "div", "dl", "dt", "element", "em", "embed", "fieldset", "figcaption", "figure", "font", "footer", "form", "frame", "frameset", "h1", "h2", "h3", "h4", "h5", "h6", "head", "header", "hgroup", "hr", "html", "i", "iframe", "img", "input", "ins", "isindex", "kbd", "keygen", "label", "legend", "li", "link", "listing", "main", "map", "mark", "marquee", "menu", "menuitem", "meta", "meter", "nav", "nobr", "noframes", "noscript", "object", "ol", "optgroup", "option", "output", "p", "param", "picture", "plaintext", "pre", "progress", "q", "rp", "rt", "ruby", "s", "samp", "script", "section", "select", "shadow", "small", "source", "spacer", "span", "strike", "strong", "style", "sub", "summary", "sup", "table", "tbody", "td", "template", "textarea", "tfoot", "th", "thead", "time", "title", "tr", "track", "tt", "u", "ul", "var", "video", "wbr", "xmp");

	public static final String SPAN = "span";
	public static final String IMAGE = "img";
	public static final String ANCHOR = "a";
	public static final String LI = "li";

	public static String toRuleCheckingTags(String... styles) {
		return toRule(true, " ", styles);
	}

	public static String toRule(String... styles) {
		return toRule(false, " ", styles);
	}

	public static String toCombinedRuleCheckingTags(String... styles) {
		return toRule(true, "", styles);
	}

	public static String toCombinedRule(String... styles) {
		return toRule(false, "", styles);
	}

	public static String toClass(String... styles) {
		String result = "";

		for (String style : styles) {
			if (style.isEmpty())
				continue;
			if (!result.isEmpty())
				result += " ";
			result += style;
		}

		return result;
	}

	private static boolean isReferenceElement(String style) {
		return elementReference.contains(style.toLowerCase());
	}

	private static String toRule(boolean checkIsTag, String separator, String... styles) {
		String result = "";

		for (String style : styles) {
			if (style.isEmpty())
				continue;
			if (!result.isEmpty())
				result += separator;
			result += (checkIsTag && isReferenceElement(style))?style:"."+style;
		}

		return result;
	}

}
