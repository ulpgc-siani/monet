package client.services.http;

import client.ApplicationTestCase;
import client.core.model.Instance;
import client.core.model.List;
import client.core.model.definition.entity.DesktopDefinition;
import client.services.Dialog;
import client.services.callback.Callback;
import client.services.http.builders.Builder;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class HttpStubTest extends ApplicationTestCase {

	private static final String DESKTOP_DEFINITION = "{\"code\":\"mtvp02a\",\"name\":\"org.monet.explorerintegration.Home\",\"type\":\"desktop\",\"label\":\"Home\",\"views\":{\"totalCount\":1,\"items\":[{\"code\":\"mcticfw\",\"type\":\"desktop\",\"default\":false,\"show\":{\"links\":{\"totalCount\":2,\"items\":[{\"value\":\"org.monet.explorerintegration.ContainerSample\"},{\"value\":\"org.monet.explorerintegration.CollectionSample\"}]}}}]}}";

	@Test
	public void testHttpStubBuilderMap() {
		Stub stub = createStub(DESKTOP_DEFINITION);
		stub.request(createDialog(), client.core.model.definition.entity.EntityDefinition.CLASS_NAME, new Callback<Object>() {
			@Override
			public void success(Object object) {
				assertTrue(object instanceof DesktopDefinition);
			}

			@Override
			public void failure(String error) {
			}
		});

	}

	private Stub createStub(String response) {
		return new MyHttpStub(response);
	}

	private Dialog createDialog() {
		return null;
	}

	private class MyHttpStub extends HttpStub {
		private String response;

		public MyHttpStub(String response) {
			super(null, new ErrorHandler() {
				@Override
				public boolean onError(String error) {
					return true;
				}
			});
			this.response = response;
		}

		@Override
		public <T> void request(Dialog dialog, Instance.ClassName className, Callback<T> callback) {
			Builder<T, List<T>> builder = builderMap.get(className);
			HttpInstance instance = JsonUtils.safeEval(response);

			callback.success(instance.isList() ? (T) builder.buildList((HttpList) instance.cast()) : builder.build(instance));
		}
	}

}