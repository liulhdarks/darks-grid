package darks.grid.executor.job;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicReference;

import darks.grid.beans.GridNode;

public class GridJobStatus implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6145756002284239163L;
	
	private GridJob job;

	private GridNode node;
	
	private AtomicReference<JobStatusType> statusType = new AtomicReference<JobStatusType>(JobStatusType.WAITING);
	
	JobResult result = null;
	
	public GridJobStatus(GridJob job, GridNode node)
	{
		this.node = node;
		this.job = job;
		this.result = new JobResult(node);
	}

	public GridNode getNode()
	{
		return node;
	}

	public void setNode(GridNode node)
	{
		this.node = node;
	}

	public GridJob getJob()
	{
		return job;
	}

	public void setJob(GridJob job)
	{
		this.job = job;
	}

	public JobStatusType getStatusType()
	{
		return statusType.get();
	}

	public void setStatusType(JobStatusType statusType)
	{
		this.statusType.getAndSet(statusType);
	}

	public JobResult getResult()
	{
		return result;
	}

    public void setResult(JobResult result)
    {
        this.result = result;
    }

    @Override
    public String toString()
    {
        return "MRJobStatus [job=" + job + ", node=" + node + ", statusType=" + statusType + ", result=" + result + "]";
    }
	
	
	
}
