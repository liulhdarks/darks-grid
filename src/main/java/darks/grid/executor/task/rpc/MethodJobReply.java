package darks.grid.executor.task.rpc;

import darks.grid.beans.meta.BaseMeta;

public class MethodJobReply extends BaseMeta
{
	
	private static final long serialVersionUID = 4108563313452408686L;

	public static final int ERR_NO_METHOD = 101;

    public static final int ERR_INVALID_OBJANDCLASS = 102;

    public static final int ERR_INSTANCE_CLASS_FAIL = 103;

    public static final int ERR_INVOKE_EXCEPTION = 104;

    public static final int ERR_GET_CLASS_METHOD = 105;
    
    private String taskId;
    
    private String jobId;
	
	private Object result;
	
	private boolean success = true;
	
	private String errorMessage;
	
	private int errorCode;
	
	public MethodJobReply()
	{
		
	}
	
	public MethodJobReply(MethodJob job)
	{
		this.taskId = job.getTaskId();
		this.jobId = job.getJobId();
	}

	public MethodJobReply(Object result)
	{
		this.result = result;
	}

	public Object getResult()
	{
		return result;
	}

	public void setResult(Object result)
	{
		this.result = result;
	}

	public boolean isSuccess()
	{
		return success;
	}

	public void setSuccess(boolean success)
	{
		this.success = success;
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

    public MethodJobReply failed()
	{
		success = false;
		return this;
	}
	
	public MethodJobReply failed(int errorCode, String errorMessage)
	{
	    this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.success = false;
		return this;
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
		return jobId;
	}

	public void setJobId(String jobId)
	{
		this.jobId = jobId;
	}

	@Override
	public String toString()
	{
		return "MethodJobRps [taskId=" + taskId + ", jobId=" + jobId + ", result=" + result
				+ ", success=" + success + ", errorMessage=" + errorMessage + ", errorCode="
				+ errorCode + "]";
	}
	
}
