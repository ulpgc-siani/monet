package org.monet.bpi;

public interface Role {

	String getId();

	String getName();

	String getLabel();

	User getUser();

	String getPartnerName();

	String getServiceUrl();

	String getFeederUrl();

}
