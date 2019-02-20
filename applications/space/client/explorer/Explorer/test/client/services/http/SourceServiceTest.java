package client.services.http;

import client.core.model.Source;
import client.core.model.types.TermList;
import client.services.Service;
import client.services.SourceService.Mode;
import client.services.callback.SourceCallback;
import client.services.callback.TermListCallback;
import org.junit.Test;

public class SourceServiceTest extends ServiceTest {

	@Test
	public void testOpenSource() {
		final SourceService service = createService();

		service.open("1", new SourceCallback() {
			@Override
			public void success(Source object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});
	}

	@Test
	public void testLoadTerms() {
		final SourceService service = createService();

		service.open("1", new SourceCallback() {
			@Override
			public void success(Source object) {
				service.getTerms(object, Mode.TREE, 0, 100, "", "", new TermListCallback() {
					@Override
					public void success(TermList object) {
						finishTest();
					}

					@Override
					public void failure(String error) {
						System.out.println(error);
					}
				});
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});
	}

	@Override
	protected <T extends Service> T createService() {
		return (T)new client.services.http.SourceService(createStub(), createServices());
	}

}