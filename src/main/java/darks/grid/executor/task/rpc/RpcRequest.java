package darks.grid.executor.task.rpc;

import java.io.Serializable;
import java.util.Arrays;

public class RpcRequest implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -8107745899221499625L;

	private String uniqueName;
	
	private Object[] params;

    private Class<?>[] types;
	
	public RpcRequest()
	{
		
	}



    public RpcRequest(String uniqueName, Object[] params, Class<?>[] types)
    {
        super();
        this.uniqueName = uniqueName;
        this.params = params;
        this.types = types;
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

    public Class<?>[] getTypes()
    {
        return types;
    }

    public void setTypes(Class<?>[] types)
    {
        this.types = types;
    }


    @Override
    public String toString()
    {
        return "RpcRequest [uniqueName=" + uniqueName + ", params=" + Arrays.toString(params) + ", types="
            + Arrays.toString(types) + "]";
    }

}
