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

	private String uniqueName;
	
	private Object[] params;
	
	private MethodConfig methodConfig;
	
	public MethodRequest()
	{
		
	}



    public MethodRequest(String uniqueName, Object[] params, MethodConfig methodConfig)
    {
        super();
        this.uniqueName = uniqueName;
        this.params = params;
        this.methodConfig = methodConfig;
    }

    
    
    public String getUniqueName()
	{
		return uniqueName;
	}



	public void setUniqueName(String uniqueName)
	{
		this.uniqueName = uniqueName;
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
		return "MethodRequest [uniqueName=" + uniqueName + ", params=" + Arrays.toString(params)
				+ ", methodConfig=" + methodConfig + "]";
	}


}
