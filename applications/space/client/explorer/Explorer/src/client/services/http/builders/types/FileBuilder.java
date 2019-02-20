package client.services.http.builders.types;

import client.core.model.List;
import client.core.system.MonetList;
import client.core.system.types.File;
import client.services.http.HttpInstance;
import client.services.http.HttpList;
import client.services.http.builders.Builder;

public class FileBuilder implements Builder<client.core.model.types.File, List<client.core.model.types.File>> {
	@Override
	public client.core.model.types.File build(HttpInstance instance) {
		if (instance == null)
			return null;

		File file = new File();
		initialize(file, instance);
		return file;
	}

	@Override
	public void initialize(client.core.model.types.File object, HttpInstance instance) {
		File file = (File)object;
		file.setId(instance.getString("id"));
		file.setLabel(instance.getString("label"));
	}

	@Override
	public List<client.core.model.types.File> buildList(HttpList instance) {
		return new MonetList<>();
	}
}
