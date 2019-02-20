package client.services.http.builders.definition.entity;

import client.ApplicationTestCase;
import client.core.model.definition.entity.ActivityDefinition;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class ActivityDefinitionBuilderTest extends ApplicationTestCase {

	public static final String ACTIVITY_DEFINITION = "{\"code\":\"mugkgvg\",\"name\":\"micv.procesos.GenerarDocumento\",\"type\":\"activity\",\"label\":\"Generar curriculum\",\"providers\":{\"totalCount\":0,\"items\":[]},\"places\":{\"totalCount\":5,\"items\":[{\"code\":\"miraydg\",\"name\":\"Iniciar\"},{\"code\":\"mcyvfwa\",\"name\":\"TipoCurriculum\",\"action\":{\"type\":\"edition\",\"label\":\"Escoja el tipo de documento a generar\"}},{\"code\":\"mkuqdja\",\"name\":\"EscogerItems\",\"action\":{\"type\":\"edition\",\"label\":\"Seleccione los items curriculares\"}},{\"code\":\"mo_gagw\",\"name\":\"Generacion\"},{\"code\":\"m9qfmrq\",\"name\":\"Final\"}]}}";

	@Test
	public void testLoadDefinition() {
		ActivityDefinitionBuilder builder = new ActivityDefinitionBuilder();
		ActivityDefinition definition = builder.build((HttpInstance) JsonUtils.safeEval(ACTIVITY_DEFINITION));

		assertEquals(0, definition.getProviders().size());
		assertEquals(5, definition.getPlaces().size());
	}

}