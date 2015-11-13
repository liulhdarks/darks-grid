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
