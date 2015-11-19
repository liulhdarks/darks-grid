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
package darks.grid.beans;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import darks.grid.GridContext;
import darks.grid.GridException;
import darks.grid.GridRuntime;
import darks.grid.network.GridSession;
import darks.grid.utils.StringUtils;

public class GridNode implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3997893622548935084L;

	private String id;
	
	private transient GridSession session;
	
	private int nodeType;
	
	private volatile boolean master;
	
	private GridContext context;
	
	private AtomicLong heartAliveTime = null;
	
	private AtomicLong pingDelay = new AtomicLong(0);
	
	private AtomicReference<MachineInfo> machineInfo = new AtomicReference<>();
	
	private volatile boolean quit = false;
	
	public GridNode()
	{
		heartAliveTime = new AtomicLong(System.currentTimeMillis());
	}
	
	public GridNode(String id, GridSession session, GridContext context, int nodeType)
	{
		this.id = id;
		this.session = session;
		this.nodeType = nodeType;
		this.context = context;
		heartAliveTime = new AtomicLong(System.currentTimeMillis());
	}
	
	public boolean isLocal()
	{
		return nodeType == GridNodeType.TYPE_LOCAL;
	}

	public boolean isValid()
	{
		if (id == null || context == null)
			return false;
		return true;
	}
	
	public boolean isAlive()
	{
		if (!session.isActive() || quit)
			return false;
		if (!isLocal())
		{
			if (System.currentTimeMillis() - heartAliveTime.get() > GridRuntime.config().getNetworkConfig().getNodesExpireTime())
				return false;
		}
		else
		{
			heartAliveTime.getAndSet(System.currentTimeMillis());
		}
		return true;
	}
	
	public boolean sendMessage(Object obj)
	{
		if (session == null || !session.isActive())
			return false;
		return session.sendMessage(obj);
	}
	
	public boolean sendSyncMessage(Object obj)
	{
		if (session == null || !session.isActive())
			return false;
		return session.sendSyncMessage(obj);
	}
	
	public boolean publishEvent(String type, Object data)
	{
		return publishEvent(type, data, false);
	} 
	
	public boolean publishEvent(String type, Object data, boolean sync)
	{
		GridEvent event = new GridEvent(data, type);
		return GridRuntime.events().publish(this, event, sync);
	} 
	
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}
	
	public GridSession getSession()
	{
		return session;
	}

	public void setSession(GridSession session)
	{
		this.session = session;
	}

	public int getNodeType()
	{
		return nodeType;
	}

	public void setNodeType(int nodeType)
	{
		this.nodeType = nodeType;
	}

	public GridContext context()
	{
		return context;
	}

	public void setContext(GridContext context)
	{
		this.context = context;
	}
	
	public long getHeartAliveTime()
	{
		return heartAliveTime.get();
	}

	public void setHeartAliveTime(long heartAliveTime)
	{
		this.heartAliveTime.getAndSet(heartAliveTime);
	}
	
	public MachineInfo getMachineInfo()
    {
		return machineInfo.get();
    }

    public void setMachineInfo(MachineInfo machineInfo)
    {
    	this.machineInfo.getAndSet(machineInfo);
    }
    
    public long getPingDelay()
	{
		return pingDelay.get();
	}

	public void setPingDelay(long pingDelay)
	{
		this.pingDelay.getAndSet(pingDelay);
	}

	public boolean isQuit()
	{
		return quit;
	}

	public void setQuit(boolean quit)
	{
		this.quit = quit;
	}
	
	public synchronized boolean isMaster()
	{
		return master;
	}

	public synchronized void setMaster(boolean master)
	{
		this.master = master;
	}

	public String toSimpleString()
	{
		String nodeStatus = GridNodeStatus.valueOf(this);
		long heartDelay = System.currentTimeMillis() - heartAliveTime.get();
		MachineInfo info = context.getMachineInfo();
		String unit = "ms";
		long delay = pingDelay.get();
		String flagMaster = master ? "[M]" : "   ";
		return StringUtils.stringBuffer(id, 
				"  [", GridNodeType.valueOf(nodeType),']', flagMaster,
				' ', String.format("%-21s", context.getServerAddress().toString()), 
				' ', nodeStatus,
				' ', String.format("%-8d", heartDelay),
				'\t', StringUtils.percent(info.getSystemCpuUsage()), 
				'\t', StringUtils.percent(info.getUsedTotalMemoryUsage()),
				' ', String.format("%-3d", info.getHealthyScore()),
				' ', delay, unit);
	}

    @Override
	public String toString()
	{
		return "GridNode [id=" + id + ", nodeType=" + nodeType + ", context=" + context
				+ ", heartAliveTime=" + heartAliveTime + ", pingDelay=" + pingDelay
				+ ", machineInfo=" + machineInfo + ", quit=" + quit + "]";
	}

	@Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GridNode other = (GridNode)obj;
        if (id == null)
        {
            if (other.id != null)
                return false;
        }
        else if (!id.equals(other.id))
            return false;
        return true;
    }

    public static class GridNodeType
    {
    	
    	public static final int TYPE_LOCAL = 1;
    	private static final String TYPE_LOCAL_STR = "L";
    	
    	public static final int TYPE_REMOTE = 2;
    	private static final String TYPE_REMOTE_STR = "R";
    	
    	public static String valueOf(int type)
    	{
    		switch (type)
    		{
    		case TYPE_LOCAL:
    			return TYPE_LOCAL_STR;
    		case TYPE_REMOTE:
    			return TYPE_REMOTE_STR;
    		default:
    			throw new GridException("Invalid grid node type " + type);
    		}
    	}
    }
    
    public static class GridNodeStatus
    {
    	
    	public static final int INVALID = 0;
    	
    	public static final int ACTIVE = 1;
    	
    	public static final int INACTIVE = 2;
    	
    	public static String valueOf(GridNode node)
    	{
    		GridSession session = node.getSession();
    		if (session == null)
    			return "INVALID";
    		if (node.isAlive())
    			return "ALIVE";
    		if (session.isActive())
    			return "ACTIVE";
    		return "INACTIVE";
    	}

    }
}
