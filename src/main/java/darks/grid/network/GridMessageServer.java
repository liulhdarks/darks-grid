package darks.grid.network;

import java.net.BindException;
import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridRuntime;
import darks.grid.config.NetworkConfig;
import darks.grid.network.handler.GridServerMessageHandler;
import darks.grid.utils.NetworkUtils;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class GridMessageServer extends GridMessageDispatcher
{
	
	private static final Logger log = LoggerFactory.getLogger(GridMessageServer.class);
	
	ExecutorService threadExecutor = Executors.newCachedThreadPool();
	
	EventLoopGroup bossGroup = null;
	
	EventLoopGroup workerGroup = null;
	
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
		    NetworkConfig config = GridRuntime.config().getNetworkConfig();
		    int bossNum = Runtime.getRuntime().availableProcessors() * config.getServerBossThreadDelta();
		    int workerNum = config.getServerWorkerThreadNumber();
		    bossGroup = new NioEventLoopGroup(bossNum, threadExecutor);
		    workerGroup = new NioEventLoopGroup(workerNum, threadExecutor);
			super.initialize();
			bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup)
            	.channel(NioServerSocketChannel.class)
				.option(ChannelOption.TCP_NODELAY, config.isTcpNodelay())
				.option(ChannelOption.SO_KEEPALIVE, config.isKeepAlive())
				.option(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))
				.option(ChannelOption.SO_TIMEOUT, config.getRecvTimeout())
				.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeout())
				.childOption(ChannelOption.TCP_NODELAY, config.isTcpNodelay())
				.childOption(ChannelOption.SO_KEEPALIVE, config.isKeepAlive())
				.childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))
				.childOption(ChannelOption.SO_TIMEOUT, config.getRecvTimeout())
				.childOption(ChannelOption.CONNECT_TIMEOUT_MILLIS, config.getConnectTimeout());
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
        return listen(null, port);
    }
	
	public boolean listen(String host, int port) throws BindException
	{
		bindAddress = null;
		if (bootstrap == null)
			return false;
		try
		{
			ChannelFuture f = null;
			if (host == null || "".equals(host))
			    f = bootstrap.bind(port).sync();
			else
			    f = bootstrap.bind(host, port).sync();
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
		threadExecutor.shutdown();
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
