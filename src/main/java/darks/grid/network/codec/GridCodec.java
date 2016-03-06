package darks.grid.network.codec;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import io.netty.handler.codec.serialization.ClassResolver;

public interface GridCodec
{

	public void encode(OutputStream out, Serializable msg) throws Exception;
	
	public Object decode(InputStream in, ClassResolver classResolver) throws Exception;
	
}
