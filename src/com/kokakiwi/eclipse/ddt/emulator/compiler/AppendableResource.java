package com.kokakiwi.eclipse.ddt.emulator.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import de.codesourcery.jasm16.compiler.io.AbstractResource;
import de.codesourcery.jasm16.compiler.io.IResource;
import de.codesourcery.jasm16.utils.ITextRegion;

public class AppendableResource extends AbstractResource
{
    private final StringBuilder sb = new StringBuilder();
    private final String        identifier;
    
    protected AppendableResource(ResourceType type, String identifier)
    {
        super(type);
        this.identifier = identifier;
    }
    
    @Override
    public String getIdentifier()
    {
        return identifier;
    }
    
    @Override
    public boolean isSame(IResource other)
    {
        return false;
    }
    
    @Override
    public InputStream createInputStream() throws IOException
    {
        return IOUtils.toInputStream(sb.toString());
    }
    
    @Override
    public OutputStream createOutputStream(boolean append) throws IOException
    {
        return new StringOutputStream();
    }
    
    @Override
    public String readText(ITextRegion range) throws IOException
    {
        return range.apply(sb.toString());
    }
    
    @Override
    public long getAvailableBytes() throws IOException
    {
        return sb.toString().getBytes().length;
    }
    
    public void append(IFile file)
    {
        try
        {
            sb.append(IOUtils.toString(file.getContents()));
            sb.append('\n');
        }
        catch (IOException | CoreException e)
        {
            e.printStackTrace();
        }
    }
    
    private class StringOutputStream extends OutputStream
    {
        @Override
        public void write(int b) throws IOException
        {
            sb.append((char) b);
        }
        
        @Override
        public String toString()
        {
            return sb.toString();
        }
        
    }
}
