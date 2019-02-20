package org.monet.space.kernel.machines.ttm;

import org.monet.space.kernel.machines.ttm.model.MailBox;
import org.monet.space.kernel.machines.ttm.model.MailBox.Type;
import org.monet.space.kernel.machines.ttm.model.Message;
import org.monet.space.kernel.machines.ttm.model.Signaling;
import org.monet.space.kernel.model.MailBoxUri;
import org.monet.space.kernel.model.TaskOrder;
import org.monet.space.kernel.model.User;

public interface CourierService {

	void deliver(User sender, Message message);

	void signaling(User sender, MailBoxUri mailBoxUri, Signaling signal);

	void signaling(String orderId, MailBoxUri mailBoxUri, Signaling signal);

    void send(String orderId, Message message);

	void sendDelayed(String orderId, Message message);

    MailBoxUri openChannel(MailBoxUri fromMailBoxUri, TaskOrder taskOrder, String taskName);

    MailBoxUri openChannel(MailBoxUri fromMailBoxUri, String taskName);

	MailBox createMailBox(String name, String taskId, Type type, User user);

	void destroyMailBox(MailBoxUri localMailBoxUri);

}
