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
package darks.grid.executor.task.rpc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import darks.grid.executor.job.GridJob;
import darks.grid.executor.job.JobResult;
import darks.grid.executor.task.mapred.MapReduceTaskAdapter;

public class GridRpcTask extends MapReduceTaskAdapter<RpcRequest, RpcResult>
{
     
    public GridRpcTask()
    {
        super(TaskType.RPC);
    }

    @Override
    public Collection<? extends GridJob> split(int jobsCount, RpcRequest request)
    {
        List<GridRpcJob> jobs = new ArrayList<GridRpcJob>(jobsCount);
        for (int i = 0; i < jobsCount; i++)
        {
            GridRpcJob job = new GridRpcJob(request);
            jobs.add(job);
        }
        return jobs;
    }

    @Override
    public RpcResult reduce(List<JobResult> results)
    {
        StringBuilder errorBuf = new StringBuilder();
        RpcResult result = new RpcResult();
        List<Object> objs = new ArrayList<Object>(results.size());
        List<String> nodeIds = new ArrayList<String>(results.size());
        for (JobResult jobRet : results)
        {
            if (!jobRet.isSuccess())
            {
                errorBuf.append("ErrorCode ").append(jobRet.getErrorCode())
                    .append(" on ").append(jobRet.getNode().getId()).append(" msg:")
                    .append(jobRet.getErrorMessage()).append('\n');
            }
            else
            {
                objs.add(jobRet.getObject());
                nodeIds.add(jobRet.getNode().getId());
            }
        }
        result.setResult(objs);
        result.setNodeIds(nodeIds);
        if (errorBuf.length() > 0)
        {
            result.setErrorMessage(errorBuf.toString());
            result.setSuccess(false);
        }
        return result;
    }
    
}
