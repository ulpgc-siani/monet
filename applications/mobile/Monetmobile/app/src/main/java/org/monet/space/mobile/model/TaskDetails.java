package org.monet.space.mobile.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class TaskDetails implements Parcelable {

	public static final int TASK_TRAY_ASSIGNED = 0;
	public static final int TASK_TRAY_FINISHED = 1;
	public static final int TASK_TRAY_UNASSIGNED = 2;

	public int tray;
	public long id;
	public String serverId;
	public String context;
	public String code;
	public String label;
	public String description;
	public String comments;
	public long sourceId;
	public String sourceLabel;

	public Position position;

	public boolean urgent;

	public Date suggestedStartDate;
	public Date suggestedEndDate;
	public Date startDate;
	public Date endDate;

	public int notReadChats;

	public int step;
	public int stepIteration;
	public int stepCount;

	public Attachment[] attachments;
	public List<ChatItem> chatItems;

	public static class Position implements Parcelable {

		public double latitude;
		public double longitude;

		public static final Parcelable.Creator<Position> CREATOR = new Parcelable.Creator<Position>() {
			public Position createFromParcel(Parcel in) {
				return new Position(in);
			}

			public Position[] newArray(int size) {
				return new Position[size];
			}
		};

		private Position(Parcel in) {
			latitude = in.readDouble();
			longitude = in.readDouble();
		}

		public Position() {
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeDouble(latitude);
			dest.writeDouble(longitude);
		}

	}

	public static class Attachment implements Parcelable {

		public long id;
		public String label;
		public String contentType;
		public String path;

		public Attachment() {
		}

		public Attachment(String label, String contentType, String path) {
			this.label = label;
			this.contentType = contentType;
			this.path = path;
		}

		@Override
		public String toString() {
			return this.label;
		}

		public static final Parcelable.Creator<Attachment> CREATOR = new Parcelable.Creator<Attachment>() {
			public Attachment createFromParcel(Parcel in) {
				return new Attachment(in);
			}

			public Attachment[] newArray(int size) {
				return new Attachment[size];
			}
		};

		private Attachment(Parcel in) {
			id = in.readLong();
			label = in.readString();
			contentType = in.readString();
			path = in.readString();
		}

		@Override
		public int describeContents() {
			return 0;
		}

		@Override
		public void writeToParcel(Parcel dest, int flags) {
			dest.writeLong(id);
			dest.writeString(label);
			dest.writeString(contentType);
			dest.writeString(path);
		}
	}

	public static final Parcelable.Creator<TaskDetails> CREATOR = new Parcelable.Creator<TaskDetails>() {
		public TaskDetails createFromParcel(Parcel in) {
			return new TaskDetails(in);
		}

		public TaskDetails[] newArray(int size) {
			return new TaskDetails[size];
		}
	};

	private TaskDetails(Parcel in) {
		tray = in.readInt();
		id = in.readLong();
		serverId = in.readString();
		context = in.readString();
		code = in.readString();
		label = in.readString();
		description = in.readString();
		comments = in.readString();
		sourceId = in.readLong();
		sourceLabel = in.readString();

		position = in.readParcelable(null);

		urgent = in.readByte() != 0;

		suggestedStartDate = (java.util.Date) in.readSerializable();
		suggestedEndDate = (java.util.Date) in.readSerializable();
		startDate = (java.util.Date) in.readSerializable();
		endDate = (java.util.Date) in.readSerializable();

		notReadChats = in.readInt();

		step = in.readInt();
		stepIteration = in.readInt();
		stepCount = in.readInt();

		attachments = (Attachment[]) in.readParcelableArray(null);
		ChatItem[] ci = (ChatItem[]) in.readParcelableArray(null);

		chatItems = new ArrayList<>(Arrays.asList(ci));
	}

	public TaskDetails() {
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(tray);
		dest.writeLong(id);
		dest.writeString(serverId);
		dest.writeString(context);
		dest.writeString(code);
		dest.writeString(label);
		dest.writeString(description);
		dest.writeString(comments);
		dest.writeLong(sourceId);
		dest.writeString(sourceLabel);

		dest.writeParcelable(position, flags);

		dest.writeByte((byte) (urgent ? 1 : 0));

		dest.writeSerializable(suggestedStartDate);
		dest.writeSerializable(suggestedEndDate);
		dest.writeSerializable(startDate);
		dest.writeSerializable(endDate);

		dest.writeInt(notReadChats);

		dest.writeInt(step);
		dest.writeInt(stepIteration);
		dest.writeInt(stepCount);

		dest.writeParcelableArray(attachments, 0);
		dest.writeParcelableArray(chatItems.toArray(new ChatItem[chatItems.size()]), 0);
	}

	@Override
	public String toString() {
		return label;
	}
}
