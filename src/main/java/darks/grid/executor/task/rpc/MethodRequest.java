package darks.grid.executor.task.rpc;

import java.io.Serializable;
import java.util.Arrays;

import darks.grid.config.MethodConfig;

public class MethodRequest implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8107745899221499625L;

	private String methodName;
	
	private Object[] params;
	
	private MethodConfig methodConfig;
	
	public MethodRequest()
	{
		
	}



    public MethodRequest(String methodName, Object[] params, MethodConfig methodConfig)
    {
        super();
        this.methodName = methodName;
        this.params = params;
        this.methodConfig = methodConfig;
    }


    public String getMethodName()
	{
		return methodName;
	}

	public void setMethodName(String methodName)
	{
		this.methodName = methodName;
	}

    public Object[] getParams()
    {
        return params;
    }

    public void setParams(Object[] params)
    {
        this.params = params;
    }



    public MethodConfig getMethodConfig()
    {
        return methodConfig;
    }



    public void setMethodConfig(MethodConfig methodConfig)
    {
        this.methodConfig = methodConfig;
    }



    @Override
    public String toString()
    {
        return "MethodRequest [methodName=" + methodName + ", params=" + Arrays.toString(params) + ", methodConfig="
            + methodConfig + "]";
    }

}
