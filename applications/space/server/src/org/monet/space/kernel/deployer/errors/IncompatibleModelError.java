package org.monet.space.kernel.deployer.errors;

import org.monet.space.kernel.deployer.Messages;
import org.monet.space.kernel.deployer.problems.Error;

public class IncompatibleModelError extends Error {

	public IncompatibleModelError(String spaceUUID, String modelUUID) {
		super("", String.format(Messages.IncompatibleVersionError_Message, spaceUUID, modelUUID), 0);
	}
}
