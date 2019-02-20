package org.monet.bpi;

public abstract class ClientService {

	protected static ClientService instance;

	public static void redirectUserTo(MonetLink monetLink) {
		instance.redirectUserToImpl(monetLink);
	}

	public static void sendMessageToUser(String message) {
		instance.sendMessageToUserImpl(message);
	}

	protected abstract void redirectUserToImpl(MonetLink monetLink);

	protected abstract void sendMessageToUserImpl(String message);
}
