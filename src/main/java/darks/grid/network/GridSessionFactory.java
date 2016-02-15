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

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.channel.Channel;

public class GridSessionFactory
{
	
	private static Map<Channel, SoftReference<GridSession>> sessionCache = new ConcurrentHashMap<Channel, SoftReference<GridSession>>();
	
	private static Lock lock = new ReentrantLock();

	public static GridSession getSession(Channel channel)
	{
		GridSession session = null;
		SoftReference<GridSession> ref = sessionCache.get(channel);
		if (ref == null || ref.get() == null)
		{
			lock.lock();
			try
			{
				if (ref == null || ref.get() == null)
				{
					session = new GridRemoteSession(channel);
					sessionCache.put(channel, new SoftReference<GridSession>(session));
					return session;
				}
				else
				{
					return ref.get();
				}
			}
			finally
			{
				lock.unlock();
			}
		}
		else
			return ref.get();
	}

	public static GridSession getLocalSession(Channel channel)
	{
		return new GridLocalSession(channel);
	}
	
	public static void removeSessionCache(Channel channel)
	{
		sessionCache.remove(channel);
	}
	
}
