package org.monet.bpi.java;

import org.monet.bpi.Exporter;
import org.monet.bpi.ExporterService;
import org.monet.metamodel.ExporterDefinition;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.model.Dictionary;

public class ExporterServiceImpl extends ExporterService {

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	@Override
	public Exporter getImpl(String name) {
		ExporterDefinition definition = Dictionary.getInstance().getExporterDefinition(name);
		ExporterImpl bpiExporter = (ExporterImpl) this.bpiClassLocator.instantiateBehaviour(definition);
		return bpiExporter;
	}

	public static void init() {
		instance = new ExporterServiceImpl();
	}

}
