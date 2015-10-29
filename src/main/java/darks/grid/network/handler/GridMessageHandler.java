package darks.grid.network.handler;

import io.netty.channel.ChannelHandlerContext;
import darks.grid.beans.GridMessage;

public interface GridMessageHandler
{

	public void handler(ChannelHandlerContext ctx, GridMessage msg) throws Exception;

	
}
