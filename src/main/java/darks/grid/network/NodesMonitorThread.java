package darks.grid.network;

import io.netty.channel.ChannelFuture;

import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.beans.GridNode;
import darks.grid.beans.GridNodeType;
import darks.grid.beans.meta.HeartAliveMeta;

public class NodesMonitorThread extends Thread
{
	
	private static final Logger log = LoggerFactory.getLogger(NodesMonitorThread.class);
	
	private volatile boolean stoped = false;
	
	long st = System.currentTimeMillis();
	
	public NodesMonitorThread()
	{
		
	}

	public void run()
	{
		try
		{
			while (!stoped && !isInterrupted())
			{
				Map<String, GridNode> nodesMap = GridRuntime.nodes().getNodesMap();
				log.info("Start to monitor nodes.size:" + nodesMap.size());
				for (Entry<String, GridNode> entry : nodesMap.entrySet())
				{
					GridNode node = entry.getValue();
					if (node.getNodeType() == GridNodeType.TYPE_LOCAL)
						continue;
					if (!node.isAlive())
					{
						log.info("Grid node " + node.getId() + " " + node.context().getServerAddress() + " miss alive.");
						GridRuntime.nodes().removeNode(node);
					}
					else
					{
						checkAlive(node);
					}
				}
				if (System.currentTimeMillis() - st > 60000)
				{
					log.info(GridRuntime.nodes().getNodesInfo());
					st = System.currentTimeMillis();
				}
				Thread.sleep(30000);
			}
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
		}
	}
	
	private void checkAlive(GridNode node)
	{
		boolean valid = true;
		try
		{
			HeartAliveMeta meta = new HeartAliveMeta(GridRuntime.context().getLocalNodeId(), GridRuntime.context());
			GridMessage msg = new GridMessage(meta, GridMessage.MSG_HEART_ALIVE);
			ChannelFuture future = node.getChannel().writeAndFlush(msg).sync();
			valid = future.isSuccess();
		}
		catch (Exception e)
		{
			log.error("Fail to check alive " + node + ". Cause " + e.getMessage());
			valid = false;
		}
		if (!valid)
		{
			GridRuntime.nodes().removeNode(node);
		}
	}

	public boolean isStoped()
	{
		return stoped;
	}

	public void setStoped(boolean stoped)
	{
		this.stoped = stoped;
	}
	
	
}
