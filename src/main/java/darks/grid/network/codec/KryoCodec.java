package darks.grid.network.codec;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

import io.netty.handler.codec.serialization.ClassResolver;

public class KryoCodec implements GridCodec
{
	

	ThreadLocal<Kryo> kryoLocal = new ThreadLocal<Kryo>();
	
	@Override
	public void encode(OutputStream out, Serializable msg) throws Exception
	{
		Kryo kryo = kryoLocal.get();
		if (kryo == null)
		{
			kryo = new Kryo();
			kryoLocal.set(kryo);
		}
		Output output = new Output(out);
		kryo.writeClassAndObject(output, msg);
		output.flush();
		output.close();
	}

	@Override
	public Object decode(InputStream in, ClassResolver classResolver) throws Exception
	{
		Kryo kryo = kryoLocal.get();
		if (kryo == null)
		{
			kryo = new Kryo();
			kryoLocal.set(kryo);
		}
		Input input = new Input(in);
		Object ret = kryo.readClassAndObject(input);
		input.close();
		return ret;
	}

}
