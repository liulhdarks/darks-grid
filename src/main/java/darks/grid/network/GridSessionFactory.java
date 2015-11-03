package darks.grid.network;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.channel.Channel;

public class GridSessionFactory
{
	
	private static Map<Channel, SoftReference<GridSession>> sessionCache = new ConcurrentHashMap<>();
	
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
