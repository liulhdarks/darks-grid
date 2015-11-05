package darks.grid.network;

import io.netty.channel.Channel;

import java.net.SocketAddress;

import darks.grid.GridRuntime;

public class GridLocalSession implements GridSession
{
	
	Channel channel;
	
	int failRetryCount;
	
	public GridLocalSession(Channel channel)
	{
		this.channel = channel;
		failRetryCount = GridRuntime.config().getNetworkConfig().getSendFailRetry();
	}

	@Override
	public String getId()
	{
		return channel.id().asShortText();
	}

	@Override
	public boolean sendMessage(Object msg)
	{
		return GridRuntime.local().offerMessage(msg);
	}

	@Override
	public boolean sendSyncMessage(Object msg)
	{
		return sendSyncMessage(msg, true);
	}
	
	@Override
	public boolean sendSyncMessage(Object msg, boolean failRetry)
	{
		boolean ret = false;
		int count = 0;
		do
		{
			ret = GridRuntime.local().offerMessage(msg);
			if (ret)
				break;
			count++;
		}
		while (count < failRetryCount && failRetry);
		return ret;
	}

	@Override
	public void close()
	{
//		channel.close();
	}

	@Override
	public boolean isActive()
	{
		return true;
	}

	@Override
	public SocketAddress remoteAddress()
	{
		return GridRuntime.context().getServerAddress();
	}


}
