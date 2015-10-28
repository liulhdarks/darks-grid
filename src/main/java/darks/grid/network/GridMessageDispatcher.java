package darks.grid.network;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GridMessageDispatcher
{
	
	private static final Logger log = LoggerFactory.getLogger(GridMessageDispatcher.class);

	protected Channel channel = null;
	
	public boolean initialize()
	{
		return true;
	}
	
	public boolean destroy()
	{
		if (channel != null)
		{
			channel.closeFuture();
		}
		return true;
	}

	public void send(String content)
	{
		send(content, false);
	}

	public boolean send(String content, boolean sync)
	{
		if (channel == null)
			return false;
		try
		{
			ChannelFuture future = channel.writeAndFlush(content);
			if (sync)
				return future.sync().isSuccess();
			return true;
		}
		catch (InterruptedException e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
	}

	public Channel getChannel()
	{
		return channel;
	}

	public void setChannel(Channel channel)
	{
		this.channel = channel;
	}
	
}
