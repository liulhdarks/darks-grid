package darks.grid.executor.task.mapred;

import java.util.Collection;
import java.util.List;

import darks.grid.balance.GridBalance;
import darks.grid.beans.GridNode;
import darks.grid.executor.job.GridJob;
import darks.grid.executor.job.JobResult;
import darks.grid.executor.task.GridTask;
import darks.grid.utils.UUIDUtils;

public abstract class MapReduceTask<T, R> extends GridTask
{

    private List<GridNode> nodes = null;
    
    private int nodeIndex = 0;
    
    public MapReduceTask()
    {
        super(TaskType.MAPRED);
    }
    
    public MapReduceTask(TaskType taskType)
    {
        super(taskType);
    }

    public void initialize(List<GridNode> nodes)
    {
        this.nodes = nodes;
        this.nodeIndex = 0;
    }
    
    public Collection<? extends GridJob> map(int nodeSize, T arg)
    {
        return null;
    }
    
    public R reduce(List<JobResult> results)
    {
        return null;
    }
    
    public GridNode nextNode()
    {
        if (nodes == null)
            return null;
        int size = nodes.size();
        return nodes.get(nodeIndex++ % size);
    }
    
    public List<GridNode> getNodes()
    {
        return nodes;
    }
    
}
