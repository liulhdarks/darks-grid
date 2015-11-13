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

import darks.grid.balance.GridBalance;
import darks.grid.beans.GridNode;
import darks.grid.executor.job.GridJob;
import darks.grid.executor.job.JobResult;

public abstract class MapReduceTaskAdapter<T, R> extends MapReduceTask<T, R>
{

    private List<GridNode> nodes = null;
    
    private GridBalance balance;
    
    public MapReduceTaskAdapter()
    {
        super(TaskType.MAPRED);
    }
    
    public MapReduceTaskAdapter(TaskType taskType)
    {
        super(taskType);
    }

    public boolean initialize(List<GridNode> nodes, GridBalance balance)
    {
        this.nodes = nodes;
        this.balance = balance;
        return true;
    }
    
    @Override
    public Collection<? extends GridJob> split(int jobsCount, T arg)
    {
        return null;
    }

    @Override
    public R reduce(List<JobResult> results)
    {
        return null;
    }

    @Override
    public GridNode map(GridJob job)
    {
        return balance == null ? null : balance.getBalanceNode();
    }
    
    public List<GridNode> getNodes()
    {
        return nodes;
    }
    
}
