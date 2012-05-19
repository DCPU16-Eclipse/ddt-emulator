package com.kokakiwi.eclipse.ddt.emulator.launch;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;

import com.kokakiwi.eclipse.ddt.emulator.Constants;

public class DCPULaunchShortcut implements ILaunchShortcut
{
    
    public void launch(ISelection selection, String mode)
    {
        if (!(selection instanceof IStructuredSelection))
        {
            return;
        }
        
        Object selected = ((IStructuredSelection) selection).getFirstElement();
        
        if (!(selected instanceof IFile))
        {
            return;
        }
        
        IFile file = (IFile) selected;
        String workspaceFilename = file.toString().substring(2);
        
        try
        {
            DebugPlugin plugin = DebugPlugin.getDefault();
            ILaunchManager manager = plugin.getLaunchManager();
            ILaunchConfigurationType type = manager
                    .getLaunchConfigurationType(Constants.LAUNCH_CONFIGURATION_TYPE_ID);
            
            ILaunchConfiguration[] configs = manager.getLaunchConfigurations();
            for (ILaunchConfiguration config : configs)
            {
                if (config.getAttribute(Constants.LAUNCH_FILE, "")
                        .equalsIgnoreCase(workspaceFilename))
                {
                    DebugUITools.launch(config, mode);
                    return;
                }
            }
            
            if (type != null)
            {
                ILaunchConfigurationWorkingCopy copy = type
                        .newInstance(
                                null,
                                manager.generateLaunchConfigurationName("DCPU16 Program"));
                copy.setAttribute(Constants.LAUNCH_FILE, workspaceFilename);
                ILaunchConfiguration config = copy.doSave();
                
                DebugUITools.launch(config, mode);
            }
        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }
    }
    
    public void launch(IEditorPart editor, String mode)
    {
        IEditorInput input = editor.getEditorInput();
        ISelection selection = new StructuredSelection(
                input.getAdapter(IFile.class));
        launch(selection, mode);
    }
    
}
