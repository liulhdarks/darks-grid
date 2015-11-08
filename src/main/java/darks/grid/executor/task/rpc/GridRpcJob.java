package darks.grid.executor.task.rpc;

import java.util.Arrays;

import darks.grid.executor.job.GridJobAdapter;

public class GridRpcJob extends GridJobAdapter
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1781136302155242623L;

	private String uniqueName;
	
	private Object[] params;

    private Class<?>[] types;
	
	public GridRpcJob()
	{
		setFailRedo(false);
	}
	
	public GridRpcJob(RpcRequest request)
	{
		this.uniqueName = request.getUniqueName();
		this.params = request.getParams();
        this.types = request.getTypes();
		setFailRedo(false);
	}
	
	
	@Override
    public Object execute()
    {
	    return RpcMethodInvoker.invoke(this);
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
        return "GridRpcJob [uniqueName=" + uniqueName + ", params=" + Arrays.toString(params) + ", types="
            + Arrays.toString(types) + "]";
    }
    
}
