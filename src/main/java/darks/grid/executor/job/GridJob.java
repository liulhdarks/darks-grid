package darks.grid.executor.job;

import darks.grid.beans.meta.BaseMeta;
import darks.grid.executor.task.GridTask;
import darks.grid.executor.task.GridTask.TaskType;

public class GridJob extends BaseMeta
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6904973711082130126L;

	private String taskId;
	
	private TaskType taskType;
	
	private boolean failRedo = true;
	
	public GridJob()
	{
		
	}

	public GridJob(GridTask task)
	{
		this.taskId = task.getId();
		this.taskType = task.getTaskType();
	}

    public void setTask(GridTask task)
    {
        this.taskId = task.getId();
        this.taskType = task.getTaskType();
    }

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
	
	public String getJobId()
	{
		return getMetaId();
	}
	

	public boolean isFailRedo()
	{
		return failRedo;
	}

	public void setFailRedo(boolean failRedo)
	{
		this.failRedo = failRedo;
	}

	public TaskType getTaskType()
    {
        return taskType;
    }

    public void setTaskType(TaskType taskType)
    {
        this.taskType = taskType;
    }

    @Override
    public String toString()
    {
        return "GridJob [taskId=" + taskId + ", taskType=" + taskType + ", failRedo=" + failRedo + "]";
    }

	
}
