package darks.grid;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridComponent;
import darks.grid.config.ComponentConfig;
import darks.grid.config.GridConfiguration;
import darks.grid.network.NodesHeartAlive;
import darks.grid.network.discovery.TCPPING;
import darks.grid.utils.ReflectUtils;

public class GridComponentManager implements GridManager
{
    
    private static final Logger log = LoggerFactory.getLogger(GridComponentManager.class);
    
    private static Map<String, Class<? extends GridComponent>> systemCompConfig = new HashMap<String, Class<? extends GridComponent>>();
    
    private LinkedHashMap<String, GridComponent> componentsMap = new LinkedHashMap<String, GridComponent>();
    
    private ExecutorService threadPool = Executors.newCachedThreadPool();
    
    static
    {
        systemCompConfig.put("tcpping", TCPPING.class);
        systemCompConfig.put("alive", NodesHeartAlive.class);
        systemCompConfig.put("log", RuntimeLog.class);
    }
    
    public GridComponentManager()
    {
        
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
        for (Entry<String, GridComponent> entry : componentsMap.entrySet())
        {
            GridComponent component = entry.getValue();
            component.destroy();
        }
        threadPool.shutdownNow();
    }
    
    public synchronized void setupComponents()
    {
        for (Entry<String, GridComponent> entry : componentsMap.entrySet())
        {
            GridComponent component = entry.getValue();
            threadPool.execute(component);
        }
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
