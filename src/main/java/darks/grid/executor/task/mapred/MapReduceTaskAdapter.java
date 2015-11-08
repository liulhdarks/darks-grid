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
