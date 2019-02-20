package org.monet.mobile.model;

import java.util.Date;

public class ChatItem extends BaseModel {
  public String ServerId;
  public String Message;
  public Long   DateTime;
  public boolean IsOut;
  
  public ChatItem() {
  }
  
  public ChatItem(String serverId, String message, Long datetime, boolean isOut) {
    this.ServerId = serverId;
    this.Message = message;
    this.DateTime = datetime;
    this.IsOut = isOut;
  }
  
  public Date getDateTime() {
    return (this.DateTime == null) ? null : new Date(this.DateTime);
  }
  
  
}
