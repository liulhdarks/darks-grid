package darks.grid.beans;

import io.netty.channel.Channel;

import java.io.Serializable;
import java.net.InetSocketAddress;

import darks.grid.utils.NetworkUtils;

public class GridNode implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3997893622548935084L;

	private String id;
	
	private transient Channel channel;
	
	private boolean localNode;
	
	private InetSocketAddress ipAddress;
	
	public GridNode()
	{
		
	}
	
	public GridNode(String id, Channel channel, boolean localNode)
	{
		this.id = id;
		this.channel = channel;
		this.localNode = localNode;
		if (localNode)
		{
			String ipHost = NetworkUtils.getIpAddress();
			InetSocketAddress ipAddr = (InetSocketAddress) channel.localAddress();
			ipAddress = new InetSocketAddress(ipHost, ipAddr.getPort());
		}
		else
		{
			ipAddress = (InetSocketAddress)channel.remoteAddress();
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

	public boolean isLocalNode()
	{
		return localNode;
	}

	public void setLocalNode(boolean localNode)
	{
		this.localNode = localNode;
	}

	public InetSocketAddress getIpAddress()
	{
		return ipAddress;
	}

	public void setIpAddress(InetSocketAddress ipAddress)
	{
		this.ipAddress = ipAddress;
	}

	@Override
	public String toString()
	{
		return "GridNode [id=" + id + ", localNode=" + localNode + ", ipAddress=" + ipAddress + "]";
	}
	
}
