package darks.grid.spring;

import java.util.HashSet;
import java.util.Set;

import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.task.TaskResultListener;

public class RpcProxyBean
{
	ExecuteConfig config = new ExecuteConfig();
	
	Set<String> targetMethods = new HashSet<>();
	
	TaskResultListener taskResultListener;
	
	boolean asyncInvoke = false;
	
	public RpcProxyBean()
	{
		
	}

	public ExecuteConfig getConfig()
	{
		return config;
	}

	public void setConfig(ExecuteConfig config)
	{
		this.config = config;
	}

	public Set<String> getTargetMethods()
	{
		return targetMethods;
	}

	public void setTargetMethods(Set<String> targetMethods)
	{
		this.targetMethods = targetMethods;
	}

	public TaskResultListener getTaskResultListener()
	{
		return taskResultListener;
	}

	public void setTaskResultListener(TaskResultListener taskResultListener)
	{
		this.taskResultListener = taskResultListener;
	}
	

	public boolean isAsyncInvoke()
	{
		return asyncInvoke;
	}

	public void setAsyncInvoke(boolean asyncInvoke)
	{
		this.asyncInvoke = asyncInvoke;
	}

	@Override
	public String toString()
	{
		return "ProxyBean [config=" + config + ", targetMethods=" + targetMethods
				+ ", taskResultListener=" + taskResultListener + ", asyncInvoke=" + asyncInvoke + "]";
	}

}
