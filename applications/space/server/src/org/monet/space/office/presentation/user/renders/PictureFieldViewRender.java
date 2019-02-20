package org.monet.space.office.presentation.user.renders;

import org.monet.metamodel.PictureFieldProperty;
import org.monet.space.kernel.model.Attribute;
import org.monet.space.kernel.model.Indicator;
import org.monet.space.office.configuration.Configuration;

import java.util.HashMap;

public class PictureFieldViewRender extends FieldViewRender {

	public PictureFieldViewRender() {
		super();
	}

	@Override
	protected String getFieldType() {
		return "picture";
	}

	@Override
	protected String initBody(String id, HashMap<String, Object> viewMap, HashMap<String, Object> declarationsMap, Attribute attribute, boolean isTemplate) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String value = this.getIndicatorValue(attribute, Indicator.VALUE);
		Configuration configuration = Configuration.getInstance();
		String title = "", lightBox = "", photoLink = "", thumbPhotoLink = "";
		PictureFieldProperty definition = (PictureFieldProperty) this.definition;

		if (value.isEmpty()) {
			String defaultValue = definition.getDefault();

			if (defaultValue != null && defaultValue.isEmpty())
				photoLink = configuration.getApiUrl() + "?op=loadbusinessmodelfile&path=images/" + defaultValue;
			else
				photoLink = configuration.getUrl() + "/images/no-picture.jpg";

			thumbPhotoLink = photoLink;
			lightBox = "";
		} else {
			photoLink = configuration.getFmsServletUrl() + "?op=downloadimage&nid=" + this.field.getNode().getId() + "&f=" + value + "&r=" + Math.random();
			thumbPhotoLink = photoLink + "&thumb=1";
			lightBox = "lightbox[" + this.field.getNode().getId() + this.definition.getCode() + "]";

			map.put("photoLink", photoLink);
			map.put("thumbPhotoLink", thumbPhotoLink);
			title = block("field.picture$title", map);
			map.clear();
		}

		if (definition.getSize() != null) {
			HashMap<String, Object> localMap = new HashMap<String, Object>();
			localMap.put("width", String.valueOf(definition.getSize().getWidth()));
			localMap.put("height", String.valueOf(definition.getSize().getHeight()));
			map.put("size", block("field.picture$size", localMap));
		} else
			map.put("size", "");

		map.put("limit", definition.getLimit() != null ? definition.getLimit() : "");
		declarationsMap.put("concreteDeclarations", block("field.picture$concreteDeclarations", map));

		map.put("id", id);
		map.put("value", value);
		map.put("title", title);
		map.put("lightBox", lightBox);
		map.put("photoLink", photoLink);
		map.put("thumbPhotoLink", thumbPhotoLink);

		return (lightBox.isEmpty()) ? block("field.picture.noLightBox", map) : block("field.picture", map);
	}

}
