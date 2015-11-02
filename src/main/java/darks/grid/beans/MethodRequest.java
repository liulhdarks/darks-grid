package darks.grid.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MethodRequest implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8107745899221499625L;

	private String methodName;
	
	private List<Object> params = new ArrayList<>();
	
	public MethodRequest()
	{
		
	}

	public String getMethodName()
	{
		return methodName;
	}

	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}

	public List<Object> getParams()
	{
		return params;
	}
	
}
