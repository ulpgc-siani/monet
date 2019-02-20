package org.monet.space.kernel.utils;

import org.monet.space.kernel.agents.AgentFilesystem;

import java.io.File;
import java.util.HashMap;

public class PackageProjectHelper {

	public static HashMap<String, String> getPackageManifest(File modelDirectory) {
		HashMap<String, String> map = new HashMap<String, String>();

		File manifestFile = new File(modelDirectory, "MANIFEST");
		if (!manifestFile.exists())
			return null;

		String content = AgentFilesystem.readFile(manifestFile.getAbsolutePath());
		for (String line : content.split("\\n")) {
			String[] entry = line.trim().split(":");
			if (entry.length == 2) {
				map.put(entry[0].trim().toLowerCase(), entry[1].trim());
			}
		}

		return map;
	}

}
