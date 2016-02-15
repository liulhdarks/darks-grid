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

import java.lang.reflect.Method;
import java.util.List;

import darks.grid.RpcReduceHandler;
import darks.grid.executor.task.rpc.RpcResult;

public class DefaultRpcReduceHandler implements RpcReduceHandler {

    /**
     * {@inheritDoc}
     */
    @Override
    public Object reduce(Object target, Method method, Object[] args, RpcResult result) {
        Object finalResult = null;
        if (method.getReturnType().equals(Object.class))
        {
            finalResult = result.getResult();
        }
        else
        {
            List<Object> list = result.getResult();
            finalResult = (list == null || list.isEmpty()) ? null : list.get(0);
        }
        return finalResult;
    }

}
