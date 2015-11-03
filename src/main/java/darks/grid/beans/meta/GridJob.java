package darks.grid.beans.meta;

public class GridJob extends BaseMeta
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6904973711082130126L;

	private String taskId;
	
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

	@Override
	public String toString()
	{
		return "JobMeta [jobId=" + getJobId() + ", taskId=" + taskId + "]";
	}

	
	
}
