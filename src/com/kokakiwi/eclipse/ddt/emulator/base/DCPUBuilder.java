package com.kokakiwi.eclipse.ddt.emulator.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import com.kokakiwi.eclipse.ddt.emulator.Constants;
import com.kokakiwi.eclipse.ddt.emulator.compiler.CoreCompiler;

public class DCPUBuilder extends IncrementalProjectBuilder
{
    public DCPUBuilder()
    {
        
    }
    
    @Override
    protected IProject[] build(int kind, Map<String, String> args,
            IProgressMonitor monitor) throws CoreException
    {
        build(monitor);
        
        return null;
    }
    
    protected void build(IProgressMonitor monitor) throws CoreException
    {
        IProject project = getProject();
        IResourceDelta delta = getDelta(project);
        if (delta != null)
        {
            monitor.beginTask("Building project...", 100);
            
            DCPUBuilderVisitor visitor = new DCPUBuilderVisitor(monitor);
            delta.accept(visitor);
            
            monitor.done();
            project.refreshLocal(IResource.DEPTH_INFINITE, null);
        }
    }
    
    private void compileFile(IProgressMonitor monitor, IFile file)
            throws CoreException
    {
        IFolder buildDir = file.getProject().getFolder(Constants.BUILD_DIR);
        
        IFile dest = buildDir.getFile(file.getName()
                .substring(0, file.getName().indexOf('.')).concat(".bin"));
        
        File fdest = dest.getLocation().toFile();
        fdest.getParentFile().mkdirs();
        
        CoreCompiler compiler = new CoreCompiler(monitor);
        compiler.include(file, false);
        
        try
        {
            compiler.setOutput(new FileOutputStream(fdest));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
        if (!compiler.compile())
        {
            // ERROR!
        }
        
        if (fdest.exists())
        {
            fdest.delete();
        }
    }
    
    private class DCPUBuilderVisitor implements IResourceDeltaVisitor
    {
        private final IProgressMonitor monitor;
        
        public DCPUBuilderVisitor(IProgressMonitor monitor)
        {
            this.monitor = monitor;
        }
        
        public boolean visit(IResourceDelta delta) throws CoreException
        {
            int deltaKind = delta.getKind();
            
            if ((deltaKind == IResourceDelta.ADDED)
                    || (deltaKind == IResourceDelta.CHANGED))
            {
                IResource resource = delta.getResource();
                int resourceType = resource.getType();
                
                if ((resourceType == IResource.FOLDER)
                        || (resourceType == IResource.PROJECT))
                {
                    return true;
                }
                else if (resourceType == IResource.FILE)
                {
                    String extension = resource.getFileExtension();
                    if ((extension != null)
                            && extension.equalsIgnoreCase("dasm"))
                    {
                        compileFile(monitor, (IFile) resource);
                    }
                }
            }
            
            return false;
        }
    }
}
