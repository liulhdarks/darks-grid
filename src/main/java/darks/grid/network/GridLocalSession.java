/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package darks.grid.network;

import io.netty.channel.Channel;

import java.net.InetSocketAddress;

import darks.grid.GridRuntime;
import darks.grid.utils.ChannelUtils;
import darks.grid.utils.NetworkUtils;

public class GridLocalSession implements GridSession
{
	
	Channel channel;
	
	int failRetryCount;
	
	InetSocketAddress bindAddress;
	
	public GridLocalSession(Channel channel)
	{
		this.channel = channel;
		failRetryCount = GridRuntime.config().getNetworkConfig().getSendFailRetry();
	}

	@Override
	public String getId()
	{
		return ChannelUtils.getChannelId(channel);
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
			ret = GridRuntime.local().offerSyncMessage(msg);
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
		return channel.isActive();
	}

	@Override
	public InetSocketAddress remoteAddress()
	{
		return localAddress();
	}

	@Override
	public synchronized InetSocketAddress localAddress()
	{
		if (channel == null)
			return null;
		if (bindAddress == null)
		{
			String ipHost = NetworkUtils.getIpAddress();
			InetSocketAddress ipAddr = (InetSocketAddress) channel.localAddress();
			bindAddress = new InetSocketAddress(ipHost, ipAddr.getPort());
		}
		return bindAddress;
	}

	
}
