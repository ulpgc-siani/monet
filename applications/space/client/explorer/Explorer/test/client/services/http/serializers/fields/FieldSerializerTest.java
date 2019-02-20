package client.services.http.serializers.fields;

import client.ApplicationTestCase;
import client.core.model.definition.entity.field.DateFieldDefinition;
import client.core.system.Form;
import client.core.system.Source;
import client.core.system.fields.*;
import client.core.system.types.*;
import client.core.system.types.Number;
import cosmos.types.Date;
import org.junit.Test;

public class FieldSerializerTest extends ApplicationTestCase {

	public static final String BOOLEAN = "{\"code\":\"test\", \"type\":\"boolean\", \"multiple\":false, \"value\":true}";
	public static final String CHECK = "{\"code\":\"test\", \"type\":\"check\", \"multiple\":false, \"value\":{\"checks\":[{\"value\":\"001\", \"label\":\"Término 001\", \"checked\":true},{\"value\":\"002\", \"label\":\"Término 002\", \"checked\":false}]}}";
	public static final String COMPOSITE = "{\"code\":\"test\", \"type\":\"composite\", \"multiple\":false, \"value\":{\"conditioned\":true}}";
	public static final String DATE = "{\"code\":\"test\", \"type\":\"date\", \"multiple\":false, \"value\":{\"value\":$time$, \"formattedValue\":\"$format$\"}}";
	public static final String FILE = "{\"code\":\"test\", \"type\":\"file\", \"multiple\":false, \"value\":{\"id\":\"/a/b/c.pdf\", \"label\":\"Nombre del fichero\"}}";
	public static final String LINK = "{\"code\":\"test\", \"type\":\"link\", \"multiple\":false, \"value\":{\"id\":\"1\", \"label\":\"Nodo 1\"}}";
	public static final String MEMO = "{\"code\":\"test\", \"type\":\"memo\", \"multiple\":false, \"value\":\"soy un campo memo\"}";
	public static final String NODE = "{\"code\":\"test\", \"type\":\"node\", \"multiple\":false, \"value\":\"1\"}";
	public static final String NUMBER = "{\"code\":\"test\", \"type\":\"number\", \"multiple\":false, \"value\":{\"value\":$number$, \"formattedValue\":\"$number$\"}}";
	public static final String PICTURE = "{\"code\":\"test\", \"type\":\"picture\", \"multiple\":false, \"value\":{\"id\":\"/a/b/c.png\", \"label\":\"Nombre de la imagen\"}}";
	public static final String SELECT = "{\"code\":\"test\", \"type\":\"select\", \"multiple\":false, \"value\":{\"value\":\"001\", \"label\":\"Valor 001\"}}";
	public static final String SELECT_WITH_SOURCE = "{\"code\":\"test\", \"type\":\"select\", \"multiple\":false, \"value\":{\"value\":\"001\", \"label\":\"Valor 001\", \"source\":\"10\"}}";
	public static final String SERIAL = "{\"code\":\"test\", \"type\":\"serial\", \"multiple\":false, \"value\":\"001/22\"}";
	public static final String SUMMATION = "{\"code\":\"test\", \"type\":\"summation\", \"multiple\":false, \"value\":\"campo summation\"}";
	public static final String TEXT = "{\"code\":\"test\", \"type\":\"text\", \"multiple\":false, \"value\":\"campo texto\"}";
	public static final String URI = "{\"code\":\"test\", \"type\":\"uri\", \"multiple\":false, \"value\":\"http://localhost/hola.html\"}";

	@Test
	public void testBooleanField() {
		BooleanField field = new BooleanField();
		field.setCode("test");
		field.setValue(true);

		assertEquals(BOOLEAN, new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testCheckField() {
		CheckField field = new CheckField();
		field.setCode("test");
		field.setValue(new CheckList() {{
			add(new Check("001", "Término 001", true));
			add(new Check("002", "Término 002", false));
		}});

		assertEquals(CHECK, new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testCompositeField() {
		CompositeField field = new CompositeField();
		field.setCode("test");
		field.setValue(new Composite() {{
			add(new TextField("001", "Campo 1") {{ setValue("campo texto");}});
			add(new BooleanField("002", "Campo 2") {{ setValue(false);}});
		}});
		field.setConditioned(true);

		assertEquals(COMPOSITE, new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testDateField() {
		DateField field = new DateField();
		Date value = new Date(2015, 2, 11);

		field.setDefinition(dateFieldDefinition());
		field.setCode("test");
		field.setValue(value);

		assertEquals(DATE.replace("$time$", String.valueOf(value.getMilliseconds())).replace("$format$", "2015/02/11"), new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testFileField() {
		FileField field = new FileField();

		field.setCode("test");
		field.setValue(new File() {{
			setLabel("Nombre del fichero");
			setId("/a/b/c.pdf");
		}});

		assertEquals(FILE, new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testLinkField() {
		LinkField field = new LinkField();

		field.setCode("test");
		field.setValue(new Link("1", "Nodo 1"));

		assertEquals(LINK, new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testMemoField() {
		MemoField field = new MemoField();

		field.setCode("test");
		field.setValue("soy un campo memo");

		assertEquals(MEMO, new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testNodeField() {
		NodeField field = new NodeField();

		field.setCode("test");
		field.setValue(new Form("1", "Formulario", false));

		assertEquals(NODE, new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testNumberField() {
		NumberField field = new NumberField();

		field.setCode("test");

		field.setFormattedValue("100.5");
		field.setValue(new Number(100.5));
		assertEquals(NUMBER.replace("$number$", String.valueOf(100.5)), new FieldSerializer().serialize(field).toString());

		field.setFormattedValue("100");
		field.setValue(new Number(100));
		assertEquals(NUMBER.replace("$number$", String.valueOf(100)), new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testPictureField() {
		PictureField field = new PictureField();

		field.setCode("test");
		field.setValue(new Picture() {{
			setLabel("Nombre de la imagen");
			setId("/a/b/c.png");
		}});

		assertEquals(PICTURE, new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testSelectField() {
		SelectField field = new SelectField();

		field.setCode("test");
		field.setValue(new Term("001", "Valor 001"));

		assertEquals(SELECT, new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testSelectFieldWithSource() {
		SelectField field = new SelectField();

		field.setCode("test");
		field.setValue(new Term("001", "Valor 001"));
		field.setSource(new Source("10", "Source"));

		assertEquals(SELECT_WITH_SOURCE, new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testSerialField() {
		SerialField field = new SerialField();

		field.setCode("test");
		field.setValue("001/22");

		assertEquals(SERIAL, new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testSummationField() {
		SummationField field = new SummationField();

		field.setCode("test");
		field.setValue("campo summation");

		assertEquals(SUMMATION, new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testTextField() {
		TextField field = new TextField();

		field.setCode("test");
		field.setValue("campo texto");

		assertEquals(TEXT, new FieldSerializer().serialize(field).toString());
	}

	@Test
	public void testUriField() {
		UriField field = new UriField();

		field.setCode("test");
		field.setValue(new Uri("http://localhost/hola.html"));

		assertEquals(URI, new FieldSerializer().serialize(field).toString());
	}




	private DateFieldDefinition dateFieldDefinition() {
		final client.core.system.definition.entity.field.DateFieldDefinition definition = new client.core.system.definition.entity.field.DateFieldDefinition();
		definition.setPrecision(DateFieldDefinition.Precision.DAYS);
		return definition;
	}
}