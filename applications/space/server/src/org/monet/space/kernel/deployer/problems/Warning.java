package org.monet.space.kernel.deployer.problems;

import java.io.File;


public class Warning extends Problem {

	public Warning(File resource, String message, int numberLine) {

		//TODO
		//super(resource, message, numberLine, Problem.SEVERITY_WARNING);
		super("", message, numberLine, Problem.SEVERITY_WARNING);
	}
}
