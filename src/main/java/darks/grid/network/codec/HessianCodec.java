package darks.grid.network.codec;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;

import io.netty.handler.codec.serialization.ClassResolver;

public class HessianCodec implements GridCodec
{
	
	
	@Override
	public void encode(OutputStream out, Serializable msg) throws Exception
	{
		HessianOutput ho = new HessianOutput(out);  
		ho.writeObject(msg);
		ho.flush();
	    out.close();
	}

	@Override
	public Object decode(InputStream in, ClassResolver classResolver) throws Exception
	{
		HessianInput hi = new HessianInput(in);
		Object result = hi.readObject();
		hi.close();
	    return result;
	}

}
