package org.monet.bpi;

public interface Role {

	String getId();

	String getName();

	String getLabel();

	User getUser();

	String getServiceUrl();

	String getFeederUrl();

}
