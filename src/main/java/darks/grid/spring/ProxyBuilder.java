package darks.grid.spring;

public interface ProxyBuilder
{
	
	public static final String JDK_PROXY_TYPE = "jdk";
	
	public static final String JAVASSIST_PROXY_TYPE = "javassist";

    public Object build(Class<?> interfaceClass);
    
}
