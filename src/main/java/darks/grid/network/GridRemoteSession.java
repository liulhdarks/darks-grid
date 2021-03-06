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

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridAddress;
import darks.grid.utils.ChannelUtils;

public class GridRemoteSession implements GridSession
{
    
    private static final Logger log = LoggerFactory.getLogger(GridRemoteSession.class);
	
	Channel channel;
	
	int failRetryCount = 3;

	public GridRemoteSession(Channel channel)
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
		if (channel == null || !channel.isActive() || !channel.isWritable())
			return false;
		try
        {
	        channel.writeAndFlush(msg);
	        return true;
        }
        catch (Exception e)
        {
            log.error("Fail to send remote message. Cause " + e.getMessage(), e);
            return false;
        }
	}

	@Override
	public boolean sendSyncMessage(Object msg)
	{
		return sendSyncMessage(msg, true);
	}
	

	@Override
	public boolean sendSyncMessage(Object msg, boolean failRetry)
	{
		if (channel == null || !channel.isActive())
			return false;
		try
		{
			boolean ret = false;
			int count = 0;
			do
			{
			    if (!channel.isActive() || !channel.isWritable())
			        return false;
				ChannelFuture future = channel.writeAndFlush(msg).sync();
				ret = future.isSuccess();
				if (log.isDebugEnabled())
					log.debug("Send " + msg + " to " + channel.remoteAddress() + " ret:" + ret);
				if (ret)
					break;
				count++;
			}
			while (count < failRetryCount && failRetry);
			return ret;
		}
		catch (Exception e)
		{
		    log.error("Fail to send remote sync message.[" + 
		    			channel.remoteAddress() + "/active:" + channel.isActive() + 
		    			"] Cause " + e.getMessage(), e);
			return false;
		}
	}
	

	@Override
    public boolean isLocal() {
        return false;
    }

    @Override
	public synchronized void close()
	{
		if (channel != null && channel.isActive() && channel.isOpen())
		{
		    log.warn("Close session channel " + channel.remoteAddress());
			channel.close();
		}
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
	public GridAddress remoteAddress()
	{
		if (channel == null)
			return null;
		return GridAddress.wrap((InetSocketAddress)channel.remoteAddress());
	}

	@Override
	public GridAddress localAddress()
	{
		if (channel == null)
			return null;
		return GridAddress.wrap((InetSocketAddress)channel.localAddress());
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
