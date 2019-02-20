package client.core.system;

import client.ApplicationTestCase;
import client.core.model.definition.entity.ContainerDefinition;
import client.services.http.HttpInstance;
import client.services.http.builders.NodeBuilder;
import client.services.http.builders.definition.entity.NodeDefinitionBuilder;
import com.google.gwt.core.client.JsonUtils;
import org.junit.Test;

public class ContainerTest extends ApplicationTestCase {

	public static final String NODE = "{\"id\":\"7\",\"label\":\" \",\"type\":\"container\",\"description\":\"El modelo de Currículum Vítae Normalizado es resultado de un trabajo en colaboración con los agentes que interaccionan con los contenidos de las trayectorias curriculares de docentes, investigadores y tecnólogos, con la finalidad de facilitar los intercambios de datos\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\"},\"owner\":{\"id\":\"3\",\"type\":\"collection\",\"label\":\"Curriculums\",\"definition\":{\"code\":\"currimulums\",\"name\":\"micv.Curriculums\",\"type\":\"collection\"}},\"children\":{\"totalCount\":3,\"items\":[{\"id\":\"8\",\"type\":\"form\",\"label\":\" \",\"definition\":{\"code\":\"ic.000.010\",\"name\":\"micv.formularios.Ic000010\",\"type\":\"form\"}},{\"id\":\"9\",\"type\":\"collection\",\"label\":\"Tipos de incidencia\",\"definition\":{\"code\":\"m5_p1gg\",\"name\":\"micv.formularios.ICColeccion\",\"type\":\"collection\"}},{\"id\":\"10\",\"type\":\"collection\",\"label\":\"Documentos\",\"definition\":{\"code\":\"mzdpp8g\",\"name\":\"micv.documentos.Coleccion\",\"type\":\"collection\"}}]}}";
	public static final String DEFINITION = "{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\",\"label\":\"Curriculum Vitae\",\"operations\":[{\"code\":\"moigazg\",\"label\":\"Fecyt\",\"description\":\"\",\"enabled\":true,\"visible\":true},{\"code\":\"mlhnokw\",\"label\":\"Micinn\",\"description\":\"\",\"enabled\":true,\"visible\":true},{\"code\":\"mzwhgca\",\"label\":\"Aciisi\",\"description\":\"\",\"enabled\":true,\"visible\":true}],\"views\":{\"totalCount\":8,\"items\":[{\"code\":\"mcfQD9g\",\"name\":\"Perfil\",\"type\":\"container\",\"label\":\"Datos de indentificación y contacto\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"Vista\",\"definition\":\"ic.000.010\"}]}}},{\"code\":\"map62bg\",\"name\":\"Documentos\",\"type\":\"container\",\"label\":\"Documentos\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"Vista\",\"definition\":\"mzdpp8g\"}]}}},{\"code\":\"m_2mxtq\",\"type\":\"container\",\"label\":\"Situación profesional\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"SituacionProfesional\",\"definition\":\"m5_p1gg\"}]}}},{\"code\":\"m_eabug\",\"type\":\"container\",\"label\":\"Formación académica recibida\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"FormacionAcademicaRecibida\",\"definition\":\"m5_p1gg\"}]}}},{\"code\":\"mscsktq\",\"type\":\"container\",\"label\":\"Actividad docente\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"ActividadDocente\",\"definition\":\"m5_p1gg\"}]}}},{\"code\":\"mdopcxq\",\"type\":\"container\",\"label\":\"Experiencia científica y tecnológica\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"ExperienciaCientificaTecnologica\",\"definition\":\"m5_p1gg\"}]}}},{\"code\":\"mwp7mag\",\"type\":\"container\",\"label\":\"Actividad científica y tecnología\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"ActividadCientificaTecnologica\",\"definition\":\"m5_p1gg\"}]}}},{\"code\":\"miomlna\",\"type\":\"container\",\"label\":\"Actividad en el campo de la sanidad\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"ActividadCampoSanidad\",\"definition\":\"m5_p1gg\"}]}}}]},\"environment\":true}";

	@Test
	public void testLoadContainer() {
		NodeBuilder<client.core.model.Container> builder = new NodeBuilder<>();
		NodeDefinitionBuilder<ContainerDefinition> definitionBuilder = new NodeDefinitionBuilder<>();
		client.core.model.Container container = builder.build((HttpInstance) JsonUtils.safeEval(NODE));

		container.setDefinition(definitionBuilder.build((HttpInstance) JsonUtils.safeEval(DEFINITION)));

		assertTrue(container.isEnvironment());
		assertEquals(8, container.getViews().size());
	}

}