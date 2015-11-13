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
import darks.grid.balance.RollPolingBalance;
import darks.grid.beans.GridNode;
import darks.grid.executor.job.GridJob;
import darks.grid.executor.job.JobResult;
import darks.grid.executor.task.GridTask;

public abstract class MapReduceTask<T, R> extends GridTask
{

    public MapReduceTask()
    {
        super(TaskType.MAPRED);
    }
    
    public MapReduceTask(TaskType taskType)
    {
        super(taskType);
    }

    public boolean initialize(List<GridNode> nodes)
    {
        return initialize(nodes, new RollPolingBalance(nodes));
    }

    public abstract boolean initialize(List<GridNode> nodes, GridBalance balance);
    
    public abstract Collection<? extends GridJob> split(int nodeSize, T arg);
    
    public abstract R reduce(List<JobResult> results);
    
    public abstract GridNode map(GridJob job);
    
}
