package org.monet.services;

import org.monet.v3.model.AgentFilesystem;

import java.io.File;
import java.io.FileNotFoundException;

public abstract class SpaceService {

	public static Space openSpace(String version, String url, String filename, String certificatePassword) {

		try {
			if (version.equals(org.monet.v2.Space.VERSION))
				return openV2Space(url, filename, certificatePassword);
			else if (version.equals(org.monet.v3.Space.VERSION))
				return openV3Space(url, filename, certificatePassword);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		return null;
	}

	private static org.monet.v2.Space openV2Space(String url, String certificateFilename, String certificatePassword) {
		org.monet.v2.Space space = new org.monet.v2.Space();

		org.monet.api.backservice.BackserviceApi api = new org.monet.api.backservice.impl.BackserviceApiImpl(url, certificateFilename, certificatePassword);
		space.injectApi(api);

		File distributionDirectory = api.downloadDistribution();

		if (distributionDirectory == null) {
			System.out.println("Could not download distribution installed in space");
			return null;
		}

		space.injectBusinessModelDirectory(distributionDirectory.getAbsolutePath());
		space.init();

		return space;
	}

	private static org.monet.v3.Space openV3Space(String url, String certificateFilename, String certificatePassword) throws FileNotFoundException {
		org.monet.v3.Space space = new org.monet.v3.Space();
		File distributionDirectory = null;

		org.monet.api.space.backservice.BackserviceApi api = new org.monet.api.space.backservice.impl.BackserviceApiImpl(url, certificateFilename, certificatePassword);
		space.injectApi(api);

		try {
			distributionDirectory = api.downloadDistribution();

			if (distributionDirectory == null) {
				System.out.println("Could not download distribution installed in space");
				return null;
			}

			space.injectBusinessModelDirectory(distributionDirectory.getAbsolutePath());
			space.init();
		}
		finally {
			if (distributionDirectory != null)
				AgentFilesystem.removeDir(distributionDirectory);
		}

		return space;
	}

}
