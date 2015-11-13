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
package darks.grid.executor.job;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class GridJobAdapter extends GridJob
{
    
    private static final long serialVersionUID = 6727035951695986292L;
    
    private List<Object> params;

    public GridJobAdapter()
    {
        params = new ArrayList<Object>();
    }

    public GridJobAdapter(Object... args) {
        if (args == null) 
        {
            this.params = new ArrayList<Object>(0);
        }
        else 
        {
            this.params = new ArrayList<Object>(args.length);
            this.params.addAll(Arrays.asList(args));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P> P getParameter()
    {
        return params.isEmpty() ? null : (P) params.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <P> P getParameter(int pos)
    {
        if (pos < 0 || pos >= params.size())
            return null;
        return (P) params.get(pos);
    }

    @Override
    public List<Object> getParameterList()
    {
        return params;
    }

    @Override
    public boolean isEmpty()
    {
        return params.isEmpty();
    }


}
