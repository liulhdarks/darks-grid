package darks.grid.network.handler;

import darks.grid.GridContext;
import darks.grid.beans.GridEvent;
import io.netty.channel.ChannelHandlerContext;

public class GridServerMessageHandler extends GridMessageHandler
{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		GridContext.getNetwork().addWaitChannel(ctx.channel());
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception
	{
		super.channelRead(ctx, msg);
	}

	
}
