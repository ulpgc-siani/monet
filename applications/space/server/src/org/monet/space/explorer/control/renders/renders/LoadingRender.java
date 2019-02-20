package org.monet.space.explorer.control.renders.renders;

import org.monet.space.explorer.control.renders.Render;
import org.siani.itrules.Frame;

import java.io.InputStream;

public class LoadingRender extends Render {

	public static final String NAME = "LoadingRender";

	@Override
	protected Frame createFrame() {
		return createMainFrame("Loading");
	}

	@Override
	protected InputStream getTemplate() {
		return configuration.getTemplate("loading");
	}
}
