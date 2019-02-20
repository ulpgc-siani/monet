package org.monet.space.explorer.control.actions;

import org.monet.space.explorer.control.dialogs.NodeDialog;
import org.monet.space.kernel.listeners.ListenerPushService;

import java.io.IOException;

public class BlurNodeViewAction extends BlurNodeAction<NodeDialog> {

    @Override
    public void execute() throws IOException {
        sendPushMessage(ListenerPushService.PushClientMessages.REMOVE_OBSERVER);
        display.write("OK");
    }
}
