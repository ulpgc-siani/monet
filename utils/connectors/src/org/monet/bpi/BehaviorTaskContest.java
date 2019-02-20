package org.monet.bpi;

public interface BehaviorTaskContest {

	void onInit();

	void onResponse(TransferResponse msg);

	void onTerminate();

	void onConstructRequest(String code, TransferRequest request);

	void onImportResponse(String code, TransferResponse response);

}
