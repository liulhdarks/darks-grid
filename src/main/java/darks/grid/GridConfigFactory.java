package darks.grid;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class GridConfigFactory
{

	private GridConfigFactory()
	{
		
	}
	
	public GridConfiguration configure(File file)
	{
		try
		{
			return configure(new FileInputStream(file));
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return null;
	}
	
	public GridConfiguration configure(InputStream ins)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder builder = factory.newDocumentBuilder(); 
			Document doc = builder.parse(ins);
		}
		catch (Exception e)
		{
			// TODO: handle exception
		}
		return null;
	}
	
	
	private void parseDocument(Document doc)
	{
		Element rootEl = doc.getDocumentElement();
		
	}
	
}
