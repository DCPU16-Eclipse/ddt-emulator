package com.kokakiwi.eclipse.ddt.emulator.launch;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

import com.kokakiwi.eclipse.ddt.emulator.launch.tabs.MainTab;

public class LaunchTabGroup extends AbstractLaunchConfigurationTabGroup
{
    @Override
    public void createTabs(ILaunchConfigurationDialog dialog, String mode)
    {
        setTabs(new ILaunchConfigurationTab[] { new MainTab() });
    }
}
