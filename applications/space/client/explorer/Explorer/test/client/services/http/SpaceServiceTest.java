package client.services.http;

import client.core.model.Space;
import client.core.model.definition.Dictionary;
import client.core.model.definition.entity.EntityDefinition;
import client.services.Service;
import client.services.callback.DefinitionCallback;
import client.services.callback.SpaceCallback;
import org.junit.Test;

public class SpaceServiceTest extends ServiceTest {

	@Test
	public void testLoad() throws Exception {
		client.services.SpaceService service = createService();

		service.load(new SpaceCallback() {
			@Override
			public void success(Space space) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println("---------------------------- ERROR: " + error);
				fail();
			}
		});
	}

	@Test
	public void testGetDefinitionLabel() {
		client.services.SpaceService service = createService();

		service.load(new SpaceCallback() {
			@Override
			public void success(Space space) {
				Dictionary dictionary = space.getDictionary();

				assertEquals("Home", dictionary.getDefinitionLabel("org.monet.explorerintegration.Home"));

				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println("---------------------------- ERROR: " + error);
				fail();
			}
		});
	}

	@Test
	public void testLoadDefinition() {
		client.services.SpaceService service = createService();

		service.loadDefinition("mtvp02a", EntityDefinition.CLASS_NAME, new DefinitionCallback<EntityDefinition>() {
			@Override
			public void success(EntityDefinition object) {
				System.out.println("correcto!");
				finishTest();
			}

			@Override
			public void failure(String error) {

			}
		});

	}

	@Override
	protected <T extends Service> T createService() {
		return (T)new client.services.http.SpaceService(createStub(), createServices());
	}
}