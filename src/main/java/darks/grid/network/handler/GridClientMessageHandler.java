package darks.grid.network.handler;

import darks.grid.beans.GridMessage;
import io.netty.channel.ChannelHandlerContext;

public class GridClientMessageHandler extends GridMessageHandler
{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		ctx.writeAndFlush(new GridMessage(null, GridMessage.MSG_JOIN));
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}

}
