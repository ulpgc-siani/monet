package client.utils;

public class PathUtils {

	public static String getImagesPath(String image) {
		return "images/" + image;
	}

	public static String getThemeImagesPath(String theme, String image) {
		return "themes/" + theme + "/images/" + image;
	}

}
