package darks.grid.network.handler.msg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.beans.meta.HeartAliveMeta;
import darks.grid.network.GridSession;

public class HEART_ALIVE implements GridMessageHandler
{
	
	private static final Logger log = LoggerFactory.getLogger(HEART_ALIVE.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void handler(GridSession session, GridMessage msg) throws Exception
	{
		HeartAliveMeta meta = msg.getData();
		String nodeId = meta.getNodeId();
		GridNode node = GridRuntime.nodes().getNode(nodeId);
		if (node == null)
		{
			GridRuntime.nodes().addRemoteNode(nodeId, session, meta.context());
		}
		else
		{
			node.setHeartAliveTime(System.currentTimeMillis());
			node.context().setMachineInfo(meta.context().getMachineInfo());
		}
		//HEART ALIVE REPLY
		if (msg.getType() == GridMessage.MSG_HEART_ALIVE)
		{
			boolean valid = true;
			try
			{
			    GridRuntime.context().getMachineInfo().update();
				HeartAliveMeta replyMeta = new HeartAliveMeta(GridRuntime.context().getLocalNodeId(), GridRuntime.context());
				GridMessage replyMsg = new GridMessage(replyMeta, GridMessage.MSG_HEART_ALIVE_REPLY, msg);
				valid = session.sendSyncMessage(replyMsg);
			}
			catch (Exception e)
			{
				log.error(e.getMessage(), e);
				valid = false;
			}
			if (!valid)
			{
				GridRuntime.nodes().removeNode(node);
			}
		}
	}
}
