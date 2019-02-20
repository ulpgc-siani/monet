
package org.monet.editor.dsl;


/**
 * Initialization support for running Xtext languages 
 * without equinox extension registry
 */
public class MonetModelingLanguageStandaloneSetup extends MonetModelingLanguageStandaloneSetupGenerated{

	public static void doSetup() {
		new MonetModelingLanguageStandaloneSetup().createInjectorAndDoEMFRegistration();
	}
}

