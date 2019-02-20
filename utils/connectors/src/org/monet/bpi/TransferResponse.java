package org.monet.bpi;

public interface TransferResponse {

	String getCode();

	String getContent();

	Node getNode(String name);

}
