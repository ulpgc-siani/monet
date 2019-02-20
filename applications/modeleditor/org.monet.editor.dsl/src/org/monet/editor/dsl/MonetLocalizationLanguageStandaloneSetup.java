
package org.monet.editor.dsl;


/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class MonetLocalizationLanguageStandaloneSetup extends MonetLocalizationLanguageStandaloneSetupGenerated{

	public static void doSetup() {
		new MonetLocalizationLanguageStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

