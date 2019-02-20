package cosmos.utils;

public class StringUtils {

	private static final int SHORT_CONTENT_LENGTH = 50;

	public static String title(String content) {
		return content.substring(0, 1).toUpperCase() + content.substring(1);
	}

	public static String sentence(String content) {
		String result = "";
		String[] wordsArray = content.split(" ");
		
		for (int i=0; i<wordsArray.length; i++) {
			String word = wordsArray[i];
			char[] wordArray = word.trim().toCharArray();
			wordArray[0] = Character.toUpperCase(wordArray[0]);
			word = new String(wordArray);

			result += ((i!=0)?" ":"") + word;
		}
		
		return result;
	}

	public static String shortContent(String content) {
		return shortContent(content, SHORT_CONTENT_LENGTH);
	}

	public static String shortContent(String content, int length) {

		if (content.length() < length)
			return content;

		return content.substring(0, length) + "...";
	}

	public static boolean isNumber(String value) {
		return isLong(value);
	}

    public static boolean isLong(String value) {
        try {
            Long.valueOf(value);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

	public static boolean isDouble(String value) {
		try {
			Double.valueOf(value);
			return true;
		} catch (NumberFormatException ex) {
			return false;
		}
	}
}
