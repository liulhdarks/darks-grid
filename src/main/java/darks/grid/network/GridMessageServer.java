package darks.grid.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.BindException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GridMessageServer extends GridMessageDispatcher
{
	
	private static final Logger log = LoggerFactory.getLogger(GridMessageServer.class);
	
	private static final int BOSS_NUMBER = Runtime.getRuntime().availableProcessors() * 2;
	
	private static final int WORKER_NUMBER = 4;
	
	ExecutorService threadExecutor = Executors.newCachedThreadPool();
	
	EventLoopGroup bossGroup = new NioEventLoopGroup(BOSS_NUMBER, threadExecutor);
	
	EventLoopGroup workerGroup = new NioEventLoopGroup(WORKER_NUMBER, threadExecutor);
	
	ServerBootstrap bootstrap = null;

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
				.option(ChannelOption.SO_REUSEADDR, true)
				.childOption(ChannelOption.TCP_NODELAY, true)
				.childOption(ChannelOption.SO_KEEPALIVE, true)
				.childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false));
			bootstrap.childHandler(new GridMessageChannelInitializer());
			return true;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
	}
	
	public boolean listen(int port)
	{
		if (bootstrap == null)
			return false;
		try
		{
			ChannelFuture f = bootstrap.bind(port).sync();
			channel = f.channel();
			return true;
		}
		catch (Exception e)
		{
			if (e instanceof BindException)
			{
				
			}
			log.error(e.getMessage(), e);
			return false;
		}
	}
	
	@Override
	public boolean destroy()
	{
		bossGroup.shutdownGracefully();
		workerGroup.shutdownGracefully();
		return super.destroy();
	}
}
