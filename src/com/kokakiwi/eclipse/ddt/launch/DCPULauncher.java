package com.kokakiwi.eclipse.ddt.launch;

import java.io.PrintStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IOConsole;
import org.eclipse.ui.console.MessageConsole;

import com.kokakiwi.dcpu.emulator.swt.CoreEmulator;
import com.kokakiwi.eclipse.ddt.emulator.Activator;
import com.kokakiwi.eclipse.ddt.emulator.Constants;
import com.kokakiwi.eclipse.ddt.emulator.compiler.CoreCompiler;

import de.codesourcery.jasm16.compiler.io.ByteArrayObjectCodeWriterFactory;

public class DCPULauncher implements ILaunchConfigurationDelegate
{
    
    @Override
    public void launch(ILaunchConfiguration config, String mode,
            ILaunch launch, IProgressMonitor monitor) throws CoreException
    {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        
        // Get file
        String workspaceFile = config.getAttribute(Constants.LAUNCH_FILE, "");
        if ((workspaceFile == null) || (workspaceFile.trim().length() < 1))
        {
            return;
        }
        
        String sep = System.getProperty("file.separator");
        if (sep.charAt(0) != '/')
        {
            sep = "\\" + sep;
        }
        
        workspaceFile = workspaceFile.replaceAll("/", sep);
        String filename = ResourcesPlugin.getWorkspace().getRoot()
                .getLocation().makeAbsolute().toOSString()
                + System.getProperty("file.separator") + workspaceFile;
        
        IFile file = ResourcesPlugin.getWorkspace().getRoot()
                .getFileForLocation(new Path(filename));
        
        // Launch
        IOConsole console = createConsole("DCPU16");
        
        ByteArrayObjectCodeWriterFactory factory = new ByteArrayObjectCodeWriterFactory();
        CoreCompiler compiler = new CoreCompiler(monitor);
        compiler.include(file, true);
        
        IProject project = file.getProject();
        if (project != null)
        {
            IFolder includesFolder = project.getFolder(store
                    .getString(Constants.INCLUDES_DIR));
            compiler.include(includesFolder, true);
        }
        
        compiler.setOutput(factory);
        
        if (compiler.compile())
        {
            CoreEmulator emulator = new CoreEmulator(new PrintStream(
                    console.newOutputStream()));
            emulator.run(factory.getBytes());
        }
    }
    
    public IOConsole createConsole(String name)
    {
        ConsolePlugin plugin = ConsolePlugin.getDefault();
        IConsoleManager mgr = plugin.getConsoleManager();
        IConsole[] consoles = mgr.getConsoles();
        for (IConsole console : consoles)
        {
            if (name.equals(console.getName()))
            {
                return (IOConsole) console;
            }
        }
        
        IOConsole console = new MessageConsole(name, null);
        mgr.addConsoles(new IConsole[] { console });
        
        return console;
    }
    
}
