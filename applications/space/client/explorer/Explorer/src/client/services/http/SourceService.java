package client.services.http;

import client.core.adapters.DefinitionAdapter;
import client.core.model.Source;
import client.core.model.definition.entity.EntityDefinition;
import client.core.model.types.Term;
import client.services.Services;
import client.services.callback.DefinitionCallback;
import client.services.callback.SourceCallback;
import client.services.callback.TermListCallback;
import client.services.http.dialogs.source.LoadSourceDialog;
import client.services.http.dialogs.source.LoadSourceTermsDialog;
import client.services.http.dialogs.source.LocateSourceDialog;
import client.services.http.dialogs.source.SearchSourceTermsDialog;

public class SourceService extends HttpService implements client.services.SourceService {

	public SourceService(Stub stub, Services services) {
		super(stub, services);
	}

	@Override
	public void open(String id, final SourceCallback callback) {
		stub.request(new LoadSourceDialog(id), Source.CLASS_NAME, new SourceCallback() {
			@Override
			public void success(final Source source) {
				loadDefinition(source, callback);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public void locate(String key, String url, final SourceCallback callback) {
		stub.request(new LocateSourceDialog(key, url), Source.CLASS_NAME, new SourceCallback() {
			@Override
			public void success(final Source source) {
				loadDefinition(source, callback);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	private void loadDefinition(final Source source, final SourceCallback callback) {
		services.getSpaceService().loadDefinition(source, new DefinitionCallback<EntityDefinition>() {
			@Override
			public void success(EntityDefinition definition) {
				new DefinitionAdapter().adapt(source, definition);
				callback.success(source);
			}

			@Override
			public void failure(String error) {
				callback.failure(error);
			}
		});
	}

	@Override
	public void getTerms(Source source, Mode mode, int start, int limit, String flatten, String depth, TermListCallback callback) {
		stub.request(new LoadSourceTermsDialog(source, mode, start, limit, flatten, depth), Term.CLASS_NAME, callback);
	}

	@Override
	public void searchTerms(Source source, Mode mode, String condition, int start, int limit, String flatten, String depth, TermListCallback callback) {
		stub.request(new SearchSourceTermsDialog(source, mode, condition, start, limit, flatten, depth), Term.CLASS_NAME, callback);
	}
}
