package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.model.List;

import java.io.OutputStream;
import java.io.PrintWriter;

public interface Display<T> {
	void write(T result);
	void writeList(List result);
	void writeError(String error);
	OutputStream getOutputStream();
	PrintWriter getWriter();
	void setContentLength(int length);
	void setContentType(String contentType);
	void redirectTo(String location);
	void addHeader(String name, String value);
}
