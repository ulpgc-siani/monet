package org.monet.docservice.core.agent.impl;

import com.google.inject.Inject;
import org.monet.docservice.core.agent.AgentFilesystem;
import org.monet.docservice.core.exceptions.FilesystemException;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;

import java.io.*;

public class AgentFilesystemImpl implements AgentFilesystem {

	private Logger logger;

	@Inject
	public void injectLogger(Logger logger) {
		this.logger = logger;
	}
	
	public Boolean forceDir(String dirName) {
		return new File(dirName).mkdirs();
	}
	
	public Boolean removeDir(String dirName) {
		logger.debug("removeDir(%s)", dirName);

		File oFile = new File(dirName);

		if (oFile.exists()) {
			File[] aFiles = oFile.listFiles();
			for (int iPos = 0; iPos < aFiles.length; iPos++) {
				if (aFiles[iPos].isDirectory()) {
					this.removeDir(aFiles[iPos].getAbsolutePath());
				} else {
					aFiles[iPos].delete();
				}
			}
		}

		return (oFile.delete());
	}

	public void copyFile(String sSource, String sDestination) {
		logger.debug("copyFile(%s, %s)", sSource, sDestination);

		OutputStream out = null;
		InputStream in = null;

		try {
			File oSource = new File(sSource);
			File oDestination = new File(sDestination);

			File parentDir = oDestination.getParentFile();
			if (parentDir != null && !parentDir.exists())
				parentDir.mkdirs();

			in = new FileInputStream(oSource);
			out = new FileOutputStream(oDestination);

			// Copy the bits from instream to outstream
			byte[] buf = new byte[16384];
			int len;
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
		} catch (IOException oException) {
			logger.error(oException.getMessage(), oException);
			throw new FilesystemException(oException.getMessage(), oException);
		} finally {
			StreamHelper.close(in);
			StreamHelper.close(out);
		}
	}

	public Boolean removeFile(String sFilename) {
		logger.debug("removeFile(%s)", sFilename);

		return new File(sFilename).delete();
	}

	public String[] listDir(String dirName) {
		FilenameFilter oFilter = new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return !name.startsWith(".");
			}
		};

		return new File(dirName).list(oFilter);
	}

}
