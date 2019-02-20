package org.monet.mocks.businessunit.control;

import com.google.inject.Inject;
import org.monet.mocks.businessunit.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public abstract class Action {

	protected Service service;

	@Inject
	public void injectService(Service service) {
		this.service = service;
	}

	public abstract void execute(Map<String, Object> params, HttpServletResponse response) throws Exception;
}
