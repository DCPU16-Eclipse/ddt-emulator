package com.kokakiwi.eclipse.ddt.emulator.compiler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.ui.internal.ide.dialogs.IDEResourceInfoUtils;

import de.codesourcery.jasm16.compiler.io.AbstractResource;
import de.codesourcery.jasm16.compiler.io.IResource;
import de.codesourcery.jasm16.utils.ITextRegion;
import de.codesourcery.jasm16.utils.Misc;

@SuppressWarnings("restriction")
public class EFile extends AbstractResource
{
    private final IFile file;
    
    private String      contents = null;
    
    public EFile(IFile file)
    {
        this(file, ResourceType.SOURCE_CODE);
    }
    
    public EFile(IFile file, ResourceType type)
    {
        super(type);
        this.file = file;
    }
    
    private String loadContents() throws IOException, CoreException
    {
        if (contents == null)
        {
            contents = Misc.readSource(file.getContents());
        }
        
        return contents;
    }
    
    public String getIdentifier()
    {
        return file.getName();
    }
    
    public boolean isSame(IResource other)
    {
        if (other instanceof EFile)
        {
            if (((EFile) other).getFile().equals(file))
            {
                return true;
            }
        }
        return false;
    }
    
    public InputStream createInputStream() throws IOException
    {
        InputStream in = null;
        
        try
        {
            in = file.getContents();
        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }
        
        return in;
    }
    
    public OutputStream createOutputStream(boolean append) throws IOException
    {
        OutputStream out = null;
        
        return out;
    }
    
    public String readText(ITextRegion range) throws IOException
    {
        try
        {
            loadContents();
            return range.apply(contents);
        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public long getAvailableBytes() throws IOException
    {
        return Long.parseLong(IDEResourceInfoUtils.getSizeString(file));
    }
    
    public boolean supportsDelete()
    {
        return true;
    }
    
    public void delete() throws IOException, UnsupportedOperationException
    {
        try
        {
            file.delete(true, null);
        }
        catch (CoreException e)
        {
            e.printStackTrace();
        }
    }
    
    public IFile getFile()
    {
        return file;
    }
    
}
