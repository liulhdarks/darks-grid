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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridComponent;

public class ComponentConfig
{

    private static final Logger log = LoggerFactory.getLogger(ComponentConfig.class);
    
    private LinkedHashMap<String, Class<? extends GridComponent>> componentsMap = new LinkedHashMap<String, Class<? extends GridComponent>>();
    
    private LinkedHashMap<String, Map<String, String>> paramsMap = new LinkedHashMap<String, Map<String,String>>();
    
    public ComponentConfig()
    {
        
    }

    public LinkedHashMap<String, Class<? extends GridComponent>> getComponentsMap()
    {
        return componentsMap;
    }

    public LinkedHashMap<String, Map<String, String>> getParamsMap()
    {
        return paramsMap;
    }

    public Class<? extends GridComponent> getComponentClass(String labelName)
    {
        return componentsMap.get(labelName);
    }

    public Map<String, String> getParams(String labelName)
    {
        return paramsMap.get(labelName);
    }
    
    public void putParamClass(String labelName, String className)
    {
        try
        {
            Class<? extends GridComponent> clazz = (Class<? extends GridComponent>) Class.forName(className);
            componentsMap.put(labelName, clazz);
        }
        catch (Exception e)
        {
            log.error("Fail to parse component class " + className + ". Cause " + e.getMessage(), e);
        }
    }
    
    public void putParamValue(String labelName, String paramName, String value)
    {
        if (value == null || paramName == null || labelName == null)
            return;
        Map<String, String> params = paramsMap.get(labelName);
        if (params == null)
        {
            params = new HashMap<String, String>();
            paramsMap.put(labelName, params);
        }
        params.put(paramName, value);
    }
    
}
