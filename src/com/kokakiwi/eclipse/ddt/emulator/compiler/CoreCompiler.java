package com.kokakiwi.eclipse.ddt.emulator.compiler;

import java.io.OutputStream;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;

import de.codesourcery.jasm16.compiler.CompilationUnit;
import de.codesourcery.jasm16.compiler.Compiler;
import de.codesourcery.jasm16.compiler.ICompilationUnit;
import de.codesourcery.jasm16.compiler.ICompiler;
import de.codesourcery.jasm16.compiler.io.IObjectCodeWriterFactory;

public class CoreCompiler
{
    private final ICompiler              compiler;
    private final IProgressMonitor       monitor;
    
    private final List<ICompilationUnit> units = new LinkedList<ICompilationUnit>();
    
    public CoreCompiler(IProgressMonitor monitor)
    {
        compiler = new Compiler();
        this.monitor = monitor;
    }
    
    public void include(IFile file)
    {
        ICompilationUnit unit = CompilationUnit.createInstance(file.getName(),
                new EFile(file));
        units.add(unit);
    }
    
    public void include(IContainer container)
    {
        
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
