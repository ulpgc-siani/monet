package org.monet.space.kernel.deployer;


public class Messages {
	public static final String ServerError_Message = "Internal error: %s";
	public static final String CantAddModelLibraryError_Message = "Can't add model library.";
	public static final String CantSwapModelError_Message = "Can't swap model, maybe it's in use.";
	public static final String IncompatibleVersionError_Message = "Incompatible versions fault. Space version: %s, Model version: %s.";
	public static final String IncompatibleModelError_Message = "Incompatible models fault. Current model UUID: %s, New model UUID: %s.";

	private Messages() {
	}
}
