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

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.timeout.IdleStateHandler;

import java.net.BindException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.config.NetworkConfig;
import darks.grid.network.codec.CodecFactory;
import darks.grid.network.handler.GridServerMessageHandler;

public class GridMessageServer extends GridMessageDispatcher
{
	
	private static final Logger log = LoggerFactory.getLogger(GridMessageServer.class);
	
	ExecutorService threadExecutor = Executors.newCachedThreadPool();
	
	EventLoopGroup bossGroup = null;
	
	EventLoopGroup workerGroup = null;
	
	ServerBootstrap bootstrap = null;
	
	public GridMessageServer()
	{
		
	}
	
	@Override
	public boolean initialize()
	{
		try
		{
		    NetworkConfig config = GridRuntime.config().getNetworkConfig();
		    int bossNum = Runtime.getRuntime().availableProcessors() * config.getServerBossThreadDelta();
		    int workerNum = config.getServerWorkerThreadNumber();
		    bossGroup = new NioEventLoopGroup(bossNum);
		    workerGroup = new NioEventLoopGroup(workerNum);
			super.initialize();
			bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
            	.channel(NioServerSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, config.isTcpNodelay())
				.option(ChannelOption.SO_KEEPALIVE, config.isTcpKeepAlive())
				.option(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true))
//				.option(ChannelOption.SO_TIMEOUT, config.getRecvTimeout())
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeout())
				.option(ChannelOption.SO_REUSEADDR, config.isTcpReuseAddr())
//				.option(ChannelOption.SO_BACKLOG, config.getTcpBacklog())
				.option(ChannelOption.SO_SNDBUF, config.getTcpSendBufferSize())
				.option(ChannelOption.SO_RCVBUF, config.getTcpRecvBufferSize())
				.childOption(ChannelOption.TCP_NODELAY, config.isTcpNodelay())
				.childOption(ChannelOption.SO_KEEPALIVE, config.isTcpKeepAlive())
				.childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(true))
				.childOption(ChannelOption.SO_TIMEOUT, config.getRecvTimeout())
				.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeout())
				.childOption(ChannelOption.SO_REUSEADDR, config.isTcpReuseAddr())
				.childOption(ChannelOption.SO_BACKLOG, config.getTcpBacklog())
				.childOption(ChannelOption.SO_SNDBUF, config.getTcpSendBufferSize())
				.childOption(ChannelOption.SO_RCVBUF, config.getTcpRecvBufferSize());
			bootstrap.childHandler(newChannelHandler());
			return true;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
	}
    
    public GridSession listen(int port) throws BindException
    {
        return listen(null, port);
    }
	
	public GridSession listen(String host, int port) throws BindException
	{
		if (bootstrap == null)
			return null;
		try
		{
			ChannelFuture f = null;
			if (host == null || "".equals(host))
			    f = bootstrap.bind(port).sync();
			else
			    f = bootstrap.bind(host, port).sync();
			Channel channel = f.channel();
			GridSession session = GridSessionFactory.getLocalSession(channel);
			log.info("Grid message server binds address " + session.remoteAddress());
			return session; 
		}
		catch (Exception e)
		{
			if (e instanceof BindException)
				throw (BindException)e;
			else
				log.error(e.getMessage(), e);
			return null;
		}
	}
	
	@Override
	public boolean destroy()
	{
		log.info("Destroying grid message server.");
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		threadExecutor.shutdown();
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
				pipeline.addLast("alive", new IdleStateHandler(60, 60, 120));
				pipeline.addLast("message", new GridServerMessageHandler());
			}
		};
		return result;
	}
	
	
}
