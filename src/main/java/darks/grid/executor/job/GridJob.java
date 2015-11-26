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
package darks.grid.executor.job;

import java.io.Serializable;
import java.util.List;

import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.ExecuteConfig.ResponseType;
import darks.grid.executor.task.GridTask;
import darks.grid.executor.task.GridTask.TaskType;
import darks.grid.utils.UUIDUtils;

public abstract class GridJob implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6904973711082130126L;

	private String jobId = UUIDUtils.newUUID();

    private String taskId;
	
	private TaskType taskType;
	
	private boolean failRedo = true;
	
	private boolean callback = true;
	
	private int timeout = 0; 
	
	public GridJob()
	{
		
	}

    public void setTask(GridTask task, ExecuteConfig config)
    {
        this.taskId = task.getId();
        this.taskType = task.getTaskType();
        this.callback = config.getResponseType() != ResponseType.NONE;
        this.timeout = config.getTimeout();
    }
    
    public abstract Object execute();
    
    public abstract <P> P getParameter();
    
    public abstract <P> P getParameter(int pos);
    
    public abstract List<Object> getParameterList();
    
    public abstract boolean isEmpty();

	public String getTaskId()
	{
		return taskId;
	}

	public void setTaskId(String taskId)
	{
		this.taskId = taskId;
	}
	
	public String getJobId()
	{
		return jobId;
	}

	public boolean isFailRedo()
	{
		return failRedo;
	}

	public void setFailRedo(boolean failRedo)
	{
		this.failRedo = failRedo;
	}

	public TaskType getTaskType()
    {
        return taskType;
    }

    public void setTaskType(TaskType taskType)
    {
        this.taskType = taskType;
    }
    
    public boolean isCallback()
    {
        return callback;
    }

    public void setCallback(boolean callback)
    {
        this.callback = callback;
    }

    public int getTimeout()
	{
		return timeout;
	}

	public void setTimeout(int timeout)
	{
		this.timeout = timeout;
	}

	@Override
	public String toString()
	{
		return "GridJob [jobId=" + jobId + ", taskId=" + taskId + ", taskType=" + taskType
				+ ", failRedo=" + failRedo + ", callback=" + callback + ", timeout=" + timeout
				+ "]";
	}

}
