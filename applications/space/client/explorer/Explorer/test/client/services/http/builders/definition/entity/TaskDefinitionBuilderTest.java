package client.services.http.builders.definition.entity;

import client.ApplicationTestCase;
import client.core.model.definition.entity.ProcessDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class TaskDefinitionBuilderTest extends ApplicationTestCase {

	private static final String DEFINITION = "{\"code\":\"mugkgvg\",\"name\":\"micv.procesos.GenerarDocumento\",\"type\":\"activity\",\"label\":\"Generar curriculum\",\"providers\":{\"totalCount\":0,\"items\":[]},\"places\":{\"totalCount\":5,\"items\":[{\"code\":\"miraydg\",\"name\":\"Iniciar\"},{\"code\":\"mcyvfwa\",\"name\":\"TipoCurriculum\",\"action\":{\"type\":\"edition\",\"label\":\"Escoja el tipo de documento a generar\"}},{\"code\":\"mkuqdja\",\"name\":\"EscogerItems\",\"action\":{\"type\":\"edition\",\"label\":\"Seleccione los items curriculares\"}},{\"code\":\"mo_gagw\",\"name\":\"Generacion\"},{\"code\":\"m9qfmrq\",\"name\":\"Final\"}]},\"views\":{\"totalCount\":1,\"items\":[{\"code\":\"me81_4g\",\"type\":\"abstract\",\"show\":{\"shortcut\":\"shCurriculum\"}}]}}";

	@Test
	public void testLoad() {
		TaskDefinitionBuilder<ProcessDefinition> builder = new TaskDefinitionBuilder();
		ProcessDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(DEFINITION));

		assertEquals(1, definition.getViews().size());
		assertEquals("me81_4g", definition.getViews().get(0).getCode());
	}

}