package darks.grid.utils;

import java.lang.ref.SoftReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class SyncPool
{
	
	static Map<Object, SoftReference<Object>> syncMap = new ConcurrentHashMap<Object, SoftReference<Object>>();
	
	static Map<Object, SoftReference<Lock>> lockMap = new ConcurrentHashMap<Object, SoftReference<Lock>>();
	
	static Object internMutex = new Object();
	
	private SyncPool()
	{
		
	}
	
	public static Object mutex(Object obj)
	{
		SoftReference<Object> mutex = syncMap.get(obj);
		if (mutex == null || mutex.get() == null)
		{
			synchronized (internMutex)
			{
				mutex = syncMap.get(obj);
				if (mutex == null || mutex.get() == null)
				{
					mutex = new SoftReference<Object>(new Object());
					syncMap.put(obj, mutex);
				}
			}
		}
		Object ret = mutex.get();
		return ret == null ? new Object() : ret;
	}
	
	public static Lock lock(Object obj)
	{
		SoftReference<Lock> mutex = lockMap.get(obj);
		if (mutex == null || mutex.get() == null)
		{
			synchronized (internMutex)
			{
				mutex = lockMap.get(obj);
				if (mutex == null || mutex.get() == null)
				{
					mutex = new SoftReference<Lock>(new ReentrantLock());
					lockMap.put(obj, mutex);
				}
			}
		}
		Lock ret = mutex.get();
		return ret == null ? new ReentrantLock() : ret;
	}
}
