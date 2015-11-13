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

import java.io.Serializable;

public class GridConfiguration implements Serializable
{

	private static final long serialVersionUID = -7262144727436897611L;
	
	private NetworkConfig networkConfig = new NetworkConfig();
	
	private ComponentConfig componentConfig = new ComponentConfig();
	
	private EventsConfig eventsConfig = new EventsConfig();
	
	private TaskConfig taskConfig = new TaskConfig();
	
	private String clusterName = "DEFAULT";
    
	public GridConfiguration()
	{
		
	}

	public String getClusterName()
	{
		return clusterName;
	}

	public void setClusterName(String clusterName)
	{
		this.clusterName = clusterName;
	}

    public NetworkConfig getNetworkConfig()
    {
        return networkConfig;
    }
    
    public ComponentConfig getComponentConfig()
    {
        return componentConfig;
    }

    public EventsConfig getEventsConfig()
	{
		return eventsConfig;
	}
    
	public TaskConfig getTaskConfig()
	{
		return taskConfig;
	}

	@Override
	public String toString()
	{
		return "GridConfiguration [networkConfig=" + networkConfig + ", componentConfig="
				+ componentConfig + ", eventsConfig=" + eventsConfig + ", taskConfig=" + taskConfig
				+ ", clusterName=" + clusterName + "]";
	}
	
	
}
