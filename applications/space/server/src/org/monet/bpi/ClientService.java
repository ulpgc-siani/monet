package org.monet.bpi;

import org.monet.bpi.types.File;

public abstract class ClientService {

	protected static ClientService instance;

	public static void redirectUserTo(MonetLink monetLink) {
		instance.redirectUserToImpl(monetLink);
	}

	public static void sendMessageToUser(String message) {
		instance.sendMessageToUserImpl(message);
	}

	public static void sendFileToUser(File file) {
		instance.sendFileToUserImpl(file);
	}

	public static User discoverUserInSession() {
		return instance.discoverUserInSessionImpl();
	}

	protected abstract void redirectUserToImpl(MonetLink monetLink);

	protected abstract void sendMessageToUserImpl(String message);

	protected abstract void sendFileToUserImpl(File file);

	protected abstract User discoverUserInSessionImpl();

}
