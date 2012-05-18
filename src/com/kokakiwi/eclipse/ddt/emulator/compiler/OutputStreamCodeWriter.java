package com.kokakiwi.eclipse.ddt.emulator.compiler;

import java.io.IOException;
import java.io.OutputStream;

import de.codesourcery.jasm16.compiler.io.AbstractObjectCodeWriter;

public class OutputStreamCodeWriter extends AbstractObjectCodeWriter
{
    private final OutputStream out;
    
    public OutputStreamCodeWriter(OutputStream out)
    {
        this.out = out;
    }
    
    @Override
    protected void closeHook() throws IOException
    {
        out.close();
    }
    
    @Override
    protected OutputStream createOutputStream() throws IOException
    {
        return out;
    }
    
    @Override
    protected void deleteOutputHook() throws IOException
    {
        
    }
    
}
