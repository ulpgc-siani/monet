package org.monet.bpi;

public interface BehaviorTaskCustomer {

	void onInit();

	void onAborted();

	void onExpiration();

	void onRequest(CustomerRequest msg);

	void onImportRequest(String code, CustomerRequest request);

	void onConstructResponse(String code, CustomerResponse response);

	void onChatMessageReceived(CustomerResponse response);

}
