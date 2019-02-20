package org.monet.mocks.businessunit.control.actions;

import org.monet.mocks.businessunit.Service;
import org.monet.mocks.businessunit.control.Action;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.Map;

public class ActionCallback extends Action {

	@Override
	public void execute(Map<String, Object> params, HttpServletResponse response) throws Exception {

		if (!params.containsKey("message")) {
			System.out.println(params.get("signaling"));
			response.getWriter().print("OK");
			return;
		}

		Service.Message message = service.readMessage((InputStream)params.get("message"));
		System.out.println(String.format("\nMensaje recibido\n-------------------------\n%s\n\n", message.getContent()));

		response.getWriter().print("OK");
	}

}
