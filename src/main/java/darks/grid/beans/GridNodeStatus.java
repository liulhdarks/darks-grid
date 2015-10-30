package darks.grid.beans;

import io.netty.channel.Channel;

public class GridNodeStatus
{
	
	public static final int INVALID = 0;
	
	public static final int ACTIVE = 1;
	
	public static final int INACTIVE = 2;
	
	public static final int OPENED = 3;
	
	public static String valueOf(GridNode node)
	{
		Channel channel = node.getChannel();
		if (channel == null)
			return "INVALID";
		if (node.isAlive())
			return "ALIVE";
		if (channel.isActive())
			return "ACTIVE";
		if (channel.isOpen())
			return "OPENED";
		return "INACTIVE";
	}

}
