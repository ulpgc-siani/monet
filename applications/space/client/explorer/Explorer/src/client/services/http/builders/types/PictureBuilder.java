package client.services.http.builders.types;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.types.Picture;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

public class PictureBuilder implements Builder<client.core.model.types.Picture, List<client.core.model.types.Picture>> {
	@Override
	public client.core.model.types.Picture build(HttpInstance instance) {
		if (instance == null)
			return null;

		Picture picture = new Picture();
		initialize(picture, instance);
		return picture;
	}

	@Override
	public void initialize(client.core.model.types.Picture object, HttpInstance instance) {
		Picture picture = (Picture)object;
		picture.setId(instance.getString("id"));
		picture.setLabel(instance.getString("label"));
	}

	@Override
	public List<client.core.model.types.Picture> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
