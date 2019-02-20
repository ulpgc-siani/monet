package org.monet.v2_v3;

import com.externa.collections.ArchivoExpedientes;
import com.externa.collections.ArchivoIncidencias;
import com.externa.collections.EncargosCorrectivos;
import com.externa.containers.Componente;
import com.externa.containers.EncargoCo;
import com.externa.containers.ExpedienteCorrectivo;
import com.externa.containers.Instalacion;
import com.externa.containers.Inventario;
import com.externa.documents.Otac;
import com.externa.parquesinfantiles.forms.FichaInstalacionParques;
import com.externa.proxies.*;
import coordinacion.analytics.dashboardincidencias.DataStoreIncidencia;
import coordinacion.analytics.dashboardincidencias.datastoreincidencia.CuboEvolucionTrabajosPendienteCube;
import coordinacion.analytics.dashboardincidencias.datastoreincidencia.TiposIncidenciaDimensionComponent;
import coordinacion.incidencias.Coleccion;
import coordinacion.incidencias.Incidencia;
import coordinacion.objetosactuacion.ObjetoActuacion;
import coordinacion.procesos.cori.encargos.encargoca.Ficha;
import org.junit.Before;
import org.junit.Test;
import org.monet.api.space.backservice.impl.helpers.StreamHelper;
import org.monet.bpi.ConsoleService;
import org.monet.bpi.DatastoreService;
import org.monet.bpi.TaskService;
import org.monet.bpi.types.Date;
import org.monet.bpi.types.Link;
import org.monet.services.SpaceService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import coordinacion.objetosactuacion.ObjetoActuacion;

public class SpaceTest {
	private org.monet.v2.Space v2Space;
	private org.monet.v3.Space v3Space;

	@Before
	public void loadSpace() {
  	    this.v2Space = (org.monet.v2.Space) SpaceService.openSpace(org.monet.v2.Space.VERSION, "http://externa.dev.gisc.siani.es/parques.coordinacion/servlet/backservice", "/monet.p12", "1234");
		//this.v3Space = (org.monet.v3.Space) SpaceService.openSpace(org.monet.v3.Space.VERSION, "http://monet32.dev.gisc.siani.es/coordinacion/servlet/backservice", "/businessunit-coordinacion32.p12", "1234");
		//this.v3Space = (org.monet.v3.Space) SpaceService.openSpace(org.monet.v3.Space.VERSION, "http://adeje.externa.monentia.es/ejecucion.ci/servlet/backservice", "/businessunit-adeje-ejecucion.p12", "1234");
		//this.v3Space = (org.monet.v3.Space) SpaceService.openSpace(org.monet.v3.Space.VERSION, "http://externa.laspalmasgc.es/viasyobras/servlet/backservice", "/businessunit-viasyobras.p12", "1234");
		this.v3Space = (org.monet.v3.Space) SpaceService.openSpace(org.monet.v3.Space.VERSION, "http://viasyobras.pre.gisc.siani.es/viasyobras/servlet/backservice", "/businessunit-viasyobras_pre.p12", "1234");
	}

	@Test
	public void copyNode() {
		ArchivoIncidencias archivoIncidenciasV2 = v2Space.getSingleton("archivo-incidencias");
		Coleccion archivoIncidenciasV3 = v3Space.getSingleton("coordinacion.incidencias.Coleccion");

		for (ReferenciaIncidencia incidenciaV2 : archivoIncidenciasV2.getAll()) {
			Incidencia incidenciaV3 = Incidencia.createNew(archivoIncidenciasV3);
			incidenciaV3.setDescripcion(incidenciaV2.getDescripcion());
			incidenciaV3.save();
		}
	}

