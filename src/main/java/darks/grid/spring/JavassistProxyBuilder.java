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
package darks.grid.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class JavassistProxyBuilder implements ProxyBuilder
{

	private static final Logger log = LoggerFactory.getLogger(JavassistProxyBuilder.class);
	
	private RpcProxyBean proxyBean;
	
	public JavassistProxyBuilder(RpcProxyBean proxyBean)
	{
		this.proxyBean = proxyBean;
	}
    
    @Override
    public Object build(Class<?> interfaceClass)
    {
        try
		{
			return JavassistProxy.make(interfaceClass, new ProxyRpcInvocationHandler(proxyBean));
		}
		catch (Exception e)
		{
			log.error(e.getMessage(), e);
			return null;
		}
    }
    
}
