package darks.grid.utils;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public final class SyncPool
{
	
	static Map<Object, WeakReference<Object>> syncMap = new ConcurrentHashMap<Object, WeakReference<Object>>();
	
	static Map<Object, WeakReference<SyncStore<Lock>>> lockMap = new ConcurrentHashMap<Object, WeakReference<SyncStore<Lock>>>();
	
	static Object internMutex = new Object();
	
	private SyncPool()
	{
		
	}
	
	public static Object mutex(Object obj)
	{
		WeakReference<Object> mutex = syncMap.get(obj);
		if (mutex == null || mutex.get() == null)
		{
			synchronized (internMutex)
			{
				mutex = syncMap.get(obj);
				if (mutex == null || mutex.get() == null)
				{
					mutex = new WeakReference<Object>(new Object());
					syncMap.put(obj, mutex);
				}
			}
		}
		Object ret = mutex.get();
		return ret == null ? new Object() : ret;
	}
	
	public static Lock lock(Object obj)
	{
		return lock(obj, 0);
	}
	
	public static Lock lock(Object obj, long expire)
	{
		WeakReference<SyncStore<Lock>> mutex = lockMap.get(obj);
		if (mutex == null || mutex.get() == null || !mutex.get().isValid())
		{
			synchronized (internMutex)
			{
				mutex = lockMap.get(obj);
				if (mutex == null || mutex.get() == null || !mutex.get().isValid())
				{
					SyncStore<Lock> store = new SyncStore<Lock>(new ReentrantLock(), expire);
					mutex = new WeakReference<SyncStore<Lock>>(store);
					lockMap.put(obj, mutex);
				}
			}
		}
		SyncStore<Lock> ret = mutex.get();
		return (ret == null || !ret.isValid()) ? new ReentrantLock() : ret.lock;
	}
	
	static class SyncStore<T>
	{
		T lock;
		
		long expireTime = 0;
		
		long startTime = System.currentTimeMillis();
		
		public SyncStore(T lock)
		{
			this.lock = lock;
		}
		
		public SyncStore(T lock, long expireTime)
		{
			this.lock = lock;
			this.expireTime = expireTime;
		}
		
		public boolean isValid()
		{
			if (lock == null)
				return false;
			long dt = System.currentTimeMillis() - startTime;
			if (expireTime > 0 && dt > expireTime)
				return false;
			return true;
		}
	}
}
