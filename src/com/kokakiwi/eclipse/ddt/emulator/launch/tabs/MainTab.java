package com.kokakiwi.eclipse.ddt.emulator.launch.tabs;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementTreeSelectionDialog;
import org.eclipse.ui.dialogs.ISelectionStatusValidator;
import org.eclipse.ui.model.WorkbenchContentProvider;
import org.eclipse.ui.model.WorkbenchLabelProvider;

import com.kokakiwi.eclipse.ddt.emulator.Activator;
import com.kokakiwi.eclipse.ddt.emulator.Constants;

public class MainTab extends AbstractLaunchConfigurationTab
{
    private Text           textFile = null;
    
    private ModifyListener listener = null;
    
    public void createControl(Composite parent)
    {
        listener = new ModifyListener() {
            public void modifyText(ModifyEvent e)
            {
                updateLaunchConfigurationDialog();
            }
        };
        
        Composite composite = new Composite(parent, parent.getStyle());
        composite.setLayout(new GridLayout(3, false));
        composite.setLayoutData(new GridData(GridData.FILL_BOTH));
        setControl(composite);
        
        Label label = new Label(composite, SWT.NONE);
        label.setText("Exec file:");
        
        textFile = new Text(composite, SWT.SINGLE | SWT.BORDER);
        textFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
        textFile.addModifyListener(listener);
        
        Button button = new Button(composite, SWT.PUSH);
        button.setText("Browse");
        button.addListener(SWT.Selection, new Listener() {
            public void handleEvent(Event event)
            {
                ElementTreeSelectionDialog dialog = new ElementTreeSelectionDialog(
                        getShell(), new WorkbenchLabelProvider(),
                        new WorkbenchContentProvider());
                
                dialog.setTitle("Select file");
                dialog.setMessage("Select file to launch");
                dialog.setInput(ResourcesPlugin.getWorkspace().getRoot());
                dialog.setAllowMultiple(false);
                dialog.setValidator(new ISelectionStatusValidator() {
                    public IStatus validate(Object[] selection)
                    {
                        if ((selection != null) && (selection.length != 0)
                                && (selection[0] instanceof IFile))
                        {
                            return new Status(IStatus.OK, Constants.PLUGIN_ID,
                                    IStatus.OK, "", null);
                        }
                        
                        return new Status(IStatus.ERROR, Constants.PLUGIN_ID,
                                IStatus.ERROR, "", null);
                    }
                });
                
                if (dialog.open() == Window.OK)
                {
                    textFile.setText(((IFile) dialog.getFirstResult())
                            .toString().substring(2));
                }
            }
        });
    }
    
    public void setDefaults(ILaunchConfigurationWorkingCopy configuration)
    {
        configuration.setAttribute(Constants.LAUNCH_FILE, "");
    }
    
    public void initializeFrom(ILaunchConfiguration configuration)
    {
        try
        {
            textFile.removeModifyListener(listener);
            textFile.setText(configuration.getAttribute(Constants.LAUNCH_FILE,
                    ""));
            textFile.addModifyListener(listener);
        }
        catch (CoreException e)
        {
            Activator
                    .getDefault()
                    .getLog()
                    .log(new Status(Status.ERROR, Constants.PLUGIN_ID,
                            Status.OK, "Launch configuration error", e));
        }
    }
    
    public void performApply(ILaunchConfigurationWorkingCopy configuration)
    {
        configuration.setAttribute(Constants.LAUNCH_FILE, textFile.getText());
    }
    
    public String getName()
    {
        return "Main";
    }
    
}
