package org.monet.editor.ui.preference;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.monet.editor.MonetPreferences;

public class PreferenceInitializer extends AbstractPreferenceInitializer {

	public void initializeDefaultPreferences() {
		MonetPreferences.init();
	}

}
