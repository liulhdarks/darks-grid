package darks.grid.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public final class IOUtils
{

	private IOUtils()
	{
		
	}
	
	public static void closeStream(Writer writer)
	{
		try
		{
			writer.close();
		}
		catch (IOException e)
		{
		}
	}
	
	public static void closeStream(Reader reader)
	{
		try
		{
			reader.close();
		}
		catch (IOException e)
		{
		}
	}
	
	public static void closeStream(InputStream ins)
	{
		try
		{
			ins.close();
		}
		catch (IOException e)
		{
		}
	}
	
	public static void closeStream(OutputStream out)
	{
		try
		{
			out.close();
		}
		catch (IOException e)
		{
		}
	}
	
}