	@Test
	public void copyNodeWithFieldFile() {
		ArchivoIncidencias archivoIncidenciasV2 = v2Space.getSingleton("archivo-incidencias");
		coordinacion.incidencias.Coleccion archivoIncidenciasV3 = v3Space.getSingleton("coordinacion.incidencias.Coleccion");

		for (ReferenciaIncidencia incidenciaV2 : archivoIncidenciasV2.getAll()) {
			com.externa.forms.Incidencia nodoIncidenciaV2 = incidenciaV2.getReferencedNode();
			Incidencia incidenciaV3 = Incidencia.createNew(archivoIncidenciasV3);
			incidenciaV3.getAdjuntosIncidenciaField().addNewAll(nodoIncidenciaV2.getAdjuntosIncidenciaField().getAll());
			incidenciaV3.save();
		}
	}

	@Test
	public void copyNodeWithFieldPicture() {
		Inventario inventario = v2Space.getSingleton("inventario");
		Instalacion instalacion = ((ReferenciaInstalacion)inventario.getInstalaciones().getAll().iterator().next()).getReferencedNode();
		coordinacion.objetosactuacion.Coleccion objetosActuacion = v3Space.getSingleton("coordinacion.objetosactuacion.Coleccion");

		for (Object referenciaComponente : instalacion.getComponentes().getAll()) {
			Componente componente = ((ReferenciaComponente)referenciaComponente).getReferencedNode();

			ObjetoActuacion objetoActuacion = ObjetoActuacion.createNew(objetosActuacion);
			coordinacion.modulos.componentes.coleccion.Componente componentev3 = coordinacion.modulos.componentes.coleccion.Componente.createNew(objetoActuacion.getColeccionComponentes());

			coordinacion.modulos.componentes.Ficha fichav3 = componentev3.getFicha();
			fichav3.setImagen(componente.getGenericoFichaComponente().getImagen());
			fichav3.save();
		}
	}

	@Test
	public void copyNodeWithFieldNode() {
		ArchivoExpedientes archivoExpedientesV2 = v2Space.getSingleton("archivo-expedientes");
		coordinacion.Expedientes archivoExpedientesV3 = v3Space.getSingleton("coordinacion.Expedientes");

		for (ReferenciaExpediente expedienteV2 : archivoExpedientesV2.getAll()) {
			Object nodoExpedienteV2 = expedienteV2.getReferencedNode();
			if (nodoExpedienteV2 instanceof ExpedienteCorrectivo) {
				coordinacion.procesos.cori.Expediente expedienteV3 = coordinacion.procesos.cori.Expediente.createNew(archivoExpedientesV3);

				EncargosCorrectivos encargosCorrectivosV2 = ((ExpedienteCorrectivo) nodoExpedienteV2).getEncargosCorrectivos();
				for (ReferenciaEncargo encargoV2 : encargosCorrectivosV2.getAll()) {
					Object nodoEncargoV2 = encargoV2.getReferencedNode();
					if (nodoEncargoV2 instanceof EncargoCo) {
						EncargoCo encargoCo = ((EncargoCo) nodoEncargoV2);
						coordinacion.procesos.cori.encargos.EncargoCA encargoCAV3 = coordinacion.procesos.cori.encargos.EncargoCA.createNew(expedienteV3.getColeccion());
						Link otLink = encargoCo.getFichaEncargoCo().getOt();
					    Otac otac = v2Space.get(otLink.getId());
						Ficha ficha = encargoCAV3.getFicha();
						coordinacion.procesos.cori.documentos.Otac otacV3 = coordinacion.procesos.cori.documentos.Otac.createNew(ficha);
						otacV3.overwriteContent(otac.getContent());
						ficha.setOt(otacV3.toLink());
						ficha.save();
					}
				}

				expedienteV3.save();
			}
		}
	}

	@Test
	public void insertarHecho() {
		try{
			coordinacion.Preferencias preferencias = (coordinacion.Preferencias) v3Space.getSingleton("coordinacion.Preferencias");
			Date fechaAnterior = preferencias.getFechaCuboIncidenciasAbiertas();
			if (fechaAnterior == null) {
				preferencias.setFechaCuboIncidenciasAbiertas(new Date());
				preferencias.save();
				return;
			}
			//Date hoy = new Date();
			preferencias.setFechaFinalCuboAbiertas(new Date());
			Date hoy = preferencias.getFechaFinalCuboAbiertas();
			if (hoy == null) hoy = new Date();
			if (fechaAnterior.before(hoy)){
				Date fechaNueva = new Date(fechaAnterior.getValue());
				preferencias.setFechaCuboIncidenciasAbiertas(fechaNueva.plusDays(1));
				preferencias.save();
				return;
			}
			if (fechaAnterior.equals(hoy)){
				return;
			}

		}catch(Exception e){
			ConsoleService.println("ERROR: La actualización del cubo de incidencias abiertas de ejecución ha fallado");
			e.printStackTrace();
		}
	}

