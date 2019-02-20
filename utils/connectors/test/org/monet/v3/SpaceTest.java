package org.monet.v3;

import coordinacion.incidencias.Coleccion;
import coordinacion.incidencias.Incidencia;
import coordinacion.incidencias.coleccion.Index;
import org.junit.Before;
import org.junit.Test;
import org.monet.bpi.Source;
import org.monet.bpi.SourceService;
import org.monet.bpi.types.File;
import org.monet.bpi.types.Term;
import org.monet.services.SpaceService;

public class SpaceTest {
	private Space space;


	@Before
	public void loadSpace() {
		//this.space = (Space) SpaceService.openSpace(Space.VERSION, "http://10.13.13.145:8092/tesauros/servlet/backservice", "/businessunit-tesauros.p12", "1234");
		//this.space = (Space) SpaceService.openSpace(Space.VERSION, "http://monet32.dev.gisc.siani.es/externain/servlet/backservice", "/businessunit-externain.p12", "1234");
//		this.space = (Space) SpaceService.openSpace(Space.VERSION, "http://externa.laspalmasgc.es/pinfantiles/servlet/backservice", "/tmp/pinfantiles.p12", "1234");

		this.space = (Space) SpaceService.openSpace(Space.VERSION, "http://10.13.32.113:8090/micv/servlet/backservice", "/businessunit-monet.p12", "1234");
	}

	@Test
	public void loadCollection() throws Exception {
		Coleccion archivoIncidencias = space.getSingleton("coordinacion.incidencias.Coleccion");
		for (Index incidencia : archivoIncidencias.getAll())
			System.out.println(incidencia.getCodigoIncidencia());
	}

	@Test
	public void loadCollectionAndFields() throws Exception {
		Coleccion archivoIncidencias = space.getSingleton("coordinacion.incidencias.Coleccion");
		for (Index incidencia : archivoIncidencias.getAll()) {
			Incidencia nodoIncidencia = (Incidencia)incidencia.getIndexedNode();
			String label = nodoIncidencia.getLabel();
			String estado = nodoIncidencia.getEstado().getLabel();
			System.out.println(label + " - " + estado);
		}
	}

	@Test
	public void addSourceTerm() {
		Source tesauroEjemplo = SourceService.get("org.usersthesaurus.tesauroejemplo", null);
		Term termino = new Term();
		termino.setKey("001");
		termino.setLabel("Término");
		termino.addTag("variable=valor");
		tesauroEjemplo.addTerm(termino);
	}

	@Test
	public void addSourceCategory() {
		Source tesauroEjemplo = SourceService.get("org.usersthesaurus.tesauroejemplo", null);
		Term termino = new Term();
		termino.setKey("002");
		termino.setLabel("Categoría");
		termino.setType(Term.CATEGORY);
		tesauroEjemplo.addTerm(termino);
	}

	@Test
	public void addSourceCategoryAndChild() {
		Source tesauroEjemplo = SourceService.get("org.usersthesaurus.tesauroejemplo", null);
		Term termino = new Term();
		termino.setKey("003");
		termino.setLabel("Categoría");
		termino.setType(Term.CATEGORY);
		tesauroEjemplo.addTerm(termino);

		Term hijo = new Term();
		hijo.setKey("003.001");
		hijo.setLabel("hijo 1");
		hijo.setType(Term.TERM);
		termino.addTerm(hijo);

		Term hijo2 = new Term();
		hijo2.setKey("003.002");
		hijo2.setLabel("hijo 2");
		hijo2.setType(Term.SUPER_TERM);
		termino.addTerm(hijo2);
	}

	@Test
	public void addSourceSuperTerm() {
		Source tesauroEjemplo = SourceService.get("org.usersthesaurus.tesauroejemplo", null);
		Term termino = new Term();
		termino.setKey("004");
		termino.setLabel("Super término");
		termino.setType(Term.SUPER_TERM);
		tesauroEjemplo.addTerm(termino);
	}

	@Test
    public void testImporter() {
	    File fichero = new File(SpaceTest.class.getResource("/calles.xml").getPath());
	    ejecucion.procesos.importador.elementos.ImportadorElementos.doImportOf(fichero).atGlobalScope();
    }

	@Test
	public void testImporterWithScope() {
		File fichero = new File(SpaceTest.class.getResource("/calles.xml").getPath());
		ejecucion.clientes.Coleccion clientes = ejecucion.clientes.Coleccion.getInstance();
		ejecucion.clientes.Cliente cliente = ejecucion.clientes.Cliente.createNew(clientes);
		ejecucion.procesos.importador.elementos.ImportadorElementos.doImportOf(fichero).atScope(cliente);
	}

}
