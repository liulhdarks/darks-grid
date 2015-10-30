package darks.grid.beans.meta;

import darks.grid.GridContext;


public class JoinNodeMeta extends BaseMeta
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3103532011798509755L;

	private String nodeId;
	
	private GridContext nodeContext;
	
	public JoinNodeMeta()
	{
		
	}
	

	public JoinNodeMeta(String nodeId, GridContext nodeContext)
	{
		this.nodeId = nodeId;
		this.nodeContext = nodeContext;
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
		return nodeContext;
	}


	public void setNodeContext(GridContext nodeContext)
	{
		this.nodeContext = nodeContext;
	}


	@Override
	public String toString()
	{
		return "JoinNodeMeta [nodeId=" + nodeId + ", nodeContext=" + nodeContext + "]";
	}

}
