/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package darks.grid.network.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.io.IOException;
import java.nio.channels.ClosedChannelException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridEvent;
import darks.grid.beans.GridMessage;
import darks.grid.network.GridSessionFactory;
import darks.grid.network.handler.msg.GridMessageHandler;
import darks.grid.network.handler.msg.MessageHandlerFactory;
import darks.grid.utils.GridStatistic;

public class GridCommonMessageHandler extends SimpleChannelInboundHandler<GridMessage>
{

	private static final Logger log = LoggerFactory.getLogger(GridCommonMessageHandler.class);
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		if (cause instanceof IOException 
				&& (cause.getMessage().indexOf("远程主机强迫关闭") >= 0
						|| cause.getMessage().indexOf("Connection reset by peer") >= 0))
		{
		    log.warn("Miss connection " + ctx.channel().remoteAddress());
			GridRuntime.nodes().removeNode(GridSessionFactory.getSession(ctx.channel()));
		}
		else if (cause instanceof ClosedChannelException)
		{
		    log.warn("Close exception " + ctx.channel().remoteAddress());
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
        GridRuntime.events().publish(GridEvent.CONNECT_ACTIVE, GridSessionFactory.getSession(ctx.channel()));
		super.channelActive(ctx);
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception
	{
		GridRuntime.nodes().removeNode(GridSessionFactory.getSession(ctx.channel()));
		super.channelInactive(ctx);
	}

	@Override
	public void channelRead0(ChannelHandlerContext ctx, GridMessage msg) throws Exception
	{
		long arriveTime = System.currentTimeMillis();
		if (log.isDebugEnabled())
			log.debug("Channel read:" + msg);
		GridMessageHandler handler = MessageHandlerFactory.getHandler(msg);
		if (handler != null)
		{
			handler.handler(GridSessionFactory.getSession(ctx.channel()), msg);
		}
		GridStatistic.incrementDelay(arriveTime - msg.getTimestamp());
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception
	{
		if (evt instanceof IdleStateEvent)
		{
			IdleStateEvent event = (IdleStateEvent) evt;
			if (event.state() == IdleState.ALL_IDLE)
			{
				
			}
        }
		else
			super.userEventTriggered(ctx, evt);
	}

	
}
