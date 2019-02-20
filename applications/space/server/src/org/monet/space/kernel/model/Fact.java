package org.monet.space.kernel.model;

import java.util.Date;
import java.util.List;

public interface Fact {

	public void setTaskId(String taskId);

	public String getTaskId();

	public void setUserId(String userId);

	public String getUserId();

	public void setCreateDate(Date createDate);

	public String getCreateDate();

	public Date getInternalCreateDate();

	public void setTitle(String title);

	public String getTitle();

	public void setSubTitle(String subtitle);

	public String getSubTitle();

	public void addLink(MonetLink monetLink);

	public List<MonetLink> getLinks();

}
