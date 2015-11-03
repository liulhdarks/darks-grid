package darks.grid.executor.task;

public interface TaskResultListener<T>
{

    public T handle(T result);
    
}
