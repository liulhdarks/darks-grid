package darks.grid.executor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Future;

import darks.grid.executor.task.GridTask;
import darks.grid.utils.ThreadUtils;

public abstract class GridExecutor
{

    static Map<String, GridTask<?>> tasksMap = new ConcurrentHashMap<String, GridTask<?>>();
	
    public static <V> Future<V> executeTask(GridTask<V> task)
    {
        tasksMap.put(task.getId(), task);
        return ThreadUtils.submitTask(task);
    }
    
    public static void completeTask(String taskId)
    {
        tasksMap.remove(taskId);
    }
}
