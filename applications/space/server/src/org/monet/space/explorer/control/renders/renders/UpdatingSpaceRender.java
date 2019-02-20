package org.monet.space.explorer.control.renders.renders;

import org.monet.space.explorer.model.Label;
import org.monet.space.explorer.model.Message;
import org.monet.space.explorer.control.renders.Render;
import org.siani.itrules.Frame;

import java.io.InputStream;

public class UpdatingSpaceRender extends Render {

	public static final String NAME = "UpdatingSpaceRender";

	@Override
	protected Frame createFrame() {
		Frame frame = createMainFrame("UpdatingSpace");
		frame.addFrame("updatingSpaceLabel", language.getLabel(Label.UPDATING_SPACE));
		frame.addFrame("updatingSpaceMessage", language.getMessage(Message.UPDATING_SPACE));
		frame.addFrame("reload", language.getLabel(Label.RELOAD));
		frame.addFrame("reloadAction", configuration.getUrl());
		return frame;
	}

	@Override
	protected InputStream getTemplate() {
		return configuration.getTemplate("updating-space");
	}
}
