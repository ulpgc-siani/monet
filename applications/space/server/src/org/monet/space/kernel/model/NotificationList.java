package org.monet.space.kernel.model;

import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;

import java.util.ArrayList;

public class NotificationList extends ArrayList<Notification> {

	private static final long serialVersionUID = -2579229594917030425L;

	private int totalCount;
	private int unread;

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setUnread(int unread) {
		this.unread = unread;
	}

	public int getUnread() {
		return unread;
	}

	public JSONObject toJson() {
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("nrows", this.totalCount);
		jsonObject.put("unread", this.unread);

		JSONArray items = new JSONArray();
		for (Notification notification : this)
			items.add(notification.toJson());

		jsonObject.put("rows", items);

		return jsonObject;
	}

	public ArrayList<String> getIds() {
		ArrayList<String> result = new ArrayList<String>();
		for (Notification notification : this)
			result.add(notification.getId());
		return result;
	}

}
