package org.monet.bpi;

public interface BehaviorTaskProvider {

	void onInit();

	void onTerminate();

	void onRejected();

	void onExpiration();

}
