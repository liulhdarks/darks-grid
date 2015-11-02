package darks.grid.beans;

import java.io.Serializable;

public class MethodResponse implements Serializable
{
	
	private static final long serialVersionUID = 4108563313452408686L;

	private Object result;
	
	private boolean success = true;
	
	private String errorMessage;
	
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
	
	public MethodResponse failed()
	{
		success = false;
		return this;
	}
	
	public MethodResponse failed(String errorMessage)
	{
		this.errorMessage = errorMessage;
		this.success = false;
		return this;
	}
}
