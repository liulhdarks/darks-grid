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
	
	static AtomicLong taskCount = new AtomicLong(0);
	static AtomicLong jobCount = new AtomicLong(0);
	
	private GridStatistic()
	{
		
	}
	
	public static void incrementDelay(long delay)
	{
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
}
