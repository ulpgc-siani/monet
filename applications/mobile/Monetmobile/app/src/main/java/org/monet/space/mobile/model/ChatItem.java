package org.monet.space.mobile.model;

import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

public class ChatItem implements Parcelable {

  public long    Id;
  public String  ServerId;
  public String  Message;
  public Date    DateTime;
  public boolean IsOut;
  public boolean IsSent;

  public ChatItem(long id, String serverId, String message, Date datetime, boolean isOut, boolean isSent) {
    this.Id = id;
    this.ServerId = serverId;
    this.Message = message;
    this.DateTime = datetime;
    this.IsOut = isOut;
    this.IsSent = isSent;
  }

  public static final Parcelable.Creator<ChatItem> CREATOR = new Parcelable.Creator<ChatItem>() {
    public ChatItem createFromParcel(Parcel in) {
      return new ChatItem(in);
    }

    public ChatItem[] newArray(int size) {
      return new ChatItem[size];
    }
  };

  private ChatItem(Parcel in) {
    Id = in.readLong();
    ServerId = in.readString();
    Message = in.readString();
    DateTime = (java.util.Date) in.readSerializable();
    IsOut = in.readByte() != 0;  
    IsSent = in.readByte() != 0;  
  }

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeLong(Id);
    dest.writeString(ServerId);
    dest.writeString(Message);
    dest.writeSerializable(DateTime);
    dest.writeByte((byte) (IsOut ? 1 : 0));  
    dest.writeByte((byte) (IsSent ? 1 : 0));  
  }
  
  
}
