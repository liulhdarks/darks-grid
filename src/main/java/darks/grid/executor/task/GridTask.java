package darks.grid.executor.task;

import darks.grid.executor.job.GridJob;
import darks.grid.utils.UUIDUtils;

public abstract class GridTask
{

    public enum TaskType
    {
        RPC, MAPRED
    }
    
    private String id = UUIDUtils.newUUID();
    
    private TaskType taskType;
    
    public GridTask(TaskType taskType)
    {
        this.id = UUIDUtils.newUUID();
        this.taskType = taskType;
    }
    
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
    
}
