package org.monet.space.explorer.control.dialogs.deserializers;

import org.monet.space.explorer.model.Language;
import org.monet.space.kernel.model.Dictionary;

public abstract class ExplorerDeserializer {
	protected Helper helper;

	public ExplorerDeserializer(Helper helper) {
		this.helper = helper;
	}

	public void setHelper(Helper helper) {
		this.helper = helper;
	}

	public interface Helper {
		Dictionary getDictionary();
		Language getLanguage();
	}
}
