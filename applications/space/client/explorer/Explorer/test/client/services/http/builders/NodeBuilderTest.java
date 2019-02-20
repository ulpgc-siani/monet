package client.services.http.builders;

import client.ApplicationTestCase;
import client.core.model.*;
import client.core.model.fields.*;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import cosmos.utils.date.DateFormatter;
import org.junit.Test;

public class NodeBuilderTest extends ApplicationTestCase {

	private static final String NODE = "{\"id\":\"3\",\"label\":\"ContainerSample\",\"definition\":{\"code\":\"miyqwmg\",\"name\":\"org.monet.explorerintegration.ContainerSample\",\"type\":\"container\"},\"isComponent\":false,\"isLocked\":false,\"type\":\"container\",\"children\":[{\"id\":\"4\",\"type\":\"form\",\"label\":\"Record\",\"definition\":{\"code\":\"mj7c5ig\",\"name\":\"org.monet.explorerintegration.ContainerSample.Record\",\"type\":\"form\"}}]}";
	private static final String DESKTOP = "{\"id\":\"1\",\"label\":\"Home\",\"definition\":{\"code\":\"mtvp02a\",\"name\":\"org.monet.explorerintegration.Home\",\"type\":\"desktop\"},\"isComponent\":false,\"isLocked\":false,\"type\":\"desktop\",\"singletons\":[{\"id\":\"3\",\"type\":\"container\",\"label\":\"ContainerSample\",\"definition\":{\"code\":\"miyqwmg\",\"name\":\"org.monet.explorerintegration.ContainerSample\",\"type\":\"container\"}},{\"id\":\"2\",\"type\":\"collection\",\"label\":\"CollectionSample\",\"definition\":{\"code\":\"myu5yzg\",\"name\":\"org.monet.explorerintegration.CollectionSample\",\"type\":\"collection\"}}]}";
	private static final String CONTAINER = "{\"id\":\"3\",\"label\":\"ContainerSample\",\"definition\":{\"code\":\"miyqwmg\",\"name\":\"org.monet.explorerintegration.ContainerSample\",\"type\":\"container\"},\"isComponent\":false,\"isLocked\":false,\"type\":\"container\",\"children\":[{\"id\":\"4\",\"type\":\"form\",\"label\":\"Record\",\"definition\":{\"code\":\"mj7c5ig\",\"name\":\"org.monet.explorerintegration.ContainerSample.Record\",\"type\":\"form\"}}]}";
	private static final String COLLECTION = "{\"id\":\"2\",\"label\":\"CollectionSample\",\"definition\":{\"code\":\"myu5yzg\",\"name\":\"org.monet.explorerintegration.CollectionSample\",\"type\":\"collection\"},\"isComponent\":false,\"isLocked\":false,\"type\":\"collection\"}";
	private static final String FORM = "{\"id\":\"7\",\"label\":\"valor del campo texto\",\"type\":\"form\",\"definition\":{\"code\":\"m6p3yvw\",\"name\":\"org.monet.explorerintegration.CollectionSample.Item\",\"type\":\"form\"},\"owner\":{\"id\":\"2\",\"label\":\"CollectionSample\",\"type\":\"collection\",\"definition\":{\"code\":\"myu5yzg\",\"name\":\"org.monet.explorerintegration.CollectionSample\",\"type\":\"collection\"},\"index\":{\"id\":\"mnntusg\",\"entriesCount\":-1,\"filters\":[]}},\"ancestors\":{\"totalCount\":1,\"items\":[{\"id\":\"2\",\"type\":\"collection\",\"label\":\"CollectionSample\",\"definition\":{\"code\":\"myu5yzg\",\"name\":\"org.monet.explorerintegration.CollectionSample\",\"type\":\"collection\"}}]},\"fields\":{\"totalCount\":14,\"items\":[{\"code\":\"m7hqppa\",\"type\":\"text\",\"label\":\"Campo texto\",\"value\":\"valor del campo texto\"},{\"code\":\"mgvw0iq\",\"type\":\"node\",\"label\":\"Campo nodo\"},{\"code\":\"mcxmnfg\",\"type\":\"summation\",\"label\":\"Campo summation\",\"value\":\"\"},{\"code\":\"m7gkhkq\",\"type\":\"check\",\"label\":\"Campo check\",\"value\":{\"checks\":{\"totalCount\":0,\"items\":[]},\"source\":{\"id\":\"1\",\"label\":\"Fuente de datos\",\"type\":\"thesaurus\",\"definition\":{\"code\":\"m99hbmw\",\"name\":\"org.monet.explorerintegration.FuenteDatos\",\"type\":\"thesaurus\"}}}},{\"code\":\"mdx7gsg\",\"type\":\"memo\",\"label\":\"Campo memo\",\"value\":\"soy unas observaciones\"},{\"code\":\"m1owpxq\",\"type\":\"picture\",\"label\":\"Campo imagen\",\"value\":{\"filename\":\"\",\"url\":\"\"}},{\"code\":\"mrwrwjq\",\"type\":\"file\",\"label\":\"Campo file\",\"value\":{\"filename\":\"El título del campo file\",\"url\":\"7/file/helloworld.pdf\"}},{\"code\":\"mcav2aq\",\"type\":\"date\",\"multiple\":true,\"label\":\"Campo fecha\",\"value\":[1419897600000,1420156800000]},{\"code\":\"m18h0ca\",\"type\":\"serial\",\"label\":\"Campo serial\",\"value\":\"formato\"},{\"code\":\"mpgywjq\",\"type\":\"composite\",\"multiple\":true,\"label\":\"Campo composite\",\"value\":[{\"fields\":[{\"code\":\"morrcaq\",\"type\":\"text\",\"label\":\"Campo texto composite\",\"value\":\"Composite texto 1\"},{\"code\":\"meuc4_g\",\"type\":\"boolean\",\"label\":\"Campo booleano composite\",\"value\":false}]},{\"fields\":[{\"code\":\"morrcaq\",\"type\":\"text\",\"label\":\"Campo texto composite\",\"value\":\"Composite texto 2\"},{\"code\":\"meuc4_g\",\"type\":\"boolean\",\"label\":\"Campo booleano composite\",\"value\":true}]}]},{\"code\":\"m6xmbeq\",\"type\":\"composite\",\"label\":\"Campo composite simple\",\"value\":{\"fields\":[{\"code\":\"mw00zhq\",\"type\":\"text\",\"label\":\"Campo texto composite simple\",\"value\":\"valor del campo texto composite simple\"}]}},{\"code\":\"muw_e5a\",\"type\":\"boolean\",\"label\":\"Campo booleano\",\"value\":true},{\"code\":\"mkatgtg\",\"type\":\"number\",\"label\":\"Campo número\",\"value\":\"0\"},{\"code\":\"mkfkq8g\",\"type\":\"select\",\"label\":\"Campo select\",\"value\":{\"value\":\"T001\",\"label\":\"Término 1\"}}]}}";

