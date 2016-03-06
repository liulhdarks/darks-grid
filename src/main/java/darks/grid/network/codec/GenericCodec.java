package darks.grid.network.codec;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import de.ruedigermoeller.serialization.FSTConfiguration;
import io.netty.handler.codec.serialization.ClassResolver;

public class GenericCodec implements GridCodec
{
	
	static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
	
	@Override
	public void encode(OutputStream out, Serializable msg) throws Exception
	{
		ObjectOutputStream oos = new ObjectOutputStream(out);
		oos.writeObject(msg);
		oos.flush();
	    out.close();
	}

	@Override
	public Object decode(InputStream in, ClassResolver classResolver) throws Exception
	{
		ObjectInputStream ois = new ObjectInputStream(in);
		Object result = ois.readObject();
		ois.close();
	    return result;
	}

}
