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

import darks.grid.config.GridConfigFactory;
import darks.grid.config.GridConfiguration;
import darks.grid.events.GridEventsManager;
import darks.grid.manager.GridComponentManager;
import darks.grid.manager.GridNodesManager;
import darks.grid.manager.GridStorageManager;
import darks.grid.master.GridMasterManager;
import darks.grid.network.local.GridLocalMessageManager;

/**
 * @author Liu lihua
 *
 */
public final class GridCloud
{

	public boolean startup()
	{
        GridConfiguration config = GridConfigFactory.configure(this.getClass().getResourceAsStream("/grid-config.xml"));
		if (config == null)
			return false;
        return GridRuntime.initialize(config);
	}

	public boolean startup(GridConfiguration config)
	{
		if (config == null)
			return false;
		return GridRuntime.initialize(config);
	}
	
	public static synchronized boolean isReady()
	{
	    return GridRuntime.isReady();
	}

	public static GridConfiguration config()
	{
		return GridRuntime.config();
	}

	public static GridNodesManager nodes()
	{
		return GridRuntime.nodes();
	}

	public static GridContext context()
	{
		return GridRuntime.context();
	}

	public static GridLocalMessageManager local()
	{
		return GridRuntime.local();
	}

	public static GridEventsManager events()
	{
		return GridRuntime.events();
	}

	public static GridStorageManager storage()
	{
		return GridRuntime.storage();
	}

	public static GridMasterManager master()
	{
		return GridRuntime.master();
	}

	public static GridComponentManager components()
	{
		return GridRuntime.components();
	}
}
