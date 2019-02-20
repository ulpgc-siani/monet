package org.monet.mocks.businessunit.control.actions;

import org.monet.mocks.businessunit.control.Action;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

public class ActionRequestService extends Action {

	@Override
	public void execute(Map<String, Object> params, HttpServletResponse response) throws Exception {
		String name = (String)params.get("service");

		this.service.requestService(name, true, new Date(), new Date(), "");
	}

}
