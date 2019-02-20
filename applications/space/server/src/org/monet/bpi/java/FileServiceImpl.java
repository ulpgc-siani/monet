package org.monet.bpi.java;

import org.monet.bpi.FileService;
import org.monet.bpi.types.File;
import org.monet.space.kernel.bpi.java.locator.BPIClassLocator;
import org.monet.space.kernel.configuration.Configuration;

public class FileServiceImpl extends FileService {

	BPIClassLocator bpiClassLocator = BPIClassLocator.getInstance();

	@Override
	public File getResourcesDirImpl() {
		Configuration configuration = Configuration.getInstance();
		return new File(configuration.getBusinessModelResourcesDir());
	}

	@Override
	public File getResourcesDataDirImpl() {
		Configuration configuration = Configuration.getInstance();
		return new File(configuration.getBusinessModelResourcesDataDir());
	}

	@Override
	public File getResourcesImagesDirImpl() {
		Configuration configuration = Configuration.getInstance();
		return new File(configuration.getBusinessModelResourcesImagesDir());
	}

	public static void init() {
		instance = new FileServiceImpl();
	}

}
