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
package darks.grid.executor.task;

import java.util.concurrent.Callable;

import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.job.GridJob;
import darks.grid.executor.task.GridTask.TaskType;
import darks.grid.executor.task.mapred.MapReduceTask;

public abstract class TaskExecutor<T, R>  implements Callable<R>
{
    
    protected String taskId;
    
    protected TaskType taskType;
    
    protected MapReduceTask<T, R> task;
    
    protected T paramters;
    
    protected GridJobFuture future = null; 
    
    private ExecuteConfig config;
    
    public TaskExecutor(TaskType taskType, MapReduceTask<T, R> task, T paramters, ExecuteConfig config)
    {
        this.taskType = taskType;
        this.task = task;
        this.paramters = paramters;
        this.taskId = task.getId();
        this.future = new GridJobFuture(this);
        this.config = config;
    }
    
    @Override
    public R call()
        throws Exception
    {
        return execute();
    }

    protected abstract R execute();
    
    public void failRedo(GridJob job)
    {
        
    }
    
    public void replyJob(GridJobReply reply)
    {
        future.replyStatus(reply);
    }
    
    public void signalStatusCheck()
    {
    	future.signalStatusCheck();
    }

    public TaskType getTaskType()
    {
        return taskType;
    }

    public String getTaskId()
    {
        return taskId;
    }

    public GridJobFuture getFuture()
    {
        return future;
    }
    
    
    public ExecuteConfig getConfig()
    {
        return config;
    }

    public String toSimpleString()
    {
        StringBuilder buf = new StringBuilder();
        buf.append("Task:").append(getTaskId()).append('\n');
        buf.append(future.toSimpleString("     "));
        return buf.toString();
    }
}
