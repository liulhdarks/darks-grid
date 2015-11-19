/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
                else if ("task".equalsIgnoreCase(el.getNodeName()))
                {
                    parseAttrForObject(el, config.getTaskConfig());
                }
                else if ("cluster".equalsIgnoreCase(el.getNodeName()))
                {
                    parseAttrForObject(el, config);
                }
                else if ("events".equalsIgnoreCase(el.getNodeName()))
                {
                    parseAttrForObject(el, config.getEventsConfig());
                }
                else if ("storage".equalsIgnoreCase(el.getNodeName()))
                {
                    parseAttrForObject(el, config.getStorageConfig());
                }
                else if ("master".equalsIgnoreCase(el.getNodeName()))
                {
                	parseMaster(config, el);
                }
                else if ("component".equalsIgnoreCase(el.getNodeName()))
                {
                    parseComponent(config, el);
                }
                else
                {
                    parseCustomComponent(config, el);
                }
		    }
		}
		return config;
	}
	
	private static void parseMaster(GridConfiguration config, Element el)
	{
		parseAttrForObject(el, config.getMasterConfig());
		NodeList nodesList = el.getChildNodes();
		for (int i = 0; i < nodesList.getLength(); i++)
		{
		    Node node =  nodesList.item(i);
		    if (node instanceof Element)
		    {
		    	System.out.println(node.getClass());
		        Element elChild = (Element) node;
		        if ("task".equalsIgnoreCase(elChild.getNodeName()))
	            {
		        	String taskName = null;
		        	String taskClassName = null;
		        	if (elChild.hasAttribute("name"))
		        		taskName = elChild.getAttribute("name");
		        	if (elChild.hasAttribute("class"))
		        		taskClassName = elChild.getAttribute("class");
		        	else
		        		throw new GridException("Invalid master task config " + elChild);
		        	if (taskClassName == null || "".equals(taskClassName.trim()))
		        		throw new GridException("Invalid master task className:" + taskClassName);
		        	if (taskName == null || "".equals(taskName.trim()))
		        		taskName = taskClassName;
		        	config.getMasterConfig().addTaskClass(taskName, taskClassName);
	            }
		    }
		}
	}
	
	private static void parseComponent(GridConfiguration config, Element el)
	{
	    String labelName = el.getAttribute("name");
	    String className = el.getAttribute("class");
	    if (labelName == null || "".equals(labelName.trim()))
	        throw new GridException("Invalid component name " + labelName + " to register configuration.");
        if (className == null || "".equals(className.trim()))
            throw new GridException("Invalid component class " + className + " to register configuration.");
        config.getComponentConfig().putParamClass(labelName.toLowerCase(), className);
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
