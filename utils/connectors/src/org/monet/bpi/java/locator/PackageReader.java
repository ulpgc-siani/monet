package org.monet.bpi.java.locator;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PackageReader {

	private String packagePath;
	private List<String> classnames;

	public PackageReader(String classpathRoot) {
		this.packagePath = classpathRoot;
	}
	
	public List<String> read() throws IOException {		
		this.loadClassNames(packagePath);
		return classnames;
	}
	
	private void loadClassNames(String packagePath) throws IOException {
		this.classnames = Collections.synchronizedList(new ArrayList<String>());
		
		File directory = new File(packagePath);
		readDirectory(directory);
	}
		
	private String toPackageName(String absolutePath) {
		String fileSeparator = System.getProperty("file.separator");
		String fileSeparatorEscaped = (fileSeparator.contains("\\"))? "\\" + fileSeparator : fileSeparator;
		
		String relativePath = absolutePath.replace(new File(this.packagePath).getAbsolutePath(), "");
		relativePath = relativePath.startsWith(fileSeparator)? relativePath.substring(1, relativePath.length()) : relativePath;
				
		String packageName = relativePath.replaceAll(fileSeparatorEscaped, ".");
		return packageName.replace(".class", "");		
	}

	
	private void readDirectory(File file) {		
		if (file.isDirectory() && (!file.getName().startsWith("."))) {
			String[] filenames = file.list(new FilenameFilter() {				
				@Override
				public boolean accept(File dir, String name) {
					return (!name.startsWith("."));					
				}
			});
			
			for (String filename : filenames) {
				String fileAbsolutePath = file.getAbsolutePath() + System.getProperty("file.separator") + filename;
				File childFile = new File(fileAbsolutePath);
				if (childFile.isFile()) this.classnames.add(toPackageName(fileAbsolutePath));
				else {
					readDirectory(childFile);					
				}
			}
		}
	}
}
