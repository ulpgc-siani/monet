package client.core.system.types;

public class Picture extends File implements client.core.model.types.Picture {

	public Picture() {
	}

	public Picture(String filename) {
		super(filename);
	}

	public Picture(String filename, String url) {
		super(filename, url);
	}
}
