package org.monet.bpi.java;

import org.monet.bpi.Source;
import org.monet.bpi.SourceService;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.components.ComponentPersistence;
import org.monet.space.kernel.components.layers.SourceLayer;
import org.monet.space.kernel.model.Dictionary;
import org.monet.space.kernel.model.FeederUri;

public class SourceServiceImpl extends SourceService {

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	@Override
	protected Source getImpl(String name, String url) {
		SourceLayer sourceLayer = ComponentPersistence.getInstance().getSourceLayer();
		String code = Dictionary.getInstance().getDefinitionCode(name);
		SourceImpl bpiSource;

		bpiSource = new SourceImpl();
		bpiSource.injectSource(sourceLayer.locateSource(code, FeederUri.build(url)));

		return bpiSource;
	}

	public static void init() {
		instance = new SourceServiceImpl();
	}

}
