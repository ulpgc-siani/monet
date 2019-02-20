package org.monet.bpi;

public interface BehaviorTaskContestant {

	void onInit();

	void onRequest(ContestantRequest msg);

	void onImportRequest(String code, ContestantRequest request);

	void onConstructResponse(String code, ContestantResponse response);

}
