package darks.grid.beans;

import io.netty.channel.Channel;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import darks.grid.GridContext;
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
			if (System.currentTimeMillis() - heartAliveTime.get() > 1000 * 60 * 5)
				return false;
		}
		else
		{
			heartAliveTime.getAndSet(System.currentTimeMillis());
		}
		return true;
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

	public String toSimpleString()
	{
		return StringUtils.stringBuffer(id, 
				"  [", GridNodeType.valueOf(nodeType),']',
				' ', context.getServerAddress(), 
				"\t", GridNodeStatus.valueOf(this),
				' ', System.currentTimeMillis() - heartAliveTime.get());
	}

	@Override
	public String toString()
	{
		return "GridNode [id=" + id + ", nodeType=" + nodeType + ", context=" + context
				+ ", heartAliveTime=" + heartAliveTime + "]";
	}

}
