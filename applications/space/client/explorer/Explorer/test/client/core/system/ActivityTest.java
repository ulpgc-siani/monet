package client.core.system;

import client.ApplicationTestCase;
import client.core.model.definition.entity.ActivityDefinition;
import client.core.model.workmap.LineAction;
import client.services.http.HttpInstance;
import client.services.http.builders.TaskBuilder;
import client.services.http.builders.definition.entity.TaskDefinitionBuilder;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class ActivityTest extends ApplicationTestCase {

	public static final String LINE = "{\"id\":\"1\",\"label\":\"TareaOne\",\"type\":\"activity\",\"description\":\"Línea\",\"definition\":{\"code\":\"mkpasmg\",\"name\":\"org.monet.explorerintegration.TareaOne\",\"type\":\"activity\"},\"updateDate\":1421232582000,\"state\":\"pending\",\"workMap\":{\"place\":{\"action\":{\"type\":\"line\",\"dueDate\":-1}}}}";

	@Test
	public void testLine() {
		TaskBuilder<Activity> builder = new TaskBuilder<>();
		Activity activity = builder.build((HttpInstance) JsonUtils.safeEval(LINE));

		assertEquals("1", activity.getId());
		assertTrue(activity.getWorkMap().getPlace().getAction() instanceof LineAction);

		LineAction action = (LineAction) activity.getWorkMap().getPlace().getAction();
		assertNull(action.getDueDate());
	}

	@Test
	public void testLoadViews() {
		TaskBuilder<Activity> builder = new TaskBuilder<>();
		Activity activity = builder.build((HttpInstance) JsonUtils.safeEval("{\"id\":\"82\",\"label\":\"Generación curriculum\",\"type\":\"activity\",\"definition\":{\"code\":\"mugkgvg\",\"name\":\"micv.procesos.GenerarDocumento\",\"type\":\"activity\"},\"updateDate\":1423578051000,\"state\":\"finished\",\"shortcuts\":{\"shCurriculum\":{\"id\":\"216\",\"label\":\"Fecyt\",\"type\":\"document\",\"definition\":{\"code\":\"mfnof4a\",\"name\":\"micv.documentos.Fecyt\",\"type\":\"document\"}}},\"workMap\":{\"place\":{\"code\":\"m9qfmrq\"}}}"));

		TaskDefinitionBuilder<ActivityDefinition> definitionBuilder = new TaskDefinitionBuilder<>();
		ActivityDefinition activityDefinition = definitionBuilder.build((HttpInstance) JsonUtils.safeEval("{\"code\":\"mugkgvg\",\"name\":\"micv.procesos.GenerarDocumento\",\"type\":\"activity\",\"label\":\"Generar curriculum\",\"providers\":{\"totalCount\":0,\"items\":[]},\"places\":{\"totalCount\":5,\"items\":[{\"code\":\"miraydg\",\"name\":\"Iniciar\"},{\"code\":\"mcyvfwa\",\"name\":\"TipoCurriculum\",\"action\":{\"type\":\"edition\",\"label\":\"Escoja el tipo de documento a generar\"}},{\"code\":\"mkuqdja\",\"name\":\"EscogerItems\",\"action\":{\"type\":\"edition\",\"label\":\"Seleccione los items curriculares\"}},{\"code\":\"mo_gagw\",\"name\":\"Generacion\"},{\"code\":\"m9qfmrq\",\"name\":\"Final\"}]},\"views\":{\"totalCount\":1,\"items\":[{\"code\":\"me81_4g\",\"type\":\"abstract\",\"label\":\"Curriculum\",\"show\":{\"shortcut\":\"shCurriculum\"}}]}}"));

		activity.setDefinition(activityDefinition);

		assertEquals(2, activity.getViews().size());
		assertEquals("me81_4g", ((TaskView)activity.getViews().get(1)).getDefinition().getCode());
		assertEquals("Curriculum", ((TaskView)activity.getViews().get(1)).getLabel());
	}

}