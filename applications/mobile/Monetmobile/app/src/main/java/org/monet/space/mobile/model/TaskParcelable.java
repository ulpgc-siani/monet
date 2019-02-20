package org.monet.space.mobile.model;

import org.monet.mobile.model.Task;

import android.os.Parcel;
import android.os.Parcelable;

public class TaskParcelable implements Parcelable {

  private Task mData;

  public int describeContents() {
      return 0;
  }

  public void writeToParcel(Parcel out, int flags) {
      out.writeString(mData.Code);
      out.writeString(mData.Description);
      out.writeString(mData.ID);
      out.writeString(mData.Label);
      out.writeDouble(mData.PositionLat != null ? mData.PositionLat : Double.MAX_VALUE);
      out.writeDouble(mData.PositionLon != null ? mData.PositionLon : Double.MAX_VALUE);
      out.writeInt(mData.StepCount);
      out.writeLong(mData.SuggestedEndDate);
      out.writeLong(mData.SuggestedStartDate);
      out.writeInt(mData.Urgent ? 1 : 0);
  }

  public static final Parcelable.Creator<TaskParcelable> CREATOR
          = new Parcelable.Creator<TaskParcelable>() {
      public TaskParcelable createFromParcel(Parcel in) {
          return new TaskParcelable(in);
      }

      public TaskParcelable[] newArray(int size) {
          return new TaskParcelable[size];
      }
  };
  
  private TaskParcelable(Parcel in) {
      mData = new Task();
      mData.Code = in.readString();
      mData.Description = in.readString();
      mData.ID = in.readString();
      mData.Label = in.readString();
      
      double lat = in.readDouble();
      mData.PositionLat = lat != Double.MAX_VALUE ? lat : null;
      
      double lon = in.readDouble();
      mData.PositionLon = lon != Double.MAX_VALUE ? lon : null;
      
      mData.StepCount = in.readInt();
      mData.SuggestedEndDate = in.readLong();
      mData.SuggestedStartDate = in.readLong();
      mData.Urgent = in.readInt() == 1;
  }
  
  public TaskParcelable(Task task) {
    this.mData = task;
  }

  public Task getTask() {
    return mData;
  }

  public void setTask(Task data) {
    this.mData = data;
  }

}
