package org.monet.federation.accountoffice.control.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.federation.accountoffice.core.database.DataRepository;
import org.monet.federation.accountoffice.core.layers.account.AccountLayer.LoginMode;
import org.monet.federation.accountoffice.core.model.User;
import org.monet.federation.accountoffice.utils.Utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.apache.commons.lang.StringEscapeUtils.escapeHtml;

@Singleton
public class AutoAuthorizationServlet extends FederationServlet {
	private static final long serialVersionUID = -8886718705718794983L;

	private static final String XML_RESPONSE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><response code=\"%s\"><verifier>%s</verifier></response>";
	private static final String CODE_IP_SUSPEND = "IP_SUSPEND";
	private static final String CODE_LOGIN_SUCCESSFULLY = "SUCCESSFULLY";
	private static final String CODE_LOGIN_INCORRECT = "INCORRECT";

	private DataRepository dataRepository;

	@Inject
	public void injectDataRepository(DataRepository dataRepository) {
		this.dataRepository = dataRepository;
	}

	@Override
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
		if (!this.isFederationRunning(response))
			return;

		this.initialize(response);

		String requestToken = escapeHtml(request.getParameter("oauth_token"));

		String lang;
		String prefLanguage = request.getHeader("Accept-Language");
		if (prefLanguage == null) {
			lang = this.configuration.isLanguageSupport("");
			request.getSession().setAttribute("lang", lang);
		} else {
			int indexPre = prefLanguage.indexOf('-');
			prefLanguage = prefLanguage.substring(0, indexPre);
			lang = this.configuration.isLanguageSupport(prefLanguage);
			request.getSession().setAttribute("lang", lang);
		}

		String user = escapeHtml(request.getParameter("username"));
		String space = escapeHtml(request.getParameter("space"));
		String rememberme = escapeHtml(request.getParameter("rememberme"));

		String remoteAddr = Utils.getAddress(request);
		String remoteUA = request.getHeader("User-Agent").toLowerCase();

		if (this.dataRepository.isIpBanned(remoteAddr))
			response.getOutputStream().write(String.format(XML_RESPONSE, CODE_IP_SUSPEND, "null").getBytes());
		else {
			boolean verifyUser = this.accountLayer.login(request, LoginMode.PASSWORD) != null;
			if (!verifyUser) {
				this.dataRepository.addIpWrongAccess(remoteAddr);
				response.getOutputStream().write(String.format(XML_RESPONSE, CODE_LOGIN_INCORRECT, "null").getBytes());
			} else {
				request.getSession().setAttribute("user", user);

				String accessToken;
				if (rememberme == null || rememberme.equals("false"))
					accessToken = this.sessionComponent.addUser(user, remoteAddr, remoteUA, lang, false, true, space);
				else {
					request.getSession().setMaxInactiveInterval((int) this.configuration.getMaxRememberTime() / 1000);
					accessToken = this.sessionComponent.addUser(user, remoteAddr, remoteUA, lang, true, true, space);
				}

				this.requestTokenComponent.setAccessToken(requestToken, accessToken);
				this.dataRepository.removeIpBan(remoteAddr);
				String verifier = this.requestTokenComponent.generateVerifier(requestToken);

				this.registerMobileDevice(user, request.getParameter("deviceId"));

				response.getOutputStream().write(String.format(XML_RESPONSE, CODE_LOGIN_SUCCESSFULLY, verifier).getBytes());
			}
		}
	}

	protected void registerMobileDevice(String username, String deviceId) {
		User user = this.accountLayer.loadFromUsername(username);
		this.dataRepository.registerMobileDevice(user.getId(), deviceId);
	}

}
