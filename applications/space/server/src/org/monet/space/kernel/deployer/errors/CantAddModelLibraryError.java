package org.monet.space.kernel.deployer.errors;

import org.monet.space.kernel.deployer.Messages;
import org.monet.space.kernel.deployer.problems.Error;

public class CantAddModelLibraryError extends Error {

	public CantAddModelLibraryError() {
		super("", String.format(Messages.CantAddModelLibraryError_Message, ""), 0);
	}
}
