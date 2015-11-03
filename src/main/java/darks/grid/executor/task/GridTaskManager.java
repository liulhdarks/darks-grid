package darks.grid.executor.task;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

import darks.grid.config.GridConfiguration;
import darks.grid.utils.ThreadUtils;

public class GridTaskManager
{

	Map<String, GridTask<?>> doingTasksMap = new ConcurrentHashMap<>();

	public GridTaskManager()
	{

	}

	public boolean initialize(GridConfiguration config)
	{
		return true;
	}

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
}
