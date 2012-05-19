package com.kokakiwi.eclipse.ddt.emulator.compiler;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;

import de.codesourcery.jasm16.compiler.CompilationUnit;
import de.codesourcery.jasm16.compiler.Compiler;
import de.codesourcery.jasm16.compiler.ICompilationUnit;
import de.codesourcery.jasm16.compiler.ICompiler;
import de.codesourcery.jasm16.compiler.io.IObjectCodeWriterFactory;
import de.codesourcery.jasm16.compiler.io.IResource.ResourceType;

public class CoreCompiler
{
    private final ICompiler              compiler;
    private final IProgressMonitor       monitor;
    
    private final List<ICompilationUnit> units      = new LinkedList<ICompilationUnit>();
    private AppendableResource           appendable = null;
    
    public CoreCompiler(IProgressMonitor monitor)
    {
        compiler = new Compiler();
        this.monitor = monitor;
    }
    
    public void include(IFile file, boolean append)
    {
        ICompilationUnit unit = null;
        
        if (append)
        {
            if (appendable == null)
            {
                appendable = new AppendableResource(ResourceType.SOURCE_CODE,
                        file.getName());
                unit = CompilationUnit.createInstance(
                        appendable.getIdentifier(), appendable);
            }
            
            appendable.append(file);
        }
        else
        {
            unit = CompilationUnit.createInstance(file.getName(), new EFile(
                    file));
        }
        
        if (unit != null)
        {
            units.add(unit);
        }
    }
    
    public void include(IContainer container, boolean append)
            throws CoreException
    {
        if (container.exists())
        {
            for (IResource resource : container.members())
            {
                if (resource instanceof IContainer)
                {
                    include((IContainer) resource, append);
                }
                else if (resource instanceof IFile)
                {
                    IFile file = (IFile) resource;
                    if (file.getFileExtension().equals("dasm"))
                    {
                        include(file, append);
                    }
                }
            }
        }
    }
    
    public void setOutput(OutputStream out)
    {
        setOutput(new OutputStreamCodeWriterFactory(out));
    }
    
    public void setOutput(IObjectCodeWriterFactory factory)
    {
        compiler.setObjectCodeWriterFactory(factory);
    }
    
    public boolean compile()
    {
        boolean result = false;
        
        compiler.compile(units);
        result = true;
        
        return result;
    }
    
    public ICompiler getCompiler()
    {
        return compiler;
    }
    
    public IProgressMonitor getMonitor()
    {
        return monitor;
    }
}
