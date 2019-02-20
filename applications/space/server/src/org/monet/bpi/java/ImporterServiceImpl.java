package org.monet.bpi.java;

import org.monet.bpi.Importer;
import org.monet.bpi.ImporterService;
import org.monet.metamodel.ImporterDefinition;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.model.Dictionary;

public class ImporterServiceImpl extends ImporterService {

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	@Override
	public Importer getImpl(String name) {
		ImporterDefinition definition = Dictionary.getInstance().getImporterDefinition(name);
		Importer bpiImporter = (Importer) this.bpiClassLocator.instantiateBehaviour(definition);
		return bpiImporter;
	}

	public static void init() {
		instance = new ImporterServiceImpl();
	}

}
