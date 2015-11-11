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
