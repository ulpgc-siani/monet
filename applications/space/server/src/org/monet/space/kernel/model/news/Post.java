package org.monet.space.kernel.model.news;

import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.BaseObject;
import org.monet.space.kernel.model.Language;
import org.monet.space.kernel.model.MonetLink;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class Post extends BaseObject {

	public static final int SERVICE_REQUEST = 0;
	public static final int SERVICE_RESPONSE = 1;
	public static final int INFO = 2;
	public static final int BUSINESS_MODEL_UPDATED = 3;
	public static final int BUSINESS_MODEL_INSTALLED = 4;
	public static final int USER_POST = 5;

	public enum Filter {
		MESSAGE, AUTHOR
	}

	private String title;
	private String body;
	private MonetLink bodyTarget;
	private MonetLink target;
	private String targetCode;
	private String tag;
	private String wallUserId;
	private int type;
	private Date createDate = new Date();
	private ArrayList<PostComment> comments = new ArrayList<PostComment>();

	public String getCreateDate() {
		return LibraryDate.getDateAndTimeString(this.createDate, Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.DEFAULT, true, Strings.BAR45);
	}

	public Date getInternalCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public MonetLink getTarget() {
		return target;
	}

	public void setTarget(MonetLink target) {
		this.target = target;
	}

	public MonetLink getBodyTarget() {
		return bodyTarget;
	}

	public void setBodyTarget(MonetLink bodyTarget) {
		this.bodyTarget = bodyTarget;
	}

	public String getTargetCode() {
		return targetCode;
	}

	public void setTargetCode(String targetCode) {
		this.targetCode = targetCode;
	}

	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public void addComment(PostComment comment) {
		this.comments.add(comment);
	}

	public ArrayList<PostComment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<PostComment> comments) {
		this.comments = comments;
	}

	public String getWallUserId() {
		return wallUserId;
	}

	public void setWallUserId(String wallUserId) {
		this.wallUserId = wallUserId;
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub
	}

}
