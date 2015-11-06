package darks.grid.executor.task.rpc;

import java.util.Arrays;

import darks.grid.beans.meta.GridJob;
import darks.grid.config.MethodConfig;

public class MethodJob extends GridJob
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1781136302155242623L;

	private String uniqueName;
	
	private Object[] params;
	
	private MethodConfig methodConfig;
	
	public MethodJob()
	{
		setFailRedo(false);
	}
	
	public MethodJob(String taskId, MethodRequest request)
	{
		super(taskId);
		this.uniqueName = request.getUniqueName();
		this.params = request.getParams();
		this.methodConfig = request.getMethodConfig();
		setFailRedo(false);
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
		return "MethodJob [uniqueName=" + uniqueName + ", params=" + Arrays.toString(params)
				+ ", methodConfig=" + methodConfig + "]";
	}


	
}
