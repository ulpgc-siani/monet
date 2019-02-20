package org.monet.space.kernel.components.layers;

import org.monet.space.kernel.model.MessageQueueItem;

import java.io.InputStream;
import java.util.List;

public interface MessageQueueLayer extends Layer {

	long create(String orderId, String url, InputStream message, String code, String type, String hash);

	void save(MessageQueueItem item);

	void delete(MessageQueueItem item);

	List<MessageQueueItem> loadNotSent();

	InputStream getMessageStream(MessageQueueItem item);

}
