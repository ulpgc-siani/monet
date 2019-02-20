package org.monet.grided.control;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.monet.grided.control.actions.Action;
import org.monet.grided.control.actions.ActionsFactory;
import org.monet.grided.control.actions.impl.BaseAction;
import org.monet.grided.control.log.Logger;
import org.monet.grided.core.serializers.json.impl.JSONErrorResponse;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AppController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ActionsFactory actionsFactory;
	private Logger logger;

	@Inject
	public AppController(ActionsFactory actionsFactory, Logger logger) {
		super();
		this.actionsFactory = actionsFactory;
		this.logger = logger;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcessRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcessRequest(request, response);
	}

	public void doProcessRequest(HttpServletRequest request, HttpServletResponse response) {
		String operation = request.getParameter("op");

		try {
			Action action = this.actionsFactory.create(operation);
			action.execute(request, response);

		} catch (Exception ex) {
			this.logger.error(ex.getMessage(), ex);
			ex.printStackTrace();
			BaseAction.sendResponse(response, JSONErrorResponse.ERROR);
		}
	}
}
