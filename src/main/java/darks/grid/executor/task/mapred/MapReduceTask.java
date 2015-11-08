package darks.grid.executor.task.mapred;

import java.util.Collection;
import java.util.List;

import darks.grid.balance.GridBalance;
import darks.grid.balance.RollPolingBalance;
import darks.grid.beans.GridNode;
import darks.grid.executor.job.GridJob;
import darks.grid.executor.job.JobResult;
import darks.grid.executor.task.GridTask;
import darks.grid.utils.UUIDUtils;

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
