package darks.grid.network.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.beans.meta.JoinMeta;
import darks.grid.network.handler.msg.GridMessageHandler;
import darks.grid.network.handler.msg.MessageHandlerFactory;

public class GridCommonMessageHandler extends ChannelHandlerAdapter
{

	private static final Logger log = LoggerFactory.getLogger(GridCommonMessageHandler.class);
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		// TODO Auto-generated method stub
		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		JoinMeta meta = new JoinMeta(GridRuntime.getLocalId(), 
				GridRuntime.context().getStartupTime(),
				GridRuntime.context().getClusterName());
		ctx.writeAndFlush(new GridMessage(meta, GridMessage.MSG_JOIN));
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
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) throws Exception
	{
		// TODO Auto-generated method stub
		super.close(ctx, promise);
	}

}
