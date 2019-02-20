package org.monet.bpi.java;

import org.monet.bpi.BPIBaseNode;
import org.monet.bpi.BPILockForm;

public class BPILockFormImpl extends BPILockImpl implements BPILockForm {

	@Override
	public String getFormId() {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends BPIBaseNode<?>> T getForm() {
		return (T) BPINodeServiceImpl.getInstance().get(this.getFormId());
	}

}
