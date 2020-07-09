/*
    The chemalot-knime package provides a framework to execute commandline
    programs that read and wrie SDF files on a remote host from the KNIME
    graphical pipelining platform. 
    Copyright (C) 2016 Genentech Inc.

    This file is part of chemalot-knime.

    chemalot-knime is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    chemalot-knime is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with chemalot-knime.  If not, see <http://www.gnu.org/licenses/>.

*/
package com.genentech.knime;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.equinox.p2.ui.ProvisioningUI;
import org.eclipse.jsch.core.IJSchService;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.knime.core.node.NodeLogger;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.prefs.Preferences;

import com.genentech.knime.commandLine.CommandList;
import com.genentech.knime.preferences.PreferenceConstants;

/**
 * The activator class controls the plug-in life cycle within Eclipse.
 */
public class GNEPlugin extends AbstractUIPlugin {

	/** The plug-in ID. */
	public static final String PLUGIN_ID = "com.genentech.knime";

	private static final NodeLogger LOG = NodeLogger.getLogger(GNEPlugin.class);

	// The shared instance
	private static GNEPlugin plugin;
	private IJSchService m_ijschService;

	/**
	 * The constructor
	 */
	public GNEPlugin() {
		// empty
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void start(final BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
		NodeLogger.getLogger(GNEPlugin.class).info("Starting Plug-in " + PLUGIN_ID);
		BundleContext bundleContext = getBundle().getBundleContext();

		@SuppressWarnings("unchecked")
		ServiceReference<IJSchService> service = (ServiceReference<IJSchService>) bundleContext
				.getServiceReference(IJSchService.class.getName());

		m_ijschService = bundleContext.getService(service);

		Preferences preferences = InstanceScope.INSTANCE.getNode(PLUGIN_ID);

		// disable knime update sites so that gne update site is the only one
		if (preferences.getBoolean(PreferenceConstants.P_DISABLE_KNIME_UPDATE_SITES, false)) {
			removeKnimeRepositories();
		}
		CommandList.init();
	}

	public static Preferences getPreferences() {
		return InstanceScope.INSTANCE.getNode(PLUGIN_ID);
	}

	/**
	 * disable update sites that include "knime.org" updates are provided by
	 * genentech update site
	 */
	private void removeKnimeRepositories() {
		final ProvisioningUI provUI = ProvisioningUI.getDefaultUI();
		URI[] repositories = provUI.getRepositoryTracker().getKnownRepositories(provUI.getSession());

		List<URI> toRemove = new ArrayList<URI>();
		for (URI uri : repositories) {
			if (uri.getHost() != null && uri.getHost().contains(".knime.org")) {
				toRemove.add(uri);
			}
		}

		provUI.getRepositoryTracker().removeRepositories(toRemove.toArray(new URI[toRemove.size()]),
				provUI.getSession());
	}

	/**
	 * @return the JSch service.
	 */
	public IJSchService getIJSchService() {
		return m_ijschService;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void stop(final BundleContext context) throws Exception {
		plugin = null;
		m_ijschService = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static GNEPlugin getDefault() {
		return plugin;
	}

	public static String getCmdConfigFilePath() {
		// TODO maybe set default file as default path?
		String path = getPreferences().get(PreferenceConstants.P_CMD_CONFIG_FILE_PATH, null);
		if (path == null || path.contentEquals("")) {
			LOG.error("Genentech command config file path not set, unable to create dynamic nodes!");
		}
		return path;
	}

}
