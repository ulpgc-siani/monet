package org.monet.space.kernel.model;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;


public class Revision extends BaseObject {


	private String idNode;
	private String idUser;
	private StringBuffer data;
	private boolean merged;
	private RevisionSuperdata superdata;
	private Date revisionDate;
	private RevisionLink revisionLink;

	public static final String REVISION_LINK = "RevisionLink";

	public Revision() {
		super();
		this.revisionLink = null;
	}

	public void setIdNode(String idNode) {
		this.idNode = idNode;
	}

	public String getIdNode() {
		return idNode;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getIdUser() {
		return idUser;
	}

	public void setData(StringBuffer data) {
		this.data = data;
	}

	public StringBuffer getData() {
		return data;
	}

	public void setMerged(boolean merged) {
		this.merged = merged;
	}

	public boolean isMerged() {
		return merged;
	}

	public void setRevisionDate(Date creationDate) {
		this.revisionDate = creationDate;
	}

	public Date getRevisionDate() {
		return revisionDate;
	}

	public void setSuperdata(RevisionSuperdata superdata) {
		this.superdata = superdata;
	}

	public RevisionSuperdata getSuperdata() {
		return superdata;
	}

	public void setRevisionLink(RevisionLink revisionLink) {
		this.revisionLink = revisionLink;
	}

	public RevisionLink getRevisionLink() {
		onLoad(this, Revision.REVISION_LINK);
		return this.revisionLink;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}

}
