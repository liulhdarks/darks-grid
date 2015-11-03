package darks.grid.utils;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public final class ThreadUtils
{
	
	private static ExecutorService threalPool = Executors.newCachedThreadPool();
	
	private ThreadUtils()
	{
		
	}
	
	public static void executeThread(Runnable runnable)
	{
		threalPool.execute(runnable);
	}
    
    public static <V> FutureTask<V> submitTask(Callable<V> callable)
    {
        return (FutureTask<V>) threalPool.submit(callable);
    }

	public static void threadSleep(long time)
	{
		try
		{
			Thread.sleep(time);
		}
		catch (InterruptedException e)
		{
		}
	}
	
	public static void shutdownAll()
	{
		try
		{
			threalPool.shutdownNow();
		}
		catch (Exception e)
		{
		}
	}

	public static ExecutorService getThrealPool()
	{
		return threalPool;
	}
	
	
	
}
