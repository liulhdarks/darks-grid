package darks.grid.executor.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.beans.JobStatus.JobStatusType;
import darks.grid.beans.meta.GridJob;
import darks.grid.network.GridSession;

public abstract class JobExecutor implements Runnable
{
	
	private static final Logger log = LoggerFactory.getLogger(JobExecutor.class);
	
	private GridSession session;
	
	private GridMessage msg;
	
	private GridJob job;
	
	private JobStatusType statusType;
	
	public JobExecutor(GridSession session, GridMessage msg)
	{
		this.session = session;
		this.msg = msg;
		this.job = msg.getData();
	}

	@Override
	public void run()
	{
		try
		{
			statusType = JobStatusType.DOING;
			execute(session, msg, job);
			statusType = JobStatusType.SUCCESS;
		}
		catch (Exception e)
		{
			statusType = JobStatusType.FAIL;
			log.error(e.getMessage(), e);
		}
		GridRuntime.jobs().completeExecuteJob(this);
	}

	public abstract void execute(GridSession session, GridMessage msg, GridJob job) throws Exception;
	
	public String getTaskId()
	{
		return job.getTaskId();
	}
	
	public String getJobId()
	{
		return job.getJobId();
	}

	public JobStatusType getStatusType()
	{
		return statusType;
	}

	public void setStatusType(JobStatusType statusType)
	{
		this.statusType = statusType;
	}
	
}
