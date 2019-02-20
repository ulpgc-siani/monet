package cosmos.gwt.model;

public interface Theme {
	String THEME = "theme";

	String getName();
	void initFromHostElement();
	String getLayout(String name);

	boolean existsLayout(String name);

	String getLogoPath();
	String getLogoReducedPath();

	String getAddPhotoPath();
	String getAddPhotoReducedPath();
}
