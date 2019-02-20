package org.monet.space.explorer.control.displays;

import org.monet.space.explorer.control.displays.serializers.NotificationSerializer;
import org.monet.space.explorer.control.displays.serializers.Serializer;
import org.monet.space.kernel.model.Notification;

public class NotificationDisplay extends HttpDisplay<Notification> {

	@Override
	protected Serializer getSerializer(Notification object) {
		return new NotificationSerializer(createSerializerHelper());
	}

}
