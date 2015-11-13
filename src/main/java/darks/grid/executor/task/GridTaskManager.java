/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
