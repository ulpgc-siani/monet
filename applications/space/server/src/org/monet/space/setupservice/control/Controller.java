/*
    Monet will assist business to process re-engineering. Monet separate the
    business logic from the underlying technology to allow Model-Driven
    Engineering (MDE). These models guide all the development process over a
    Service Oriented Architecture (SOA).

    Copyright (C) 2009  Grupo de Ingenieria del Sofware y Sistemas de la Universidad de Las Palmas de Gran Canaria

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as
    published by the Free Software Foundation, either version 3 of the
    License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see http://www.gnu.org/licenses/.
*/

// TODO Manejar exepciones en los adaptadores

package org.monet.space.setupservice.control;

import org.apache.commons.codec.binary.Base64;
import org.monet.encrypt.CertificateVerifier;
import org.monet.encrypt.extractor.CertificateExtractor;
import org.monet.encrypt.extractor.ExtractorUser;
import org.monet.space.applications.library.LibraryRequest;
import org.monet.space.kernel.agents.AgentLogger;
import org.monet.space.kernel.agents.AgentSession;
import org.monet.space.kernel.components.ComponentFederation;
import org.monet.space.kernel.components.layers.MasterLayer;
import org.monet.space.kernel.constants.ApplicationInterface;
import org.monet.space.kernel.constants.Database;
import org.monet.space.kernel.model.BusinessUnit;
import org.monet.space.kernel.model.Context;
import org.monet.space.kernel.model.Language;
import org.monet.space.kernel.model.UserInfo;
import org.monet.space.setupservice.ApplicationSetupService;
import org.monet.space.setupservice.control.actions.Action;
import org.monet.space.setupservice.control.actions.ActionsFactory;
import org.monet.space.setupservice.control.constants.Actions;
import org.monet.space.setupservice.control.constants.Parameter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.UUID;

public class Controller extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet {
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig oConfiguration) throws ServletException {
		super.init(oConfiguration);
	}

	private String createSession() {
		AgentSession agentSession = AgentSession.getInstance();
		String idSession = UUID.randomUUID().toString();
		org.monet.space.kernel.model.Session session = agentSession.get(idSession);

		if (session == null) {
			agentSession.add(idSession);
		}

		return idSession;
	}

	private Boolean doRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String result, operation = null;
		Action action;
		String idSession = this.createSession();
		Context context = Context.getInstance();
		Long idThread = Thread.currentThread().getId();
		ArrayList<Entry<String, Object>> parameterList = LibraryRequest.parametersToList(request);
		HashMap<String, Object> parameters = LibraryRequest.parametersToMap(parameterList);
		String signature, timestamp;
		MasterLayer masterLayer = ComponentFederation.getMasterLayer();

		try {
			context.setApplication(idThread, LibraryRequest.getRealIp(request), ApplicationSetupService.NAME, ApplicationInterface.USER);
			context.setUserServerConfig(idThread, request.getServerName(), request.getContextPath(), request.getServerPort());
			context.setSessionId(idThread, idSession);
			context.setDatabaseConnectionType(idThread, Database.ConnectionTypes.AUTO_COMMIT);
			Language.fillCurrentLanguage(request);

			if ((operation = (String) parameters.get(Parameter.OPERATION)) == null) {
				context.clear(idThread);
				return false;
			}

			signature = (String) parameters.get(Parameter.SIGNATURE);
			timestamp = (String) parameters.get(Parameter.TIMESTAMP);

			if (signature == null) {
				response.setStatus(403);
				response.getWriter().println("No signature on request");
				return false;
			}

			if (timestamp == null) {
				response.setStatus(403);
				response.getWriter().println("No timestamp on request");
				return false;
			}

			byte[] signatureBytes = Base64.decodeBase64(signature);
			X509Certificate certificate = CertificateVerifier.getCertificateFromSignature(signatureBytes);
			CertificateExtractor extractor = new CertificateExtractor();
			ExtractorUser user = extractor.extractUser(certificate);
			String authority = extractor.extractAuthorityName(certificate);

			if (!BusinessUnit.getInstance().isInstalled()) {
				UserInfo info = new UserInfo();

				info.setFullname(user.getFullname());
				info.setEmail(user.getEmail());

				masterLayer.addMaster(user.getUsername(), authority, true, info);
			} else {
				if (!masterLayer.exists(user.getUsername(), authority)) {
					response.setStatus(403);
					response.getWriter().println("master not granted to upload model");
					return false;
				}
			}

			action = ActionsFactory.getInstance().get(operation, request, response, parameters);
			result = action.execute();
		} catch (Throwable exception) {
			AgentLogger.getInstance().error(exception);
			if (operation != null && !operation.equals(Actions.UPDATE_MODEL)) {
				response.setStatus(500);
				response.getWriter().println(exception.getMessage());
			}
			return false;
		} finally {
			context.clear(idThread);
			AgentSession.getInstance().remove(idSession);
		}

		if (result != null) response.getWriter().print(result);

		return true;
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		this.doRequest(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
		throws ServletException, IOException {
		this.doRequest(request, response);
	}

}
