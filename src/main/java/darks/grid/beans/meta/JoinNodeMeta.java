package darks.grid.beans.meta;


public class JoinNodeMeta extends BaseMeta
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -3103532011798509755L;

	private String nodeId;
	
	private long startupTime;
	
	public JoinNodeMeta()
	{
		
	}
	

	public JoinNodeMeta(String nodeId, long startupTime)
	{
		super();
		this.nodeId = nodeId;
		this.startupTime = startupTime;
	}



	public String getNodeId()
	{
		return nodeId;
	}

	public void setNodeId(String nodeId)
	{
		this.nodeId = nodeId;
	}

	
	
	public long getStartupTime()
	{
		return startupTime;
	}

	public void setStartupTime(long startupTime)
	{
		this.startupTime = startupTime;
	}

	@Override
	public String toString()
	{
		return "JoinNodeMeta [nodeId=" + nodeId + ", startupTime=" + startupTime + "]";
	}

	
}
