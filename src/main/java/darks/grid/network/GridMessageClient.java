package darks.grid.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

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
            NetworkConfig config = GridRuntime.config().getNetworkConfig();
            int workerNum = config.getClientWorkerThreadNumber();
		    workerGroup = new NioEventLoopGroup(workerNum, threadExecutor);
			bootstrap = new Bootstrap();
			bootstrap.group(workerGroup).channel(NioSocketChannel.class)
					.option(ChannelOption.TCP_NODELAY, config.isTcpNodelay())
					.option(ChannelOption.SO_KEEPALIVE, config.isKeepAlive())
					.option(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))
//	                .option(ChannelOption.SO_TIMEOUT, config.getRecvTimeout())
	                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeout());
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
			log.info("Succeed to connect " + address);
			return true;
		}
		catch (Exception e)
		{
			if (e instanceof ConnectException)
				log.error("Fail to connect " + address);
			else
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
