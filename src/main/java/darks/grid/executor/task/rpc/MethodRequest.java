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

    private Class<?>[] types;
	
	private MethodConfig methodConfig;
	
	public MethodRequest()
	{
		
	}



    public MethodRequest(String uniqueName, Object[] params, Class<?>[] types, MethodConfig methodConfig)
    {
        super();
        this.uniqueName = uniqueName;
        this.params = params;
        this.types = types;
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

    public Class<?>[] getTypes()
    {
        return types;
    }

    public void setTypes(Class<?>[] types)
    {
        this.types = types;
    }

    @Override
    public String toString() {
        return "MethodRequest{" +
                "uniqueName='" + uniqueName + '\'' +
                ", params=" + Arrays.toString(params) +
                ", types=" + Arrays.toString(types) +
                ", methodConfig=" + methodConfig +
                '}';
    }
}
