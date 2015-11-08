package darks.grid.executor.task;

public interface TaskResultListener<R>
{

    public R handle(R result);
    
}
