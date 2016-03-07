package darks.grid.network.codec;

import io.netty.handler.codec.serialization.ClassResolver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.Map;

public interface GridCodec
{
    
    public void initialize(Map<String, String> params);

	public void encode(OutputStream out, Serializable msg) throws Exception;
	
	public Object decode(InputStream in, ClassResolver classResolver) throws Exception;
	
}
