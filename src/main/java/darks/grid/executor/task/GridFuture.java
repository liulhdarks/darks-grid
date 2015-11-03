package darks.grid.executor.task;

import java.util.List;
import java.util.concurrent.TimeUnit;

public abstract class GridFuture<V>
{

	public abstract boolean isSuccess();

	public abstract boolean isCanceled();
	
	public abstract boolean await();
	
	public abstract boolean await(int timeout, TimeUnit unit);
	
	public V get()
	{
		List<V> list = getList();
		if (list != null && !list.isEmpty())
			return list.get(0);
		else
			return null;
	}
	
	public abstract List<V> getList();
}
