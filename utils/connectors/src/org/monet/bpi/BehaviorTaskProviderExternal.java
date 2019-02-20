package org.monet.bpi;

public interface BehaviorTaskProviderExternal extends BehaviorTaskProvider {

	void onResponse(ProviderResponse msg);

	void onChatMessageReceived(ProviderResponse msg);

	void onConstructRequest(String code, ProviderRequest request);

	void onImportResponse(String code, ProviderResponse response);

}
