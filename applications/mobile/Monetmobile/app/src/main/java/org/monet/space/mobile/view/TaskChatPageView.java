package org.monet.space.mobile.view;

import org.monet.space.mobile.model.ChatItem;
import org.monet.space.mobile.mvp.View;

public interface TaskChatPageView extends View {

  void addChatItem(ChatItem item);
  
}
