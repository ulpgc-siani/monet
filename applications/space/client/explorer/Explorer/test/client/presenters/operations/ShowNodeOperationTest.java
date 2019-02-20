package client.presenters.operations;

import client.ApplicationTestCase;
import client.core.model.*;
import client.core.model.definition.entity.ContainerDefinition;
import client.core.model.definition.entity.FormDefinition;
import client.core.model.definition.entity.NodeDefinition;
import client.core.model.workmap.DelegationAction;
import client.core.model.workmap.LineAction;
import client.services.*;
import client.services.callback.*;
import client.services.http.HttpInstance;
import client.services.http.builders.ContainerBuilder;
import client.services.http.builders.FormBuilder;
import client.services.http.builders.NodeBuilder;
import client.services.http.builders.SpaceBuilder;
import client.services.http.builders.definition.entity.ContainerDefinitionBuilder;
import client.services.http.builders.definition.entity.FormDefinitionBuilder;
import client.services.http.builders.definition.entity.NodeDefinitionBuilder;
import com.google.gwt.core.client.JsonUtils;
import cosmos.presenters.Operation;
import cosmos.presenters.Presenter;
import cosmos.types.Date;
import cosmos.types.DatePrecision;
import cosmos.types.DayOfWeek;
import org.junit.Test;

public class ShowNodeOperationTest extends ApplicationTestCase {

	@Test
	public void testShowContainer() {
		ShowNodeOperation operation = new ShowNodeOperation(createContext(), new client.core.system.Container("1",""), null);
		operation.inject(createServices());
		operation.execute();
	}

