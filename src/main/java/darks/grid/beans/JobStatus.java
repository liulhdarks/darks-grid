package darks.grid.beans;

import java.io.Serializable;

import darks.grid.beans.meta.GridJob;

public class JobStatus implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6145756002284239163L;
	
	public enum JobStatusType
	{
		WAITING, DOING, FAIL, SUCCESS
	}
	
	private Object result;
	
	private GridJob job;

	private GridNode node;
	
	private JobStatusType statusType = JobStatusType.WAITING;
	
	private String errorMessage;
	
	private int errorCode;
	
	public JobStatus(GridJob job, GridNode node)
	{
		this.node = node;
		this.job = job;
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
		return statusType;
	}

	public void setStatusType(JobStatusType statusType)
	{
		this.statusType = statusType;
	}

	public Object getResult()
	{
		return result;
	}

	public void setResult(Object result)
	{
		this.result = result;
	}

	
	public String getErrorMessage()
	{
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage)
	{
		this.errorMessage = errorMessage;
	}

	public int getErrorCode()
	{
		return errorCode;
	}

	public void setErrorCode(int errorCode)
	{
		this.errorCode = errorCode;
	}

	@Override
	public String toString()
	{
		return "JobStatus [result=" + result + ", job=" + job + ", node=" + node + ", statusType="
				+ statusType + "]";
	}
	
}
