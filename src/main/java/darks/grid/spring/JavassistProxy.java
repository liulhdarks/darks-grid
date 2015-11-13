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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.concurrent.atomic.AtomicLong;

import darks.grid.utils.StringUtils;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import javassist.CtNewConstructor;

public class JavassistProxy
{
    private static final String INIT_NAME_TAG = "<init>";
	private static final String PROXY_CLASS_NAME_SUFFIX = "$Proxy_";
	private static AtomicLong proxyClassIndex = new AtomicLong(1);

	public static Object make(Class<?> interfaceClass, InvocationHandler handler) throws Exception
	{
        ProtectionDomain domain = interfaceClass.getProtectionDomain();
		ClassPool cp = ClassPool.getDefault();
		String interfaceName = interfaceClass.getName();
		String proxyClassName = interfaceName + PROXY_CLASS_NAME_SUFFIX + proxyClassIndex.getAndIncrement();
		CtClass ctInterface = cp.getCtClass(interfaceName);
		CtClass proxyClass = cp.makeClass(proxyClassName);
		proxyClass.addInterface(ctInterface);

		CtField methodField = newField("public static java.lang.reflect.Method[] methods;", proxyClass);
		CtField handleField = newField(cp, InvocationHandler.class, "handler", proxyClass);
		proxyClass.addField(methodField);
		proxyClass.addField(handleField);

		proxyClass.addConstructor(CtNewConstructor.defaultConstructor(proxyClass));
		proxyClass.addConstructor(newConstructor(Modifier.PUBLIC, new Class<?>[] { InvocationHandler.class }, new Class<?>[0],
                "handler=$1;", proxyClass));
		
		Method[] methods = interfaceClass.getMethods();
		for (int i = 0; i < methods.length; i++)
		{
			Method method = methods[i];
            Class<?> rt = method.getReturnType();
            Class<?>[] pts = method.getParameterTypes();
			StringBuilder code = new StringBuilder();
			code.append("public ").append( rt.getName()).append(' ').append(method.getName()).append('(');
			for (int j = 0; j < pts.length; j++)
			{
				Class<?> clazz = pts[j];
				code.append(clazz.getName()).append(" arg").append(j);
				if (j != pts.length - 1)
					code.append(", ");
			}
			code.append(")");
			code.append('{');
			code.append("Object[] args = new Object[").append(pts.length).append("];");
            for (int j = 0; j < pts.length; j++)
                code.append(" args[").append(j).append("] = ($w)$").append(j + 1).append(";");
            code.append(" Object ret = handler.invoke(this, methods[" + i + "], args);");
            if (!Void.TYPE.equals(rt))
                code.append(" return ").append(asArgument(rt, "ret")).append(";");
			code.append('}');
            CtMethod ctm = CtMethod.make(code.toString(), proxyClass);
            proxyClass.addMethod(ctm);
		}
		 Class<?> clazz = proxyClass.toClass(interfaceClass.getClassLoader(), domain);
         clazz.getField("methods").set(null, methods);
         Constructor<?> cst = clazz.getConstructor(InvocationHandler.class);
         return cst.newInstance(handler);
	}
	
	private static CtField newField(String fieldCode, CtClass proxyClass) throws Exception
	{
		return CtField.make(fieldCode, proxyClass);
	}
	
	private static CtField newField(ClassPool cp, Class<?> type, String fieldName, CtClass proxyClass) throws Exception
	{
		CtClass tClass = cp.get(type.getName());
		CtField field = new CtField(tClass, fieldName, proxyClass);
		return field;
	}
	
	private static CtConstructor makeConstructor(String code, CtClass proxyClass) throws Exception
	{
        String[] sn = proxyClass.getSimpleName().split("\\$+"); // inner class name include $.
        return CtNewConstructor.make(
                    code.replaceFirst(INIT_NAME_TAG, sn[sn.length - 1]), proxyClass);
	}
	
	
	private static CtConstructor newConstructor(int mod, Class<?>[] pts, Class<?>[] ets, String body, CtClass proxyClass) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append(Modifier.toString(mod)).append(' ').append(INIT_NAME_TAG);
        sb.append('(');
        for (int i = 0; i < pts.length; i++) {
            if (i > 0)
                sb.append(',');
            sb.append(pts[i].getName());
            sb.append(" arg").append(i);
        }
        sb.append(')');
        if (ets != null && ets.length > 0) {
            sb.append(" throws ");
            for (int i = 0; i < ets.length; i++) {
                if (i > 0)
                    sb.append(',');
                sb.append(ets[i].getName());
            }
        }
        sb.append('{').append(body).append('}');
        return makeConstructor(sb.toString(), proxyClass);
    }

    private static String asArgument(Class<?> cl, String name) 
    {
        if (cl.isPrimitive()) {
            if (Boolean.TYPE == cl)
                return name + "==null?false:((Boolean)" + name + ").booleanValue()";
            if (Byte.TYPE == cl)
                return name + "==null?(byte)0:((Byte)" + name + ").byteValue()";
            if (Character.TYPE == cl)
                return name + "==null?(char)0:((Character)" + name + ").charValue()";
            if (Double.TYPE == cl)
                return name + "==null?(double)0:((Double)" + name + ").doubleValue()";
            if (Float.TYPE == cl)
                return name + "==null?(float)0:((Float)" + name + ").floatValue()";
            if (Integer.TYPE == cl)
                return name + "==null?(int)0:((Integer)" + name + ").intValue()";
            if (Long.TYPE == cl)
                return name + "==null?(long)0:((Long)" + name + ").longValue()";
            if (Short.TYPE == cl)
                return name + "==null?(short)0:((Short)" + name + ").shortValue()";
            throw new RuntimeException(name + " is unknown primitive type.");
        }
        return StringUtils.stringBuffer('(', cl.getName(), ')', name);
    }
}