	@Test
	public void testFindViewFromTab() {
		client.core.model.Space space = new SpaceBuilder().build((HttpInstance) JsonUtils.safeEval("{\"name\":\"monet\",\"title\":\"MiCV\",\"subTitle\":\"Gestor de items curriculares\",\"language\":\"es\",\"theme\":\"micv\",\"instanceId\":\"746d0a91-7587-408b-a428-2dd6d36d3581\",\"federation\":{\"label\":\"English Text\",\"logoUrl\":\"http://10.13.13.145:8080/monet/explorer/api/model$file/?file=images/organization.logo.png\",\"url\":\"http://10.13.13.145:8080/federation\"},\"account\":{\"id\":\"36\",\"user\":{\"id\":\"36\",\"fullName\":\"usuario\",\"email\":\"usuario@mock.com\",\"photo\":\"\"},\"rootNode\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\"}}},\"configuration\":{\"domain\":\"10.13.13.145\",\"url\":\"http://10.13.13.145:8080/monet/explorer\",\"apiUrl\":\"http://10.13.13.145:8080/monet/explorer/api\",\"pushUrl\":\"http://10.13.13.145:8080/monet/explorer/push\",\"analyticsUrl\":\"http://10.13.13.145:8080/monet/analytics\",\"fmsUrl\":\"http://10.13.13.145:8080/monet/servlet/fms\",\"digitalSignatureUrl\":\"http://10.13.13.145:8080/monet/explorer/digitalsignature\",\"imagesPath\":\"/monet/explorer/images\"},\"tabs\":[{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\"}},\"view\":{\"code\":\"mcfQD9g\",\"name\":\"Perfil\",\"type\":\"NODE_VIEW\",\"label\":\"Datos de indentificación y contacto\"},\"type\":\"ENVIRONMENT\",\"active\":false},{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\"}},\"view\":{\"code\":\"m_2mxtq\",\"type\":\"NODE_VIEW\",\"label\":\"Situación profesional\"},\"type\":\"ENVIRONMENT\",\"active\":false},{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\"}},\"view\":{\"code\":\"m_eabug\",\"type\":\"NODE_VIEW\",\"label\":\"Formación académica recibida\"},\"type\":\"ENVIRONMENT\",\"active\":false},{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\"}},\"view\":{\"code\":\"mscsktq\",\"type\":\"NODE_VIEW\",\"label\":\"Actividad docente\"},\"type\":\"ENVIRONMENT\",\"active\":false},{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\"}},\"view\":{\"code\":\"mdopcxq\",\"type\":\"NODE_VIEW\",\"label\":\"Experiencia científica y tecnológica\"},\"type\":\"ENVIRONMENT\",\"active\":false},{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\"}},\"view\":{\"code\":\"mwp7mag\",\"type\":\"NODE_VIEW\",\"label\":\"Actividad científica y tecnología\"},\"type\":\"ENVIRONMENT\",\"active\":false},{\"entity\":{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\"}},\"view\":{\"code\":\"miomlna\",\"type\":\"NODE_VIEW\",\"label\":\"Actividad en el campo de la sanidad\"},\"type\":\"ENVIRONMENT\",\"active\":false}],\"dictionary\":{\"definitions\":{\"totalCount\":102,\"items\":[{\"code\":\"mbk_yva\",\"name\":\"micv.entidades.Ficha\",\"label\":\"Entidad\"},{\"code\":\"Icpadre\",\"name\":\"micv.formularios.Icpadre\",\"label\":\"Item curricular\"},{\"code\":\"ic.030.010\",\"name\":\"micv.formularios.Ic030010\",\"label\":\"Docencia impartida\"},{\"code\":\"mnwlavw\",\"name\":\"micv.documentos.Aciisi.Exportador\",\"label\":\"Exportador Aciisi\"},{\"code\":\"mhtytaa\",\"name\":\"micv.CatalogoEditoriales\",\"label\":\"Editoriales\"},{\"code\":\"mamth_a\",\"name\":\"micv.RoleInvestigador\",\"label\":\"Investigador\"},{\"code\":\"m1rsupg\",\"name\":\"micv.TesauroLugares\",\"label\":\"Lugares\"},{\"code\":\"mjoy1rg\",\"name\":\"micv.CatalogoEntidades.Index\",\"label\":\"Entidades\"},{\"code\":\"ic.060.010.030\",\"name\":\"micv.formularios.Ic060010030\",\"label\":\"Trabajo de divulgación\"},{\"code\":\"ic.060.020.010\",\"name\":\"micv.formularios.Ic060020010\",\"label\":\"Comités científicos\"},{\"code\":\"mfnof4a\",\"name\":\"micv.documentos.Fecyt\",\"label\":\"Fecyt\"},{\"code\":\"ic.060.030.030\",\"name\":\"micv.formularios.Ic060030030\",\"label\":\"Pertenencia a sociedades científicas y/o profesionales\"},{\"code\":\"ic.010.020\",\"name\":\"micv.formularios.Ic010020\",\"label\":\"Cargo y actividad desempeñada\"},{\"code\":\"mnttymw\",\"name\":\"micv.documentos.Fecyt.Exportador\",\"label\":\"Exportador Fecyt\"},{\"code\":\"mnr8nqq\",\"name\":\"micv.formularios.ICColeccion.Index\",\"label\":\"Items curriculares\"},{\"code\":\"ic.040.080\",\"name\":\"micv.formularios.Ic040080\",\"label\":\"Tutoría de atención salud\"},{\"code\":\"m5_p1gg\",\"name\":\"micv.formularios.ICColeccion\",\"label\":\"Tipos de incidencia\"},{\"code\":\"mljv8aa\",\"name\":\"micv.editoriales.Coleccion.Index\",\"label\":\"Editoriales\"},{\"code\":\"mx63isq\",\"name\":\"micv.Curriculums.Index\",\"label\":\"Curriculums\"},{\"code\":\"ic.030.090\",\"name\":\"micv.formularios.Ic030090\",\"label\":\"Ponencia de innovación docente\"},{\"code\":\"ic.040.090\",\"name\":\"micv.formularios.Ic040090\",\"label\":\"Curso y seminario impartido en el campo de la salud\"},{\"code\":\"ic.060.030.040\",\"name\":\"micv.formularios.Ic060030040\",\"label\":\"Pertenencia a redes\"},{\"code\":\"mqhcwba\",\"name\":\"micv.TesauroDoctorados\",\"label\":\"Doctorados\"},{\"code\":\"ic.040.010\",\"name\":\"micv.formularios.Ic040010\",\"label\":\"Experiencias en instituciones de la UE\"},{\"code\":\"myu7xaw\",\"name\":\"micv.documentos.Coleccion.Index\",\"label\":\"Entidades\"},{\"code\":\"ic.060.010.020\",\"name\":\"micv.formularios.Ic060010020\",\"label\":\"Congresos, Jornadas, Seminarios...\"},{\"code\":\"ic.060.020.040\",\"name\":\"micv.formularios.Ic060020040\",\"label\":\"Gestión de I+D+i\"},{\"code\":\"mfgs_fw\",\"name\":\"micv.TesauroPalsClave\",\"label\":\"Palabras clave\"},{\"code\":\"mq6ecog\",\"name\":\"micv.procesos.TestAciisi\",\"label\":\"Testeo para la Aciisi\"},{\"code\":\"ic.050.020.020\",\"name\":\"micv.formularios.Ic050020020\",\"label\":\"Proyectos I+D+i\"},{\"code\":\"ic.060.020.050\",\"name\":\"micv.formularios.Ic060020050\",\"label\":\"Representación nacional en foros\"},{\"code\":\"mgjgfbq\",\"name\":\"micv.documentos.Micinn.Exportador\",\"label\":\"Exportador Micinn\"},{\"code\":\"ic.030.030\",\"name\":\"micv.formularios.Ic030030\",\"label\":\"Programa de Formación en I+D Sanitaria, y/o I+D Postformación Sanitaria Especializada Impartida\"},{\"code\":\"ic.060.030.080\",\"name\":\"micv.formularios.Ic060030080\",\"label\":\"Premio innovación docente\"},{\"code\":\"m4qf1eg\",\"name\":\"micv.documentos.CargaDocente\",\"label\":\"Carga docente\"},{\"code\":\"mdnaszg\",\"name\":\"micv.TesauroPostGrados\",\"label\":\"PostGrados\"},{\"code\":\"mw3uwrg\",\"name\":\"micv.CatalogoEditoriales.Index\",\"label\":\"Editoriales\"},{\"code\":\"m8s43nw\",\"name\":\"micv.TesauroTitulaciones\",\"label\":\"Tipos de entidad\"},{\"code\":\"mhp_atg\",\"name\":\"micv.editoriales.Ficha\",\"label\":\"Editorial\"},{\"code\":\"ic.020.050\",\"name\":\"micv.formularios.Ic020050\",\"label\":\"Curso y seminario recibido cuyo objetivo sea la mejora de la docencia\"},{\"code\":\"ic.040.120\",\"name\":\"micv.formularios.Ic040120\",\"label\":\"Proyecto de planificación/mejora sanitaria\"},{\"code\":\"m8mf2pw\",\"name\":\"micv.TesauroTiposEntidad\",\"label\":\"Tipos de entidad\"},{\"code\":\"ic.040.020\",\"name\":\"micv.formularios.Ic040020\",\"label\":\"Experiencias en instituciones de la UE\"},{\"code\":\"mxbh4ng\",\"name\":\"micv.documentos.Aciisi\",\"label\":\"Documento Aciisi\"},{\"code\":\"ic.060.020.030\",\"name\":\"micv.formularios.Ic060020030\",\"label\":\"Organización actividad I+D+i\"},{\"code\":\"mxap5iq\",\"name\":\"micv.editoriales.Coleccion\",\"label\":\"Editoriales\"},{\"code\":\"mk8fkfq\",\"name\":\"micv.documentos.Micinn\",\"label\":\"Micinn\"},{\"code\":\"ic.030.070\",\"name\":\"micv.formularios.Ic030070\",\"label\":\"Elaboración de material docente\"},{\"code\":\"maja39g\",\"name\":\"micv.documentos.Excel\",\"label\":\"Excel\"},{\"code\":\"mktvluw\",\"name\":\"micv.procesos.TestFecyt\",\"label\":\"Testeo para la Fecyt\"},{\"code\":\"ic.060.030.010\",\"name\":\"micv.formularios.Ic060030010\",\"label\":\"Ayuda y Beca Obtenida\"},{\"code\":\"ic.060.010.010\",\"name\":\"micv.formularios.Ic060010010\",\"label\":\"Publicación Científica o Técnica\"},{\"code\":\"ic.040.130\",\"name\":\"micv.formularios.Ic040130\",\"label\":\"Ponencia de atención a la salud\"},{\"code\":\"ic.030.040\",\"name\":\"micv.formularios.Ic030040\",\"label\":\"Dirección de Tesis y Proyectos\"},{\"code\":\"currimulums\",\"name\":\"micv.Curriculums\",\"label\":\"Curriculums\"},{\"code\":\"mu_sfca\",\"name\":\"micv.entidades.Coleccion.Index\",\"label\":\"Entidades\"},{\"code\":\"ic.060.030.100\",\"name\":\"micv.formularios.Ic060030100\",\"label\":\"Compendio de otros méritos\"},{\"code\":\"ic.020.010.020\",\"name\":\"micv.formularios.Ic020010020\",\"label\":\"Otra formación universitaria\"},{\"code\":\"ic.000.010\",\"name\":\"micv.formularios.Ic000010\",\"label\":\"Información personal\"},{\"code\":\"ic.060.030.070\",\"name\":\"micv.formularios.Ic060030070\",\"label\":\"Tramos de investigación\"},{\"code\":\"ic.060.030.050\",\"name\":\"micv.formularios.Ic060030050\",\"label\":\"Título y premio obtenido\"},{\"code\":\"SeleccionarTipo\",\"name\":\"micv.SeleccionarTipo\",\"label\":\"Seleccion del sitio al que se desea enviar el curriculum\"},{\"code\":\"ic.020.060\",\"name\":\"micv.formularios.Ic020060\",\"label\":\"Conocimiento de idiomas\"},{\"code\":\"mzxdakg\",\"name\":\"micv.documentos.Curriculum\",\"label\":\"Curriculum\"},{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"label\":\"Curriculum Vitae\"},{\"code\":\"ic.030.080\",\"name\":\"micv.formularios.Ic030080\",\"label\":\"Proyecto de innovación docente\"},{\"code\":\"mguslja\",\"name\":\"micv.SeleccionarItems\",\"label\":\"Seleccion de items que participarán en la generación de su curriculum\"},{\"code\":\"ic.030.020\",\"name\":\"micv.formularios.Ic030020\",\"label\":\"Programa de Formación Sanitaria Especializada Impartida\"},{\"code\":\"mutrlra\",\"name\":\"micv.RoleAdministrador\",\"label\":\"Administrador\"},{\"code\":\"mqwqr0a\",\"name\":\"micv.documentos.Xml\",\"label\":\"Xml\"},{\"code\":\"ic.040.140\",\"name\":\"micv.formularios.Ic040140\",\"label\":\"Otras actividades/méritos no incluidos en el campo de la sanidad\"},{\"code\":\"mjcpzxg\",\"name\":\"micv.CatalogoEntidades\",\"label\":\"Entidades\"},{\"code\":\"ms7ti8a\",\"name\":\"micv.procesos.TestMicinn\",\"label\":\"Testeo para la Micinn\"},{\"code\":\"ic.040.110\",\"name\":\"micv.formularios.Ic040110\",\"label\":\"Proyecto de Innovación Sanitaria\"},{\"code\":\"miikw2g\",\"name\":\"micv.EscritorioAdministrador\",\"label\":\"Admin\"},{\"code\":\"ic.030.060\",\"name\":\"micv.formularios.Ic030060\",\"label\":\"Cursos y seminarios impartidos\"},{\"code\":\"mbvpdkw\",\"name\":\"micv.entidades.Coleccion\",\"label\":\"Entidades\"},{\"code\":\"ic.050.020.010\",\"name\":\"micv.formularios.Ic050020010\",\"label\":\"Proyectos I+D+i\"},{\"code\":\"ic.060.020.020\",\"name\":\"micv.formularios.Ic060020020\",\"label\":\"Otros modos de colaboración\"},{\"code\":\"ic.050.020.030\",\"name\":\"micv.formularios.Ic050020030\",\"label\":\"Obras artísticas dirigidas\"},{\"code\":\"ic.010.010\",\"name\":\"micv.formularios.Ic010010\",\"label\":\"Cargo y actividad desempeñada\"},{\"code\":\"ic.050.010\",\"name\":\"micv.formularios.Ic050010\",\"label\":\"Participación grupos I+D+i\"},{\"code\":\"ic.060.030.090\",\"name\":\"micv.formularios.Ic060030090\",\"label\":\"Reconocimiento de actividad docente\"},{\"code\":\"mwgwh5a\",\"name\":\"micv.TesauroItemsCurriculares\",\"label\":\"Items curriculares\"},{\"code\":\"mj_j3iq\",\"name\":\"micv.documentos.Cvn\",\"label\":\"Cvn\"},{\"code\":\"ic.020.020\",\"name\":\"micv.formularios.Ic020020\",\"label\":\"Formación distinta a la académica reglada y a la sanitaria\"},{\"code\":\"ic.040.100\",\"name\":\"micv.formularios.Ic040100\",\"label\":\"Elaboración de protocolos de atención a la salud\"},{\"code\":\"m60by_a\",\"name\":\"micv.TesauroCategoriaA\",\"label\":\"Categorías A\"},{\"code\":\"ic.060.030.020\",\"name\":\"micv.formularios.Ic060030020\",\"label\":\"Pertenencia a sociedades científicas o profesionales\"},{\"code\":\"ic.060.020.060\",\"name\":\"micv.formularios.Ic060020060\",\"label\":\"Experiencia evaluación I+D\"},{\"code\":\"ic.030.050\",\"name\":\"micv.formularios.Ic030050\",\"label\":\"Tutoría académica\"},{\"code\":\"ic.060.010.050\",\"name\":\"micv.formularios.Ic060010050\",\"label\":\"Estancia en centros I+D+i\"},{\"code\":\"ic.050.030.010\",\"name\":\"micv.formularios.Ic050030010\",\"label\":\"Propiedad intelectual e industrial\"},{\"code\":\"ic.020.010.010\",\"name\":\"micv.formularios.Ic020010010\",\"label\":\"Diplomaturas, licenciaturas e ingenierías, grados y másteres\"},{\"code\":\"mzdpp8g\",\"name\":\"micv.documentos.Coleccion\",\"label\":\"Documentos\"},{\"code\":\"ic.060.030.060\",\"name\":\"micv.formularios.Ic060030060\",\"label\":\"Carrera profesional\"},{\"code\":\"ic.030.100\",\"name\":\"micv.formularios.Ic030100\",\"label\":\"Otras actividades/méritos no incluidos en la actividad docente\"},{\"code\":\"mfekrpa\",\"name\":\"micv.TesauroProvincia\",\"label\":\"Provincias\"},{\"code\":\"ic.040.030\",\"name\":\"micv.formularios.Ic040030\",\"label\":\"Experiencias en instituciones de la UE\"},{\"code\":\"ic.020.030\",\"name\":\"micv.formularios.Ic020030\",\"label\":\"Programa de Formación Sanitaria Especializada Recibida\"},{\"code\":\"mfcqxja\",\"name\":\"micv.TesauroIdiomas\",\"label\":\"Idiomas\"},{\"code\":\"_i1ppeg\",\"name\":\"org.monet.metamodel.internal.TaskOrderDefinition\",\"label\":\"Configuración de un encargo\"}]}}}"));
		Node rootNode = (Node) new NodeBuilder().build((HttpInstance) JsonUtils.safeEval("{\"id\":\"21\",\"label\":\"Curriculum Vitae\",\"type\":\"container\",\"definition\":{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\"},\"owner\":{\"id\":\"17\",\"type\":\"collection\",\"label\":\"Curriculums\",\"definition\":{\"code\":\"currimulums\",\"name\":\"micv.Curriculums\",\"type\":\"collection\"}},\"children\":{\"totalCount\":3,\"items\":[{\"id\":\"22\",\"type\":\"form\",\"label\":\" \",\"definition\":{\"code\":\"ic.000.010\",\"name\":\"micv.formularios.Ic000010\",\"type\":\"form\"}},{\"id\":\"23\",\"type\":\"collection\",\"label\":\"Tipos de incidencia\",\"definition\":{\"code\":\"m5_p1gg\",\"name\":\"micv.formularios.ICColeccion\",\"type\":\"collection\"}},{\"id\":\"24\",\"type\":\"collection\",\"label\":\"Documentos\",\"definition\":{\"code\":\"mzdpp8g\",\"name\":\"micv.documentos.Coleccion\",\"type\":\"collection\"}}]}}"));
		NodeDefinition rootNodeDefinition = new NodeDefinitionBuilder().build((HttpInstance) JsonUtils.safeEval("{\"code\":\"mpnu5yw\",\"name\":\"micv.CvInvestigador\",\"type\":\"container\",\"label\":\"Curriculum Vitae\",\"operations\":[{\"code\":\"moigazg\",\"label\":\"Fecyt\",\"description\":\"\",\"enabled\":true,\"visible\":true},{\"code\":\"mlhnokw\",\"label\":\"Micinn\",\"description\":\"\",\"enabled\":true,\"visible\":true},{\"code\":\"mzwhgca\",\"label\":\"Aciisi\",\"description\":\"\",\"enabled\":true,\"visible\":true}],\"views\":{\"totalCount\":7,\"items\":[{\"code\":\"mcfQD9g\",\"name\":\"Perfil\",\"type\":\"container\",\"label\":\"Datos de indentificación y contacto\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"Vista\",\"definition\":\"ic.000.010\"}]}}},{\"code\":\"m_2mxtq\",\"type\":\"container\",\"label\":\"Situación profesional\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"SituacionProfesional\",\"definition\":\"m5_p1gg\"}]}}},{\"code\":\"m_eabug\",\"type\":\"container\",\"label\":\"Formación académica recibida\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"FormacionAcademicaRecibida\",\"definition\":\"m5_p1gg\"}]}}},{\"code\":\"mscsktq\",\"type\":\"container\",\"label\":\"Actividad docente\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"ActividadDocente\",\"definition\":\"m5_p1gg\"}]}}},{\"code\":\"mdopcxq\",\"type\":\"container\",\"label\":\"Experiencia científica y tecnológica\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"ExperienciaCientificaTecnologica\",\"definition\":\"m5_p1gg\"}]}}},{\"code\":\"mwp7mag\",\"type\":\"container\",\"label\":\"Actividad científica y tecnología\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"ActividadCientificaTecnologica\",\"definition\":\"m5_p1gg\"}]}}},{\"code\":\"miomlna\",\"type\":\"container\",\"label\":\"Actividad en el campo de la sanidad\",\"default\":false,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"ActividadCampoSanidad\",\"definition\":\"m5_p1gg\"}]}}}]},\"environment\":true}"));
		client.core.model.Space.Tab tab = space.getTabs().get(0);

		rootNode.setDefinition(rootNodeDefinition);

		assertNotNull(rootNode.getViews().get(tab.getView().getKey()));
	}

