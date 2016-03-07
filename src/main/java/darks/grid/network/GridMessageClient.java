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
package darks.grid.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.beans.GridAddress;
import darks.grid.config.NetworkConfig;
import darks.grid.network.codec.CodecFactory;
import darks.grid.network.handler.GridClientMessageHandler;
import darks.grid.utils.ThreadUtils;

public class GridMessageClient extends GridMessageDispatcher
{
	
	private static final Logger log = LoggerFactory.getLogger(GridMessageClient.class);
	
	ExecutorService threadExecutor = null;
	
	EventLoopGroup workerGroup = null;
	
	Bootstrap bootstrap = null;

	public GridMessageClient()
	{
		threadExecutor = Executors.newCachedThreadPool();
	}

	public GridMessageClient(ExecutorService threadExecutor)
	{
		this.threadExecutor = threadExecutor;
	}
	

	@Override
	public boolean initialize()
	{
		super.initialize();
		try
		{
			log.info("Initialize message client.");
            NetworkConfig config = GridRuntime.config().getNetworkConfig();
            int workerNum = config.getClientWorkerThreadNumber();
		    workerGroup = new NioEventLoopGroup(workerNum, ThreadUtils.getThreadFactory());
			bootstrap = new Bootstrap();
			bootstrap.group(workerGroup).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, config.isTcpNodelay())
					.option(ChannelOption.SO_KEEPALIVE, config.isTcpKeepAlive())
					.option(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true))
//	                .option(ChannelOption.SO_TIMEOUT, config.getRecvTimeout())
	                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeout())
					.option(ChannelOption.SO_SNDBUF, config.getTcpSendBufferSize())
					.option(ChannelOption.SO_RCVBUF, config.getTcpRecvBufferSize());
			bootstrap.handler(newChannelHandler());
			return true;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
	}


	public Channel connect(String host, int port)
	{
		return connect(new InetSocketAddress(host, port));
	}

    public Channel connect(GridAddress address)
    {
        return connect(address.getSocketAddress());
    }

    public Channel connect(GridAddress address, boolean sync)
    {
        return connect(address.getSocketAddress(), sync);
    }

	public Channel connect(SocketAddress address)
	{
		return connect(address, true);
	}

	public Channel connect(SocketAddress address, boolean sync)
	{
		if (bootstrap == null)
			return null;
		try
		{
			ChannelFuture f = bootstrap.connect(address);
			if (sync)
			{
				f.sync();
				Channel channel = f.channel();
				if (channel != null)
					log.info("Succeed to connect " + address);
				else
					log.error("Fail to connect " + address);
				return channel;
			}
			else
				return f.channel();
		}
		catch (Exception e)
		{
			if (e instanceof ConnectException)
				log.error("Fail to connect " + address);
			else
				log.error(e.getMessage(), e);
			return null;
		}
	}

	@Override
	public boolean destroy()
	{
		workerGroup.shutdownGracefully();
		return super.destroy();
	}
	

	
	private ChannelInitializer<SocketChannel> newChannelHandler()
	{
		ChannelInitializer<SocketChannel> result = new ChannelInitializer<SocketChannel>()
		{
			
			@Override
			protected void initChannel(SocketChannel ch) throws Exception
			{
				ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("decoder", CodecFactory.createDecoder(ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                pipeline.addLast("encoder", CodecFactory.createEncoder());
//				pipeline.addLast("alive", new IdleStateHandler(60, 60, 120));
				pipeline.addLast("message", new GridClientMessageHandler());
			}
		};
		return result;
	}
}
