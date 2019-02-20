package org.monet.bpi;

import org.monet.bpi.types.File;

public abstract class FileService {

	protected static FileService instance;

	public static File getResourcesDir() {
		return instance.getResourcesDirImpl();
	}

	public static File getResourcesDataDir() {
		return instance.getResourcesDataDirImpl();
	}

	public static File getResourcesImagesDir() {
		return instance.getResourcesImagesDirImpl();
	}

	protected abstract File getResourcesDirImpl();

	protected abstract File getResourcesDataDirImpl();

	protected abstract File getResourcesImagesDirImpl();

}