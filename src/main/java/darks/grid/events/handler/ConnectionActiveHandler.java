package darks.grid.events.handler;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridMessage;
import darks.grid.beans.meta.JoinMeta;
import darks.grid.events.GridEventHandler;
import darks.grid.network.GridSession;
import io.netty.channel.Channel;

public class ConnectionActiveHandler extends GridEventHandler
{
    
    private static final Logger log = LoggerFactory.getLogger(ConnectionActiveHandler.class);
    
    private static final int AWAIT_READY_TIMEOUT = 5;
    
    @Override
    public void handle(GridEvent event)
        throws Exception
    {
        GridSession session = event.getData();
        if (GridRuntime.awaitReady(AWAIT_READY_TIMEOUT, TimeUnit.SECONDS))
        {
            JoinMeta meta = new JoinMeta(GridRuntime.getLocalId(), GridRuntime.context());
            session.sendSyncMessage(new GridMessage(meta, GridMessage.MSG_JOIN));
        }
        else
        {
            log.warn("Wait for runtime ready timeout. Cancel to send join request.");
        }
    }
    
}