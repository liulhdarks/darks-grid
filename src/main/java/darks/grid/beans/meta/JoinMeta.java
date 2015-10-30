package darks.grid.beans.meta;

import io.netty.channel.Channel;
import darks.grid.GridContext;

public class JoinMeta extends BaseMeta
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3978065537969750522L;

	private String nodeId;
	
	private GridContext nodeContext;
	
	private transient Channel channel;
	
	private long joinTime;
	
	public JoinMeta()
	{
		
	}

	public JoinMeta(String nodeId, GridContext context)
	{
		super();
		this.nodeId = nodeId;
		this.nodeContext = context;
	}

	public String getNodeId()
	{
		return nodeId;
	}

	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}

	public GridContext context()
	{
		return nodeContext;
	}

	public void setNodeContext(GridContext nodeContext)
	{
		this.nodeContext = nodeContext;
	}

	public Channel getChannel()
	{
		return channel;
	}

	public void setChannel(Channel channel)
	{
		this.channel = channel;
	}

	public long getJoinTime()
	{
		return joinTime;
	}

	public void setJoinTime(long joinTime)
	{
		this.joinTime = joinTime;
	}

	@Override
	public String toString()
	{
		return "JoinMeta [nodeId=" + nodeId + ", nodeContext=" + nodeContext + ", joinTime="
				+ joinTime + "]";
	}

	
	
}
