package darks.grid.network.codec;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import de.ruedigermoeller.serialization.FSTConfiguration;
import de.ruedigermoeller.serialization.FSTObjectInput;
import de.ruedigermoeller.serialization.FSTObjectOutput;
import io.netty.handler.codec.serialization.ClassResolver;

public class FSTCodec implements GridCodec
{
	
	static FSTConfiguration conf = FSTConfiguration.createDefaultConfiguration();
	
	@Override
	public void encode(OutputStream out, Serializable msg) throws Exception
	{
		FSTObjectOutput fout = conf.getObjectOutput(out);
		fout.writeObject(msg);
		fout.flush();
	    out.close();
	}

	@Override
	public Object decode(InputStream in, ClassResolver classResolver) throws Exception
	{
		FSTObjectInput fin = conf.getObjectInput(in);
		Object result = fin.readObject();
		in.close();
	    return result;
	}

}
