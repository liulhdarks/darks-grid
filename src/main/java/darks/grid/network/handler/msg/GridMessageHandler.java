package darks.grid.network.handler.msg;

import darks.grid.beans.GridMessage;
import darks.grid.network.GridSession;

public interface GridMessageHandler
{

	public void handler(GridSession session, GridMessage msg) throws Exception;

	
}
