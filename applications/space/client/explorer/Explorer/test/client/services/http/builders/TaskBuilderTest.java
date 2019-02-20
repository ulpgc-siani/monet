package client.services.http.builders;

import client.ApplicationTestCase;
import client.core.model.Task;
import client.core.model.Process;
import client.services.http.HttpInstance;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class TaskBuilderTest extends ApplicationTestCase {

	private static final String TASK = "{\"id\":\"62\",\"label\":\"Generación curriculum\",\"type\":\"activity\",\"definition\":{\"code\":\"mugkgvg\",\"name\":\"micv.procesos.GenerarDocumento\",\"type\":\"activity\"},\"updateDate\":1423557224000,\"state\":\"finished\",\"shortcuts\":{\"shCurriculum\":{\"id\":\"178\",\"label\":\"Fecyt\",\"type\":\"document\",\"definition\":{\"code\":\"mfnof4a\",\"name\":\"micv.documentos.Fecyt\",\"type\":\"document\"}}},\"workMap\":{\"place\":{\"code\":\"m9qfmrq\"}}}";

	@Test
	public void testLoad() {
		TaskBuilder<Task> builder = new TaskBuilder();
		Task task = builder.build((HttpInstance) JsonUtils.safeEval(TASK));

		assertTrue(task instanceof Process);
		assertEquals(1, ((Process)task).getShortcuts().keySet().size());
		assertEquals("178", ((Process)task).getShortcut("shCurriculum").getId());
	}

}