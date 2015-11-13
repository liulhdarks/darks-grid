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
package darks.grid.executor.task.mapred;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.executor.ExecuteConfig;
import darks.grid.executor.ExecuteConfig.CallType;
import darks.grid.executor.ExecuteConfig.ResponseType;
import darks.grid.executor.job.GridJob;
import darks.grid.executor.job.GridJobStatus;
import darks.grid.executor.job.JobResult;
import darks.grid.executor.job.JobStatusType;
import darks.grid.executor.task.GridTask.TaskType;
import darks.grid.executor.task.TaskExecutor;
import darks.grid.executor.task.TaskResultListener;

public class MapReduceExecutor<T, R> extends TaskExecutor<T, R>
{

    private TaskResultListener listener;
    
    public MapReduceExecutor(MapReduceTask<T, R> task,
        T paramters, ExecuteConfig config, TaskResultListener listener)
    {
        super(TaskType.MAPRED, task, paramters, config);
        this.listener = listener;
    }

    @Override
    protected R execute()
    {
        ExecuteConfig config = getConfig();
        List<GridNode> nodesList = GridRuntime.nodes().getSnapshotNodes();
        task.initialize(nodesList, config.getBalance());
        int jobCount = nodesList.size();
        if (config.getCallType() == CallType.SINGLE)
            jobCount = 1;
        Collection<? extends GridJob> jobs = task.split(jobCount, paramters);
        for (GridJob job : jobs)
        {
            job.setTask(task, config);
            executeJob(job);
        }
        if (config.getResponseType() == ResponseType.NONE)
        	return null;
        future.await(config.getTimeout(), TimeUnit.MILLISECONDS);
        GridRuntime.tasks().completeTask(getTaskId());
        List<JobResult> jobResults = future.getList();
        R ret = task.reduce(jobResults);
        if (listener != null)
            listener.handle(ret);
        return ret;
    }

    @Override
    public void failRedo(GridJob job)
    {
        executeJob(job);
    }

    private boolean executeJob(GridJob job)
    {
        GridNode node = task.map(job);
        if (node != null)
            executeJobOnNode(node, job);
        return true;
    }

    private boolean executeJobOnNode(GridNode node, GridJob job)
    {
        GridJobStatus status = new GridJobStatus(job, node);
        status.setStatusType(JobStatusType.DOING);
        future.addJobStatus(status);
        GridRuntime.jobs().addNodeJob(node, job);
        GridMessage message = new GridMessage(job, GridMessage.MSG_MR_REQUEST);
        boolean ret = node.sendSyncMessage(message);
        if (!ret)
        {
            future.removeJobStatus(job.getJobId());
            GridRuntime.jobs().removeNodeJob(node.getId(), job.getJobId());
        }
        return ret;
    }

}
