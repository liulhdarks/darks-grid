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

    @Override
    public Object execute()
    {
        // TODO Auto-generated method stub
        return null;
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
