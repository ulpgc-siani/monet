package org.monet.space.kernel.deployer.problems;

public class Error extends Problem {

	public Error(String filename, String message, int numberLine) {
		super(filename, message, numberLine, Problem.SEVERITY_ERROR);
	}
}
