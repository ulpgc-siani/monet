package client.services.http;

import client.core.model.Field;
import client.core.model.Node;
import client.core.model.types.Check;
import client.core.system.Form;
import client.core.system.MonetList;
import client.core.system.fields.*;
import client.core.system.types.CheckList;
import client.core.system.types.Composite;
import client.services.Service;
import client.services.callback.NodeCallback;
import client.services.callback.VoidCallback;
import org.junit.Test;

public class NodeServiceTest extends ServiceTest {

	@Test
	public void testOpenNode() {
		client.services.NodeService service = createService();

		service.open("1", new NodeCallback() {
			@Override
			public void success(Node node) {
				assertEquals("1", node.getId());
				assertEquals("Home", node.getDefinition().getLabel());

				finishTest();
			}

			@Override
			public void failure(String error) {
			}
		});

	}

	public void testSaveBooleanField() {
		final Form form = new Form("14", "Formulario", false);
		final client.services.NodeService service = createService();
		final BooleanField booleanField = new BooleanField("muw_e5a", "", true);

		service.saveField(form, booleanField, new VoidCallback() {
			@Override
			public void success(Void object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});
	}

	public void testSaveCheckField() {
		final Form form = new Form("14", "Formulario", false);
		final client.services.NodeService service = createService();
		CheckList value = new CheckList(new Check[]{ new client.core.system.types.Check("001", "Termino 1", true) });
		final CheckField checkField = new CheckField("m7gkhkq", "", value);

		service.saveField(form, checkField, new VoidCallback() {
			@Override
			public void success(Void object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});
	}

	public void testSaveMultipleCompositeField() {
		final Form form = new Form("14", "Formulario", false);
		final client.services.NodeService service = createService();

		service.saveField(form, new MultipleCompositeField("mpgywjq", "", new MonetList<client.core.model.fields.CompositeField>() {{
			add(createComposite("valor1", true));
			add(createComposite("valor2", false));
		}}), new VoidCallback() {
			@Override
			public void success(Void object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});
	}

	public void testSaveFieldOfMultipleCompositeField() {
		final Form form = new Form("14", "Formulario", false);
		final client.services.NodeService service = createService();
		final MultipleCompositeField field = new MultipleCompositeField("mpgywjq", "", new MonetList<client.core.model.fields.CompositeField>() {{
			add(createComposite("valor 1 MODIFICADO", false));
			add(createComposite("valor2", true));
		}});

		service.saveField(form, field.get(0).getValue().get(0), new VoidCallback() {
			@Override
			public void success(Void object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});

		service.saveField(form, field.get(0).getValue().get(1), new VoidCallback() {
			@Override
			public void success(Void object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});

		service.saveField(form, field.get(1).getValue().get(1), new VoidCallback() {
			@Override
			public void success(Void object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});
	}

	public void testSaveTextField() {
		final Form form = new Form("14", "Formulario", false);
		final client.services.NodeService service = createService();
		final TextField textField = new TextField("m7hqppa", "", "soy un valor que tengo que guardar");

		service.saveField(form, textField, new VoidCallback() {
			@Override
			public void success(Void object) {
				finishTest();
			}

			@Override
			public void failure(String error) {
				System.out.println(error);
			}
		});
	}

	@Override
	protected <T extends Service> T createService() {
		return (T)new client.services.http.NodeService(createStub(), createServices());
	}

	private CompositeField createComposite(String textFieldValue, boolean booleanFieldValue) {
		final CompositeField compositeField = new CompositeField();
		final TextField textField = new TextField("morrcaq", "", textFieldValue);
		final BooleanField booleanField = new BooleanField("meuc4_g", "", booleanFieldValue);

		compositeField.setCode("mpgywjq");
		compositeField.setValue(new Composite(new Field[]{
			textField, booleanField
		}));

		return compositeField;
	}

}