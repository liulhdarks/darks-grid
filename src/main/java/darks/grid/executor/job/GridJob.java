package darks.grid.executor.job;

import java.io.Serializable;
import java.util.List;

import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.ExecuteConfig.ResponseType;
import darks.grid.executor.task.GridTask;
import darks.grid.executor.task.GridTask.TaskType;
import darks.grid.utils.UUIDUtils;

public abstract class GridJob implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6904973711082130126L;

	private String jobId = UUIDUtils.newUUID();

    private String taskId;
	
	private TaskType taskType;
	
	private boolean failRedo = true;
	
	private boolean callback = true;
	
	public GridJob()
	{
		
	}

    public void setTask(GridTask task, ExecuteConfig config)
    {
        this.taskId = task.getId();
        this.taskType = task.getTaskType();
        this.callback = config.getResponseType() != ResponseType.NONE;
    }
    
    public abstract Object execute();
    
    public abstract <P> P getParameter();
    
    public abstract <P> P getParameter(int pos);
    
    public abstract List<Object> getParameterList();
    
    public abstract boolean isEmpty();

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
		return jobId;
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
    
    public boolean isCallback()
    {
        return callback;
    }

    public void setCallback(boolean callback)
    {
        this.callback = callback;
    }

    @Override
    public String toString()
    {
        return "GridJob [jobId=" + jobId + ", taskId=" + taskId + ", taskType=" + taskType + ", failRedo=" + failRedo
            + ", callback=" + callback + "]";
    }

}
