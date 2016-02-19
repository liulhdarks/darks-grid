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
package darks.grid.beans;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.annotations.TimeField;

public abstract class GridComponent extends Thread implements Serializable, Cloneable
{
    
    private static final long serialVersionUID = -3274470227313357300L;

    private static final Logger log = LoggerFactory.getLogger(GridComponent.class);

    private volatile boolean destroyed = false;
    
    @TimeField
    private int interval = 1000;
    
    private int delay = 0;
    
    private boolean singleUse = false;
    
    public GridComponent()
    {
        
    }
    
    @Override
    public void run()
    {
        try
        {
            log.info("Grid component " + getClass() + " startup.");
            if (delay > 0)
                Thread.sleep(delay);
            if (singleUse)
                execute();
            else
            {
                while (isRunning())
                {
                    execute();
                    if (interval > 0)
                        Thread.sleep(interval);
                }
            }
            log.info("Grid component " + getClass() + " normal shutdown.");
        }
        catch (Exception e)
        {
            log.error("Grid component " + getClass() + " abnormal shutdown. Cause " + e.getMessage(), e);
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

	public int getDelay()
	{
		return delay;
	}

	public void setDelay(int delay)
	{
		this.delay = delay;
	}

    public boolean isSingleUse() {
        return singleUse;
    }

    public void setSingleUse(boolean singleUse) {
        this.singleUse = singleUse;
    }
    
}
