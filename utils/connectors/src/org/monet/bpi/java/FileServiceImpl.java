package org.monet.bpi.java;


import org.monet.api.space.backservice.BackserviceApi;
import org.monet.bpi.FileService;
import org.monet.bpi.types.File;
import org.monet.v3.BPIClassLocator;

public class FileServiceImpl extends FileService {
	private BackserviceApi api;
	private org.monet.v3.model.Dictionary dictionary;
	private BPIClassLocator bpiClassLocator;
	private String businessModelResourcesDir;

	public static void injectInstance(FileServiceImpl service) {
		instance = service;
	}

	public void injectApi(BackserviceApi api) {
		this.api = api;
	}

	public void injectDictionary(org.monet.v3.model.Dictionary dictionary) {
		this.dictionary = dictionary;
	}

	public void injectBPIClassLocator(BPIClassLocator bpiClassLocator) {
		this.bpiClassLocator = bpiClassLocator;
	}

	public void injectBusinessModelResourcesDir(String businessModelResourcesDir) {
		this.businessModelResourcesDir = businessModelResourcesDir;
	}

	@Override
	public File getResourcesDirImpl() {
		return new File(this.businessModelResourcesDir);
	}

	@Override
	public File getResourcesDataDirImpl() {
		return new File(this.businessModelResourcesDir + "/data");
	}

	@Override
	public File getResourcesImagesDirImpl() {
		return new File(this.businessModelResourcesDir + "/images");
	}

	public static void init() {
	}

}
