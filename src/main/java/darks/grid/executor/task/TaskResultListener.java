package darks.grid.executor.task;

public interface TaskResultListener<T>
{

    public void handle(T result);
    
}
