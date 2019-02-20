package org.monet.space.kernel.deployer.errors;

import org.monet.space.kernel.deployer.Messages;
import org.monet.space.kernel.deployer.problems.Error;

public class ServerError extends Error {

	public ServerError(String name, String message) {
		super(name, String.format(Messages.ServerError_Message, message), 0);
	}
}
