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
import java.util.concurrent.atomic.AtomicReference;

import darks.grid.beans.GridNode;

public class GridJobStatus implements Serializable
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -6145756002284239163L;
	
	private GridJob job;

	private GridNode node;
	
	private AtomicReference<JobStatusType> statusType = new AtomicReference<JobStatusType>(JobStatusType.WAITING);
	
	JobResult result = null;
	
	public GridJobStatus(GridJob job, GridNode node)
	{
		this.node = node;
		this.job = job;
		this.result = new JobResult(node);
	}

	public GridNode getNode()
	{
		return node;
	}

	public void setNode(GridNode node)
	{
		this.node = node;
	}

	public GridJob getJob()
	{
		return job;
	}

	public void setJob(GridJob job)
	{
		this.job = job;
	}

	public JobStatusType getStatusType()
	{
		return statusType.get();
	}

	public void setStatusType(JobStatusType statusType)
	{
		this.statusType.getAndSet(statusType);
	}

	public JobResult getResult()
	{
		return result;
	}

    public void setResult(JobResult result)
    {
        this.result = result;
    }

    @Override
    public String toString()
    {
        return "GridJobStatus [job=" + job + ", node=" + node + ", statusType=" + statusType + ", result=" + result + "]";
    }
	
	
	
}