	private Operation.Context createContext() {
		return new Operation.Context() {
			@Override
			public <T extends Presenter> T getCanvas() {
				return null;
			}

			@Override
			public Operation getReferral() {
				return null;
			}
		};
	}

	private Services createServices() {
		return new Services() {
			@Override
			public SpaceService getSpaceService() {
				return null;
			}

			@Override
			public NodeService getNodeService() {
				return new NodeService() {
					@Override
					public void open(String id, NodeCallback callback) {
						Node node = null;

						if (id.equals("1")) {
							ContainerBuilder builder = new ContainerBuilder();
							node = builder.build((HttpInstance) JsonUtils.safeEval("{\"id\":\"1\",\"label\":\"ContainerSample\",\"type\":\"container\",\"definition\":{\"code\":\"miyqwmg\",\"name\":\"org.monet.explorerintegration.ContainerSample\",\"type\":\"container\"},\"children\":{\"totalCount\":1,\"items\":[{\"id\":\"2\",\"type\":\"form\",\"label\":\"Record\",\"definition\":{\"code\":\"mj7c5ig\",\"name\":\"org.monet.explorerintegration.ContainerSample.Record\",\"type\":\"form\"}}]}}"));

							ContainerDefinitionBuilder definitionBuilder = new ContainerDefinitionBuilder();
							ContainerDefinition definition = definitionBuilder.build((HttpInstance) JsonUtils.safeEval("{\"code\":\"miyqwmg\",\"name\":\"org.monet.explorerintegration.ContainerSample\",\"type\":\"container\",\"label\":\"ContainerSample\",\"operations\":[{\"code\":\"mv6_slw\",\"label\":\"Crear tarea\",\"description\":\"\",\"enabled\":true,\"visible\":true}],\"views\":{\"totalCount\":1,\"items\":[{\"code\":\"mokspya\",\"type\":\"container\",\"label\":\"Record\",\"default\":true,\"show\":{\"components\":{\"totalCount\":1,\"items\":[{\"value\":\"Normal\",\"definition\":\"mj7c5ig\"}]}}}]},\"environment\":false}"));

							node.setDefinition(definition);
						}
						else if (id.equals("2")) {
							FormBuilder builder = new FormBuilder();
							node = builder.build((HttpInstance) JsonUtils.safeEval("{\"id\":\"2\",\"label\":\"Record\",\"type\":\"form\",\"definition\":{\"code\":\"mj7c5ig\",\"name\":\"org.monet.explorerintegration.ContainerSample.Record\",\"type\":\"form\"},\"isComponent\":true,\"owner\":{\"id\":\"1\",\"label\":\"ContainerSample\",\"type\":\"container\",\"definition\":{\"code\":\"miyqwmg\",\"name\":\"org.monet.explorerintegration.ContainerSample\",\"type\":\"container\"},\"children\":{\"totalCount\":1,\"items\":[{\"id\":\"2\",\"type\":\"form\",\"label\":\"Record\",\"definition\":{\"code\":\"mj7c5ig\",\"name\":\"org.monet.explorerintegration.ContainerSample.Record\",\"type\":\"form\"}}]}},\"ancestors\":{\"totalCount\":1,\"items\":[{\"id\":\"1\",\"type\":\"container\",\"label\":\"ContainerSample\",\"definition\":{\"code\":\"miyqwmg\",\"name\":\"org.monet.explorerintegration.ContainerSample\",\"type\":\"container\"}}]},\"fields\":{\"totalCount\":1,\"items\":[{\"code\":\"mnvhjfw\",\"type\":\"text\",\"label\":\"Name\",\"value\":\"\"}]}}"));

							FormDefinitionBuilder definitionBuilder = new FormDefinitionBuilder();
							FormDefinition definition = definitionBuilder.build((HttpInstance) JsonUtils.safeEval("{\"code\":\"mj7c5ig\",\"name\":\"org.monet.explorerintegration.ContainerSample.Record\",\"type\":\"form\",\"label\":\"Record\",\"views\":{\"totalCount\":1,\"items\":[{\"code\":\"mntkjgw\",\"name\":\"Normal\",\"type\":\"form\",\"default\":false,\"show\":{\"fields\":{\"totalCount\":1,\"items\":[{\"value\":\"Name\",\"definition\":\"mj7c5ig\"}]}}}]},\"fields\":{\"totalCount\":1,\"items\":[{\"code\":\"mnvhjfw\",\"name\":\"Name\",\"label\":\"Name\",\"type\":\"text\",\"displays\":{\"totalCount\":0,\"items\":[]},\"patterns\":[]}]}}"));

							node.setDefinition(definition);
						}

						callback.success(node);
					}

					@Override
					public void getLabel(Node node, Callback<String> callback) {

					}

					@Override
					public void add(Node parent, String code, NodeCallback callback) {

					}

					@Override
					public void delete(Node node, VoidCallback callback) {

					}

					@Override
					public void delete(Node[] nodes, VoidCallback callback) {

					}

					@Override
					public void saveNote(Node node, String name, String value, NoteCallback callback) {

					}

					@Override
					public void saveField(Node node, Field field, VoidCallback callback) {

					}

					@Override
					public void addField(Node node, MultipleField parent, Field field, int pos, VoidCallback callback) {

					}

					@Override
					public void deleteField(Node node, MultipleField parent, int pos, VoidCallback callback) {

					}

					@Override
					public void changeFieldOrder(Node node, MultipleField parent, int previousPos, int newPos, VoidCallback callback) {

					}

					@Override
					public void clearField(Node node, MultipleField parent, VoidCallback callback) {

					}

					@Override
					public void searchFieldHistory(Field field, String dataStore, String filter, int start, int limit, TermListCallback callback) {

					}

					@Override
					public void getFieldHistory(Field field, String dataStore, int start, int limit, TermListCallback callback) {

					}

					@Override
					public void saveFile(String filename, String data, Node node, Callback<String> callback) {

					}

					@Override
					public void savePicture(String filename, String data, Node node, Callback<String> callback) {

					}

					@Override
					public void loadHelpPage(Node node, HtmlPageCallback callback) {

					}

					@Override
					public void executeCommand(Node node, Command command, NodeCommandCallback callback) {

					}

                    @Override
                    public void focusNodeField(Node node, Field field) {
                    }

                    @Override
                    public void focusNodeView(Node node) {
                    }

                    @Override
                    public void blurNodeField(Node node, Field field) {
                    }

                    @Override
                    public void blurNodeView(Node node) {
                    }

                    @Override
					public String getNodePreviewBaseUrl(String documentId) {
						return null;
					}

					@Override
					public String getDownloadNodeUrl(Node node) {
						return null;
					}

					@Override
					public String getDownloadNodeImageUrl(Node node, String imageId) {
						return null;
					}

					@Override
					public String getDownloadNodeThumbnailUrl(Node node, String imageId) {
						return null;
					}

					@Override
					public String getDownloadNodeFileUrl(Node node, String fileId) {
						return null;
					}
				};
			}

			@Override
			public TaskService getTaskService() {
				return null;
			}

			@Override
			public AccountService getAccountService() {
				return null;
			}

			@Override
			public SourceService getSourceService() {
				return null;
			}

			@Override
			public IndexService getIndexService() {
				return null;
			}

			@Override
			public NewsService getNewsService() {
				return null;
			}

			@Override
			public TranslatorService getTranslatorService() {
				return new TranslatorService() {
					@Override
					public String getLoadingLabel() {
						return "";
					}

					@Override
					public String getNoPhoto() {
						return "";
					}

					@Override
					public String getAddPhoto() {
						return "";
					}

					@Override
					public String translateSavedPeriodAgo(String message, Date date) {
						return "Guardado automáticamente a las " + translateFullDate(date);
					}

					@Override
					public String translateSearchForCondition(String condition) {
						return null;
					}

					@Override
					public String translateCountDate(Date date) {
						return null;
					}

					@Override
					public int dayOfWeekToNumber(DayOfWeek dayOfWeek) {
						return 0;
					}

					@Override
					public int monthToNumber(String month) {
						return 0;
					}

					@Override
					public String monthNumberToString(Integer month) {
						return null;
					}

					@Override
					public String[] getDateSeparators() {
						return new String[0];
					}

					@Override
					public String translateDateWithPrecision(Date date, DatePrecision precision) {
						return null;
					}

					@Override
					public String translateFullDate(Date date) {
						return null;
					}

					@Override
					public String translateFullDateByUser(Date date, String user) {
						return null;
					}

					@Override
					public String getCountLabel(int count) {
						return null;
					}

					@Override
					public String getSelectionCountLabel(int selectionCount) {
						return null;
					}

					@Override
					public String getTaskAssignedToMeMessage() {
						return null;
					}

					@Override
					public String getTaskAssignedToMeBySenderMessage(User sender) {
						return null;
					}

					@Override
					public String getTaskAssignedToUserMessage(User Owner) {
						return null;
					}

					@Override
					public String getTaskAssignedToUserBySenderMessage(User owner, User sender) {
						return null;
					}

					@Override
					public String getTaskDateMessage(Date createDate, Date updateDate) {
						return null;
					}

					@Override
					public String getTaskNotificationsMessage(int count) {
						return null;
					}

					@Override
					public String getTaskDelegationMessage(DelegationAction.Message message, Date failureDate, String roleTypeLabel) {
						return null;
					}

					@Override
					public String getTaskLineTimeoutMessage(Date timeout, LineAction.Stop timeoutStop) {
						return null;
					}

					@Override
					public String getTaskWaitMessage(Date dueDate) {
						return null;
					}

					@Override
					public String getTaskStateLabel(Task.State state) {
						return null;
					}

					@Override
					public String getTaskTypeIcon(Task.Type type) {
						return null;
					}

					@Override
					public String getCurrentLanguage() {
						return null;
					}

					@Override
					public String translate(Object name) {
						return null;
					}

					@Override
					public String translateHTML(String html) {
						return null;
					}
				};
			}

            @Override
            public NotificationService getNotificationService() {
                return null;
            }
		};
	}

}