	@Test
	public void testLoadNode() {
		NodeBuilder<Container> builder = new NodeBuilder();
		Container node = builder.build((HttpInstance) JsonUtils.safeEval(NODE));

		assertEquals("3", node.getId());
		assertEquals("miyqwmg", node.getKey().getCode());
		assertEquals("org.monet.explorerintegration.ContainerSample", node.getKey().getName());
		assertEquals("ContainerSample", node.getLabel());
		assertEquals("miyqwmg", node.getDefinition().getCode());
		assertEquals("org.monet.explorerintegration.ContainerSample", node.getDefinition().getName());
	}

	@Test
	public void testLoadDesktop() {
		NodeBuilder<Desktop> builder = new NodeBuilder();
		Desktop desktop = builder.build((HttpInstance) JsonUtils.safeEval(DESKTOP));

		assertEquals(2, desktop.getSingletonsCount());
		assertEquals("ContainerSample", desktop.getSingleton("miyqwmg").getLabel());
		assertEquals("CollectionSample", desktop.getSingleton("myu5yzg").getLabel());
	}

	@Test
	public void testLoadContainer() {
		NodeBuilder<Container> builder = new NodeBuilder();
		Container container = builder.build((HttpInstance) JsonUtils.safeEval(CONTAINER));

		assertEquals("4", container.getChild("mj7c5ig").getId());
		assertEquals("mj7c5ig", container.getChild("org.monet.explorerintegration.ContainerSample.Record").getKey().getCode());
		assertEquals("org.monet.explorerintegration.ContainerSample.Record", container.getChild("org.monet.explorerintegration.ContainerSample.Record").getKey().getName());
		assertEquals("Record", container.getChild("org.monet.explorerintegration.ContainerSample.Record").getLabel());
		assertEquals(1, container.getChildrenCount());
	}

