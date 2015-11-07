package darks.grid.test.spring;

import darks.grid.RpcReduceHandler;
import darks.grid.beans.MethodResult;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Administrator on 2015/11/6.
 */
public class RpcComputerReduceHandler implements RpcReduceHandler
{

    @Override
    public Object reduce(MethodResult result) {
        System.out.println(result.getResult());
        List<Object> list = result.getResult();
        if (list != null && list.size() > 1)
        {
            Collections.sort(list, new Comparator<Object>() {
                @Override
                public int compare(Object o1, Object o2) {
                    Integer v1 = (Integer) o1;
                    v1 = v1 == null ? 0 : v1;
                    Integer v2 = (Integer) o2;
                    v2 = v2 == null ? 0 : v2;
                    return v2 - v1;
                }
            });
        }
        return (list == null || list.isEmpty()) ? null : list.get(0);
    }
}
