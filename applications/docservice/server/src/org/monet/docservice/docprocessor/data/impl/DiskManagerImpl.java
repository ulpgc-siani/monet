package org.monet.docservice.docprocessor.data.impl;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import org.monet.docservice.core.Key;
import org.monet.docservice.core.agent.AgentFilesystem;
import org.monet.docservice.core.log.Logger;
import org.monet.docservice.core.util.StreamHelper;
import org.monet.docservice.docprocessor.configuration.Configuration;
import org.monet.docservice.docprocessor.data.DiskManager;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Singleton
public class DiskManagerImpl implements DiskManager {
	private Configuration configuration;
	private AgentFilesystem agentFilesystem;
	private Logger logger;

	@Inject
	public void DiskManager(Configuration configuration, AgentFilesystem agentFilesystem, Logger logger) {
		this.configuration = configuration;
		this.agentFilesystem = agentFilesystem;
		this.logger = logger;
	}

	public InputStream readDocument(Key documentKey, String location) {
		File file = new File(location);
		InputStream result;

		try {
//			if (!file.exists())	return null;

			result = new FileInputStream(file);
		} catch (IOException exception) {
			logger.error(String.format("Could not read document %s from file located at %s", documentKey, location), exception);
			return null;
		}

		return result;
	}

	@Override
	public String addDocument(Key documentKey, InputStream data) {
		return saveDocumentToFile(documentKey, this.createTimeDirectory() + "/" + prepareId(documentKey), data);
	}

	@Override
	public void saveDocument(Key documentKey, String location, InputStream data) {
		saveDocumentToFile(documentKey, location, data);
	}

	@Override
	public void overrideDocument(Key fromDocumentId, String fromLocation, Key toDocumentId, String toLocation) {
		InputStream fromData = this.readDocument(fromDocumentId, fromLocation);
		this.saveDocumentToFile(toDocumentId, toLocation, fromData);
	}

	@Override
	public void deleteDocument(Key documentKey, String location) {

		if (location == null)
			return;

		File file = new File(location);
		if (file.exists())
			file.delete();
	}

	private String saveDocumentToFile(Key documentKey, String location, InputStream data) {
		File file = new File(location);
		OutputStream fileStream = null;

		try {
			if (!file.exists())
				file.createNewFile();

			fileStream = new FileOutputStream(file);
			StreamHelper.copy(data, fileStream);
		} catch (IOException exception) {
			logger.error(String.format("Could not save document %s to file in location %s", documentKey, location));
		} finally {
			StreamHelper.close(fileStream);
		}

		return location;
	}

	private String prepareId(Key documentKey) {
		return documentKey.getId().replaceAll("/", "");
	}

	private String createTimeDirectory() {
		SimpleDateFormat format = new SimpleDateFormat("/yyyy/MM/dd");
		Date date = new Date();
		String directory = this.selectDisk() + format.format(date);

		agentFilesystem.forceDir(directory);
		directory += "/" + this.getNumberedDirectory(directory);

		return directory;
	}

	private String getNumberedDirectory(String directory) {
		String[] numberedDirectories = agentFilesystem.listDir(directory);
		int lastDirectory = numberedDirectories.length>0?numberedDirectories.length-1:0;
		String selectedDirectory = String.format("%03d", lastDirectory);
		File selectedDirectoryFile = new File(directory + "/" + selectedDirectory);

		if (!selectedDirectoryFile.exists()) {
			agentFilesystem.forceDir(directory + "/" + selectedDirectory);
			return selectedDirectory;
		}

		if (agentFilesystem.listDir(directory + "/" + selectedDirectory).length < 65534)
			return selectedDirectory;

		lastDirectory++;
		selectedDirectory = String.format("%03d", lastDirectory);
		agentFilesystem.forceDir(directory + "/" + selectedDirectory);

		return selectedDirectory;
	}

	private String selectDisk() {
		String[] disks = configuration.getDocumentDisks();
		String selectedDisk = null;

		for (int i = 0; i < disks.length; i++) {
			String disk = disks[i];

			File diskFile = new File(disk);

			if (!diskFile.exists())
				continue;

			long totalSpace = diskFile.getTotalSpace();
			long freeSpace = diskFile.getFreeSpace();
			long spaceRatio = freeSpace / totalSpace;

			if (spaceRatio >= 0.8)
				continue;

			selectedDisk = disk;
			break;
		}

		if (selectedDisk == null) {
			logger.error("No available disk found to save documents!");
			throw new RuntimeException("No available disk found to save documents!");
		}

		logger.info("selectDisk: " + selectedDisk);
		return selectedDisk;
	}

}
