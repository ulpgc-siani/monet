package client.services;

import client.core.model.Source;
import client.services.callback.SourceCallback;
import client.services.callback.TermListCallback;

public interface SourceService extends Service {
	void open(String id, final SourceCallback callback);
	void locate(String key, String url, final SourceCallback callback);
	void getTerms(Source source, Mode mode, int start, int limit, String flatten, String depth, TermListCallback callback);
	void searchTerms(Source source, Mode mode, String condition, int limit, int count, String flatten, String depth, TermListCallback callback);

	enum Mode {
		ALL, FLATTEN, TREE
	}
}
