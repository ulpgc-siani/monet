package client.core.model;

import cosmos.types.Date;

public interface Fact extends Instance {

	ClassName CLASS_NAME = new ClassName("Fact");

	String getTitle();
	String getSubTitle();
	String getUser();
	List<MonetLink> getLinks();
	Date getCreateDate();

}
