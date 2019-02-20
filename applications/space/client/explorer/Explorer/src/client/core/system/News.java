package client.core.system;

public class News extends Entity implements client.core.model.News {

	public News() {
	}

	public News(String id, String label) {
		super(id, label);
	}

	@Override
	public final ClassName getClassName() {
		return client.core.model.News.CLASS_NAME;
	}

}
