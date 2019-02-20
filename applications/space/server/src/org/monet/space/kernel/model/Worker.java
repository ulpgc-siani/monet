package org.monet.space.kernel.model;

import net.minidev.json.JSONObject;
import org.monet.space.kernel.constants.LabelCode;
import org.monet.space.office.core.model.Language;
import org.xmlpull.v1.XmlSerializer;

import java.io.IOException;

public class Worker extends BaseObject {
	private String idUser;
	private String fullname;
	private String photo;

	public Worker(String id, String code, String idUser, String name, String photo) {
		this.id = id;
		this.code = code;
		this.idUser = idUser;
		this.fullname = name;
		this.photo = photo;
	}

	public String getIdUser() {
		return this.idUser;
	}

	public void setIdUser(String idUser) {
		this.idUser = idUser;
	}

	public String getFullname() {
		return this.fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public String getPhoto() {
		return this.photo;
	}

	public void setPhoto(String photo) {
		this.photo = photo;
	}

	public JSONObject toJson() {
		JSONObject result = new JSONObject();
		String label = this.getFullname();

		result.put("id", this.getFullname());

		if (label == null || label.trim().isEmpty())
			label = Language.getInstance().getLabel(LabelCode.NO_LABEL);

		result.put("id", this.id);
		result.put("code", this.code);
		result.put("label", label);
		result.put("idUser", idUser);
		result.put("fullname", this.fullname);
		result.put("photo", this.photo);

		return result;
	}

	@Override
	public void serializeToXML(XmlSerializer serializer, int depth) throws IllegalArgumentException, IllegalStateException, IOException {
	}

}
