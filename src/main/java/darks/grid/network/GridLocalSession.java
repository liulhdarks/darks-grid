package darks.grid.network;

import java.net.SocketAddress;

import darks.grid.GridRuntime;
import io.netty.channel.Channel;

public class GridLocalSession implements GridSession
{
	
	Channel channel;
	
	public GridLocalSession(Channel channel)
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
		GridRuntime.local().offerMessage(msg);
		return true;
	}

	@Override
	public boolean sendSyncMessage(Object msg)
	{
		GridRuntime.local().offerMessage(msg);
		return true;
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
