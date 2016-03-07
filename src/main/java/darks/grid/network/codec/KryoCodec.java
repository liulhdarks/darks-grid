package darks.grid.network.codec;

import io.netty.handler.codec.serialization.ClassResolver;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.ref.SoftReference;
import java.util.Map;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoCodec implements GridCodec
{
//    private static final String DEFAULT_STREAM_FACTORY = "default";
//    private static final String FAST_STREAM_FACTORY = "fast";
//    private static final String KEY_STREAM_FACTORY = "stream_factory";

    private ThreadLocal<SoftReference<Kryo>> kryoLocal = new ThreadLocal<SoftReference<Kryo>>();
    
	@Override
    public void initialize(Map<String, String> params) 
	{
//	    String streamFactoryType = DEFAULT_STREAM_FACTORY;
//        if (params.containsKey(KEY_STREAM_FACTORY))
//        {
//            String value = params.get(KEY_STREAM_FACTORY);
//            if (value != null && FAST_STREAM_FACTORY.equals(value))
//            {
//                streamFactoryType = FAST_STREAM_FACTORY;
//            }
//        }
//        if (FAST_STREAM_FACTORY.equals(streamFactoryType))
//            streamFactory = new FastestStreamFactory();
//        else
//            streamFactory = new DefaultStreamFactory();
    }

    @Override
	public void encode(OutputStream out, Serializable msg) throws Exception
	{
        Kryo kryo = getKryo();
		Output output = new Output(out);
		kryo.writeClassAndObject(output, msg);
		output.flush();
		output.close();
	}

	@Override
	public Object decode(InputStream in, ClassResolver classResolver) throws Exception
	{
		Kryo kryo = getKryo();
		Input input = new Input(in);
		Object ret = kryo.readClassAndObject(input);
		input.close();
		return ret;
	}

	private Kryo getKryo() 
	{
	    SoftReference<Kryo> kryoRef = kryoLocal.get();
	    Kryo result = kryoRef == null ? null : kryoRef.get();
        if (result == null)
        {
            result = new Kryo();
            kryoLocal.set(new SoftReference<Kryo>(result));
        }
        return result;
	}
}
