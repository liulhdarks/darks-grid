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
package darks.grid.executor.task.rpc;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import darks.grid.beans.GridRpcMethod;
import darks.grid.executor.RpcExecutor;
import darks.grid.executor.task.GridJobReply;
import darks.grid.utils.ReflectUtils;

public class RpcMethodInvoker
{
    
    private static final Logger log = LoggerFactory.getLogger(RpcMethodInvoker.class);

    public static GridJobReply invoke(GridRpcJob job)
    {
        GridJobReply rep = new GridJobReply(job);
        String uniqueName = job.getUniqueName();
        GridRpcMethod bean = RpcExecutor.getRpcMethod(uniqueName);
        if (bean == null)
            return rep.failed(GridJobReply.ERR_NO_METHOD, "Cannot find method " + uniqueName);
        Object obj = null;
        obj = bean.getTargetObject();
        if (obj == null)
        {
            if (bean.getTargetClass() == null)
            {
                return rep.failed(GridJobReply.ERR_INVALID_OBJANDCLASS, 
                    "Invalid target object and class which is null.");
            }
            obj = ReflectUtils.newInstance(bean.getTargetClass());
            if (obj == null)
                return rep.failed(GridJobReply.ERR_INSTANCE_CLASS_FAIL, "Fail to instance class " + bean.getTargetClass());
        }
        try
        {
            Method method = bean.getMethod();
            if (method == null)
            {
                synchronized (bean)
                {
                    method = bean.getMethod();
                    if (method == null)
                    {
                        Class<?>[] paramsTypes = job.getTypes();
                        if (paramsTypes == null)
                            paramsTypes = ReflectUtils.getObjectClasses(job.getParams());
                        method = ReflectUtils.getDeepMethod(obj.getClass(), bean.getMethodName(), paramsTypes);
                        bean.setMethod(method);
                    }
                }
            }
            if (method == null)
                return rep.failed(GridJobReply.ERR_GET_CLASS_METHOD, 
                    "Fail to get deep method " + bean.getMethodName() + " from " + obj.getClass() + " [" + Arrays.toString(job.getTypes()) + "]");
            if (!method.isAccessible())
                method.setAccessible(true);
            Object retObj = ReflectUtils.invokeMethod(obj, method, job.getParams());
            rep.setResult(retObj);
            return rep;
        }
        catch (Exception e)
        {
            log.error(e.getMessage(), e);
            return rep.failed(GridJobReply.ERR_INVOKE_EXCEPTION, 
                "Fail to invoke method " + uniqueName + ". Cause " + e.getMessage());
        }
    }
    
}
