package org.monet.space.kernel.deployer.problems;


public abstract class Problem {

	public static final String SEVERITY_WARNING = "Warning";
	public static final String SEVERITY_ERROR = "Error";

	private String message;
	private String resource;
	private int numberLine;
	private String severity;
	private String stage;

	public Problem() {
	}

	public Problem(String resource, String message, int numberLine, String severity) {
		this.message = message;
		this.resource = resource;
		this.severity = severity;
		this.setNumberLine(numberLine == -1 ? 1 : numberLine);
	}

	protected void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setResource(String resource) {
		this.resource = resource;
	}

	public String getResource() {
		return resource;
	}

	public void setNumberLine(int numberLine) {
		this.numberLine = numberLine;
	}

	public int getNumberLine() {
		return numberLine;
	}

	public void setSeverity(String severity) {
		this.severity = severity;
	}

	public String getSeverity() {
		return severity;
	}

	public String toString(String crlf) {
		String filename = resource != null ? resource : null;
		return String.format("Stage: %s " + crlf + "File: %s " + crlf + " Line: %d " + crlf + " Message: %s" + crlf + crlf, stage, filename, numberLine, message);
	}

	public void setStage(String stage) {
		this.stage = stage;
	}

	public String getStage() {
		return stage;
	}

}
