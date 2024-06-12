//package org.monet.v2;
//
//import com.externa.collections.ArchivoIncidencias;
//import com.externa.forms.Incidencia;
//import com.externa.proxies.ReferenciaIncidencia;
//import com.micv.catalogs.Curriculums;
//import com.micv.containers.CvInvestigador;
//import com.micv.proxies.ReferenciaCurriculumVitae;
//import org.junit.Before;
//import org.junit.Test;
//import org.monet.services.SpaceService;
//
//public class SpaceTest {
//	private Space space;
//
//	@Before
//	public void loadSpace() {
//  	    //this.space = (Space) SpaceService.openSpace(Space.VERSION, "http://externa.dev.gisc.siani.es/parques.coordinacion/servlet/backservice", "/monet.p12", "1234");
////		this.space = (Space) SpaceService.openSpace(Space.VERSION, "http://micvpre.ulpgc.es/servlet/backservice", "/tmp/businessunit-micv-pre.p12", "1234");
//		this.space = (Space) SpaceService.openSpace(Space.VERSION, "http://micv2.ulpgc.es/servlet/backservice", "/tmp/monet_exp.p12", "1234");
//	}
//
//	@Test
//	public void loadCollection() throws Exception {
//		ArchivoIncidencias archivoIncidencias = space.getSingleton("archivo-incidencias");
//		for (ReferenciaIncidencia incidencia : archivoIncidencias.getAll()) {
//			System.out.println(incidencia.getIdIncidencia());
//		}
//	}
//
//	@Test
//	public void loadCollectionAndFields() throws Exception {
//		ArchivoIncidencias archivoIncidencias = space.getSingleton("archivo-incidencias");
//		for (ReferenciaIncidencia incidencia : archivoIncidencias.getAll()) {
//			Incidencia nodoIncidencia = incidencia.getReferencedNode();
//			String label = nodoIncidencia.getLabel();
//			String estado = nodoIncidencia.getEstado().getLabel();
//			System.out.println(label + " - " + estado);
//		}
//	}
//
//	@Test
//	public void loadCatalog() {
//		Curriculums cv20 = this.space.getSingleton("curriculums");
//		for (ReferenciaCurriculumVitae ref: cv20.get(ReferenciaCurriculumVitae.Nombre.Ne(""))){
//			CvInvestigador referencedNode = ref.getReferencedNode();
//		}
//	}
//}
