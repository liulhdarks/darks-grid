package darks.grid.beans.meta;

public class MethodResponse extends BaseMeta
{
	
	private static final long serialVersionUID = 4108563313452408686L;

	public static final int ERR_NO_METHOD = 101;

    public static final int ERR_INVALID_OBJANDCLASS = 102;

    public static final int ERR_INSTANCE_CLASS_FAIL = 103;

    public static final int ERR_INVOKE_EXCEPTION = 104;

    public static final int ERR_GET_CLASS_METHOD = 105;
	
	private Object result;
	
	private boolean success = true;
	
	private String errorMessage;
	
	private int errorCode;
	
	public MethodResponse()
	{
		
	}

	public MethodResponse(Object result)
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

    public MethodResponse failed()
	{
		success = false;
		return this;
	}
	
	public MethodResponse failed(int errorCode, String errorMessage)
	{
	    this.errorCode = errorCode;
		this.errorMessage = errorMessage;
		this.success = false;
		return this;
	}
}
