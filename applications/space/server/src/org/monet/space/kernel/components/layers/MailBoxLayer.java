package org.monet.space.kernel.components.layers;

import org.monet.space.kernel.machines.ttm.model.MailBox;

public interface MailBoxLayer extends Layer {

    boolean exists(String mailBoxId);

	MailBox load(String id);

	void create(MailBox mailBox);

	void delete(String id);

	void deleteWithTaskId(String taskId);

	void addPermission(String mailBoxId, String userId);

	void removePermission(String mailBoxId, String userId);

	boolean hasPermission(String mailBoxId, String userId);

}
