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

import java.io.Serializable;

import darks.grid.beans.GridAddress;
import darks.grid.beans.MachineInfo;
import darks.grid.commons.MachineInfoFactory;
import darks.grid.config.GridConfiguration;
import darks.grid.manager.GridManager;

public class GridContext implements Serializable, GridManager
{

	private static final long serialVersionUID = -4898402719806434910L;

	private long startupTime = 0;
	
	private long startupNanoTime = 0;
	
	private String localNodeId;
	
	private String clusterName;
	
	private GridAddress serverAddress;
	
	private transient MachineInfoFactory machineInfoFactory;
	
	private MachineInfo machineInfo = null;;
	
	public GridContext()
	{
	}
	
	@Override
	public boolean initialize(GridConfiguration config)
	{
        machineInfoFactory = MachineInfoFactory.buildFactory(config);
        machineInfo = machineInfoFactory.createMachineInfo();
		clusterName = config.getClusterName();
		startupTime = System.currentTimeMillis();
		startupNanoTime = System.nanoTime();
		return true;
	}
	
	@Override
	public void destroy()
	{
		
	}
	
	public synchronized boolean isReady()
	{
	    if (localNodeId == null || serverAddress == null)
	        return false;
	    else
	        return true;
	}

	public long getStartupTime()
	{
		return startupTime;
	}

	public void setStartupTime(long startupTime)
	{
		this.startupTime = startupTime;
	}

	public long getStartupNanoTime()
	{
		return startupNanoTime;
	}

	public void setStartupNanoTime(long startupNanoTime)
	{
		this.startupNanoTime = startupNanoTime;
	}

	public synchronized String getLocalNodeId()
	{
		return localNodeId;
	}

	public synchronized void setLocalNodeId(String localNodeId)
	{
		this.localNodeId = localNodeId;
	}

	public String getClusterName()
	{
		return clusterName;
	}

	public void setClusterName(String clusterName)
	{
		this.clusterName = clusterName;
	}

	public synchronized GridAddress getServerAddress()
	{
		return serverAddress;
	}

	public synchronized void setServerAddress(GridAddress serverAddress)
	{
		this.serverAddress = serverAddress;
	}
	
	public MachineInfo getMachineInfo()
    {
        return machineInfo;
    }

    public void setMachineInfo(MachineInfo machineInfo)
    {
        this.machineInfo = machineInfo;
    }
    
    public MachineInfoFactory getMachineInfoFactory()
	{
		return machineInfoFactory;
	}

	public void setMachineInfoFactory(MachineInfoFactory machineInfoFactory)
	{
		this.machineInfoFactory = machineInfoFactory;
	}

	@Override
	public String toString()
	{
		return "GridContext [startupTime=" + startupTime + ", startupNanoTime=" + startupNanoTime
				+ ", localNodeId=" + localNodeId + ", clusterName=" + clusterName
				+ ", serverAddress=" + serverAddress+ ", machineInfo=" + machineInfo + "]";
	}
	
}
