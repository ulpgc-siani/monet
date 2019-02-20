package org.monet.mobile.service.results;

import java.util.ArrayList;

import org.monet.mobile.model.ChatItem;
import org.monet.mobile.service.Result;

public class SyncChatsResult extends Result {
  
  public ArrayList<ChatItem> Chats;
  public long SyncMark;

}
