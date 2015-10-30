package darks.grid.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.network.handler.GridServerMessageHandler;
import darks.grid.utils.NetworkUtils;

public class GridMessageServer extends GridMessageDispatcher
{
	
	private static final Logger log = LoggerFactory.getLogger(GridMessageServer.class);
	
	private static final int BOSS_NUMBER = Runtime.getRuntime().availableProcessors() * 2;
	
	private static final int WORKER_NUMBER = 4;
	
	ExecutorService threadExecutor = Executors.newCachedThreadPool();
	
	EventLoopGroup bossGroup = new NioEventLoopGroup(BOSS_NUMBER, threadExecutor);
	
	EventLoopGroup workerGroup = new NioEventLoopGroup(WORKER_NUMBER, threadExecutor);
	
	ServerBootstrap bootstrap = null;
	
	boolean binded = false;
	
	private InetSocketAddress bindAddress;
	
	public GridMessageServer()
	{
		
	}
	
	@Override
	public boolean initialize()
	{
		try
		{
			super.initialize();
			bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
            	.channel(NioServerSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.option(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))
				.option(ChannelOption.SO_TIMEOUT, 10000)
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
				.childOption(ChannelOption.TCP_NODELAY, true)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))
				.childOption(ChannelOption.SO_TIMEOUT, 10000)
				.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000);
			bootstrap.childHandler(new GridMessageChannelInitializer(new GridServerMessageHandler()));
			bindAddress = null;
			return true;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
	}
	
	public boolean listen(int port) throws BindException
	{
		bindAddress = null;
		if (bootstrap == null)
			return false;
		try
		{
			ChannelFuture f = bootstrap.bind(port).sync();
			channel = f.channel();
			binded = true;
			log.info("Grid message server binds address " + getAddress());
			return true;
		}
		catch (Exception e)
		{
			if (e instanceof BindException)
				throw (BindException)e;
			else
				log.error(e.getMessage(), e);
			return false;
		}
	}
	
	@Override
	public boolean destroy()
	{
		log.info("Destroying grid message server.");
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		return super.destroy();
	}

	public boolean isBinded()
	{
		return binded;
	}
	
	public synchronized InetSocketAddress getAddress()
	{
		if (channel == null)
			return null;
		if (bindAddress == null)
		{
			String ipHost = NetworkUtils.getIpAddress();
			InetSocketAddress ipAddr = (InetSocketAddress) channel.localAddress();
			bindAddress = new InetSocketAddress(ipHost, ipAddr.getPort());
		}
		return bindAddress;
	}
	
}
