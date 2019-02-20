package client.core.model;

public interface BusinessUnit extends Entity {

	ClassName CLASS_NAME = new ClassName("BusinessUnit");

	ClassName getClassName();
	String getName();
	boolean isActive();
	boolean isDisabled();
	String getUrl();
	boolean isMember();
	boolean isPartner();
	boolean equals(Object object);

}
