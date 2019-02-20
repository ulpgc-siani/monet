package org.monet.space.kernel.model;

public class Banner {
	private String title;
	private String subTitle;
	private String spaceUrl;
	private String logoUrl;
	private int countActiveTasks;
	private int countAliveTasks;
	private boolean hasPermissions;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	public String getSpaceUrl() {
		return spaceUrl;
	}

	public void setSpaceUrl(String spaceUrl) {
		this.spaceUrl = spaceUrl;
	}

	public String getLogoUrl() {
		return logoUrl;
	}

	public void setLogoUrl(String logoUrl) {
		this.logoUrl = logoUrl;
	}

	public int getCountActiveTasks() {
		return countActiveTasks;
	}

	public void setCountActiveTasks(int countActiveTasks) {
		this.countActiveTasks = countActiveTasks;
	}

	public int getCountAliveTasks() {
		return countAliveTasks;
	}

	public void setCountAliveTasks(int countAliveTasks) {
		this.countAliveTasks = countAliveTasks;
	}

	public boolean hasPermissions() {
		return hasPermissions;
	}

	public void setHasPermissions(boolean value) {
		this.hasPermissions = value;
	}
}
