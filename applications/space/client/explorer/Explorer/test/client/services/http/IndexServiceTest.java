package client.services.http;

import client.core.model.*;
import client.core.model.definition.entity.IndexDefinition;
import client.core.system.Collection;
import client.core.system.MonetList;
import client.services.callback.FilterOptionsCallback;
import client.services.callback.IndexCallback;
import client.services.callback.NodeIndexEntriesCallback;
import org.junit.Test;

public class IndexServiceTest extends ServiceTest {

	@Test
	public void testOpenService() {
		client.services.IndexService service = createService();

		service.open(IndexDefinition.DEFAULT, new IndexCallback() {
			@Override
			public void success(Index index) {
				assertEquals(IndexDefinition.DEFAULT, index.getId());

				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.print(error);
			}
		});
	}

	@Test
	public void testLoadEntries() {
		client.services.IndexService service = createService();
		final Index index = createDescriptorIndex();

		service.getEntries(index, new client.services.IndexService.NodeScope() {
			@Override
			public String getIndexView() {
				return null;
			}

			@Override
			public Set getSet() {
				return new Collection("2", "", false);
			}

			@Override
			public Key getSetView() {
				return new client.core.system.Key("m8ppx_a");
			}

		}, new MonetList<Filter>(), new MonetList<Order>(), 0, 100, new NodeIndexEntriesCallback() {
			@Override
			public void success(List<NodeIndexEntry> object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.print(error);
			}
		});
	}

	@Test
	public void testLoadEntriesOfIndex() {
		client.services.IndexService service = createService();
		Index index = createIndex();

		service.getEntries(index, new client.services.IndexService.NodeScope() {
			@Override
			public String getIndexView() {
				return "myqq8ia";
			}

			@Override
			public Set getSet() {
				return new Collection("2", "", false);
			}

			@Override
			public Key getSetView() {
				return new client.core.system.Key("mcmkpow");
			}

		}, new MonetList<Filter>(), new MonetList<Order>(), 0, 100, new NodeIndexEntriesCallback() {
			@Override
			public void success(List<NodeIndexEntry> object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.print(error);
			}
		});
	}

	@Test
	public void testLoadOptionsOfIndex() {
		client.services.IndexService service = createService();
		Index index = createIndex();

		service.getFilterOptions(index, new client.services.IndexService.NodeScope() {
			@Override
			public String getIndexView() {
				return "myqq8ia";
			}

			@Override
			public Set getSet() {
				return new Collection("2", "", false);
			}

			@Override
			public Key getSetView() {
				return new client.core.system.Key("mcmkpow");
			}

		}, new client.core.system.Filter("mwkjusw", "FieldText"), new FilterOptionsCallback() {
			@Override
			public void success(List<Filter.Option> object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.print(error);
			}
		});
	}

	@Override
	protected <T extends client.services.Service> T createService() {
		return (T)new client.services.http.IndexService(createStub(), createServices());
	}

	private Index createDescriptorIndex() {
		return new client.core.system.Index<>(IndexDefinition.DEFAULT);
	}

	private Index createIndex() {
		return new client.core.system.Index<>("mnntusg");
	}

}