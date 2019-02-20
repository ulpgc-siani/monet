package org.monet.space.mobile.control;

import org.monet.mobile.exceptions.ActionException;
import org.monet.mobile.service.ActionCode;
import org.monet.mobile.service.Response;
import org.monet.mobile.service.errors.BusinessUnitNotAvailableError;
import org.monet.mobile.service.errors.ServerError;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.constants.ApplicationInterface;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.constants.ErrorCode;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.utils.PersisterHelper;
import org.monet.space.kernel.utils.StreamHelper;
import org.monet.space.mobile.ApplicationMobile;
import org.monet.space.mobile.control.actions.Action;
import org.monet.space.mobile.control.actions.ActionFactory;
import org.monet.space.mobile.control.actions.TypedAction;
import org.monet.space.mobile.library.LibraryRequest;
import org.monet.space.mobile.model.Language;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringWriter;
import java.util.zip.GZIPOutputStream;

public class Controller extends HttpServlet {

	private static final long serialVersionUID = -9110665304913574501L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AgentLogger logger = AgentLogger.getInstance();
		ActionFactory actionFactory = ActionFactory.getInstance();
		String idSession = request.getSession().getId();
		Context oContext = Context.getInstance();
		Long idThread = Thread.currentThread().getId();

		GZIPOutputStream outputStream = null;
		Response result = null;
		String actionCode = request.getParameter("op");

		try {
			oContext.setApplication(idThread, LibraryRequest.getRealIp(request), ApplicationMobile.NAME, ApplicationInterface.USER);
			oContext.setUserServerConfig(idThread, request.getServerName(), request.getContextPath(), request.getServerPort());
			oContext.setSessionId(idThread, idSession);
			oContext.setDatabaseConnectionType(idThread, Database.ConnectionTypes.AUTO_COMMIT);
			Language.fillCurrentLanguage(request);

			try {
				if ((!BusinessUnit.isRunning()) || (!ApplicationMobile.isRunning())) {
					result = new Response(new BusinessUnitNotAvailableError());
				} else {
					logger.debug("Request action: %s", actionCode);
					Action<?> action = (Action<?>) actionFactory.get(ActionCode.valueOf(actionCode));
					if (action instanceof TypedAction)
						result = new Response(((TypedAction<?, ?>) action).execute(request, response));
					else {
						action.execute(request, response);
						return;
					}
				}
			} catch (ActionException e) {
				result = new Response(e.getErrorResult());
			} catch (Exception e) {
				logger.error(e);
				result = new Response(new ServerError());
			}

			response.setHeader("Content-Encoding", "gzip");
			response.setContentType("text/xml");
			outputStream = new GZIPOutputStream(response.getOutputStream());
			StringWriter writer = new StringWriter();
			PersisterHelper.save(writer, result);
			outputStream.write(writer.toString().getBytes("UTF-8"));
		} catch (Exception e) {
			logger.error(e);
			try {
				PersisterHelper.save(outputStream, new Response(new ServerError()));
			} catch (Exception ex) {
				logger.error(ex);
			}
		}
		StreamHelper.close(outputStream);
		StreamHelper.close(response.getOutputStream());
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AgentLogger logger = AgentLogger.getInstance();
		ActionFactory actionFactory = ActionFactory.getInstance();
		String idSession = request.getSession().getId();
		Context oContext = Context.getInstance();
		Long idThread = Thread.currentThread().getId();
		String result = null;

		String actionCode = request.getParameter("op");

		try {
			oContext.setApplication(idThread, LibraryRequest.getRealIp(request), ApplicationMobile.NAME, ApplicationInterface.USER);
			oContext.setUserServerConfig(idThread, request.getServerName(), request.getContextPath(), request.getServerPort());
			oContext.setSessionId(idThread, idSession);
			oContext.setDatabaseConnectionType(idThread, Database.ConnectionTypes.AUTO_COMMIT);
			Language.fillCurrentLanguage(request);

			try {
				if ((!BusinessUnit.isRunning()) || (!ApplicationMobile.isRunning())) {
					result = ErrorCode.BUSINESS_UNIT_STOPPED;
				} else {
					logger.debug("Request action: %s", actionCode);
					Action<?> action = actionFactory.get(ActionCode.valueOf(actionCode));
					action.execute(request, response);
					return;
				}
			} catch (ActionException e) {
				result = e.getErrorResult().toString();
			} catch (Exception e) {
				logger.error(e);
				result = (new ServerError()).toString();
			}
			response.getWriter().write(result);
		} catch (Exception e) {
			logger.error(e);
		}
	}
}
