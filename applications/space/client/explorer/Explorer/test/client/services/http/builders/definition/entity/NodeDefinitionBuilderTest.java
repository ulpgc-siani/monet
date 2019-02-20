package client.services.http.builders.definition.entity;

import client.ApplicationTestCase;
import client.core.model.List;
import client.core.model.definition.entity.*;
import client.core.model.definition.views.ContainerViewDefinition;
import client.core.model.definition.views.DesktopViewDefinition;
import client.core.model.definition.views.SetViewDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class NodeDefinitionBuilderTest extends ApplicationTestCase {

	private static final String NODE = "{\"code\":\"mtvp02a\",\"name\":\"org.monet.explorerintegration.Home\",\"type\":\"desktop\",\"label\":\"Home\",\"description\":\"\",\"operations\":[],\"views\":[{\"code\":\"mcticfw\",\"type\":\"desktop\",\"default\":true,\"show\":{\"links\":[{\"value\":\"org.monet.explorerintegration.ContainerSample\"},{\"value\":\"org.monet.explorerintegration.CollectionSample\"}]}}]}";
	private static final String DESKTOP = "{\"code\":\"mtvp02a\",\"name\":\"org.monet.explorerintegration.Home\",\"type\":\"desktop\",\"label\":\"Home\",\"description\":\"\",\"operations\":[],\"views\":[{\"code\":\"mcticfw\",\"type\":\"desktop\",\"default\":true,\"show\":{\"links\":[{\"value\":\"org.monet.explorerintegration.ContainerSample\"},{\"value\":\"org.monet.explorerintegration.CollectionSample\"}]}}]}";
	private static final String CONTAINER = "{\"code\":\"miyqwmg\",\"name\":\"org.monet.explorerintegration.ContainerSample\",\"type\":\"container\",\"label\":\"ContainerSample\",\"description\":\"\",\"views\":[{\"code\":\"mokspya\",\"type\":\"container\",\"label\":\"Record\",\"default\":true,\"show\":{\"components\":[{\"value\":\"Normal\",\"definition\":\"org.monet.explorerintegration.ContainerSample.Record\",\"qualifiedName\":\"org.monet.explorerintegration.ContainerSample.Record.Normal\"}]}}],\"environment\":true}";
	private static final String COLLECTION = "{\"code\":\"myu5yzg\",\"name\":\"org.monet.explorerintegration.CollectionSample\",\"type\":\"collection\",\"label\":\"CollectionSample\",\"description\":\"\",\"views\":[{\"code\":\"m8ppx_a\",\"type\":\"set\",\"label\":\"CollectionSample\",\"default\":false,\"show\":{\"items\":{}}}]}";
	private static final String FORM = "{\"code\":\"m6p3yvw\",\"name\":\"org.monet.explorerintegration.CollectionSample.Item\",\"type\":\"form\",\"label\":\"CollectionSample\",\"views\":{\"totalCount\":1,\"items\":[{\"code\":\"m5wrlnq\",\"type\":\"form\",\"default\":false,\"show\":{\"fields\":{\"totalCount\":13,\"items\":[{\"value\":\"FieldBoolean\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldCheck\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldComposite\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldCompositeSimple\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldDate\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldFile\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldMemo\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldNode\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldNumber\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldPicture\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldSelect\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldSerial\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldText\",\"definition\":\"m6p3yvw\"}]}}}]},\"fields\":{\"totalCount\":14,\"items\":[{\"code\":\"m7hqppa\",\"name\":\"FieldText\",\"label\":\"Campo texto\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"allowHistory\":{},\"length\":{\"min\":10,\"max\":100},\"edition\":{\"mode\":\"LOWERCASE\"},\"patterns\":[{\"regExp\":\"(.*)\",\"metas\":[{\"indicator\":\"meta1\",\"position\":1}]},{\"regExp\":\"(.*)\",\"metas\":[{\"indicator\":\"meta2\",\"position\":1}]}]},{\"code\":\"mgvw0iq\",\"name\":\"FieldNode\",\"label\":\"Campo nodo\",\"type\":\"node\",\"displays\":{\"totalCount\":0,\"items\":[]},\"contain\":{\"node\":\"mj7c5ig\"},\"add\":{\"nodes\":[\"mj7c5ig\"]}},{\"code\":\"mcxmnfg\",\"name\":\"FieldSummation\",\"label\":\"Campo summation\",\"type\":\"summation\",\"displays\":{\"totalCount\":0,\"items\":[]},\"terms\":{\"totalCount\":2,\"items\":[{\"key\":\"T001\",\"label\":\"Término 1\",\"type\":\"ACCOUNT\",\"multiple\":false,\"negative\":false,\"terms\":{\"totalCount\":0,\"items\":[]}},{\"key\":\"T002\",\"label\":\"Término 2\",\"type\":\"ACCOUNT\",\"multiple\":false,\"negative\":false,\"terms\":{\"totalCount\":0,\"items\":[]}}]},\"select\":{\"depth\":10,\"flatten\":\"INTERNAL\"},\"format\":\"format\",\"range\":{\"min\":109,\"max\":109}},{\"code\":\"m7gkhkq\",\"name\":\"FieldCheck\",\"label\":\"Campo check\",\"type\":\"check\",\"displays\":{\"totalCount\":0,\"items\":[]},\"allowKey\":true,\"source\":\"m99hbmw\",\"select\":{\"depth\":1,\"filter\":{\"tags\":[{\"value\":\"FieldText\",\"definition\":\"m6p3yvw\"}]},\"flatten\":\"INTERNAL\",\"root\":\"hola\"}},{\"code\":\"mdx7gsg\",\"name\":\"FieldMemo\",\"label\":\"Campo memo\",\"type\":\"memo\",\"displays\":{\"totalCount\":0,\"items\":[]}},{\"code\":\"m1owpxq\",\"name\":\"FieldPicture\",\"label\":\"Campo imagen\",\"type\":\"picture\",\"displays\":{\"totalCount\":0,\"items\":[]},\"defaultValue\":\"nophoto.jpg\",\"size\":{\"width\":100,\"height\":100}},{\"code\":\"mrwrwjq\",\"name\":\"FieldFile\",\"label\":\"Campo file\",\"type\":\"file\",\"displays\":{\"totalCount\":0,\"items\":[]}},{\"code\":\"mcav2aq\",\"name\":\"FieldDate\",\"label\":\"Campo fecha\",\"type\":\"date\",\"displays\":{\"totalCount\":0,\"items\":[]},\"precision\":\"DAYS\",\"purpose\":\"NEAR_DATE\",\"range\":{\"min\":10,\"max\":10}},{\"code\":\"m18h0ca\",\"name\":\"FieldSerial\",\"label\":\"Campo serial\",\"type\":\"serial\",\"displays\":{\"totalCount\":0,\"items\":[]},\"serial\":{\"name\":\"SerialName\",\"format\":\"formato\"}},{\"code\":\"mpgywjq\",\"name\":\"FieldComposite\",\"label\":\"Campo composite\",\"type\":\"composite\",\"displays\":{\"totalCount\":0,\"items\":[]},\"extensible\":false,\"conditional\":false,\"range\":{\"min\":10,\"max\":10},\"fields\":{\"totalCount\":2,\"items\":[{\"code\":\"morrcaq\",\"name\":\"FieldTextComposite\",\"label\":\"Campo texto composite\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]},{\"code\":\"meuc4_g\",\"name\":\"FieldBooleanComposite\",\"label\":\"Campo booleano composite\",\"type\":\"boolean\",\"displays\":{\"totalCount\":0,\"items\":[]}}]},\"view\":{\"show\":{\"fields\":[\"morrcaq\",\"meuc4_g\"]}}},{\"code\":\"m6xmbeq\",\"name\":\"FieldCompositeSimple\",\"label\":\"Campo composite simple\",\"type\":\"composite\",\"displays\":{\"totalCount\":0,\"items\":[]},\"extensible\":false,\"conditional\":false,\"fields\":{\"totalCount\":1,\"items\":[{\"code\":\"mw00zhq\",\"name\":\"FieldTextCompositeSimple\",\"label\":\"Campo texto composite simple\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]}]},\"view\":{\"show\":{\"fields\":[\"mw00zhq\"]}}},{\"code\":\"muw_e5a\",\"name\":\"FieldBoolean\",\"label\":\"Campo booleano\",\"type\":\"boolean\",\"displays\":{\"totalCount\":0,\"items\":[]}},{\"code\":\"mkatgtg\",\"name\":\"FieldNumber\",\"label\":\"Campo número\",\"type\":\"number\",\"displays\":{\"totalCount\":0,\"items\":[]},\"format\":\"##\",\"range\":{\"min\":100,\"max\":100},\"edition\":\"BUTTON\"},{\"code\":\"mkfkq8g\",\"name\":\"FieldSelect\",\"label\":\"Campo select\",\"type\":\"select\",\"displays\":{\"totalCount\":0,\"items\":[]},\"terms\":{\"totalCount\":2,\"items\":[{\"key\":\"T001\",\"label\":\"Término 1\",\"terms\":{\"totalCount\":0,\"items\":[]}},{\"key\":\"T001\",\"label\":\"Término 1\",\"terms\":{\"totalCount\":0,\"items\":[]}}]},\"allowHistory\":{},\"allowOther\":true,\"allowKey\":true,\"select\":{\"depth\":10,\"filter\":{\"tags\":[{\"value\":\"FieldText\",\"definition\":\"m6p3yvw\"}]},\"flatten\":\"ALL\",\"root\":{\"value\":\"FieldText\",\"definition\":\"m6p3yvw\"},\"context\":\"null\",\"embedded\":true}}]}}";
	private static final String FORM_MICV_IC000010 = "{\"code\":\"ic.000.010\",\"name\":\"micv.formularios.Ic000010\",\"type\":\"form\",\"label\":\"Información personal\",\"description\":\"El modelo de Currículum Vítae Normalizado es resultado de un trabajo en colaboración con los agentes que interaccionan con los contenidos de las trayectorias curriculares de docentes, investigadores y tecnólogos, con la finalidad de facilitar los intercambios de datos\",\"views\":{\"totalCount\":1,\"items\":[{\"code\":\"mluge_w\",\"name\":\"Vista\",\"type\":\"form\",\"label\":\"Inormación personal\",\"default\":false,\"show\":{\"fields\":{\"totalCount\":21,\"items\":[{\"value\":\"documentosAdjuntos\",\"definition\":\"Icpadre\"},{\"value\":\"FotografiaDigital\",\"definition\":\"ic.000.010\"},{\"value\":\"Nombre\",\"definition\":\"ic.000.010\"},{\"value\":\"Apellidos\",\"definition\":\"ic.000.010\"},{\"value\":\"TelefonoFijo\",\"definition\":\"ic.000.010\"},{\"value\":\"TelefonoMovil\",\"definition\":\"ic.000.010\"},{\"value\":\"Fax\",\"definition\":\"ic.000.010\"},{\"value\":\"PaginaWeb\",\"definition\":\"ic.000.010\"},{\"value\":\"CorreoElectronico\",\"definition\":\"ic.000.010\"},{\"value\":\"Sexo\",\"definition\":\"ic.000.010\"},{\"value\":\"FechaNacimiento\",\"definition\":\"ic.000.010\"},{\"value\":\"PaisNacionalidad\",\"definition\":\"ic.000.010\"},{\"value\":\"PaisNacimiento\",\"definition\":\"ic.000.010\"},{\"value\":\"Dni\",\"definition\":\"ic.000.010\"},{\"value\":\"Nie\",\"definition\":\"ic.000.010\"},{\"value\":\"Pasaporte\",\"definition\":\"ic.000.010\"},{\"value\":\"DireccionContacto\",\"definition\":\"ic.000.010\"},{\"value\":\"CodigoPostal\",\"definition\":\"ic.000.010\"},{\"value\":\"PaisContacto\",\"definition\":\"ic.000.010\"},{\"value\":\"ProvinciaContacto\",\"definition\":\"ic.000.010\"},{\"value\":\"esInvestigador\",\"definition\":\"ic.000.010\"}]}}}]},\"fields\":{\"totalCount\":22,\"items\":[{\"code\":\"Nombre\",\"name\":\"Nombre\",\"label\":\"Nombre\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]},{\"code\":\"Apellidos\",\"name\":\"Apellidos\",\"label\":\"Apellidos\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]},{\"code\":\"TelefonoFijo\",\"name\":\"TelefonoFijo\",\"label\":\"Teléfono fijo\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]},{\"code\":\"TelefonoMovil\",\"name\":\"TelefonoMovil\",\"label\":\"Teléfono móvil\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]},{\"code\":\"Fax\",\"name\":\"Fax\",\"label\":\"Fax\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]},{\"code\":\"PaginaWeb\",\"name\":\"PaginaWeb\",\"label\":\"Página web personal\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]},{\"code\":\"CorreoElectronico\",\"name\":\"CorreoElectronico\",\"label\":\"Correo electrónico\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]},{\"code\":\"DNI\",\"name\":\"Dni\",\"label\":\"DNI\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]},{\"code\":\"NIE\",\"name\":\"Nie\",\"label\":\"NIE\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]},{\"code\":\"Pasaporte\",\"name\":\"Pasaporte\",\"label\":\"Pasaporte\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]},{\"code\":\"CodigoPostal\",\"name\":\"CodigoPostal\",\"label\":\"Código Postal\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]},{\"code\":\"DireccionContacto\",\"name\":\"DireccionContacto\",\"label\":\"Dirección de contacto\",\"type\":\"memo\",\"displays\":{\"totalCount\":0,\"items\":[]}},{\"code\":\"RestoDireccionContacto\",\"name\":\"RestoDireccionContacto\",\"label\":\"Resto de dirección de contacto\",\"type\":\"memo\",\"displays\":{\"totalCount\":0,\"items\":[]}},{\"code\":\"FotografiaDigital\",\"name\":\"FotografiaDigital\",\"label\":\"Fotografía Digital\",\"type\":\"picture\",\"displays\":{\"totalCount\":0,\"items\":[]},\"size\":{\"width\":90,\"height\":110}},{\"code\":\"moagd5q\",\"name\":\"documentosAdjuntos\",\"label\":\"Documentos adjuntos\",\"type\":\"file\",\"displays\":{\"totalCount\":0,\"items\":[]}},{\"code\":\"FechaNacimiento\",\"name\":\"FechaNacimiento\",\"label\":\"Fecha de nacimiento\",\"type\":\"date\",\"displays\":{\"totalCount\":0,\"items\":[]},\"precision\":\"DAYS\"},{\"code\":\"Sexo\",\"name\":\"Sexo\",\"label\":\"Sexo\",\"type\":\"select\",\"displays\":{\"totalCount\":0,\"items\":[]},\"terms\":{\"totalCount\":2,\"items\":[{\"key\":\"000\",\"label\":\"Hombre\",\"terms\":{\"totalCount\":0,\"items\":[]}},{\"key\":\"010\",\"label\":\"Mujer\",\"terms\":{\"totalCount\":0,\"items\":[]}}]},\"allowOther\":false,\"allowKey\":false,\"select\":{\"embedded\":true}},{\"code\":\"PaisNacionalidad\",\"name\":\"PaisNacionalidad\",\"label\":\"País de nacionalidad\",\"type\":\"select\",\"displays\":{\"totalCount\":0,\"items\":[]},\"source\":\"m1rsupg\",\"allowOther\":false,\"allowKey\":false},{\"code\":\"PaisNacimiento\",\"name\":\"PaisNacimiento\",\"label\":\"País de nacimiento\",\"type\":\"select\",\"displays\":{\"totalCount\":0,\"items\":[]},\"source\":\"m1rsupg\",\"allowOther\":false,\"allowKey\":false},{\"code\":\"PaisContacto\",\"name\":\"PaisContacto\",\"label\":\"País de contacto\",\"type\":\"select\",\"displays\":{\"totalCount\":0,\"items\":[]},\"source\":\"m1rsupg\",\"allowOther\":false,\"allowKey\":false},{\"code\":\"esInvestigador\",\"name\":\"esInvestigador\",\"label\":\"Es investigador\",\"type\":\"select\",\"displays\":{\"totalCount\":0,\"items\":[]},\"terms\":{\"totalCount\":2,\"items\":[{\"key\":\"1\",\"label\":\"Sí\",\"terms\":{\"totalCount\":0,\"items\":[]}},{\"key\":\"2\",\"label\":\"No\",\"terms\":{\"totalCount\":0,\"items\":[]}}]},\"allowOther\":false,\"allowKey\":false,\"select\":{\"embedded\":true}},{\"code\":\"Provincia\",\"name\":\"ProvinciaContacto\",\"label\":\"Provincia de contacto\",\"type\":\"select\",\"displays\":{\"totalCount\":0,\"items\":[]},\"source\":\"mfekrpa\",\"allowOther\":false,\"allowKey\":false}]}}";

	@Test
	public void testLoadDefinition() {
		NodeDefinitionBuilder builder = new NodeDefinitionBuilder<>();
		NodeDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(NODE));

		assertEquals("mtvp02a", definition.getCode());
		assertEquals("org.monet.explorerintegration.Home", definition.getName());
		assertEquals("Home", definition.getLabel());
		assertEquals(0, definition.getOperations().size());
		assertEquals(1, definition.getViews().size());
	}

	@Test
	public void testLoadDesktopDefinition() {
		NodeDefinitionBuilder<DesktopDefinition> builder = new NodeDefinitionBuilder<>();
		DesktopDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(DESKTOP));
		List<DesktopViewDefinition> views = definition.getViews();
		DesktopViewDefinition view = views.get(0);

		assertEquals(1, definition.getViews().size());

		assertTrue(view.isDefault());
		assertEquals("mcticfw", view.getCode());
		assertEquals(2, view.getShow().getLinks().size());
		assertEquals("org.monet.explorerintegration.ContainerSample", view.getShow().getLinks().get(0).getValue());
		assertEquals("org.monet.explorerintegration.CollectionSample", view.getShow().getLinks().get(1).getValue());
	}

	@Test
	public void testLoadContainerDefinition() {
		NodeDefinitionBuilder<ContainerDefinition> builder = new NodeDefinitionBuilder<>();
		ContainerDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(CONTAINER));
		List<ContainerViewDefinition> views = definition.getViews();
		ContainerViewDefinition view = views.get(0);

		assertEquals(1, definition.getViews().size());
		assertTrue(definition.isEnvironment());

		assertEquals("mokspya", view.getCode());
		assertTrue(view.isDefault());
		assertEquals(1, view.getShow().getComponent().size());
		assertEquals("Normal", view.getShow().getComponent().get(0).getValue());
	}

	@Test
	public void testLoadCollectionDefinition() {
		NodeDefinitionBuilder<CollectionDefinition> builder = new NodeDefinitionBuilder<>();
		CollectionDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(COLLECTION));
		List<SetViewDefinition> views = definition.getViews();
		SetViewDefinition view = views.get(0);

		assertEquals(1, definition.getViews().size());

		assertEquals("m8ppx_a", view.getCode());
		assertNotNull(view.getShow().getItems());
	}

	@Test
	public void testLoadFormDefinition() {
		NodeDefinitionBuilder<FormDefinition> builder = new NodeDefinitionBuilder<>();
		FormDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(FORM));

		assertEquals(1, definition.getViews().size());
	}
}