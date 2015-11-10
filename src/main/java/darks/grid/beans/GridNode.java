package darks.grid.beans;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import darks.grid.GridContext;
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
	
	private GridContext context;
	
	private AtomicLong heartAliveTime = null;
	
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
		if (nodeType == GridNodeType.TYPE_REMOTE)
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
    
    public boolean isQuit()
	{
		return quit;
	}

	public void setQuit(boolean quit)
	{
		this.quit = quit;
	}

	public String toSimpleString()
	{
		return StringUtils.stringBuffer(id, 
				"  [", GridNodeType.valueOf(nodeType),']',
				' ', String.format("%21s", context.getServerAddress().toString()), 
				' ', GridNodeStatus.valueOf(this),
				' ', String.format("%8d", System.currentTimeMillis() - heartAliveTime.get()),
				'\t', StringUtils.percent(context.getMachineInfo().getSystemCpuUsage()), 
				'\t', StringUtils.percent(context.getMachineInfo().getUsedTotalMemoryUsage()));
	}

	@Override
	public String toString()
	{
		return "GridNode [id=" + id + ", nodeType=" + nodeType + ", context=" + context
				+ ", heartAliveTime=" + heartAliveTime + "]";
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

	
}