	@Test
	public void testLoadCollection() {
		NodeBuilder<Collection> builder = new NodeBuilder();
		Collection collection = builder.build((HttpInstance) JsonUtils.safeEval(COLLECTION));

		assertNotNull(collection);
	}

	@Test
	public void testLoadForm() {
		NodeBuilder<Form> builder = new NodeBuilder();
		Form form = builder.build((HttpInstance) JsonUtils.safeEval(FORM));

		assertEquals(14, form.get().size());

		// boolean
		assertTrue(((BooleanField)form.get("muw_e5a")).getValue());

		// check
		assertTrue(((CheckField) form.get("m7gkhkq")).getValue().get(0).isChecked());
		assertFalse(((CheckField) form.get("m7gkhkq")).getValue().get(1).isChecked());

		// composite
		CompositeField compositeField = (CompositeField) form.get("m6xmbeq");
		assertEquals("valor del campo texto composite simple", ((TextField) compositeField.getValue().get(0)).getValue());

		// composite multiple
		MultipleCompositeField multipleCompositeField = (MultipleCompositeField) form.get("mpgywjq");
		assertEquals("Composite texto 1", ((TextField) multipleCompositeField.get(0).getValue().get(0)).getValue());
		assertFalse(((BooleanField) multipleCompositeField.get(0).getValue().get(1)).getValue());
		assertEquals("Composite texto 2", ((TextField) multipleCompositeField.get(1).getValue().get(0)).getValue());
		assertTrue(((BooleanField) multipleCompositeField.get(1).getValue().get(1)).getValue());

		// date
		MultipleDateField multipleDateField = (MultipleDateField) form.get("mcav2aq");
		assertEquals("30/12/2014", DateFormatter.format(multipleDateField.get(0).getValue(), "dd/MM/yyyy"));
		assertEquals("02/01/2015", DateFormatter.format(multipleDateField.get(1).getValue(), "dd/MM/yyyy"));

		// file
		assertEquals("El título del campo file", ((FileField)form.get("mrwrwjq")).getValue().getLabel());
		assertEquals("7/file/helloworld.pdf", ((FileField)form.get("mrwrwjq")).getValue().getId());

		// memo
		assertEquals("soy unas observaciones", ((MemoField)form.get("mdx7gsg")).getValue());

		// node
		assertNull(((NodeField) form.get("mgvw0iq")).getValue());

		// number
		assertEquals(0.0, ((NumberField)form.get("mkatgtg")).getValue().getValue());

		// picture
		assertEquals("", ((PictureField)form.get("m1owpxq")).getValue().getLabel());
		assertEquals("", ((PictureField)form.get("m1owpxq")).getValue().getId());

		// select
		assertEquals("T001", ((SelectField)form.get("mkfkq8g")).getValue().getValue());
		assertEquals("Término 1", ((SelectField)form.get("mkfkq8g")).getValue().getLabel());

		// serial
		assertEquals("formato", ((SerialField)form.get("m18h0ca")).getValue());

		// text
		assertEquals("valor del campo texto", ((TextField) form.get("m7hqppa")).getValue());
	}

	@Test
	public void testLoadFormCheckSource() {
		NodeBuilder<Form> builder = new NodeBuilder();
		Form form = builder.build((HttpInstance) JsonUtils.safeEval(FORM));
		CheckField checkField = ((CheckField) form.get("m7gkhkq"));

		assertEquals("1", checkField.getSource().getId());
	}

	@Test
	public void testLoadFormNumberValue() {
		NodeBuilder<Form> builder = new NodeBuilder();
		Form form = builder.build((HttpInstance) JsonUtils.safeEval(FORM));
		NumberField numberField = ((NumberField)form.get("mkatgtg"));

		assertEquals(0.0, numberField.getValue().getValue());
	}

}