package org.monet.mocks.businessunit.control;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.http.LibraryRequest;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Singleton
public class Controller extends HttpServlet {
	private ActionsFactory actionsFactory;

	@Inject
	public void injectActionsFactory(ActionsFactory actionsFactory) {
		this.actionsFactory = actionsFactory;
	}

	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			Map<String, Object> params = LibraryRequest.parseParameters(request);
			String actionName = (String)params.get(RequestParams.REQUEST_PARAM_ACTION);

			Action action = this.actionsFactory.create(actionName);
			if(action != null) action.execute(params, response);
			else
				System.out.println(String.format("Action(%s) not found", actionName));

		} catch (Exception e) {
			System.out.println(e.getMessage());
			response.sendError(500, e.getMessage());
		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}
}
