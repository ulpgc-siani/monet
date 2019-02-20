package org.monet.space.kernel.model;

import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;
import java.util.Date;


public class RevisionSuperdata extends BaseObject {


	private String idNode;
	private String idUser;
	private String idSuperdata;
	private StringBuffer data;
	private String value;
	private boolean merged;
	private Date revisionDate;

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

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setIdSuperdata(String idSuperdata) {
		this.idSuperdata = idSuperdata;
	}

	public String getIdSuperdata() {
		return idSuperdata;
	}

	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {

	}

}
