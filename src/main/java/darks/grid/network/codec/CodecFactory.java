/**
 * 
 * Copyright 2015 The Darks Grid Project (Liu lihua)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package darks.grid.network.codec;

import io.netty.channel.ChannelHandler;
import io.netty.handler.codec.serialization.ClassResolver;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.HashMap;
import java.util.Map;

import darks.grid.GridException;
import darks.grid.GridRuntime;
import darks.grid.config.CodecConfig;
import darks.grid.utils.ReflectUtils;

public final class CodecFactory 
{
    
    public static final String NETTY_CODEC = "netty";
    public static final String GENERIC_CODEC = "generic";
    public static final String FST_CODEC = "fst";
    public static final String HESSIAN_CODEC = "hessian";
    public static final String KRYO_CODEC = "kryo";
    
    public static final String DEFAULT_CODEC = NETTY_CODEC;
    
    private static Map<String, Class<? extends GridCodec>> codecMap = 
                            new HashMap<String, Class<? extends GridCodec>>();
    
    static
    {
        register(GENERIC_CODEC, GenericCodec.class);
        register(KRYO_CODEC, KryoCodec.class);
        register(FST_CODEC, FSTCodec.class);
        register(HESSIAN_CODEC, HessianCodec.class);
    }
    
    public static void register(String type, Class<? extends GridCodec> clazz)
    {
        codecMap.put(type, clazz);
    }

    public static ChannelHandler createEncoder()
    {
        CodecConfig codecConfig = GridRuntime.config().getNetworkConfig().getCodecConfig();
        String type = DEFAULT_CODEC;
        Class<? extends GridCodec> codecClass = null;
        if (codecConfig.getType() != null)
        {
            type = codecConfig.getType().toLowerCase();
            codecClass = codecConfig.getCodecClass();
        }
        if (NETTY_CODEC.equals(type))
        {
            return new ObjectEncoder();
        }
        if (codecMap.containsKey(type) && codecClass == null)
        {
            codecClass = codecMap.get(type);
        }
        if (codecClass == null)
            throw new GridException("Cannot find codec " + type);
        GridCodec codec = ReflectUtils.newInstance(codecClass);
        codec.initialize(codecConfig.getParameters());
        return new GridObjectEncoder(codec);
    }

    public static ChannelHandler createDecoder(ClassResolver classResolver)
    {
        CodecConfig codecConfig = GridRuntime.config().getNetworkConfig().getCodecConfig();
        String type = DEFAULT_CODEC;
        Class<? extends GridCodec> codecClass = null;
        if (codecConfig.getType() != null)
        {
            type = codecConfig.getType().toLowerCase();
            codecClass = codecConfig.getCodecClass();
        }
        if (NETTY_CODEC.equals(type))
        {
            return new ObjectDecoder(Integer.MAX_VALUE, classResolver);
        }
        if (codecMap.containsKey(type) && codecClass == null)
        {
            codecClass = codecMap.get(type);
        }
        if (codecClass == null)
            throw new GridException("Cannot find codec " + type);
        GridCodec codec = ReflectUtils.newInstance(codecClass);
        codec.initialize(codecConfig.getParameters());
        return new GridObjectDecoder(classResolver, codec);
    }
}
