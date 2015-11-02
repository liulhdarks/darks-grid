package darks.grid.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.GridException;

public final class ReflectUtils
{
    private static final Logger log = LoggerFactory.getLogger(ReflectUtils.class);
    
    
    private ReflectUtils()
    {
        
    }
    
    public static <T> T newInstance(Class<T> clazz)
    {
        try
        {
            return clazz.newInstance();
        }
        catch (InstantiationException | IllegalAccessException e)
        {
            log.error(e.getMessage(), e);
        }
        return null;
    }
    
    public static String getRealFieldName(String fieldName)
    {
        String[] params = fieldName.split("_");
        StringBuilder buf = new StringBuilder();
        for (int i = 0; i < params.length; i++)
        {
            String param = params[i];
            if (i > 0)
            {
                char ch = param.charAt(0);
                ch = Character.toUpperCase(ch);
                String suffix = param.substring(1);
                buf.append(ch).append(suffix);
            }
            else
            {
                buf.append(param);
            }
        }
        return buf.toString();
    }
    
    public static Object invokeMethod(Object obj, Method method, Object... vals)
    {
        try
        {
            return method.invoke(obj, vals);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return null;
        }
    }
    
    public static Field getDeepField(Class<?> clazz, String fieldName)
    {
        try
        {
            Field field = null;
            do
            {
                try
                {
                    field = clazz.getDeclaredField(fieldName);
                    if (field == null)
                        clazz = clazz.getSuperclass();
                }
                catch (NoSuchFieldException e)
                {
                    clazz = clazz.getSuperclass();
                }
            }
            while (!clazz.equals(Object.class) && field == null);
            return field;
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return null;
        }
    }
    
    public static Method getDeepMethod(Class<?> clazz, String methodName, Class<?>... types)
    {
        try
        {
        	Method method = null;
            do
            {
                try
                {
                	method = clazz.getDeclaredMethod(methodName, types);
                    if (method == null)
                        clazz = clazz.getSuperclass();
                }
                catch (NoSuchMethodException e)
                {
                    clazz = clazz.getSuperclass();
                }
            }
            while (!clazz.equals(Object.class) && method == null);
            return method;
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return null;
        }
    }
    
    public static Class<?>[] getObjectClasses(List<Object> list)
    {
    	if (list == null || list.isEmpty())
    		return null;
    	Class<?>[] classes = new Class<?>[list.size()];
    	for (int i = 0; i < list.size(); i++)
    	{
    		classes[i] = list.get(i) == null ? null : list.get(i).getClass();
    	}
    	return classes;
    }
    
    public static Method getSetMethod(Class<?> clazz, String fieldName, Class<?>... types)
    {
        try
        {
            String[] params = fieldName.split("_");
            StringBuilder buf = new StringBuilder();
            buf.append("set");
            for (String param : params)
            {
                char ch = param.charAt(0);
                ch = Character.toUpperCase(ch);
                String suffix = param.substring(1);
                buf.append(ch).append(suffix);
            }
            String methodName = buf.toString();
            return clazz.getMethod(methodName, types);
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return null;
        }
    }
    
    public static Object convertStringToClass(Class<?> typeClass, String val)
    {
        if (val == null)
            return null;
        if (typeClass.equals(String.class))
            return val;
        else if (typeClass.equals(Integer.class) || typeClass.equals(int.class))
            return Integer.parseInt(val);
        else if (typeClass.equals(Double.class) || typeClass.equals(double.class))
            return Double.parseDouble(val);
        else if (typeClass.equals(Float.class) || typeClass.equals(float.class))
            return Float.parseFloat(val);
        else if (typeClass.equals(Long.class) || typeClass.equals(long.class))
            return Long.parseLong(val);
        else if (typeClass.equals(Short.class) || typeClass.equals(short.class))
            return Short.parseShort(val);
        else if (typeClass.equals(Boolean.class) || typeClass.equals(boolean.class))
            return Boolean.parseBoolean(val);
        else if (typeClass.equals(Character.class) || typeClass.equals(char.class))
            return val.charAt(0);
        throw new GridException("Invalid convert class from string to " + typeClass);
    }
    
    public static void putAttrToObjectField(Object obj, String attrName, String attrValue)
    {
        Class<?> clazz = obj.getClass();
        String fieldName = ReflectUtils.getRealFieldName(attrName);
        Field field = ReflectUtils.getDeepField(clazz, fieldName);
        if (field == null)
            throw new GridException("Cannot find field " + fieldName + " for attribute " + attrName);
        Method method = ReflectUtils.getSetMethod(clazz, attrName, field.getType());
        if (method == null)
            throw new GridException("Cannot find set method for attribute " + attrName);
        Class<?>[] typeClass = method.getParameterTypes();
        if (typeClass.length != 1)
            throw new GridException("Invalid set method " + method + " for attribute " + attrName);
        Object value = ReflectUtils.convertStringToClass(typeClass[0], attrValue);
        ReflectUtils.invokeMethod(obj, method, value);
    }
    
}
