package darks.grid.beans;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class GridComponent extends Thread implements Serializable, Cloneable
{
    
    private static final long serialVersionUID = -3274470227313357300L;

    private static final Logger log = LoggerFactory.getLogger(GridComponent.class);

    private boolean destroyed = false;
    
    private int interval = 1000;
    
    public GridComponent()
    {
        
    }
    
    @Override
    public void run()
    {
        try
        {
            log.info("Grid component " + getClass() + " startup.");
            while (isRunning())
            {
                execute();
                if (interval > 0)
                    Thread.sleep(interval);
            }
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }
    
    protected abstract void execute() throws Exception;
    
    public boolean initialize()
    {
        return true;
    }
    
    public boolean isRunning()
    {
        return !destroyed && !isInterrupted();
    }

    public boolean isDestroyed()
    {
        return destroyed;
    }

    public void destroy()
    {
        destroyed = true;
        interrupt();
    }

    public int getInterval()
    {
        return interval;
    }

    public void setInterval(int interval)
    {
        this.interval = interval;
    }
    
}
