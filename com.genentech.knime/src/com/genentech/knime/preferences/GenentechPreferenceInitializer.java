package com.genentech.knime.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.knime.core.eclipseUtil.OSGIHelper;
import org.knime.core.util.EclipseUtil;

import com.genentech.knime.GNEPlugin;

/**
 * Class used to initialize default preference values.
 */
public class GenentechPreferenceInitializer extends AbstractPreferenceInitializer {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer#
	 * initializeDefaultPreferences()
	 */
	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = GNEPlugin.getDefault().getPreferenceStore();

		final String config_path = System.getProperty("GNEKnimeConfigDir");
		if (config_path != null) {
			store.setDefault(PreferenceConstants.P_CONFIG_ROOT_PATH, config_path);
		} else {
			store.setDefault(PreferenceConstants.P_CONFIG_ROOT_PATH,
					OSGIHelper.getBundle(GenentechPreferenceInitializer.class).getLocation() + "/config");
			// TODO add path to default config shipped with plugin
//			store.setDefault(name, value);
		}

		store.setDefault(PreferenceConstants.P_CMD_CONFIG_FILE_PATH, "/cmdLine/commandLinePrograms.xml");

	}

}
