package darks.grid.network.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import darks.grid.beans.GridMessage;

public class GridClientMessageHandler extends GridCommonMessageHandler
{

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		// TODO Auto-generated method stub
		super.channelInactive(ctx);
	}

}
