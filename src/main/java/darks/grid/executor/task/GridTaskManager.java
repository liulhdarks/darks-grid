package darks.grid.executor.task;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

import darks.grid.GridManager;
import darks.grid.config.GridConfiguration;
import darks.grid.utils.ThreadUtils;

public class GridTaskManager implements GridManager
{

	Map<String, GridTask<?>> doingTasksMap = new ConcurrentHashMap<>();

	public GridTaskManager()
	{

	}

	@Override
	public boolean initialize(GridConfiguration config)
	{
		return true;
	}

	@Override
	public void destroy()
	{

	}

	public <V> FutureTask<V> executeTask(GridTask<V> task)
	{
		doingTasksMap.put(task.getId(), task);
		return (FutureTask<V>) ThreadUtils.submitTask(task);
	}

	public void completeTask(String taskId)
	{
		doingTasksMap.remove(taskId);
	}
	
	public GridTask<?> getTask(String taskId)
	{
		return doingTasksMap.get(taskId);
	}
	
	public String toSimgleString()
	{
		StringBuilder buf = new StringBuilder();
		for (Entry<String, GridTask<?>> entry : doingTasksMap.entrySet())
		{
			buf.append(entry.getValue().toSimpleString().trim()).append('\n');
		}
		return buf.toString();
	}
}
