package org.monet.federation.accountoffice.control.servlets;

import com.google.inject.Singleton;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.monet.filesystem.StreamHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Singleton
public class AuthorizationResourcesServlet extends FederationServlet {
	private static final long serialVersionUID = 8389601486760256588L;

	@Override
	protected void doProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {

		if (!this.isFederationRunning(response)) return;

		this.initialize(response);

		String id = request.getParameter("id");

		if (id.equals("logo")) {
			response.setContentType("image/png");
			response.setHeader("Content-Disposition", String.format("attachment; filename=%s.png", id));
			InputStream img = new FileInputStream(this.configuration.getLogoFilename());
			StreamHelper.copy(img, response.getOutputStream());
		} else if (id.equals("space")) {
			response.setContentType("image/png");
			response.setHeader("Content-Disposition", String.format("attachment; filename=%s.png", id));
			InputStream img = new FileInputStream(this.configuration.getDefaultSpaceLogoFilename());
			StreamHelper.copy(img, response.getOutputStream());
		} else if (id.equals("captcha")) {
			response.setContentType("image/png");
			response.setHeader("Content-Disposition", String.format("attachment; filename=%s.png", id));
			Captcha captcha = new Captcha.Builder(200, 50).addText().addBackground().addNoise().gimp().addBorder().build();
			String nvUser = (String) request.getSession().getAttribute("nvUser");
			if (nvUser != null) {
				this.sessionComponent.setCaptchaAnswerToUser(nvUser, captcha.getAnswer());
				CaptchaServletUtil.writeImage(response, captcha.getImage());
			}
		}
	}

}
