package org.monet.space.mobile.events;

import org.monet.space.mobile.model.ChatItem;

public class ChatMessageReceivedEvent {

    public long TaskId;
    public ChatItem Message;
    public boolean Captured;

    public ChatMessageReceivedEvent(long taskId, ChatItem chatItem) {
        this.TaskId = taskId;
        this.Message = chatItem;
    }

}
