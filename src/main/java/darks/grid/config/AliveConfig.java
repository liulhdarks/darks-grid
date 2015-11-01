package darks.grid.config;

public class AliveConfig
{
    
    private int interval = 300000;
    
    private int expire = 600000;
    
    private int printNodesInterval = 180000;
    
    public AliveConfig()
    {
        
    }

    public int getInterval()
    {
        return interval;
    }

    public void setInterval(int interval)
    {
        this.interval = interval;
    }

    public int getExpire()
    {
        return expire;
    }

    public void setExpire(int expire)
    {
        this.expire = expire;
    }
    
    public int getPrintNodesInterval()
    {
        return printNodesInterval;
    }

    public void setPrintNodesInterval(int printNodesInterval)
    {
        this.printNodesInterval = printNodesInterval;
    }

    @Override
    public String toString()
    {
        return "AliveConfig [interval=" + interval + ", expire=" + expire + 
            ", printNodesInterval=" + printNodesInterval + "]";
    }
    
    
}
