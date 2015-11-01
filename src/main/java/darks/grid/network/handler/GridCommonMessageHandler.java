package darks.grid.network.handler;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridMessage;
import darks.grid.beans.meta.JoinMeta;
import darks.grid.network.handler.msg.GridMessageHandler;
import darks.grid.network.handler.msg.MessageHandlerFactory;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;

public class GridCommonMessageHandler extends ChannelHandlerAdapter
{

	private static final Logger log = LoggerFactory.getLogger(GridCommonMessageHandler.class);
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		if (cause instanceof IOException && cause.getMessage().indexOf("远程主机强迫关闭") >= 0)
		{
		    log.warn("Miss connection " + ctx.channel().remoteAddress());
		}
		else
		{
	        log.error(cause.getMessage(), cause);
		}
//		super.exceptionCaught(ctx, cause);
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception
	{
		JoinMeta meta = new JoinMeta(GridRuntime.getLocalId(), GridRuntime.context());
		ctx.writeAndFlush(new GridMessage(meta, GridMessage.MSG_JOIN));
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		GridRuntime.nodes().removeNode(ctx.channel());
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
		GridRuntime.nodes().removeNode(ctx.channel());
		super.close(ctx, promise);
	}

}
