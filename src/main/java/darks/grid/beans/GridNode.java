package darks.grid.beans;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import darks.grid.GridContext;
import darks.grid.GridRuntime;
import darks.grid.utils.StringUtils;

public class GridNode implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3997893622548935084L;

	private String id;
	
	private transient Channel channel;
	
	private int nodeType;
	
	private GridContext context;
	
	private AtomicLong heartAliveTime = null;
	
	private AtomicReference<MachineInfo> machineInfo = new AtomicReference<>();
	
	public GridNode()
	{
		heartAliveTime = new AtomicLong(System.currentTimeMillis());
	}
	
	public GridNode(String id, Channel channel, GridContext context, int nodeType)
	{
		this.id = id;
		this.channel = channel;
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
		if (!channel.isActive())
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
		if (channel == null || !channel.isActive())
			return false;
		channel.writeAndFlush(obj);
		return true;
	}
	
	public boolean sendSyncMessage(Object obj)
	{
		if (channel == null || !channel.isActive())
			return false;
		try
		{
			ChannelFuture future = channel.writeAndFlush(obj).sync();
			return future.isSuccess();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public Channel getChannel()
	{
		return channel;
	}

	public void setChannel(Channel channel)
	{
		this.channel = channel;
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

    public String toSimpleString()
	{
		return StringUtils.stringBuffer(id, 
				"  [", GridNodeType.valueOf(nodeType),']',
				' ', context.getServerAddress(), 
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

}
