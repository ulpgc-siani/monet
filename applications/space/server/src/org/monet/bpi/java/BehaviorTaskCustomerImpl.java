package org.monet.bpi.java;

import org.monet.bpi.BehaviorTaskCustomer;
import org.monet.bpi.CustomerRequest;
import org.monet.bpi.CustomerResponse;

public class BehaviorTaskCustomerImpl extends BehaviorTaskBaseComponent implements BehaviorTaskCustomer {

	@Override
	public void onInit() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onAborted() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onExpiration() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onRequest(CustomerRequest msg) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onImportRequest(String code, CustomerRequest request) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onConstructResponse(String code, CustomerResponse response) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onChatMessageReceived(CustomerResponse response) {
		// TODO Auto-generated method stub

	}

}
