package org.monet.space.kernel.deployer.errors;

import org.monet.space.kernel.deployer.Messages;
import org.monet.space.kernel.deployer.problems.Error;

public class CantSwapModelError extends Error {

	public CantSwapModelError() {
		super("", String.format(Messages.CantSwapModelError_Message, ""), 0);
	}
}
