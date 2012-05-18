package com.kokakiwi.eclipse.ddt.emulator.base;

import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.runtime.CoreException;

import com.kokakiwi.eclipse.ddt.emulator.Constants;

public class DCPUNature implements IProjectNature
{
    private IProject project = null;
    
    @Override
    public void configure() throws CoreException
    {
        IProjectDescription desc = project.getDescription();
        ICommand[] commands = desc.getBuildSpec();
        boolean found = false;
        
        for (int i = 0; i < commands.length; ++i)
        {
            if (commands[i].getBuilderName().equals(Constants.BUILDER_ID))
            {
                found = true;
                break;
            }
        }
        if (!found)
        {
            // add builder to project
            ICommand command = desc.newCommand();
            command.setBuilderName(Constants.BUILDER_ID);
            ICommand[] newCommands = new ICommand[commands.length + 1];
            
            // Add it before other builders.
            System.arraycopy(commands, 0, newCommands, 1, commands.length);
            newCommands[0] = command;
            desc.setBuildSpec(newCommands);
            project.setDescription(desc, null);
        }
    }
    
    @Override
    public void deconfigure() throws CoreException
    {
        
    }
    
    @Override
    public IProject getProject()
    {
        return project;
    }
    
    @Override
    public void setProject(IProject project)
    {
        this.project = project;
    }
    
}
