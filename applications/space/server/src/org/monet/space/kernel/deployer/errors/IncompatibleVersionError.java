package org.monet.space.kernel.deployer.errors;

import org.monet.space.kernel.deployer.Messages;
import org.monet.space.kernel.deployer.problems.Error;

public class IncompatibleVersionError extends Error {

	public IncompatibleVersionError(String spaceVersion, String modelVersion) {
		super("", String.format(Messages.IncompatibleVersionError_Message, spaceVersion, modelVersion), 0);
	}
}
