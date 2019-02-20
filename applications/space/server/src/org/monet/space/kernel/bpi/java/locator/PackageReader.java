package org.monet.space.kernel.bpi.java.locator;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class PackageReader {

	private String packagePath;
	private Set<String> classnames;

	public PackageReader(String classpathRoot) {
		this.packagePath = classpathRoot;
	}

	public Set<String> read() throws IOException {
		this.loadClassNames(packagePath);
		return classnames;
	}

	private void loadClassNames(String packagePath) throws IOException {
		this.classnames = new HashSet<String>();

		File directory = new File(packagePath);
		readDirectory(directory);
	}

	private String toPackageName(String absolutePath) {
		String fileSeparator = System.getProperty("file.separator");
		String fileSeparatorEscaped = (fileSeparator.contains("\\")) ? "\\" + fileSeparator : fileSeparator;

		String relativePath = absolutePath.replace(new File(this.packagePath).getAbsolutePath(), "");
		relativePath = relativePath.startsWith(fileSeparator) ? relativePath.substring(1, relativePath.length()) : relativePath;

		String packageName = relativePath.replaceAll(fileSeparatorEscaped, ".");
		return packageName.replace(".class", "");
	}

	private void readDirectory(File file) {
		File[] filenames = file.listFiles(new FileFilter() {
			@Override
			public boolean accept(File file) {
				return (file.isDirectory() && !file.getName().startsWith(".")) || file.getName().endsWith(".class");
			}
		});

		if (filenames != null) {
			for (File filename : filenames) {
				if (filename.isFile())
					this.classnames.add(toPackageName(filename.getAbsolutePath()));
				else
					readDirectory(filename);
			}
		}
	}
}
