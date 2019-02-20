package org.monet.bpi.java;

import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPILockService;

public class BPILockServiceImpl extends BPILockImpl implements BPILockService {


	@Override
	public String getRequestDocumentId() {
		return null;
	}

	@Override
	public <T extends BPIBaseNode<?>> T getRequestDocument() {
		return null;
	}

	@Override
	public String getResponseDocumentId() {
		return null;
	}

	@Override
	public <T extends BPIBaseNode<?>> T getResponseDocument() {
		return null;
	}
}
