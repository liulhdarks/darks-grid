package darks.grid.config;

public class TaskConfig
{
	
	private int jobsExecCount = Runtime.getRuntime().availableProcessors() * 5;

	public TaskConfig() 
	{
		
	}

	public int getJobsExecCount()
	{
		return jobsExecCount;
	}

	public void setJobsExecCount(int jobsExecCount)
	{
		this.jobsExecCount = jobsExecCount;
	}

	@Override
	public String toString()
	{
		return "TaskConfig [jobsExecCount=" + jobsExecCount + "]";
	}
	
	
}
