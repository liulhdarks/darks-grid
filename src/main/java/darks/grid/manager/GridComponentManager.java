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
package darks.grid.manager;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridException;
import darks.grid.RuntimeLog;
import darks.grid.beans.GridComponent;
import darks.grid.config.ComponentConfig;
import darks.grid.config.GridConfiguration;
import darks.grid.executor.job.JobExecuteGuard;
import darks.grid.network.NodesHeartAlive;
import darks.grid.network.discovery.MERGE_NODES;
import darks.grid.network.discovery.TCPPING;
import darks.grid.utils.ReflectUtils;

public class GridComponentManager implements GridManager
{
    
    private static final Logger log = LoggerFactory.getLogger(GridComponentManager.class);
    
    private static Map<String, Class<? extends GridComponent>> systemCompConfig = new HashMap<String, Class<? extends GridComponent>>();
    
    private LinkedHashMap<String, GridComponent> componentsMap = new LinkedHashMap<String, GridComponent>();
    
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    
    private List<GridComponent> runningComponents = new LinkedList<GridComponent>();
    
    static
    {
        systemCompConfig.put("tcpping", TCPPING.class);
        systemCompConfig.put("alive", NodesHeartAlive.class);
        systemCompConfig.put("log", RuntimeLog.class);
        systemCompConfig.put("merge", MERGE_NODES.class);
        
    }
    
    public GridComponentManager()
    {
        
    }
    
    private void registerSystemComponents()
    {
        registerComponent("$GRID_JOB_EXEC_GUARD", new JobExecuteGuard());
    }
    
    @Override
    public synchronized boolean initialize(GridConfiguration config)
    {
        try
        {
            ComponentConfig cfg = config.getComponentConfig();
            for (Entry<String, Map<String, String>> compParamsEntry : cfg.getParamsMap().entrySet())
            {
                String labelName = compParamsEntry.getKey();
                if (componentsMap.containsKey(labelName))
                {
                    log.error("Fail to register component " + labelName + " which has been registered before.");
                    continue;
                }
                Class<? extends GridComponent> clazz = cfg.getComponentClass(labelName);
                if (clazz == null)
                    clazz = systemCompConfig.get(labelName);
                if (clazz == null)
                {
                    log.error("Fail to register component " + labelName + ". Cannot find specify class.");
                    continue;
                }
                GridComponent comp = ReflectUtils.newInstance(clazz);
                if (comp != null)
                {
                    Map<String, String> paramsMap = compParamsEntry.getValue();
                    if (paramsMap != null && !paramsMap.isEmpty())
                    {
                        for (Entry<String, String> paramEntry : paramsMap.entrySet())
                        {
                            String attrName = paramEntry.getKey();
                            String attrValue = paramEntry.getValue();
                            ReflectUtils.putAttrToObjectField(comp, attrName, attrValue);
                        }
                    }
                    registerComponent(labelName, comp);
                }
            }
            registerSystemComponents();
            return true;
        }
        catch (Exception e)
        {
            log.error("Fail to initialize component. Cause " + e.getMessage(), e);
            return false;
        }
    }
    
    @Override
    public synchronized void destroy()
    {
        for (GridComponent component : runningComponents)
        {
        	if (component != null)
        		component.destroy();
        }
        threadPool.shutdownNow();
    }
    
    public synchronized void setupComponents()
    {
        for (Entry<String, GridComponent> entry : componentsMap.entrySet())
        {
        	setupComponent(entry.getValue());
        }
    }
    
    public synchronized void setupComponent(GridComponent component)
    {
        threadPool.execute(component);
        runningComponents.add(component);
    }
    
    public synchronized void registerComponent(String name, GridComponent component)
    {
        if (componentsMap.containsKey(name))
        {
            throw new GridException("Grid component " + name + " has exist. Please check your component name.");
        }
        componentsMap.put(name, component);
    }
    
    public synchronized GridComponent getComponent(String name)
    {
        return componentsMap.get(name);
    }

    public LinkedHashMap<String, GridComponent> getComponentsMap()
    {
        return componentsMap;
    }
    
}
