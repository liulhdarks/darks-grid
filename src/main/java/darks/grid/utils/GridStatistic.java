package darks.grid.utils;

import java.util.concurrent.atomic.AtomicLong;

public final class GridStatistic
{

	static AtomicLong messageMaxDelay = new AtomicLong(0);
	static AtomicLong messageDelay = new AtomicLong(0);
	static AtomicLong messageCount = new AtomicLong(0);

	static AtomicLong messageLocalMaxDelay = new AtomicLong(0);
	static AtomicLong messageLocalDelay = new AtomicLong(0);
	static AtomicLong messageLocalCount = new AtomicLong(0);
	
	static AtomicLong jobWaitDelay = new AtomicLong(0);
	static AtomicLong jobWaitDelayCount = new AtomicLong(0);
	static AtomicLong jobWaitMaxDelay = new AtomicLong(0);
	
	static AtomicLong jobWaitCount = new AtomicLong(0);
	
	static AtomicLong taskCount = new AtomicLong(0);
	static AtomicLong jobCount = new AtomicLong(0);
	
	private GridStatistic()
	{
		
	}
	
	public static void addWaitJobCount(long delta)
	{
		jobWaitCount.getAndAdd(delta);
	}
	
	public static void incrementJobDelay(long delay)
	{
		if (delay < 0)
			return;
		jobWaitDelay.getAndAdd(delay);
		jobWaitDelayCount.getAndIncrement();
		while (true) {
            long current = jobWaitMaxDelay.get();
            long next = Math.max(delay, current);
            if (jobWaitMaxDelay.compareAndSet(current, next))
                break;
        }
	}
	
	public static void incrementDelay(long delay)
	{
		if (delay < 0)
			return;
		messageDelay.getAndAdd(delay);
		messageCount.getAndIncrement();
		while (true) {
            long current = messageMaxDelay.get();
            long next = Math.max(delay, current);
            if (messageMaxDelay.compareAndSet(current, next))
                break;
        }
	}
	
	public static void incrementLocalDelay(long delay)
	{
		if (delay < 0)
			return;
		messageLocalDelay.getAndAdd(delay);
		messageLocalCount.getAndIncrement();
		while (true) {
            long current = messageLocalMaxDelay.get();
            long next = Math.max(delay, current);
            if (messageLocalMaxDelay.compareAndSet(current, next))
                break;
        }
	}
	
	public static void incrementTaskCount()
	{
		taskCount.getAndIncrement();
	}
	
	public static void incrementJobCount()
	{
		jobCount.getAndIncrement();
	}
	
	public static long getAvgLocalDelay()
	{
		long delay = messageLocalDelay.get();
		long count = messageLocalCount.get();
		return count == 0 ? 0 : (delay / count);
	}
	
	public static long getMaxLocalDelay()
	{
		return messageLocalMaxDelay.get();
	}
	
	public static long getAvgDelay()
	{
		long delay = messageDelay.get();
		long count = messageCount.get();
		return count == 0 ? 0 : (delay / count);
	}
	
	public static long getMaxDelay()
	{
		return messageMaxDelay.get();
	}
	
	public static long getJobCount()
	{
		return jobCount.get();
	}
	
	public static long getTaskCount()
	{
		return taskCount.get();
	}
	
	public static long getAvgJobWaitDelay()
	{
		long delay = jobWaitDelay.get();
		long count = jobWaitDelayCount.get();
		return count == 0 ? 0 : (delay / count);
	}
	
	public static long getMaxJobWaitDelay()
	{
		return jobWaitMaxDelay.get();
	}
	
	public static long getCurJobWaitCount()
	{
		return jobWaitCount.get();
	}
}
