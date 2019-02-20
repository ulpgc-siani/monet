package org.monet.space.explorer.control.renders.renders;

import org.monet.space.explorer.control.renders.Render;
import org.monet.space.explorer.model.Label;
import org.monet.space.explorer.model.Message;
import org.siani.itrules.Frame;

import java.io.InputStream;

public class LogoutRender extends Render {

	public static final String NAME = "LogoutRender";

	@Override
	protected Frame createFrame() {
		Frame frame = createMainFrame("Logout");
		frame.addFrame("logoutAction", configuration.getApiUrl() + "/logout");
		frame.addFrame("userNotGrantedLabel", language.getLabel(Label.USER_NOT_GRANTED));
		frame.addFrame("userNotGrantedMessage", language.getMessage(Message.USER_NOT_GRANTED));
		frame.addFrame("logout", language.getLabel(Label.LOGOUT));
		return frame;
	}

	@Override
	protected InputStream getTemplate() {
		return configuration.getTemplate("logout");
	}
}
