package org.monet.deployservice.utils;

public class ExceptionPrinter {

	private static final String NL = System.getProperty("line.separator");	
	
	public String printToString(Throwable throwable) {
		StringBuilder sb = new StringBuilder();		
				
		for (Throwable e = throwable; e != null; e = e.getCause()) {
			sb.append(NL);
			sb.append(e == throwable ? "Exception:" : "Caused By:").append(NL);
			sb.append("----------").append(NL);

			if (AppException.class.isInstance(throwable)) {
				AppException ex = (AppException) throwable; 
				sb.append(">>> ErrorCode: ").append(ex.getErrorCode()).append(NL);
			}

			sb.append(">>> Type: ").append(e.getClass().getName()).append(NL);
			sb.append(">>> Message: ").append(e.getMessage()).append(NL);			
			StackTraceElement[] elems = e.getStackTrace();
			if (elems != null && elems.length > 0) {
				sb.append(">>> Stack trace: ").append(NL);

				for (StackTraceElement elem : elems) {
					sb.append(">>>     at ");
					sb.append(elem.getClassName());
					sb.append('.');
					sb.append(elem.getMethodName());
					sb.append('(');
					sb.append(elem.getFileName());
					sb.append(':');
					sb.append(Math.max(1, elem.getLineNumber()));
					sb.append(')');
					sb.append(NL);
				}
			}
		}
		return sb.toString();
	}
}
