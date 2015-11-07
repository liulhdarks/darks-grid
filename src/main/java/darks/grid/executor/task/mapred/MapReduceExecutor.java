package darks.grid.executor.task.mapred;

import java.util.Collection;
import java.util.List;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.executor.job.GridJob;
import darks.grid.executor.job.GridJobStatus;
import darks.grid.executor.job.JobResult;
import darks.grid.executor.job.JobStatusType;
import darks.grid.executor.task.TaskExecutor;

public class MapReduceExecutor<T, R> extends TaskExecutor<T, R>
{

    
    public MapReduceExecutor(MapReduceTask<T, R> task,
        T paramters)
    {
        super(TaskType.MAPRED, task, paramters);
    }

    @Override
    protected R execute()
    {
        List<GridNode> nodesList = GridRuntime.nodes().getSnapshotNodes();
        task.initialize(nodesList);
        Collection<? extends GridJob> jobs = task.map(nodesList.size(), paramters);
        for (GridJob job : jobs)
        {
            job.setTask(task);
            executeJob(job);
        }
        future.await();
        GridRuntime.tasks().completeTask(getTaskId());
        List<JobResult> jobResults = future.getList();
        return task.reduce(jobResults);
    }

    @Override
    public void failRedo(GridJob job)
    {
        executeJob(job);
    }

    private boolean executeJob(GridJob job)
    {
        GridNode node = task.nextNode();
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
