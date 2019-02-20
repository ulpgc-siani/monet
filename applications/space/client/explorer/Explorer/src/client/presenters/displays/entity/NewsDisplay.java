package client.presenters.displays.entity;

import client.core.model.News;
import client.core.model.View;
import client.presenters.displays.EntityDisplay;

public class NewsDisplay extends EntityDisplay<News, View> {

	public static final Type TYPE = new Type("NewsDisplay", EntityDisplay.TYPE);

	public NewsDisplay(News news, View view) {
		super(news, view);
	}

	@Override
	protected void onInjectServices() {
	}

	@Override
	public Type getType() {
		return TYPE;
	}
}
