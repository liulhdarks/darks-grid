package darks.grid.network.discovery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridComponent;

public abstract class GridDiscovery extends GridComponent
{
    
    private static final Logger log = LoggerFactory.getLogger(GridDiscovery.class);
    
    private static final long serialVersionUID = 1459651037836923247L;
    

    @Override
    protected void execute() throws Exception
    {
        try
        {
            findNodes();
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
        }
    }

    public abstract void findNodes();
	
}
