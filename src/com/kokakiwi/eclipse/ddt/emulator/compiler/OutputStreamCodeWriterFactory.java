package com.kokakiwi.eclipse.ddt.emulator.compiler;

import java.io.IOException;
import java.io.OutputStream;

import de.codesourcery.jasm16.compiler.ICompilationContext;
import de.codesourcery.jasm16.compiler.io.IObjectCodeWriter;
import de.codesourcery.jasm16.compiler.io.IObjectCodeWriterFactory;

public class OutputStreamCodeWriterFactory implements IObjectCodeWriterFactory
{
    private final OutputStream out;
    
    public OutputStreamCodeWriterFactory(OutputStream out)
    {
        this.out = out;
    }
    
    @Override
    public IObjectCodeWriter getWriter(ICompilationContext context)
    {
        return new OutputStreamCodeWriter(out);
    }
    
    @Override
    public void closeObjectWriters() throws IOException
    {
        
    }
    
    @Override
    public void deleteOutput() throws IOException
    {
        
    }
    
}
