package client.core.system;

import client.core.model.List;
import client.core.model.MonetLink;
import cosmos.types.Date;

public class Fact implements client.core.model.Fact {
	private String title;
	private String subTitle;
	private String user;
	private List<MonetLink> links = new MonetList<>();
	private Date createDate;

	public static final ClassName CLASS_NAME = new ClassName("Fact");

	@Override
	public ClassName getClassName() {
		return client.core.model.Fact.CLASS_NAME;
	}

	@Override
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String getSubTitle() {
		return subTitle;
	}

	public void setSubTitle(String subTitle) {
		this.subTitle = subTitle;
	}

	@Override
	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public List<MonetLink> getLinks() {
		return links;
	}

	public void setLinks(List<MonetLink> links) {
		this.links = links;
	}

	@Override
	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
