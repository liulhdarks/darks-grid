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
package darks.grid;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.config.GridConfiguration;
import darks.grid.events.GridEventsManager;
import darks.grid.executor.job.GridJobManager;
import darks.grid.executor.task.GridTaskManager;
import darks.grid.network.GridNetworkManager;
import darks.grid.network.local.GridLocalMessageManager;
import darks.grid.utils.ThreadUtils;

public final class GridRuntime
{
    
    private static final Logger log = LoggerFactory.getLogger(GridRuntime.class);

	static GridConfiguration config;
	
	static GridNetworkManager network = new GridNetworkManager();
	
	static GridNodesManager nodesManager = new GridNodesManager();
	
	static GridContext context = new GridContext();
	
	static GridComponentManager componentsManager = new GridComponentManager();
	
	static GridTaskManager tasksManager = new GridTaskManager();
	
	static GridJobManager jobsManager = new GridJobManager();
	
	static GridLocalMessageManager localManager = new GridLocalMessageManager();
	
	static GridEventsManager eventsManager = new GridEventsManager();
	
	static volatile boolean finishLoaded = false;
	
	private GridRuntime()
	{
		
	}
	
	public static boolean initialize(GridConfiguration config)
	{
		GridRuntime.config = config;
		boolean ret = context.initialize(config);
		if (!ret)
		{
            log.error("Fail to initialize grid context.");
            return false;
		}
		ret = eventsManager.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize events manager.");
            return false;
        }
        ret = localManager.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize local message manager.");
            return false;
        }
        ret = tasksManager.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize tasks manager.");
            return false;
        }
        ret = jobsManager.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize jobs manager.");
            return false;
        }
		ret = componentsManager.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize grid components.");
            return false;
        }
        ret = nodesManager.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize node manager.");
            return false;
        }
        ret = network.initialize(config);
        if (!ret)
        {
            log.error("Fail to initialize network.");
            return false;
        }
        componentsManager.setupComponents();
        finishLoaded = true;
		return awaitReady();
	}
	
	public static void destroy()
	{
		jobsManager.destroy();
		tasksManager.destroy();
		componentsManager.destroy();
	    localManager.destroy();
		network.destroy();
		nodesManager.destroy();
		eventsManager.destroy();
		ThreadUtils.shutdownAll();
	}
	
	public static boolean awaitReady()
	{
	    return awaitReady(0, TimeUnit.MILLISECONDS);
	}
    
    public static boolean awaitReady(long timeout, TimeUnit unit)
    {
        long time = unit.toMillis(timeout);
        long st = System.currentTimeMillis();
        try
        {
            for (;;)
            {
                if (isReady())
                    return true;
                if (timeout > 0 && System.currentTimeMillis() - st > time)
                    break;
                Thread.sleep(100);
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
        return false;
    }
	
	public static boolean isReady()
	{
	    if (finishLoaded && context().isReady())
	        return true;
	    else
	        return false;
	}
	
	public static String getLocalId()
	{
		return context().getLocalNodeId();
	}

	public static GridConfiguration config()
	{
		return config;
	}

	public static GridNetworkManager network()
	{
		return network;
	}

	public static GridNodesManager nodes()
	{
		return nodesManager;
	}

	public static GridTaskManager tasks()
	{
		return tasksManager;
	}

	public static GridContext context()
	{
		return context;
	}

	public static GridLocalMessageManager local()
	{
		return localManager;
	}

	public static GridEventsManager events()
	{
		return eventsManager;
	}

	public static GridJobManager jobs()
	{
		return jobsManager;
	}
	
	
}
