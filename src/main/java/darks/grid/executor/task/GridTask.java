package darks.grid.executor.task;

import java.util.concurrent.Callable;

import darks.grid.beans.meta.GridJob;
import darks.grid.utils.UUIDUtils;

public abstract class GridTask<V> implements Callable<V>
{

    public enum TaskType
    {
        RPC, MAPRED
    }
    
    private String id;
    
    private TaskType taskType;
    
    public GridTask(TaskType taskType)
    {
        this.id = UUIDUtils.newUUID();
        this.taskType = taskType;
    }
    
    @Override
    public V call()
        throws Exception
    {
        return execute();
    }

    protected abstract V execute();
    
    protected boolean failRedo(GridJob job)
	{
		return true;
	}

    public String getId()
    {
        return id;
    }

    public TaskType getTaskType()
    {
        return taskType;
    }
    
    public String toSimpleString()
    {
    	return id;
    }
    
}
