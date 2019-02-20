package org.monet.space.kernel.model.news;

import org.monet.space.kernel.constants.Strings;
import org.monet.space.kernel.library.LibraryDate;
import org.monet.space.kernel.model.BaseObject;
import org.monet.space.kernel.model.Language;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;

public class PostComment extends BaseObject {

	private String authorId;
	private String author;
	private String text;
	private Date createDate;
	private String postId;

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCreateDate() {
		return LibraryDate.getDateAndTimeString(this.createDate, Language.getCurrent(), Language.getCurrentTimeZone(), LibraryDate.Format.DEFAULT, true, Strings.BAR45);
	}

	public Date getInternalCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getAuthorId() {
		return authorId;
	}

	public void setAuthorId(String authorId) {
		this.authorId = authorId;
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
		// TODO Auto-generated method stub

	}

	public String getPostId() {
		return postId;
	}

	public void setPostId(String postId) {
		this.postId = postId;
	}

}
