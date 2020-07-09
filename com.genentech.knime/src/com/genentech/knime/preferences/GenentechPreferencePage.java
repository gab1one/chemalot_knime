package com.genentech.knime.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import com.genentech.knime.GNEPlugin;

/**
 * This class represents a preference page that is contributed to the
 * Preferences dialog. By subclassing <samp>FieldEditorPreferencePage</samp>, we
 * can use the field support built into JFace that allows us to create a page
 * that is small and knows how to save, restore and apply itself.
 * <p>
 * This page is used to modify preferences only. They are stored in the
 * preference store that belongs to the main plug-in class. That way,
 * preferences can be accessed directly via the preference store.
 */

public class GenentechPreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage {

	public GenentechPreferencePage() {
		super(GRID);
		setPreferenceStore(GNEPlugin.getDefault().getPreferenceStore());
		setDescription("Genentech Preferences");
	}

	/**
	 * Creates the field editors. Field editors are abstractions of the common GUI
	 * blocks needed to manipulate various types of preferences. Each field editor
	 * knows how to save and restore itself.
	 */
	@Override
	public void createFieldEditors() {
		addField(new DirectoryFieldEditor(PreferenceConstants.P_CONFIG_ROOT_PATH, "&Base Directory",
				getFieldEditorParent()));
		addField(new StringFieldEditor(PreferenceConstants.P_CMD_CONFIG_FILE_PATH, "&Path to config directory",
				getFieldEditorParent()));
	}

	@Override
	public void init(IWorkbench workbench) {

	}

}