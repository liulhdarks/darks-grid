package darks.grid.beans;

import io.netty.channel.Channel;

import java.io.Serializable;

public class GridNode implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3997893622548935084L;

	private String id;
	
	private transient Channel channel;
	
	private boolean localNode;
	
	public GridNode()
	{
		
	}
	
	public GridNode(Channel channel, boolean localNode)
	{
		this.id = channel.id().asShortText();
		this.channel = channel;
		this.localNode = localNode;
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

	public boolean isLocalNode()
	{
		return localNode;
	}

	public void setLocalNode(boolean localNode)
	{
		this.localNode = localNode;
	}
	
}
