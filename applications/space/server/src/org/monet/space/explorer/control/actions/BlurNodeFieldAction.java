package org.monet.space.explorer.control.actions;

import org.monet.space.explorer.control.dialogs.NodeFieldDialog;
import org.monet.space.kernel.listeners.ListenerPushService;

import java.io.IOException;

public class BlurNodeFieldAction extends BlurNodeAction<NodeFieldDialog>{

    @Override
    public void execute() throws IOException {
        sendPushMessage(ListenerPushService.PushClientMessages.REFRESH_OBSERVER);
        display.write("OK");
    }
}
