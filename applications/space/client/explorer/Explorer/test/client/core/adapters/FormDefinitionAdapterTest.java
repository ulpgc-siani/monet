package client.core.adapters;

import client.ApplicationTestCase;
import client.core.model.Form;
import client.core.model.Node;
import client.core.model.definition.entity.FormDefinition;
import client.core.model.definition.entity.NodeDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.definition.entity.NodeDefinitionBuilder;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class FormDefinitionAdapterTest extends ApplicationTestCase {

	private static final String NODE = "{\"id\":\"3\",\"label\":\"ContainerSample\",\"definition\":{\"code\":\"miyqwmg\",\"name\":\"org.monet.explorerintegration.ContainerSample\",\"type\":\"container\"},\"isComponent\":false,\"isLocked\":false,\"type\":\"container\",\"children\":[{\"id\":\"4\",\"type\":\"form\",\"label\":\"Record\",\"definition\":{\"code\":\"mj7c5ig\",\"name\":\"org.monet.explorerintegration.ContainerSample.Record\",\"type\":\"form\"}}]}";
	private static final String DEFINITION = "{\"code\":\"miyqwmg\",\"name\":\"org.monet.explorerintegration.ContainerSample\",\"type\":\"container\",\"label\":\"ContainerSample\",\"description\":\"\",\"views\":[{\"code\":\"mokspya\",\"type\":\"container\",\"label\":\"Record\",\"default\":true,\"show\":{\"components\":[{\"value\":\"Normal\",\"definition\":\"org.monet.explorerintegration.ContainerSample.Record\",\"qualifiedName\":\"org.monet.explorerintegration.ContainerSample.Record.Normal\"}]}}],\"environment\":true}";

	private static final String FORM = "{\"id\":\"7\",\"label\":\"valor del campo texto\",\"definition\":{\"code\":\"m6p3yvw\",\"name\":\"org.monet.explorerintegration.CollectionSample.Item\",\"type\":\"form\"},\"type\":\"form\",\"owner\":{\"id\":\"2\",\"label\":\"CollectionSample\",\"definition\":{\"code\":\"myu5yzg\",\"name\":\"org.monet.explorerintegration.CollectionSample\",\"type\":\"collection\"},\"type\":\"collection\"},\"ancestors\":[{\"id\":\"2\",\"label\":\"CollectionSample\",\"definition\":{\"code\":\"myu5yzg\",\"name\":\"org.monet.explorerintegration.CollectionSample\",\"type\":\"collection\"},\"isLocked\":true,\"type\":\"collection\"}],\"fields\":[{\"code\":\"m7hqppa\",\"type\":\"text\",\"label\":\"Campo texto\",\"value\":{\"value\":\"valor del campo texto\"}},{\"code\":\"mgvw0iq\",\"type\":\"node\",\"label\":\"Campo nodo\",\"value\":{}},{\"code\":\"mcxmnfg\",\"type\":\"summation\",\"label\":\"Campo summation\",\"value\":{\"value\":\"\"}},{\"code\":\"m7gkhkq\",\"type\":\"check\",\"label\":\"Campo check\",\"value\":{\"value\":\"\",\"label\":\"\",\"source\":{\"id\":\"\"},\"checked\":\"\"}},{\"code\":\"mdx7gsg\",\"type\":\"memo\",\"label\":\"Campo memo\",\"value\":{\"value\":\"soy unas observaciones\"}},{\"code\":\"m1owpxq\",\"type\":\"picture\",\"label\":\"Campo imagen\",\"value\":{\"filename\":\"\"}},{\"code\":\"mrwrwjq\",\"type\":\"file\",\"label\":\"Campo file\",\"value\":{\"filename\":\"\"}},{\"code\":\"mcav2aq\",\"type\":\"date\",\"label\":\"Campo fecha\",\"value\":[{\"value\":\"30/12/2014\"},{\"value\":\"02/01/2015\"}]},{\"code\":\"m18h0ca\",\"type\":\"serial\",\"label\":\"Campo serial\",\"value\":{\"value\":\"formato\"}},{\"code\":\"mpgywjq\",\"type\":\"composite\",\"label\":\"Campo composite\",\"value\":[{\"value\":{\"fields\":[{\"code\":\"morrcaq\",\"type\":\"text\",\"label\":\"Campo texto composite\",\"value\":{\"value\":\"Composite texto 1\"}},{\"code\":\"meuc4_g\",\"type\":\"boolean\",\"label\":\"Campo booleano composite\",\"value\":{\"value\":\"\"}}]}},{\"value\":{\"fields\":[{\"code\":\"morrcaq\",\"type\":\"text\",\"label\":\"Campo texto composite\",\"value\":{\"value\":\"Composite texto 2\"}},{\"code\":\"meuc4_g\",\"type\":\"boolean\",\"label\":\"Campo booleano composite\",\"value\":{\"value\":\"\"}}]}}]},{\"code\":\"muw_e5a\",\"type\":\"boolean\",\"label\":\"Campo booleano\",\"value\":{\"value\":\"\"}},{\"code\":\"mkatgtg\",\"type\":\"number\",\"label\":\"Campo número\",\"value\":{\"value\":{\"value\":\"0\"}}},{\"code\":\"mkfkq8g\",\"type\":\"select\",\"label\":\"Campo select\",\"value\":{\"value\":\"\",\"label\":\"\",\"source\":{\"id\":\"\"}}}]}";
	private static final String FORM_DEFINITION = "{\"code\":\"m6p3yvw\",\"name\":\"org.monet.explorerintegration.CollectionSample.Item\",\"type\":\"form\",\"label\":\"CollectionSample\",\"description\":\"\",\"views\":[{\"code\":\"m5wrlnq\",\"type\":\"form\",\"default\":false,\"show\":{\"fields\":[{\"value\":\"FieldBoolean\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldCheck\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldComposite\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldDate\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldFile\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldMemo\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldNode\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldNumber\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldPicture\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldSelect\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldSerial\",\"definition\":\"m6p3yvw\"},{\"value\":\"FieldText\",\"definition\":\"m6p3yvw\"}]}}],\"fields\":[{\"code\":\"m7hqppa\",\"name\":\"FieldText\",\"label\":\"Campo texto\",\"type\":\"text\",\"displays\":[],\"allowHistory\":{},\"length\":{\"min\":10,\"max\":100},\"edition\":{\"mode\":\"LOWERCASE\"},\"patterns\":[{\"regExp\":\"(.*)\",\"metas\":[{\"indicator\":\"meta1\",\"position\":1}]},{\"regExp\":\"(.*)\",\"metas\":[{\"indicator\":\"meta2\",\"position\":1}]}]},{\"code\":\"mgvw0iq\",\"name\":\"FieldNode\",\"label\":\"Campo nodo\",\"type\":\"node\",\"displays\":[],\"contain\":{\"node\":\"mj7c5ig\"},\"add\":{\"nodes\":[\"mj7c5ig\"]}},{\"code\":\"mcxmnfg\",\"name\":\"FieldSummation\",\"label\":\"Campo summation\",\"type\":\"summation\",\"displays\":[],\"terms\":[{\"key\":\"T001\",\"label\":\"Término 1\",\"type\":\"ACCOUNT\",\"multiple\":false,\"negative\":false,\"terms\":[]},{\"key\":\"T002\",\"label\":\"Término 2\",\"type\":\"ACCOUNT\",\"multiple\":false,\"negative\":false,\"terms\":[]}],\"select\":{\"depth\":10,\"flatten\":\"INTERNAL\"},\"format\":\"format\",\"range\":{\"min\":109,\"max\":109}},{\"code\":\"m7gkhkq\",\"name\":\"FieldCheck\",\"allowKey\":true,\"terms\":[{\"key\":\"t001\",\"label\":\"Término 1\",\"terms\":[]},{\"key\":\"t002\",\"label\":\"Término 2\",\"terms\":[]}],\"select\":{\"depth\":1,\"filter\":{\"tags\":[{\"value\":\"FieldText\",\"definition\":\"m6p3yvw\"}]},\"flatten\":\"INTERNAL\",\"root\":\"hola\"}},{\"code\":\"mdx7gsg\",\"name\":\"FieldMemo\",\"label\":\"Campo memo\",\"type\":\"memo\",\"displays\":[]},{\"code\":\"m1owpxq\",\"name\":\"FieldPicture\",\"label\":\"Campo imagen\",\"type\":\"picture\",\"displays\":[],\"defaultValue\":\"nophoto.jpg\",\"size\":{\"width\":100,\"height\":100}},{\"code\":\"mrwrwjq\",\"name\":\"FieldFile\",\"label\":\"Campo file\",\"type\":\"file\",\"displays\":[]},{\"code\":\"mcav2aq\",\"name\":\"FieldDate\",\"label\":\"Campo fecha\",\"type\":\"date\",\"displays\":[],\"precision\":\"DAYS\",\"purpose\":\"NEAR_DATE\",\"range\":{\"min\":10,\"max\":10}},{\"code\":\"m18h0ca\",\"name\":\"FieldSerial\",\"label\":\"Campo serial\",\"type\":\"serial\",\"displays\":[],\"serial\":{\"name\":\"SerialName\",\"format\":\"formato\"}},{\"code\":\"mpgywjq\",\"name\":\"FieldComposite\",\"label\":\"Campo composite\",\"type\":\"composite\",\"displays\":[],\"extensible\":false,\"conditional\":false,\"range\":{\"min\":10,\"max\":10},\"fields\":[{\"code\":\"morrcaq\",\"name\":\"FieldTextComposite\",\"label\":\"Campo texto composite\",\"type\":\"text\",\"displays\":[],\"patterns\":[]},{\"code\":\"meuc4_g\",\"name\":\"FieldBooleanComposite\",\"label\":\"Campo booleano composite\",\"type\":\"boolean\",\"displays\":[]}],\"view\":{\"show\":{\"fields\":[\"morrcaq\",\"meuc4_g\"]}}},{\"code\":\"muw_e5a\",\"name\":\"FieldBoolean\",\"label\":\"Campo booleano\",\"type\":\"boolean\",\"displays\":[]},{\"code\":\"mkatgtg\",\"name\":\"FieldNumber\",\"label\":\"Campo número\",\"type\":\"number\",\"displays\":[],\"format\":\"##\",\"range\":{\"min\":100,\"max\":100},\"edition\":\"BUTTON\"},{\"code\":\"mkfkq8g\",\"name\":\"FieldSelect\",\"label\":\"Campo select\",\"type\":\"select\",\"displays\":[],\"terms\":[{\"key\":\"T001\",\"label\":\"Término 1\",\"terms\":[]},{\"key\":\"T001\",\"label\":\"Término 1\",\"terms\":[]}],\"allowHistory\":{},\"allowOther\":true,\"allowKey\":true,\"select\":{\"depth\":10,\"filter\":{\"tags\":[{\"value\":\"FieldText\",\"definition\":\"m6p3yvw\"}]},\"flatten\":\"ALL\",\"root\":{\"value\":\"FieldText\",\"definition\":\"m6p3yvw\"},\"context\":\"null\",\"embedded\":true}}]}";

	@Test
	public void testAdaptNode() {
		Node<NodeDefinition> node = loadNode();
		NodeDefinition definition = loadNodeDefinition();

		assertEquals(0, node.getDefinition().getViews().size());

		new NodeDefinitionAdapter(null).adapt(node, definition);

		assertEquals(1, node.getDefinition().getViews().size());
	}

	@Test
	public void testAdaptForm() {
		Form form = loadForm();
		FormDefinition definition = loadFormDefinition();

		assertEquals(0, form.getDefinition().getViews().size());

		new NodeDefinitionAdapter(null).adapt(form, definition);

		assertEquals(1, form.getDefinition().getViews().size());
	}

	private Node loadNode() {
		return new client.services.http.builders.NodeBuilder<>().build((HttpInstance) JsonUtils.safeEval(NODE));
	}

	private Form loadForm() {
		return new client.services.http.builders.FormBuilder().build((HttpInstance) JsonUtils.safeEval(FORM));
	}

	private NodeDefinition loadNodeDefinition() {
		return new NodeDefinitionBuilder<>().build((HttpInstance) JsonUtils.safeEval(DEFINITION));
	}

	private FormDefinition loadFormDefinition() {
		return new NodeDefinitionBuilder<FormDefinition>().build((HttpInstance) JsonUtils.safeEval(FORM_DEFINITION));
	}

}