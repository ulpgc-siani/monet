package org.monet.federation.accountoffice.control.servlets;

import com.google.inject.Singleton;
import com.itextpdf.text.pdf.BarcodeQRCode;
import nl.captcha.Captcha;
import nl.captcha.servlet.CaptchaServletUtil;
import org.monet.filesystem.StreamHelper;
import org.monet.federation.accountoffice.core.components.totpcomponent.TotpComponent;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Singleton
public class AuthorizationResourcesServlet extends FederationServlet {
	private static final long serialVersionUID = 8389601486760256588L;
	@com.google.inject.Inject
	private TotpComponent totpComponent;

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
		} else if (id.equals("totpqr")) {
			TotpComponent.PendingChallenge challenge = this.totpComponent.getPendingChallenge(request.getSession());
			if (challenge == null || !challenge.isSetupMode() || challenge.getOtpAuthUri() == null || challenge.getOtpAuthUri().isEmpty()) {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return;
			}

			response.setContentType("image/png");
			response.setHeader("Content-Disposition", "inline; filename=totpqr.png");
			BufferedImage qrImage = createQrImage(challenge.getOtpAuthUri(), 180, 180);
			ImageIO.write(qrImage, "png", response.getOutputStream());
		}
	}

	private BufferedImage createQrImage(String content, int width, int height) {
		BarcodeQRCode barcodeQRCode = new BarcodeQRCode(content, width, height, null);
		Image awtImage = barcodeQRCode.createAwtImage(java.awt.Color.BLACK, java.awt.Color.WHITE);
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D graphics = bufferedImage.createGraphics();
		try {
			graphics.drawImage(awtImage, 0, 0, width, height, null);
		} finally {
			graphics.dispose();
		}
		return bufferedImage;
	}

}
