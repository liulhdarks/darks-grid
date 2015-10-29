package darks.grid.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.network.handler.GridClientMessageHandler;

public class GridMessageClient extends GridMessageDispatcher
{
	
	private static final Logger log = LoggerFactory.getLogger(GridMessageClient.class);
	
	private static final int WORK_NUMBER = Runtime.getRuntime().availableProcessors() * 2;
	
	ExecutorService threadExecutor = Executors.newCachedThreadPool();
	
	EventLoopGroup workerGroup = new NioEventLoopGroup(WORK_NUMBER, threadExecutor);
	
	Bootstrap bootstrap = null;

	public GridMessageClient()
	{

	}
	

	@Override
	public boolean initialize()
	{
		super.initialize();
		try
		{
			bootstrap = new Bootstrap();
			bootstrap.group(workerGroup).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, true)
					.option(ChannelOption.SO_KEEPALIVE, true)
					.option(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false));
			bootstrap.handler(new GridMessageChannelInitializer(new GridClientMessageHandler()));
			return true;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
	}


	public boolean connect(String host, int port)
	{
		return connect(new InetSocketAddress(host, port));
	}

	public boolean connect(SocketAddress address)
	{
		if (bootstrap == null)
			return false;
		try
		{
			ChannelFuture f = bootstrap.connect(address).sync();
			channel = f.channel();
			return true;
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return false;
		}
	}

	@Override
	public boolean destroy()
	{
		workerGroup.shutdownGracefully();
		return super.destroy();
	}
}
