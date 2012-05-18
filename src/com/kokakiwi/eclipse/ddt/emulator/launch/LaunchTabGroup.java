package com.kokakiwi.eclipse.ddt.emulator.launch;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class LaunchTabGroup extends AbstractLaunchConfigurationTabGroup
{
    
    public LaunchTabGroup()
    {
        
    }
    
    @Override
    public void createTabs(ILaunchConfigurationDialog dialog, String mode)
    {
        setTabs(new ILaunchConfigurationTab[] {});
    }
    
}