	@Test
	public void hechoActualizarAbiertas() {
		Date fechaHecho = new Date();
		ArrayList<Incidencia> listaIncidencias = null;
		DataStoreIncidencia dataStoreInstance = (DataStoreIncidencia) DatastoreService.get("coordinacion.analytics.dashboardincidencias.DataStoreIncidencia");
		CuboEvolucionTrabajosPendienteCube cuboIncidencia = dataStoreInstance.getCuboEvolucionTrabajosPendiente();
		TiposIncidenciaDimensionComponent tipoIncidencia = dataStoreInstance.getTiposIncidencia().insertComponent("001");
		tipoIncidencia.addTipoIncidencia("Ejemplo");

		while (fechaHecho != null){
			coordinacion.incidencias.Coleccion incidencias = (coordinacion.incidencias.Coleccion ) v3Space.getSingleton("coordinacion.incidencias.Coleccion");
			if (listaIncidencias == null)
				listaIncidencias = new ArrayList();
			fechaHecho = null;
		}

		cuboIncidencia.save();
		tipoIncidencia.save();
	}

	@Test
	public void testDownloadImage() {
		Inventario inventario = (Inventario) v2Space.getSingleton("inventario");
		com.externa.collections.Instalaciones instalaciones = inventario.getInstalaciones();
		coordinacion.objetosactuacion.Coleccion objetosActuacion = (coordinacion.objetosactuacion.Coleccion) v3Space.getSingleton("coordinacion.objetosactuacion.Coleccion");

		for (ReferenciaInstalacion ref : instalaciones.getAll()) {
			Instalacion<?> instalacion = ref.getReferencedNode();
			ObjetoActuacion objetov3 = coordinacion.objetosactuacion.ObjetoActuacion.createNew(objetosActuacion);
			FichaInstalacionParques ficha = (FichaInstalacionParques) instalacion.getGenericoFichaInstalacion();
			coordinacion.objetosactuacion.objetoactuacion.Ficha fichav3 = objetov3.getFicha();

			try {
				File file = new File("/tmp/" + ficha.toLink().getId());
				file.createNewFile();
				FileOutputStream stream = new FileOutputStream(file);
				StreamHelper.copyData(ficha.getImagen().getContent(), stream);

			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testTransferImage() {
		Inventario inventario = (Inventario) v2Space.getSingleton("inventario");
		com.externa.collections.Instalaciones instalaciones = inventario.getInstalaciones();
		coordinacion.objetosactuacion.Coleccion objetosActuacion = (coordinacion.objetosactuacion.Coleccion) v3Space.getSingleton("coordinacion.objetosactuacion.Coleccion");

		for (ReferenciaInstalacion ref : instalaciones.getAll()) {
			Instalacion<?> instalacion = ref.getReferencedNode();
			ObjetoActuacion objetov3 = coordinacion.objetosactuacion.ObjetoActuacion.createNew(objetosActuacion);
			FichaInstalacionParques ficha = (FichaInstalacionParques) instalacion.getGenericoFichaInstalacion();
			coordinacion.objetosactuacion.objetoactuacion.Ficha fichav3 = objetov3.getFicha();

			fichav3.setImagen(ficha.getImagen());
			fichav3.save();
		}
	}

	@Test
	public void testLoadTaskIN_PRODUCTION_SPACE() {
		coordinacion.procesos.cori.Workmap tareas= (coordinacion.procesos.cori.Workmap) TaskService.get("94");
		String label = tareas.getLabel();
		tareas.resume();
	}

}
