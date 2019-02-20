package org.monet.bpi.java;

import org.monet.bpi.TranslationService;
import org.monet.space.kernel.model.Language;

public class TranslationServiceImpl extends TranslationService {

	@Override
	public String translateImpl(String resourceId, String languageCode) {
		Language language = Language.getInstance();
		return language.getModelResource(resourceId, languageCode);
	}

	public static void init() {
		instance = new TranslationServiceImpl();
	}

}
