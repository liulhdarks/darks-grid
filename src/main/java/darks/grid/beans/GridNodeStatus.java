package darks.grid.beans;

import darks.grid.network.GridSession;

public class GridNodeStatus
{
	
	public static final int INVALID = 0;
	
	public static final int ACTIVE = 1;
	
	public static final int INACTIVE = 2;
	
	public static String valueOf(GridNode node)
	{
		GridSession session = node.getSession();
		if (session == null)
			return "INVALID";
		if (node.isAlive())
			return "ALIVE";
		if (session.isActive())
			return "ACTIVE";
		return "INACTIVE";
	}

}
