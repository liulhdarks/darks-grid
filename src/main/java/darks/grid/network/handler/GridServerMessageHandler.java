package darks.grid.network.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import darks.grid.GridContext;
import darks.grid.beans.GridMessage;
import darks.grid.network.handler.msg.MessageHandlerFactory;

public class GridServerMessageHandler extends ChannelHandlerAdapter
{
	
	private static final Logger log = LoggerFactory.getLogger(GridServerMessageHandler.class);

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
		GridMessage message = (GridMessage) msg;
		GridMessageHandler handler = MessageHandlerFactory.getHandler(message);
		if (handler != null)
		{
			handler.handler(ctx, message);
		}
		else
			log.error("Fail to find handler for message " + msg);
		super.channelRead(ctx, msg);
	}

	
}
