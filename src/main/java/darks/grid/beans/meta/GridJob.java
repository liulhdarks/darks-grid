package darks.grid.beans.meta;

public class GridJob extends BaseMeta
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6904973711082130126L;

	private String taskId;
	
	private boolean failRedo = true;
	
	public GridJob()
	{
		
	}

	public GridJob(String taskId)
	{
		super();
		this.taskId = taskId;
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

	@Override
	public String toString()
	{
		return "GridJob [taskId=" + taskId + ", failRedo=" + failRedo + "]";
	}

	
}
