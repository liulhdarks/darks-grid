package darks.grid.network;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class GridMessageChannelInitializer extends ChannelInitializer<SocketChannel>
{
	
	ChannelHandler[] handlers = null;

	public GridMessageChannelInitializer(ChannelHandler... handlers)
	{
		this.handlers = handlers;
	}
	
	@Override
	protected void initChannel(SocketChannel channel) throws Exception
	{
		ChannelPipeline pipeline = channel.pipeline();
		pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.softCachingConcurrentResolver(this.getClass().getClassLoader())));
		pipeline.addLast("encoder", new ObjectEncoder());
		if (handlers != null)
		{
			for (ChannelHandler handler : handlers)
			{
				pipeline.addLast(handler);
			}
		}
	}

}
