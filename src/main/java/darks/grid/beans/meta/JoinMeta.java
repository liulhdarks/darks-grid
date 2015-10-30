package darks.grid.beans.meta;

import io.netty.channel.Channel;

public class JoinMeta extends BaseMeta
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3978065537969750522L;

	private String nodeId;
	
	private long startupTime;
	
	private transient Channel channel;
	
	private long joinTime;
	
	private String clusterName;
	
	public JoinMeta()
	{
		
	}

	public JoinMeta(String nodeId, long startupTime, String clusterName)
	{
		super();
		this.nodeId = nodeId;
		this.startupTime = startupTime;
		this.clusterName = clusterName;
	}

	public String getNodeId()
	{
		return nodeId;
	}

	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}

	public long getStartupTime()
	{
		return startupTime;
	}

	public void setStartupTime(long startupTime)
	{
		this.startupTime = startupTime;
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
	
	

	public String getClusterName()
	{
		return clusterName;
	}

	public void setClusterName(String clusterName)
	{
		this.clusterName = clusterName;
	}

	@Override
	public String toString()
	{
		return "JoinMeta [nodeId=" + nodeId + ", startupTime=" + startupTime + ", joinTime="
				+ joinTime + ", clusterName=" + clusterName + "]";
	}

	
	
}
