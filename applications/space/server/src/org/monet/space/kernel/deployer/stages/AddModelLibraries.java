package org.monet.space.kernel.deployer.stages;

import org.monet.space.kernel.agents.AgentFilesystem;
import org.monet.space.kernel.configuration.Configuration;
import org.monet.space.kernel.deployer.Stage;
import org.monet.space.kernel.deployer.errors.CantAddModelLibraryError;
import org.monet.space.kernel.library.LibraryZip;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AddModelLibraries extends Stage {
	private static final List<String> loadedLibraries = new ArrayList<>();

	@Override
	public void execute() {
		Configuration configuration = Configuration.getInstance();
		String librariesDir = configuration.getBusinessModelLibrariesDir();
		String[] libraries = AgentFilesystem.listDir(librariesDir);
		String librariesClassesDir = Configuration.getInstance().getBusinessModelLibrariesClassesDir();
		Integer pos;

		if (libraries == null)
			return;

		for (pos = 0; pos < libraries.length; pos++) {
			String libraryPath = librariesDir + "/" + libraries[pos];

			File libraryFile = new File(libraryPath);
			try {
				LibraryZip.decompress(libraryFile, librariesClassesDir);
			} catch (Exception exception) {
				this.problems.add(new CantAddModelLibraryError());
				throw new RuntimeException("Can't add model library error.");
			}

			loadedLibraries.add(libraryPath);
		}
	}

	@Override
	public String getStepInfo() {
		return "Adding model libraries to classpath";
	}
}