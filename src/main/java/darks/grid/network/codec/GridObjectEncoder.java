package darks.grid.network.codec;

import java.io.Serializable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class GridObjectEncoder extends MessageToByteEncoder<Serializable>
{

    private static final byte[] LENGTH_PLACEHOLDER = new byte[4];
    
    GridCodec codec = null;

	@Override
	protected void encode(ChannelHandlerContext ctx, Serializable msg, ByteBuf out) throws Exception
	{
		int startIdx = out.writerIndex();

        ByteBufOutputStream bout = new ByteBufOutputStream(out);
        bout.write(LENGTH_PLACEHOLDER);
        codec.encode(bout, msg);
        int endIdx = out.writerIndex();

        out.setInt(startIdx, endIdx - startIdx - 4);
	}

}
