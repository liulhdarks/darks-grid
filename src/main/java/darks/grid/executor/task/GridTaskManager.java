package darks.grid.executor.task;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.FutureTask;

import darks.grid.GridManager;
import darks.grid.config.GridConfiguration;
import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.task.mapred.MapReduceExecutor;
import darks.grid.executor.task.mapred.MapReduceTask;
import darks.grid.utils.GridStatistic;
import darks.grid.utils.ThreadUtils;

public class GridTaskManager implements GridManager
{

	Map<String, TaskExecutor<?, ?>> doingTasksMap = new ConcurrentHashMap<>();

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

	public <T, R> FutureTask<R> executeMapReduceTask(MapReduceTask<T, R> task, T args, 
	        ExecuteConfig config, TaskResultListener listener)
	{
		GridStatistic.incrementTaskCount();
	    TaskExecutor<T, R> executor = new MapReduceExecutor<T, R>(task, args, config, listener);
		doingTasksMap.put(task.getId(), executor);
		return (FutureTask<R>) ThreadUtils.submitTask(executor);
	}

	public void completeTask(String taskId)
	{
		doingTasksMap.remove(taskId);
	}
	
	public TaskExecutor<?, ?> getTaskExecutor(String taskId)
	{
		return doingTasksMap.get(taskId);
	}
	
	public String toSimgleString()
	{
		StringBuilder buf = new StringBuilder();
		for (Entry<String, TaskExecutor<?, ?>> entry : doingTasksMap.entrySet())
		{
			buf.append(entry.getValue().toSimpleString().trim()).append('\n');
		}
		return buf.toString();
	}
}
