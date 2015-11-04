package darks.grid.events;

import darks.grid.beans.GridEvent;

public abstract class GridEventHandler
{

	public abstract void handle(GridEvent event) throws Exception;
	
}
