package org.monet.docservice.core.agent;

public interface AgentFilesystem {

	Boolean forceDir(String sDirname);
	Boolean removeDir(String sDirname);
	void copyFile(String sSource, String sDestination);
	Boolean removeFile(String sFilename);
	public String[] listDir(String sDirname);

}