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

package darks.grid.annotations;

import java.lang.reflect.Method;

import darks.grid.RpcReduceHandler;
import darks.grid.utils.ReflectUtils;


public class GridAnnotationParser {

    
    public static GridClassAnnotation parse(Class<?> clazz) {
        GridClassAnnotation result = new GridClassAnnotation();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            GridMethodAnnotation gm = parseMethod(method);
            if (gm != null) {
                String methodUniqueName = ReflectUtils.getMethodUniqueName(method);
                result.addMethodAnnotation(methodUniqueName, gm);
            }
        }
        return result;
    }
    
    private static GridMethodAnnotation parseMethod(Method method) {
        RpcMethod rpcMethod = (RpcMethod) method.getAnnotation(RpcMethod.class);
        if (rpcMethod != null) {
            GridMethodAnnotation result = new GridMethodAnnotation();
            result.setCallType(rpcMethod.callType());
            result.setResponseType(rpcMethod.responseType());
            result.setTimeout(rpcMethod.timeout());
            Class<? extends RpcReduceHandler> handlerClass = rpcMethod.reducer();
            if (handlerClass != null && !BlankRpcReduceHandler.class.equals(handlerClass))
                result.setReducer(ReflectUtils.newInstance(handlerClass));
            return result;
        }
        return null;
    }
}
