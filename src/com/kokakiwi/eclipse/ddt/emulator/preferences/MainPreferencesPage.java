package com.kokakiwi.eclipse.ddt.emulator.preferences;

import org.eclipse.jface.preference.*;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.eclipse.ui.IWorkbench;
import com.kokakiwi.eclipse.ddt.emulator.Activator;
import com.kokakiwi.eclipse.ddt.emulator.Constants;

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

public class MainPreferencesPage extends FieldEditorPreferencePage implements
        IWorkbenchPreferencePage
{
    
    public MainPreferencesPage()
    {
        super(GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setTitle("DDT Preferences");
        setDescription("DCPU16 Developer Toolkit Preferences");
    }
    
    /**
     * Creates the field editors. Field editors are abstractions of the common
     * GUI blocks needed to manipulate various types of preferences. Each field
     * editor knows how to save and restore itself.
     */
    public void createFieldEditors()
    {
        addField(new StringFieldEditor(Constants.INCLUDES_DIR, "Includes dir",
                getFieldEditorParent()));
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.eclipse.ui.IWorkbenchPreferencePage#init(org.eclipse.ui.IWorkbench)
     */
    public void init(IWorkbench workbench)
    {
    }
    
}