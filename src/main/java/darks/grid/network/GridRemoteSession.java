package darks.grid.network;

import java.net.SocketAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

public class GridRemoteSession implements GridSession
{
	
	Channel channel;

	public GridRemoteSession(Channel channel)
	{
		this.channel = channel;
	}
	
	@Override
	public String getId()
	{
		return channel.id().asShortText();
	}

	@Override
	public boolean sendMessage(Object msg)
	{
		if (channel == null || !channel.isActive())
			return false;
		channel.writeAndFlush(msg);
		return true;
	}

	@Override
	public boolean sendSyncMessage(Object msg)
	{
		if (channel == null || !channel.isActive())
			return false;
		try
		{
			ChannelFuture future = channel.writeAndFlush(msg).sync();
			return future.isSuccess();
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void close()
	{
		if (channel != null)
			channel.close();
		GridSessionFactory.removeSessionCache(channel);
	}

	@Override
	public boolean isActive()
	{
		if (channel == null || !channel.isActive())
			return false;
		else
			return true;
	}

	@Override
	public SocketAddress remoteAddress()
	{
		if (channel == null)
			return null;
		return channel.remoteAddress();
	}


	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((channel == null) ? 0 : channel.hashCode());
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
		GridRemoteSession other = (GridRemoteSession) obj;
		if (channel == null)
		{
			if (other.channel != null)
				return false;
		}
		else if (!channel.equals(other.channel))
			return false;
		return true;
	}
	
}
