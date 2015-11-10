package darks.grid.network.handler.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridMessage;
import darks.grid.network.GridSession;

public class EVENT_TRIGGER implements GridMessageHandler
{

	private static final Logger log = LoggerFactory.getLogger(EVENT_TRIGGER.class);
	
	@Override
	public void handler(GridSession session, GridMessage msg) throws Exception
	{
	    GridEvent event = msg.getData();
	    if (event != null)
	    	GridRuntime.events().publish(event);
	    else
	    	log.error("Invalid event message " + msg);
	}

}
