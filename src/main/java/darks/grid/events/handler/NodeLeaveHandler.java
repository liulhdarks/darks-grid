package darks.grid.events.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridNode;
import darks.grid.events.GridEventHandler;

public class NodeLeaveHandler extends GridEventHandler
{
	
	private static final Logger log = LoggerFactory.getLogger(NodeLeaveHandler.class);

	@Override
	public void handle(GridEvent event) throws Exception
	{
		GridNode node = event.getData();
		if (node != null)
		{	
			log.info("Grid node " + node.getId() + " " + node.context().getServerAddress() + " quit.");
			node.setQuit(true);
			GridRuntime.jobs().removeNodeAllJobs(node.getId());
		}
		else
		{
			log.error("Unknown grid node quit." + event);
		}
	}

}
