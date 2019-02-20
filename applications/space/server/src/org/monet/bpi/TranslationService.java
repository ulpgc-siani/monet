package org.monet.bpi;

public abstract class TranslationService {

	protected static TranslationService instance;

	public static String translate(String resourceId, String language) {
		return instance.translateImpl(resourceId, language);
	}

	protected abstract String translateImpl(String resourceId, String languageCode);

}