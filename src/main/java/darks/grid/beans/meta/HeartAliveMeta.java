package darks.grid.beans.meta;

import darks.grid.GridContext;

public class HeartAliveMeta extends BaseMeta
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2008358585638132743L;
	
	private String nodeId;
	
	private GridContext context;

	public HeartAliveMeta()
	{
		
	}
	
	

	public HeartAliveMeta(String nodeId, GridContext context)
	{
		super();
		this.nodeId = nodeId;
		this.context = context;
	}



	public String getNodeId()
	{
		return nodeId;
	}

	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}

	public GridContext context()
	{
		return context;
	}

	public void setContext(GridContext context)
	{
		this.context = context;
	}

	@Override
	public String toString()
	{
		return "HeartAliveMeta [nodeId=" + nodeId + ", context=" + context + "]";
	}
	
	
}
