/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package darks.grid.spring;

import java.util.HashSet;
import java.util.Set;

import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.task.TaskResultListener;

public class RpcProxyBean
{
	ExecuteConfig config = new ExecuteConfig();
	
	Set<String> targetMethods = new HashSet<String>();
	
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
