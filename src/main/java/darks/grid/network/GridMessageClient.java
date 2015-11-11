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
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.config.NetworkConfig;
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

	public Channel connect(SocketAddress address)
	{
		if (bootstrap == null)
			return null;
		try
		{
			ChannelFuture f = bootstrap.connect(address).sync();
			Channel channel = f.channel();
			log.info("Succeed to connect " + address);
			return channel;
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
				pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
				pipeline.addLast("encoder", new ObjectEncoder());
//				pipeline.addLast("alive", new IdleStateHandler(60, 60, 120));
				pipeline.addLast("message", new GridClientMessageHandler());
			}
		};
		return result;
	}
}
