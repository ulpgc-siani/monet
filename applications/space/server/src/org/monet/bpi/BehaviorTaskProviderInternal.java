package org.monet.bpi;

public interface BehaviorTaskProviderInternal extends BehaviorTaskProvider {

	void onResponse(InsourcingResponse msg);

	void onChatMessageReceived(InsourcingResponse msg);

	void onConstructRequest(String code, InsourcingRequest request);

	void onImportResponse(String code, InsourcingResponse response);

}
