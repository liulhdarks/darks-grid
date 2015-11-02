package darks.grid.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import darks.grid.GridException;
import darks.grid.utils.ReflectUtils;

public final class GridConfigFactory
{

    private static final Logger log = LoggerFactory.getLogger(GridConfigFactory.class);
    
    
	private GridConfigFactory()
	{
		
	}
	
	public static GridConfiguration configure(File file)
	{
		try (InputStream ins = new FileInputStream(file))
		{
			return configure(ins);
		}
		catch (Exception e)
		{
            log.error(e.getMessage(), e);
		}
		return null;
	}
	
	public static GridConfiguration configure(InputStream ins)
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance(); 
			DocumentBuilder builder = factory.newDocumentBuilder(); 
			Document doc = builder.parse(ins);
			return parseDocument(doc);
		}
		catch (Exception e)
		{
		    log.error(e.getMessage(), e);
		}
		return null;
	}
	
	
	private static GridConfiguration parseDocument(Document doc)
	{
        GridConfiguration config = new GridConfiguration();
		Element rootEl = doc.getDocumentElement();
		NodeList nodesList = rootEl.getChildNodes();
		for (int i = 0; i < nodesList.getLength(); i++)
		{
		    Node node =  nodesList.item(i);
		    if (node instanceof Element)
		    {
		        Element el = (Element) node;
		        if ("network".equalsIgnoreCase(el.getNodeName()))
	            {
	                parseAttrForObject(el, config.getNetworkConfig());
	            }
                else if ("cluster".equalsIgnoreCase(el.getNodeName()))
                {
                    parseAttrForObject(el, config);
                }
                else if ("register".equalsIgnoreCase(el.getNodeName()))
                {
                    parseRegister(config, el);
                }
                else
                {
                    parseCustomComponent(config, el);
                }
		    }
		}
		return config;
	}
	
	private static void parseRegister(GridConfiguration config, Element el)
	{
	    String labelName = el.getAttribute("name");
	    String className = el.getAttribute("class");
	    if (labelName == null || "".equals(labelName.trim()))
	        throw new GridException("Invalid component name " + labelName + " to register configuration.");
        if (className == null || "".equals(className.trim()))
            throw new GridException("Invalid component class " + className + " to register configuration.");
        config.getComponentConfig().putParamClass(labelName, className);
	}
    
    private static void parseCustomComponent(GridConfiguration config, Element el)
    {
        String labelName = el.getNodeName().trim().toLowerCase();
        if (labelName == null || "".equals(labelName))
        {
            log.error("Invalid component configuration element:" + el);
            return;
        }
        NamedNodeMap nameNodes = el.getAttributes();
        for (int i = 0; i < nameNodes.getLength(); i++)
        {
            Node node = nameNodes.item(i);
            String attrName = node.getNodeName().trim();
            String attrValue = node.getNodeValue().trim();
            config.getComponentConfig().putParamValue(labelName, attrName, attrValue);
        }
    }
    
    private static void parseAttrForObject(Element el, Object obj)
    {
        NamedNodeMap nameNodes = el.getAttributes();
        for (int i = 0; i < nameNodes.getLength(); i++)
        {
            Node node = nameNodes.item(i);
            String attrName = node.getNodeName().trim();
            String attrValue = node.getNodeValue().trim();
            ReflectUtils.putAttrToObjectField(obj, attrName, attrValue);
        }
    }
	
}
